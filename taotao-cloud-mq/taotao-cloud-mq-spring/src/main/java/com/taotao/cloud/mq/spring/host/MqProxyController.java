package com.taotao.cloud.mq.spring.host;

import com.taotao.boot.common.utils.json.JacksonUtils;
import com.taotao.cloud.mq.client.MQClientInstance;
import com.taotao.cloud.mq.remoting.netty.NettyClientConfig;
import com.taotao.cloud.mq.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.remoting.protocol.RequestCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

/**
 * 消息队列 (ttc-mq) 管理面适配器: 把 ttc-mq 的<b>私有 Netty 二进制协议</b>转成 ttc-opc 的统一前缀
 * {@code /api/mq/**} 下的只读 JSON.
 *
 * <p><b>为什么这里不是"薄代理" (与 VectorProxyController / GraphProxyController 的关键差别)</b>:
 * ttc-vector 有 {@code QdrantRestServer}(HTTP)、ttc-graph 有 {@code GraphControlServer}(JDK HttpServer),
 * 它们能逐字节透传。ttc-mq <b>一个 HTTP 面都没有</b> —— 对 ttc-mq 全仓 Java 源码扫
 * {@code HttpServer|RestController|@GetMapping} 命中 0 条; nameserver(:9876) 与 broker(:10911)
 * 都是 {@code NettyRemotingServer} + {@code RequestCode} 命令字的私有二进制协议。
 * 所以"前端 axios 打到 8888、8888 转发 HTTP"这条形状在 mq 上直接不成立:
 * 上游没有可转发的 HTTP。本类改走 TASK-008 四件套里预告的那条贵路 ——
 * <b>Java 侧用 ttc-mq-remoting 的客户端发只读请求码, 在这里转成 JSON</b>。
 *
 * <p><b>形状不是随便定的, 有仓内成品背书</b>: 同一个模式已经被两处跟踪代码用过 ——
 * {@code TtcMqTopicBootstrap}(同一个 config 包)用 {@code MQClientInstance.invokeSync} 发
 * {@code UPDATE_AND_CREATE_TOPIC}/{@code GET_ROUTEINFO_BY_TOPIC}; {@code ttc-mq-tools} 的
 * {@code ClusterAdmin} 用 {@code GET_ALL_TOPIC_LIST}/{@code GET_ROUTEINFO_BY_TOPIC}/
 * {@code GET_BROKER_CLUSTER_INFO} 做只读台账。本类的请求码集合就是 {@code ClusterAdmin} 那一组
 * (加上 broker 侧 320/321/322/323/324)。注意 {@code ttc-mq-tools} <b>不在 main-starter 的 classpath 上</b>,
 * 所以是照它写而不是 import 它。
 *
 * <p><b>响应保真怎么保证 (取代 vector/graph 那版的双打探针)</b>: 上游各 handler 的响应体本来就是
 * {@code JsonCodec.encode(ClusterInfo/TopicList/TopicRouteData/…)} 的产物, 本类只做
 * {@code readTree(body)} 后塞进 {@code data} 字段, <b>不重名字段、不裁剪、不算派生值</b>。
 * 也就是说"字段名与上游 DTO 一致"这件事由代码结构保证, 而不是靠探针; 探针只能验 HTTP 转发,
 * 对二进制协议无从下手 (见交接文件 §四 的复用说明与替代方案)。
 *
 * <p><b>三条刻意的收窄</b>:
 * <ol>
 *   <li><b>只读码白名单, 且没有任何通用转发口子。</b> 类里能到达的码是写死的
 *       (namesrv 3/4/8 + broker 8/320/321/322/323/324)。请求体里传不进码, 也传不进地址。
 *       400/322/323 这类是 BrokerOutAPI 的内部同步接口, 只读且对进程无副作用。
 *       写码 (5=UPDATE_AND_CREATE_TOPIC / 7=DELETE_TOPIC / 400=CREATE_TOPIC /
 *       410=RESET_CONSUMER_OFFSET / 411=TERMINATE_CONSUMER / 200=SEND_MESSAGE / 10/11=KV 写)
 *       一律不可达。孵化期只做读面, 写面单独立卡。</li>
 *   <li><b>只拨环回地址。</b> broker 地址是从 nameserver 的 {@code GET_BROKER_CLUSTER_INFO} 里发现的,
 *       但发现到什么不一定可信, 所以再过一道 {@link #LOOPBACK_ADDR}: 非环回的地址直接拒绝连接。
 *       这一条同时关掉了 SSRF。</li>
 *   <li><b>归属自证优先于数据。</b> {@code __instance} 把"谁在应答这两个端口"算成机械判定
 *       (见下一段), 且 {@link #instance} 之外的每个端点在"本 JVM 的子进程没 bind 上"时
 *       会带上 {@code attributionUnverified=true} —— 页面必须显示它。</li>
 * </ol>
 *
 * <p><b>为什么 ttc-mq 的"外来监听者"判定比 ttc-graph 那一档麻烦</b>: ttc-mq 在 ttc-opc 里是
 * {@link TtcMqEmbeddedServerConfig} 用 {@code ProcessBuilder} <b>spawn 的两个子进程</b>
 * (ttc-mq 的 Main 会 {@code Thread.currentThread().join()}, inline 起会卡死 Tomcat),
 * 所以"属主 PID != 8888 那个 PID"根本不构成外来证据 —— 合法形态本来就 PID 不同。
 * 真正的判据是 {@code 端口属主 PID == 本 JVM 自己 spawn 的那个子进程 PID}。
 * 更阴的一层: {@code Process.isAlive()} 单独是<b>假信号</b> —— 实测本次构建的
 * {@code NameServerStartup}/{@code BrokerStartup} 子进程都活着, 但两者监听套接字数为 0,
 * 因为 {@code main} 线程已经因 {@code BindException: Address already in use} 退出,
 * 而 Netty 的 boss/worker 是非守护线程 ⇒ JVM 不退、端口不接。
 * 于是下面的 {@code namesrvChildAlive}/{@code namesrvPortOwnedByMyChild} 必须分成两格。
 *
 * <p>只挂在 {@code /api/**} 下, 不开裸路径 —— 裸路径不在
 * {@code sso.intercept-paths=**&#47;api/**,/agent/**} 内 (值见 application.properties,
 * 这里把第一个 {@code /} 写成实体, 否则它会提前关掉本段注释), 等于不设防 (TASK-20260924-018 的教训)。
 * 9876/10911 两个 Netty 端口都是 bind 通配地址且
 * ttc-mq 的 remoting 层没有默认鉴权, 所以"前端只走 8888"不只是规约, 是实际的安全边界。
 */
