package com.taotao.cloud.sys.biz.modules.versioncontrol.git;

import com.alibaba.fastjson.JSON;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.sanri.tools.modules.core.dtos.param.AuthParam;
import com.sanri.tools.modules.core.service.connect.ConnectService;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.TransportCommand;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.transport.*;
import org.eclipse.jgit.transport.http.HttpConnection;
import org.eclipse.jgit.transport.http.HttpConnectionFactory;
import org.eclipse.jgit.transport.http.JDKHttpConnectionFactory;
import org.eclipse.jgit.util.FS;
import org.eclipse.jgit.util.HttpSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

@Service
public class GitSecurityService {
    @Autowired
    private ConnectService connectService;

    /**
     * 模块 : git
     */
    private static final String MODULE = "git";

    /**
     * 命令授权
     * @param authDto
     */
    public void addAuth(AuthDto authDto) throws IOException, URISyntaxException {
        final TransportCommand transportCommand = authDto.getTransportCommand();

        // 获取仓库 url
        String url = authDto.getUrl();
        if (StringUtils.isBlank(url)){
            final List<RemoteConfig> allRemoteConfigs = RemoteConfig.getAllRemoteConfigs(transportCommand.getRepository().getConfig());
            final URIish urIish = allRemoteConfigs.get(0).getURIs().get(0);
            url = urIish.toString();
        }

        // 如果 git 开头, 则使用 sshKey 来授权, 否则使用帐号密码
        if (url.startsWith("git")){
            // 直接使用服务器的私钥, 让用户把服务器的公钥放到他仓库的 sshkey 里面去
            File sshKeyFile = new File(System.getProperty("user.home")+"/.ssh/id_rsa");
            final CustomSshSessionFactory customSshSessionFactory = new CustomSshSessionFactory(sshKeyFile);
            final SshTransportConfigCallback sshTransportConfigCallback = new SshTransportConfigCallback(customSshSessionFactory);
            transportCommand.setTransportConfigCallback(sshTransportConfigCallback);
            return ;
        }

        // 帐号密码授权
        final String content = connectService.loadContent(MODULE, authDto.getGroup());
        final AuthParam authParam = JSON.parseObject(content, AuthParam.class);
        final UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider = new UsernamePasswordCredentialsProvider(authParam.getUsername(), authParam.getPassword());
        transportCommand.setCredentialsProvider(usernamePasswordCredentialsProvider);
    }

    public class CustomSshSessionFactory extends JschConfigSessionFactory {
        private File sshKeyFile;

        public CustomSshSessionFactory(File sshKeyFile) {
            this.sshKeyFile = sshKeyFile;
        }

        @Override
        protected void configure(OpenSshConfig.Host hc, Session session) {
            session.setConfig("StrictHostKeyChecking", "no");
        }

        @Override
        protected JSch createDefaultJSch(FS fs) throws JSchException {
            final JSch defaultJSch = super.createDefaultJSch(fs);
//        defaultJSch.addIdentity(sshKeyFile.getAbsolutePath(),"");
            defaultJSch.addIdentity(sshKeyFile.getAbsolutePath());
            return defaultJSch;
        }
    }

    public class SshTransportConfigCallback implements TransportConfigCallback {
        private SshSessionFactory sshSessionFactory;

        public SshTransportConfigCallback(SshSessionFactory sshSessionFactory) {
            this.sshSessionFactory = sshSessionFactory;
        }

        @Override
        public void configure(Transport transport) {
            SshTransport sshTransport = (SshTransport) transport;
            sshTransport.setSshSessionFactory(sshSessionFactory);
        }
    }


    /**
     * 给命令授权需要提供的信息
     */
    @Data
    public static final class AuthDto{
        private TransportCommand transportCommand;
        /**
         * 如果是 clone , 需要提供 url ,其它命令不需要
         */
        private String url;
        private String group;

        public AuthDto(TransportCommand transportCommand, String group) {
            this.transportCommand = transportCommand;
            this.group = group;
        }

        public AuthDto(TransportCommand transportCommand, String url, String group) {
            this.transportCommand = transportCommand;
            this.url = url;
            this.group = group;
        }
    }

    static class InsecureHttpConnectionFactory implements HttpConnectionFactory {
        @Override
        public HttpConnection create(URL url ) throws IOException {
            return create( url, null );
        }

        @Override
        public HttpConnection create( URL url, Proxy proxy ) throws IOException {
            HttpConnection connection = new JDKHttpConnectionFactory().create( url, proxy );
            HttpSupport.disableSslVerify( connection );
            return connection;
        }
    }


    static {
        HttpTransport.setConnectionFactory(new InsecureHttpConnectionFactory());
    }
}
