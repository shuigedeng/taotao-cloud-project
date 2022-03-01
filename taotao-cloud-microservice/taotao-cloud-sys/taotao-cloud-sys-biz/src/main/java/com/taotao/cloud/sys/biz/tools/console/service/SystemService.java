package com.taotao.cloud.sys.biz.tools.console.service;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.KeyPair;
import groovy.util.logging.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class SystemService implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        // 如果没有公钥, 则生成公钥对
        final File file = publicKeyFile();
        if (!file.exists()){
            log.info("默认密钥对 id_rsa id_rsa.pub 不存在, 将生成密钥对");
            createDefaultJsch();
        }
    }

    /**
     * 生成默认的密钥对, 即 ~/.ssh/id_rsa.pub id_rsa
     */
    public void createDefaultJsch(){
        final File publicKeyFile = publicKeyFile();
        final File privateKeyFile = privateKeyFile();

        JSch jsch = new JSch();
        KeyPair kpair = null;
        try {
            kpair = KeyPair.genKeyPair(jsch, KeyPair.RSA);
            //私钥
            ByteArrayOutputStream baos = new ByteArrayOutputStream();//向OutPutStream中写入
            kpair.writePrivateKey(baos);
            String privateKeyString = baos.toString();
            FileUtils.writeStringToFile(privateKeyFile,privateKeyString, StandardCharsets.UTF_8);
            //公钥
            baos = new ByteArrayOutputStream();
            kpair.writePublicKey(baos, "SSHCerts");
            String publicKeyString = baos.toString();
            FileUtils.writeStringToFile(publicKeyFile,publicKeyString,StandardCharsets.UTF_8);
        } catch (JSchException | IOException e) {
            log.error("生成系统密钥对发生异常, 请自行生成密钥对, 以便需要 ssh 功能的模块使用, 异常信息为: {}",e.getMessage());
        } finally {
            if (kpair != null){
                kpair.dispose();
            }
        }
    }

    /**
     * 系统公钥文件
     * @return
     */
    public File publicKeyFile(){
        return new File(System.getProperty("user.home")+"/.ssh/id_rsa.pub");
    }

    /**
     * 系统私钥文件
     * @return
     */
    public File privateKeyFile(){
        return new File(System.getProperty("user.home")+"/.ssh/id_rsa");
    }
}