@RestController("opsMqProxyController")
public class MqProxyController {

    private static final String PREFIX = "/api/mq";

    /** broker 地址白名单: 只允许环回。名字带 brokerName 的 (DefaultBroker:10911 之类) 一律拒绝。 */
    private static final Pattern LOOPBACK_ADDR =
            Pattern.compile("^(127\\.0\\.0\\.1|localhost|\\[?::1\\]?):(\\d{1,5})$");
    /** topic 名按 ttc-mq 侧的取值放宽到字母数字与 -_.:; 不把合法 topic 挡成 400 */
    private static final Pattern SAFE_TOPIC = Pattern.compile("^[A-Za-z0-9_\\-.:;%#]{1,120}$");
    private static final Pattern SAFE_BROKER_NAME = Pattern.compile("^[A-Za-z0-9_\\-.]{1,120}$");

    private static final int INVOKE_TIMEOUT_MS = 5000;
    private static final int TCP_PROBE_TIMEOUT_MS = 400;
    private static final int MAX_BODY_BYTES = 4 * 1024 * 1024;
    /** 自省用的只读码集合; 与下面真正的 @GetMapping 一一对应, 只为让 __instance 能自报能力面 */
    private static final Map<String, Integer> READ_ONLY_CODES = new LinkedHashMap<>();
    private static final Map<String, String> CODE_NAMES = new LinkedHashMap<>();

    static {
        READ_ONLY_CODES.put("namesrv.cluster", RequestCode.GET_BROKER_CLUSTER_INFO);   // 4
        READ_ONLY_CODES.put("namesrv.topics", RequestCode.GET_ALL_TOPIC_LIST);          // 8
        READ_ONLY_CODES.put("namesrv.route", RequestCode.GET_ROUTEINFO_BY_TOPIC);       // 3
        READ_ONLY_CODES.put("broker.topics", RequestCode.GET_ALL_TOPIC_LIST);           // 8
        READ_ONLY_CODES.put("broker.topicConfig", RequestCode.GET_ALL_TOPIC_CONFIG);    // 320
        READ_ONLY_CODES.put("broker.consumerOffset", RequestCode.GET_ALL_CONSUMER_OFFSET); // 321
        READ_ONLY_CODES.put("broker.delayOffset", RequestCode.GET_ALL_DELAY_OFFSET);    // 322
        READ_ONLY_CODES.put("broker.subscription", RequestCode.GET_ALL_SUBSCRIPTION_GROUP); // 323
        READ_ONLY_CODES.put("broker.dataVersion", RequestCode.QUERY_DATA_VERSION);      // 324
        CODE_NAMES.put("3", "GET_ROUTEINFO_BY_TOPIC");
        CODE_NAMES.put("4", "GET_BROKER_CLUSTER_INFO");
        CODE_NAMES.put("8", "GET_ALL_TOPIC_LIST");
        CODE_NAMES.put("320", "GET_ALL_TOPIC_CONFIG");
        CODE_NAMES.put("321", "GET_ALL_CONSUMER_OFFSET");
        CODE_NAMES.put("322", "GET_ALL_DELAY_OFFSET");
        CODE_NAMES.put("323", "GET_ALL_SUBSCRIPTION_GROUP");
        CODE_NAMES.put("324", "QUERY_DATA_VERSION");
    }

    private final ObjectProvider<TtcMqEmbeddedServerConfig> embeddedProvider;
    private final AtomicLong clientSeq = new AtomicLong();

    @Value("${ttc.mq.namesrv-addr:127.0.0.1:9876}")
    private String namesrvAddr;

    @Value("${ttc.mq.namesrv-port:9876}")
    private int namesrvPort;

    @Value("${ttc.mq.broker-port:10911}")
    private int brokerPort;

    @Value("${ttc.mq.broker-store-path:${java.io.tmpdir}/ttc-mq-store}")
    private String brokerStorePath;

