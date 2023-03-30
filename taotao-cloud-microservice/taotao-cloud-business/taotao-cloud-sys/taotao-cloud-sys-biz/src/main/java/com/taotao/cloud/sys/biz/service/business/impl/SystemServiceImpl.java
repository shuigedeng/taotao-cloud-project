/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.sys.biz.service.business.impl;

import com.taotao.cloud.sys.biz.service.business.ISystemService;
import org.springframework.stereotype.Service;

/**
 * SystemServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-03 14:34:51
 */
@Service
public class SystemServiceImpl implements ISystemService {

    //	private Version version;
    //
    //	@Autowired
    //	private ApplicationContext applicationContext;
    //
    //	@PostConstruct
    //	public void printVersion(){
    //		try {
    ////            ClassPathResource version = new ClassPathResource("version.txt");
    //			final Resource resource = applicationContext.getResource("classpath:version.txt");
    ////            String fileToString = FileUtils.readFileToString(resource.getFile(),
    // StandardCharsets.UTF_8);
    //			final String fileToString = IOUtils.toString(resource.getInputStream(),
    // StandardCharsets.UTF_8);
    //			this.version = new Version(fileToString);
    //			log.info("当前工具版本:{}",fileToString);
    //		} catch (IOException e) {log.error("获取当前工具版本失败:{}",e.getMessage());}
    //	}
    //
    //	/**
    //	 * 获取当前版本
    //	 * @return
    //	 */
    //	public Version currentVersion() {
    //		return version;
    //	}
    //
    //    @Override
    //    public void afterPropertiesSet() throws Exception {
    //        // 如果没有公钥, 则生成公钥对
    //        final File file = publicKeyFile();
    //        if (!file.exists()){
    //            log.info("默认密钥对 id_rsa id_rsa.pub 不存在, 将生成密钥对");
    //            createDefaultJsch();
    //        }
    //    }
    //
    //    /**
    //     * 生成默认的密钥对, 即 ~/.ssh/id_rsa.pub id_rsa
    //     */
    //    public void createDefaultJsch(){
    //        final File publicKeyFile = publicKeyFile();
    //        final File privateKeyFile = privateKeyFile();
    //
    //        JSch jsch = new JSch();
    //        KeyPair kpair = null;
    //        try {
    //            kpair = KeyPair.genKeyPair(jsch, KeyPair.RSA);
    //            //私钥
    //            ByteArrayOutputStream baos = new ByteArrayOutputStream();//向OutPutStream中写入
    //            kpair.writePrivateKey(baos);
    //            String privateKeyString = baos.toString();
    //            FileUtils.writeStringToFile(privateKeyFile,privateKeyString,
    // StandardCharsets.UTF_8);
    //            //公钥
    //            baos = new ByteArrayOutputStream();
    //            kpair.writePublicKey(baos, "SSHCerts");
    //            String publicKeyString = baos.toString();
    //            FileUtils.writeStringToFile(publicKeyFile,publicKeyString,StandardCharsets.UTF_8);
    //        } catch (JSchException | IOException e) {
    //            log.error("生成系统密钥对发生异常, 请自行生成密钥对, 以便需要 ssh 功能的模块使用, 异常信息为: {}",e.getMessage());
    //        } finally {
    //            if (kpair != null){
    //                kpair.dispose();
    //            }
    //        }
    //    }
    //
    //    /**
    //     * 系统公钥文件
    //     * @return
    //     */
    //    public File publicKeyFile(){
    //        return new File(System.getProperty("user.home")+"/.ssh/id_rsa.pub");
    //    }
    //
    //    /**
    //     * 系统私钥文件
    //     * @return
    //     */
    //    public File privateKeyFile(){
    //        return new File(System.getProperty("user.home")+"/.ssh/id_rsa");
    //    }
}
