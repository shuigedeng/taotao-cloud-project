package com.taotao.cloud.sys.biz.controller.tools;

import com.alibaba.dubbo.remoting.RemotingException;
import com.taotao.cloud.sys.biz.tools.dubbo.DubboProviderDto;
import com.taotao.cloud.sys.biz.tools.dubbo.dtos.DubboInvokeParam;
import com.taotao.cloud.sys.biz.service.MainDubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/dubbo")
@Validated
public class DubboController {
    @Autowired
    private MainDubboService dubboService;

    @GetMapping("/connects")
    public List<String> connects(){
        return dubboService.connects();
    }

    /**
     * 所有的 dubbo 服务,在某个连接上
     * @param connName
     * @return
     */
    @GetMapping("/services")
    public List<String> services(@NotNull String connName) throws IOException {
        return dubboService.services(connName);
    }

    /**
     * 某个服务的提供者列表
     * @param connName
     * @param serviceName
     * @return
     */
    @GetMapping("/providers")
    public List<DubboProviderDto> providers(@NotNull String connName, @NotNull String serviceName) throws IOException {
        return dubboService.providers(connName,serviceName);
    }

    /**
     * 调用 dubbo 服务
     * @param dubboInvokeParam
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws RemotingException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @PostMapping("/invoke")
    public Object invoke(@RequestBody @Valid DubboInvokeParam dubboInvokeParam) throws ClassNotFoundException, NoSuchMethodException, RemotingException, InterruptedException, ExecutionException {
        return dubboService.invoke(dubboInvokeParam);
    }
}