    /**
     * 归属自证要不要真的去问内核"这个端口的属主是谁"。
     * 只能靠 {@code lsof} —— Java 8 目标 (源码/字节码都是 1.8, 不能用 java.net.http 之外,
     * 也没有任何 JDK API 能从进程内查到 socket owner)。
     * 关掉时 {@code __instance} 会把 {@code attributionAvailable} 置 false,
     * 页面据此显示"无法自证归属", 而不是悄悄当成自己的。
     */
    @Value("${ttc.mq.proxy.lsof-attribution.enabled:true}")
    private boolean lsofAttributionEnabled;

    public MqProxyController(ObjectProvider<TtcMqEmbeddedServerConfig> embeddedProvider) {
        this.embeddedProvider = embeddedProvider;
    }

    // ================================================================== 自省

    /**
     * 自省接口: 这一路到底连的是谁、那两个端口归谁。
     *
     * <p>必须存在, 而且要单独存在一屏 —— {@code ZCompanyMainStarter} 的 {@code static{}}
     * 对内嵌启动失败是 {@code catch Throwable} 静默跳过的, 而 ttc-mq 这一层更隐蔽:
     * 子进程 {@code spawn} 成功、{@code isAlive()} 为真, 但 {@code bind} 已经失败
     * (见类注释)。没有这个端点, "页面空白"会被 100% 误读成"集群里没有 topic"。
     */
    @GetMapping(PREFIX + "/__instance")
    public void instance(HttpServletResponse resp) throws Exception {
        TtcMqEmbeddedServerConfig cfg = embeddedProvider.getIfAvailable();

        ChildState nsChild = childOf(cfg, "nameserverProcess");
        ChildState brChild = childOf(cfg, "brokerProcess");

        boolean nsReachable = tcpReachable(namesrvPort);
        boolean brReachable = tcpReachable(brokerPort);
        Long nsOwner = lsofOwnerPid(namesrvPort);
        Long brOwner = lsofOwnerPid(brokerPort);

        // 归属判定的核心: 属主必须正是本 JVM spawn 的那个 pid
        boolean nsOwned = nsOwner != null && nsOwner.equals(nsChild.pid);
        boolean brOwned = brOwner != null && brOwner.equals(brChild.pid);

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("jvm", ManagementFactory.getRuntimeMXBean().getName());
        out.put("jvmPid", nsafe(out.get("jvm")).split("@")[0]);
        out.put("surface", "remoting-netty-private-binary");
        out.put("surfaceNote", "ttc-mq 没有 HTTP 管理面；本接口由 MqProxyController 自己应答，"
                + "下面每个端点都是「只读请求码 → JSON」的转换，不是 HTTP 转发");
        out.put("embeddedConfigLoaded", cfg != null);
        out.put("ttcEnabled", cfg != null);
        out.put("namesrvAddr", namesrvAddr);
        out.put("namesrvPort", namesrvPort);
        out.put("brokerPort", brokerPort);
        out.put("storePath", brokerStorePath);
        out.put("storePathExists", new File(brokerStorePath).isDirectory());
        out.put("storePathEmpty", isDirEmpty(new File(brokerStorePath)));

        // —— NameServer 这一档
        out.put("namesrvChildSpawned", nsChild.seen);
        out.put("namesrvChildPid", nsChild.pid);
        out.put("namesrvChildAlive", nsChild.alive);
        out.put("namesrvPortReachable", nsReachable);
        out.put("namesrvPortOwnerPid", nsOwner);
        out.put("namesrvPortOwnedByMyChild", nsOwned);
        out.put("namesrvEmbeddedRunning", nsOwned);

        // —— Broker 这一档（两个端口必须分开报: NameServer 通 / Broker 不通 是常见中间态）
        out.put("brokerChildSpawned", brChild.seen);
        out.put("brokerChildPid", brChild.pid);
        out.put("brokerChildAlive", brChild.alive);
        out.put("brokerPortReachable", brReachable);
        out.put("brokerPortOwnerPid", brOwner);
        out.put("brokerPortOwnedByMyChild", brOwned);
        out.put("brokerEmbeddedRunning", brOwned);

        // 外来监听者嫌疑: 端口有人答, 但属主不是本 JVM 的子进程
        out.put("namesrvForeignListenerSuspected", nsReachable && !nsOwned);
        out.put("brokerForeignListenerSuspected", brReachable && !brOwned);
        out.put("foreignListenerSuspected",
                (nsReachable && !nsOwned) || (brReachable && !brOwned));
        // 拿不到属主 pid 时不许把"没嫌疑"当"是自己的"
        out.put("attributionAvailable",
                !lsofAttributionEnabled || (nsOwner != null && brOwner != null));
        out.put("attributionMethod", lsofAttributionEnabled
                ? "lsof -nP -iTCP:<port> -sTCP:LISTEN -t，与本 JVM 子进程 pid 比对；"
                + "不能用「属主 == 8888 那个 pid」，因为 ttc-mq 的内嵌形态天然是子进程"
                : "已关闭 (ttc.proxy.lsof-attribution.enabled=false) ⇒ 归属无法自证");

        // bind 失败的原文：spawn 器把子进程 stdout/stderr 追加到 ${java.io.tmpdir}/ttc-mq-<label>.log
        Map<String, Object> nsLog = spawnLog("nameserver");
        Map<String, Object> brLog = spawnLog("broker");
        out.put("namesrvSpawnLog", nsLog);
        out.put("brokerSpawnLog", brLog);
        out.put("bindError", nsLog.get("lastBindError") != null ? nsLog.get("lastBindError")
                : brLog.get("lastBindError"));
        out.put("readOnlyRequestCodes", READ_ONLY_CODES);
        out.put("requestCodeNames", CODE_NAMES);
        out.put("unavailableOnBroker120", Arrays.asList(
                "GET_BROKER_CONFIG(402) 未注册", "CHECK_ROUTE_EXIST(401) 未注册",
                "QUERY_MESSAGE(230)/VIEW_MESSAGE_BY_KEY(231) 未注册",
                "CONSUMER_HEARTBEAT(221/222) 未注册 ⇒ 消费者在线状态在 1.2.0 无码可发",
                "RESET_CONSUMER_OFFSET(410)/TERMINATE_CONSUMER(411) 未注册"));
        out.put("unavailableOnNamesrv120", Arrays.asList(
                "GET_CLUSTER_INFO(12) 未实现", "GET_KV_CONFIG(9) 未实现",
                "DELETE_TOPIC(7) 未实现（写码，本来也不该由页面触达）"));
        writeJson(resp, 200, out);
    }

