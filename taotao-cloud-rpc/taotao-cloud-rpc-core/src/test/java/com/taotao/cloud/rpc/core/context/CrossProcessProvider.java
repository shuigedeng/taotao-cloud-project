package com.taotao.cloud.rpc.core.context;

import org.apache.logging.log4j.ThreadContext;

import java.lang.management.ManagementFactory;

/**
 * 另一个 JVM 里的 Provider。由 {@code CrossProcessE2ETest} 用 ProcessBuilder 拉起来，
 * 走的是生产实现：真 {@link RpcServer} + 真 Netty 管线。
 * <p>
 * 打印 {@code READY <port> <pid>} 后原地不退出的意义在于：这一行只有在端口真的绑上之后才会出现，
 * 测试进程据此判断"可以连了"，而不是靠 sleep。
 */
public final class CrossProcessProvider {

    /** 用来验证异常类型跨进程还原的业务异常。 */
    public static class ProbeFailure extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public ProbeFailure(String message) {
            super(message);
        }
    }

    /** 服务实现：方法名要与线上请求里的 methodName 对得上；prefix 用来说明命中的是哪条注册。 */
    public static final class DemoService {
        private final String prefix;

        DemoService(String prefix) {
            this.prefix = prefix;
        }

        public String where() {
            return "prefix=" + prefix
                    + ";pid=" + ownPid()
                    + ";thread=" + Thread.currentThread().getName()
                    + ";mdc=" + ThreadContext.get("traceId");
        }

        public String echo(String input) {
            return prefix + ":" + input;
        }

        public String boom() {
            throw new ProbeFailure("cross-jvm-8899");
        }
    }

    private CrossProcessProvider() {
    }

    static String ownPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        int at = name.indexOf('@');
        return at > 0 ? name.substring(0, at) : name;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("USAGE <port> [serviceKey]");
            System.out.flush();
            return;
        }
        int port = Integer.parseInt(args[0].trim());
        String serviceKey = args.length > 1 ? args[1].trim() : "cross.demo.DemoService";

        RpcServer server = new RpcServer("127.0.0.1", port);
        server.registerService(serviceKey, new DemoService("child"));
        // 带版本那条注册用来证明 version attachment 真的跨过了进程边界；
        // 键由生产侧的拼键函数给出，不在测试里重拼一遍。
        server.registerService(RpcServer.serviceKey(serviceKey, "7.7.7"), new DemoService("versioned-child"));
        server.start(true);
        if (!server.isStarted()) {
            System.out.println("BIND-FAILED " + port);
            System.out.flush();
            return;
        }
        System.out.println("READY " + server.getPort() + " " + ownPid());
        System.out.flush();
        Thread.sleep(Long.MAX_VALUE);
    }
}