    // ============================================================ NameServer 侧

    /** nameserver {@code GET_BROKER_CLUSTER_INFO(4)} → ClusterInfo{brokerAddrTable, clusterAddrTable} */
    @GetMapping(PREFIX + "/cluster")
    public void cluster(HttpServletResponse resp) throws Exception {
        emit(resp, callNamesrv(RequestCode.GET_BROKER_CLUSTER_INFO, null));
    }

    /** nameserver {@code GET_ALL_TOPIC_LIST(8)} → TopicList{topics:[…]}（只有名字，没有队列数） */
    @GetMapping(PREFIX + "/topics")
    public void topics(HttpServletResponse resp) throws Exception {
        emit(resp, callNamesrv(RequestCode.GET_ALL_TOPIC_LIST, null));
    }

    /** nameserver {@code GET_ROUTEINFO_BY_TOPIC(3)} → TopicRouteData{queueDatas, brokerDatas, …} */
    @GetMapping(PREFIX + "/route")
    public void route(@RequestParam(value = "topic", required = false) String topic,
                      HttpServletResponse resp) throws Exception {
        if (topic == null || topic.trim().isEmpty()) {
            writeJson(resp, 400, err("topic 参数缺失: /api/mq/route?topic=<name>", String.valueOf(topic)));
            return;
        }
        if (!SAFE_TOPIC.matcher(topic).matches()) {
            // 与"没传 topic"分开: 混成一句话, 页面就没法区分"我漏了参数"和"这个名字代理层不接"
            writeJson(resp, 400, err("bad topic name (白名单 " + SAFE_TOPIC + ", 长度 1-120)", topic));
            return;
        }
        Map<String, String> ext = new LinkedHashMap<>();
        ext.put("topic", topic);
        emit(resp, callNamesrv(RequestCode.GET_ROUTEINFO_BY_TOPIC, ext));
    }

    // ================================================================ Broker 侧

    /** broker {@code GET_ALL_TOPIC_LIST(8)} → TopicListResult{topics:[…]}（本 broker 的 topicConfigTable） */
    @GetMapping(PREFIX + "/broker/topics")
    public void brokerTopics(@RequestParam(value = "brokerName", required = false) String brokerName,
                             @RequestParam(value = "brokerAddr", required = false) String brokerAddr,
                             HttpServletResponse resp) throws Exception {
        emitBroker(resp, brokerName, brokerAddr, RequestCode.GET_ALL_TOPIC_LIST, null);
    }

    /** broker {@code GET_ALL_TOPIC_CONFIG(320)} → TopicConfigSerializeWrapper{topicConfigTable, dataVersion} */
    @GetMapping(PREFIX + "/broker/topic-config")
    public void brokerTopicConfig(@RequestParam(value = "brokerName", required = false) String brokerName,
                                  @RequestParam(value = "brokerAddr", required = false) String brokerAddr,
                                  HttpServletResponse resp) throws Exception {
        emitBroker(resp, brokerName, brokerAddr, RequestCode.GET_ALL_TOPIC_CONFIG, null);
    }

    /** broker {@code GET_ALL_CONSUMER_OFFSET(321)} → ConsumerOffsetSerializeWrapper{offsetTable, dataVersion} */
    @GetMapping(PREFIX + "/broker/consumer-offset")
    public void brokerConsumerOffset(@RequestParam(value = "brokerName", required = false) String brokerName,
                                     @RequestParam(value = "brokerAddr", required = false) String brokerAddr,
                                     HttpServletResponse resp) throws Exception {
        emitBroker(resp, brokerName, brokerAddr, RequestCode.GET_ALL_CONSUMER_OFFSET, null);
    }

    /** broker {@code GET_ALL_DELAY_OFFSET(322)}（源码注释：简化为由 Topic 自身 offset 推导） */
    @GetMapping(PREFIX + "/broker/delay-offset")
    public void brokerDelayOffset(@RequestParam(value = "brokerName", required = false) String brokerName,
                                  @RequestParam(value = "brokerAddr", required = false) String brokerAddr,
                                  HttpServletResponse resp) throws Exception {
        emitBroker(resp, brokerName, brokerAddr, RequestCode.GET_ALL_DELAY_OFFSET, null);
    }

    /**
     * broker {@code GET_ALL_SUBSCRIPTION_GROUP(323)}。
     * <b>已知恒空</b>: BrokerOutAPI 的注释写的是「订阅组配置 (MVP 暂返回空)」——
     * 所以这一页的空表不是"没有订阅组"，是"上游没实现"，页面必须把这句挂出来。
     */
    @GetMapping(PREFIX + "/broker/subscription")
    public void brokerSubscription(@RequestParam(value = "brokerName", required = false) String brokerName,
                                   @RequestParam(value = "brokerAddr", required = false) String brokerAddr,
                                   HttpServletResponse resp) throws Exception {
        emitBroker(resp, brokerName, brokerAddr, RequestCode.GET_ALL_SUBSCRIPTION_GROUP, null);
    }

    /** broker {@code QUERY_DATA_VERSION(324)} → DataVersion{stateVersion, timestamp}（主从增量判断用的版本号） */
    @GetMapping(PREFIX + "/broker/data-version")
    public void brokerDataVersion(@RequestParam(value = "brokerName", required = false) String brokerName,
                                  @RequestParam(value = "brokerAddr", required = false) String brokerAddr,
                                  HttpServletResponse resp) throws Exception {
        emitBroker(resp, brokerName, brokerAddr, RequestCode.QUERY_DATA_VERSION, null);
    }

    // ==================================================== 兜底: 不许变成通用转发器

    /**
     * {@code /api/mq/**} 的其余子路径一律 404, 且把可用端点列表回显。
     *
     * <p>这一条是刻意写的: ttc-mq 的"上游"不是 HTTP, 所以这里<b>不可能</b>做 vector/graph 那种
     * 通配透传。写死兜底是为了防止后来人"顺手补个透传" —— 那等于把 Netty 协议面 + 全部写码
     * 打开成 HTTP 面。新端点只能一个个加只读码。
     */
    @RequestMapping(path = PREFIX + "/**",
            method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public void fallback(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String sub = req.getRequestURI();
        Map<String, Object> body = err("no such endpoint on the ttc-mq read surface", sub);
        body.put("method", req.getMethod());
        body.put("available", new ArrayList<Object>(READ_ONLY_CODES.keySet()));
        writeJson(resp, 404, body);
    }

    // ================================================================= 内部实现

    private Map<String, Object> callNamesrv(int code, Map<String, String> ext) {
        return invoke(namesrvAddr, code, ext, "namesrv");
    }

    private Map<String, Object> invoke(String addr, int code, Map<String, String> ext, String tier) {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("tier", tier);
        r.put("target", addr);
        r.put("requestCode", code);
        r.put("requestName", CODE_NAMES.get(String.valueOf(code)));
        if (addr == null || !LOOPBACK_ADDR.matcher(addr).matches()) {
            r.put("httpStatus", 403);
            r.put("error", "refused non-loopback target: " + addr
                    + "（只允许拨 127.0.0.1/localhost/[::1]:port；nameserver 发现的地址若指向外部主机一律拒绝）");
            return r;
        }
        MQClientInstance client = null;
        long t0 = System.currentTimeMillis();
        try {
            client = new MQClientInstance("ttc-opc-mq-admin-" + clientSeq.incrementAndGet(),
                    namesrvAddr, new NettyClientConfig());
            client.start();
            RemotingCommand request = RemotingCommand.createRequestCommand(code);
            if (ext != null) {
                for (Map.Entry<String, String> e : ext.entrySet()) {
                    request.addExtField(e.getKey(), e.getValue());
                }
            }
            RemotingCommand response = client.invokeSync(addr, request, INVOKE_TIMEOUT_MS);
            r.put("elapsedMs", System.currentTimeMillis() - t0);
            // ⚠ 这一格刻意不叫 "code": 前端 common/utils/request.ts 的 defaultUnwrap 见到
            //    {code, data} 两个键就按 ttc-opc 的 Result 信封解包, 并要求 code 落在 2xx ——
            //    而 ttc-mq 成功回的恰恰是 0, 于是**每个正常的只读码响应**都会在浏览器里变成
            //    reject(new Error("请求失败")): HTTP 200、curl 有完整 JSON, 页面却四档全红。
            //    名字与上面的 requestCode 成对, 也省掉"这是 HTTP 码还是业务码"的歧义。
            r.put("responseCode", response.getCode());
            r.put("remark", response.getRemark());
            r.put("data", parseBody(response.getBody()));
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            r.put("httpStatus", 504);
            r.put("error", "interrupted while invoking " + CODE_NAMES.get(String.valueOf(code)));
        } catch (Exception e) {
            r.put("elapsedMs", System.currentTimeMillis() - t0);
            r.put("httpStatus", 502);
            r.put("error", e.getClass().getSimpleName() + ": " + safeMsg(e));
            r.put("hint", buildHint(addr, e));
        } finally {
            if (client != null) {
                try {
                    client.shutdown();
                } catch (Exception ignore) {
                    // 探针用的短命客户端, 关不掉也不能把错误盖掉
                }
            }
        }
        return r;
    }

    /**
     * broker 侧统一入口: 先问 nameserver 要地址, 再打码。
     * {@code brokerAddr} 只接受环回形式, 且只允许在 nameserver 确实报出过它时使用。
     */
    private void emitBroker(HttpServletResponse resp, String brokerName,
                            String brokerAddr, int code, Map<String, String> ext)
            throws Exception {
        Resolved resolved = resolveBrokerAddr(brokerName, brokerAddr);
        if (resolved.error != null) {
            Map<String, Object> body = err(resolved.error, brokerName == null ? brokerAddr : brokerName);
            body.put("discovered", resolved.discovered);
            // 连不上 nameserver 时这条错误本身就是最重要的孵化证据
            body.put("httpStatus", resolved.status);
            writeJson(resp, resolved.status, body);
            return;
        }
        Map<String, Object> r = invoke(resolved.addr, code, ext, "broker");
        if (resolved.noteMultiple) {
            r.put("multipleBrokersDiscovered", resolved.discovered);
            r.put("brokerNameNotGiven",
                    "发现了多个 broker 而请求没带 brokerName，这里取了注册表里的第一个；"
                            + "要指定就带 ?brokerName=");
        }
        emit(resp, r);
    }

    /** 取 JSON 对象的某个子节点；不是对象时返回 null（等价于 Jackson 的 {@code JsonNode.path()} 返回 MissingNode 的场景）。 */
    private static Object asChild(Object node, String key) {
        if (!(node instanceof Map)) {
            return null;
        }
        return ((Map<?, ?>) node).get(key);
    }

    private static class Resolved {
        String addr;
        String error;
        int status = 503;
        boolean noteMultiple;
        List<String> discovered = new ArrayList<>();
    }

    /**
     * 从 nameserver 的 {@code GET_BROKER_CLUSTER_INFO} 里解析 broker 地址（优先 brokerId=0 的 master）。
     * 不做任何缓存: 缓存会把"broker 已经消失"这件事藏起来, 而孵化期最需要的就是这件事。
     */
    private Resolved resolveBrokerAddr(String brokerName, String explicitAddr) {
        Resolved r = new Resolved();
        Map<String, Object> cluster = callNamesrv(RequestCode.GET_BROKER_CLUSTER_INFO, null);
        Object data = cluster.get("data");
        Object table = asChild(data, "brokerAddrTable");
        if (cluster.get("error") != null || !(table instanceof Map)) {
            r.error = "无法从 NameServer 发现 broker：" + (cluster.get("error") != null
                    ? cluster.get("error") : "GET_BROKER_CLUSTER_INFO 没有返回可解析的 body")
                    + (cluster.get("hint") != null ? " — " + cluster.get("hint") : "");
            r.status = statusOf(cluster);
            return r;
        }
        Map<String, String> byName = new LinkedHashMap<>();
        for (Map.Entry<?, ?> entry : ((Map<?, ?>) table).entrySet()) {
            String name = String.valueOf(entry.getKey());
            Object addrs = asChild(entry.getValue(), "brokerAddrs");
            String addr = null;
            if (addrs instanceof Map) {
                Map<?, ?> addrMap = (Map<?, ?>) addrs;
                Object master = addrMap.get("0");
                if (master == null) master = addrMap.get("00");
                if (master != null) addr = String.valueOf(master);
                if (addr == null && !addrMap.isEmpty()) {
                    addr = String.valueOf(addrMap.values().iterator().next());
                }
            }
            if (addr != null) {
                byName.put(name, addr);
                r.discovered.add(name + " -> " + addr);
            }
        }
        if (explicitAddr != null && !explicitAddr.isEmpty()) {
            // 显式地址必须是 nameserver 注册表里报出来的值之一 —— 否则等于让调用方指定拨号目标
            boolean known = byName.containsValue(explicitAddr);
            if (!known) {
                r.error = "拒绝未在 NameServer 注册表里出现过的 brokerAddr: " + explicitAddr;
                r.status = 403;
                return r;
            }
            r.addr = explicitAddr;
            return r;
        }
        if (brokerName != null && !brokerName.isEmpty()) {
            if (!SAFE_BROKER_NAME.matcher(brokerName).matches()) {
                r.error = "bad brokerName: " + brokerName;
                r.status = 400;
                return r;
            }
            String addr = byName.get(brokerName);
            if (addr == null) {
                r.error = "NameServer 的 brokerAddrTable 里没有 brokerName=" + brokerName;
                r.status = 404;
                return r;
            }
            r.addr = addr;
            return r;
        }
        if (byName.isEmpty()) {
            r.error = "NameServer 报出的 brokerAddrTable 为空 —— broker 没注册上来（通常是 "
                    + "REGISTER_BROKER 心跳还没跑，或 broker 根本没起）";
            r.status = 503;
            return r;
        }
        if (byName.size() > 1) {
            r.addr = byName.values().iterator().next();
            r.noteMultiple = true;
            return r;
        }
        r.addr = byName.values().iterator().next();
        return r;
    }

    /** 上游 JsonCodec 的 body 直接 readTree；解不动就退回原文字符串，绝不自己编字段 */
    private Object parseBody(byte[] body) {
        if (body == null || body.length == 0) {
            return null;
        }
        if (body.length > MAX_BODY_BYTES) {
            return "<body exceeds " + MAX_BODY_BYTES + " bytes, truncated>";
        }
        String raw = new String(body, StandardCharsets.UTF_8);
        try {
            return JacksonUtils.toObject(raw, Object.class);
        } catch (Exception e) {
            // 上游 handler 里有几条是手写字符串的情形；原样透出，页面自己标"非 JSON"
            Map<String, Object> w = new LinkedHashMap<>();
            w.put("notJson", true);
            w.put("raw", raw);
            return w;
        }
    }

    private int statusOf(Map<String, Object> r) {
        Object s = r.get("httpStatus");
        return s instanceof Integer ? (Integer) s : 200;
    }

    private void emit(HttpServletResponse resp, Map<String, Object> r) throws Exception {
        int http = statusOf(r);
        // 把"这不是本次构建的数据"这件事跟在每一个数据响应上，页面藏不住
        if (http == 200) {
            TtcMqEmbeddedServerConfig cfg = embeddedProvider.getIfAvailable();
            boolean isNamesrv = "namesrv".equals(String.valueOf(r.get("tier")));
            ChildState cs = childOf(cfg, isNamesrv ? "nameserverProcess" : "brokerProcess");
            int port = isNamesrv ? namesrvPort : brokerPort;
            Long owner = lsofOwnerPid(port);
            boolean owned = owner != null && owner.equals(cs.pid);
            r.put("attributionVerified", owned);
            if (!owned) {
                r.put("attributionUnverified", true);
                r.put("attributionNote", "应答 " + port
                        + " 的进程不是本 JVM spawn 的子进程（ownerPid=" + owner
                        + ", myChildPid=" + cs.pid + "）—— 下面的 data 是别人的集群状态，"
                        + "不能算本次孵化的结果");
            }
        }
        writeJson(resp, http, r);
    }

    private String buildHint(String addr, Exception e) {
        boolean connectFail = e instanceof com.taotao.cloud.mq.remoting.exception.RemotingConnectException
                || (e.getMessage() != null && e.getMessage().toLowerCase().contains("connect"));
        int port = portOf(addr);
        boolean reachable = tcpReachable(port);
        ChildState cs = childOf(embeddedProvider.getIfAvailable(),
                port == brokerPort ? "brokerProcess" : "nameserverProcess");
        if (!reachable) {
            return "端口 " + port + " 上没有任何监听者 ⇒ 内嵌子进程没起或 bind 失败。"
                    + " childSpawned=" + cs.seen + ", childAlive=" + cs.alive
                    + "（alive=true 而端口不通 = main 线程已因 BindException 退出、"
                    + "Netty 非守护线程吊着僵尸 JVM；见 __instance 的 *SpawnLog.lastBindError）";
        }
        Long owner = lsofOwnerPid(port);
        if (owner != null && !owner.equals(cs.pid)) {
            return "端口 " + port + " 可连，但属主 PID " + owner + " 不是本 JVM 的子进程（myChildPid="
                    + cs.pid + "）⇒ 那是外部常驻实例，它的数据不属于本次构建。"
                    + " 只有两种解法：让出端口，或给 ttc." + (port == brokerPort ? "broker" : "namesrv")
                    + "-port 换一个端口——代理层不会替你去占端口。";
        }
        return connectFail ? "环回端口 " + port + " 建连失败：" + safeMsg(e)
                : "上游返回异常，但归属检查未判定为外来（lsofAttributionEnabled="
                + lsofAttributionEnabled + "）：" + safeMsg(e);
    }

    private static int portOf(String addr) {
        if (addr == null) return -1;
        int i = addr.lastIndexOf(':');
        if (i < 0 || i == addr.length() - 1) return -1;
        try {
            return Integer.parseInt(addr.substring(i + 1));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // ------------------------------------------------------- 子进程 / 归属 取证

    private static class ChildState {
        boolean seen;
        boolean alive;
        Long pid;
    }

    /**
     * 反射读 {@link TtcMqEmbeddedServerConfig} 的私有 {@code nameserverProcess}/{@code brokerProcess}。
     *
     * <p>为什么用反射而不是给那个类加 getter：本卡硬约束是"只新增文件、禁止修改任何共享/跟踪文件"，
     * {@code TtcMqEmbeddedServerConfig} 是跟踪文件。加 public accessor 是更干净的形状，
     * 已作为可选粘贴块写进交接文件 §四；在它被采纳之前，反射这条路能让本类<b>零改动依赖</b>跑起来。
     * 拿不到就全部降级为 {@code seen=false}，绝不猜。
     */
    private ChildState childOf(TtcMqEmbeddedServerConfig cfg, String fieldName) {
        ChildState s = new ChildState();
        if (cfg == null) {
            return s;
        }
        s.seen = true;
        try {
            Field f = TtcMqEmbeddedServerConfig.class.getDeclaredField(fieldName);
            f.setAccessible(true);
            Object p = f.get(cfg);
            if (p instanceof Process) {
                Process proc = (Process) p;
                s.alive = proc.isAlive();
                s.pid = pidOf(proc);
            } else if (p == null) {
                s.seen = false; // 字段在，但没 spawn 成功（boot jar 没找到时会走这条）
            }
        } catch (NoSuchFieldException e) {
            s.seen = false;
        } catch (Throwable t) {
            // Java 25 上对应用各类的私有字段 setAccessible 是允许的；真被挡住就降级而不是抛
            s.alive = false;
        }
        return s;
    }

    /** Java 9+ 有 {@code Process.pid()}；1.8 目标不能直接调，所以反射方法，退化到 ProcessImpl 的私有 pid 字段 */
    private Long pidOf(Process proc) {
        // ⚠ 必须用 Process.class.getMethod 而不是 proc.getClass().getMethod:
        //    运行时真身 java.lang.ProcessImpl 是包私有类, 对它的 public 方法做 invoke 会先撞
        //    IllegalAccessException; 声明在 public 的 java.lang.Process 上就合法。
        //    而"反射 ProcessImpl 私有 pid 字段"在 JDK 25 上被强封装挡死 (InaccessibleObjectException)。
        //    两条都失败过 ⇒ pid=null ⇒ 与 lsof 属主比对永不相等 ⇒ 本次自己的集群被自证成"外来"。
        try {
            Object v = Process.class.getMethod("pid").invoke(proc);
            if (v instanceof Number) return ((Number) v).longValue();
        } catch (Throwable ignore) {
            // 只有真跑在 Java 8 上才会走到这里
        }
        try {
            Field f = proc.getClass().getDeclaredField("pid");
            f.setAccessible(true);
            Object v = f.get(proc);
            if (v instanceof Number) return ((Number) v).longValue();
        } catch (Throwable ignore) {
            // 拿不到就是拿不到
        }
        return null;
    }

    /** {@code lsof -nP -iTCP:<port> -sTCP:LISTEN -t} → 第一个 pid。失败/不可用返回 null（由上层报 unavailable） */
    private Long lsofOwnerPid(int port) {
        if (!lsofAttributionEnabled || port <= 0) {
            return null;
        }
        Process p = null;
        try {
            p = new ProcessBuilder("lsof", "-nP", "-iTCP:" + port, "-sTCP:LISTEN", "-t")
                    .redirectErrorStream(false).start();
            byte[] out = readAll(p.getInputStream(), 4096);
            if (!p.waitFor(2, java.util.concurrent.TimeUnit.SECONDS)) {
                p.destroyForcibly();
                return null;
            }
            String s = new String(out, StandardCharsets.UTF_8).trim();
            if (s.isEmpty()) {
                return null;
            }
            // 同一端口可能有多个 pid（罕见）：取第一个，剩下的交给页面去核
            return Long.parseLong(s.split("\\s+")[0]);
        } catch (Throwable t) {
            return null;
        } finally {
            if (p != null) {
                try {
                    p.destroy();
                } catch (Throwable ignore) {
                }
            }
        }
    }

    private boolean tcpReachable(int port) {
        if (port <= 0) return false;
        Socket s = new Socket();
        try {
            s.connect(new InetSocketAddress("127.0.0.1", port), TCP_PROBE_TIMEOUT_MS);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                s.close();
            } catch (Exception ignore) {
            }
        }
    }

    /**
     * 读 spawn 器的子进程日志，把最后一条 bind 异常捞出来。
     * 路径与  里写死的一致：
     * {@code ${java.io.tmpdir}/ttc-mq-<label>.log}。
     */
    private Map<String, Object> spawnLog(String label) {
        Map<String, Object> m = new LinkedHashMap<>();
        File f = new File(System.getProperty("java.io.tmpdir"), "ttc-mq-" + label + ".log");
        m.put("path", f.getAbsolutePath());
        m.put("exists", f.isFile());
        if (!f.isFile()) {
            m.put("lastBindError", null);
            return m;
        }
        m.put("lastModified", f.lastModified());
        try {
            long len = f.length();
            int tail = (int) Math.min(len, 256 * 1024);
            try (RandomAccessFile raf = new RandomAccessFile(f, "r")) {
                raf.seek(len - tail);
                byte[] buf = new byte[tail];
                raf.readFully(buf);
                String text = new String(buf, StandardCharsets.UTF_8);
                String[] lines = text.split("\\r?\\n");
                String last = null;
                int count = 0;
                for (String line : lines) {
                    if (line.contains("BindException") || line.contains("Address already in use")) {
                        count++;
                        last = line.trim();
                    }
                }
                m.put("bindErrorLines", count);
                m.put("lastBindError", last);
            }
        } catch (Exception e) {
            m.put("readError", e.getClass().getSimpleName() + ": " + safeMsg(e));
        }
        return m;
    }

    private static boolean isDirEmpty(File dir) {
        if (!dir.isDirectory()) return true;
        String[] names = dir.list();
        return names == null || names.length == 0;
    }

    // --------------------------------------------------------------- 工具

    private Map<String, Object> err(String msg, String path) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("status", "error");
        m.put("source", "ttc-opc-mq-proxy");
        m.put("message", msg);
        m.put("path", path);
        m.put("shape", "ttc-mq 无 HTTP 面；/api/mq/** 只暴露固定的只读请求码，见 __instance.readOnlyRequestCodes");
        return m;
    }

    private static String safeMsg(Throwable t) {
        String m = t.getMessage();
        return m == null ? "(no message)" : m;
    }

    private static String nsafe(Object o) {
        return o == null ? "" : String.valueOf(o);
    }

    private static byte[] readAll(InputStream in, int cap) throws java.io.IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        byte[] chunk = new byte[2048];
        int n, total = 0;
        while ((n = in.read(chunk)) > 0) {
            total += n;
            if (total > cap) break;
            buf.write(chunk, 0, n);
        }
        return buf.toByteArray();
    }

    private void writeJson(HttpServletResponse resp, int status, Object body) throws Exception {
        byte[] out = JacksonUtils.toJson(body).getBytes(StandardCharsets.UTF_8);
        resp.setStatus(status);
        resp.setContentType("application/json;charset=UTF-8");
        resp.setContentLength(out.length);
        resp.getOutputStream().write(out);
        resp.getOutputStream().flush();
    }
}
