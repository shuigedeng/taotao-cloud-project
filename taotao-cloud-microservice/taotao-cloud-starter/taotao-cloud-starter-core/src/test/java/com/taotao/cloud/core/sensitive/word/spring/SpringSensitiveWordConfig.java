//package com.taotao.cloud.core.sensitive.word.spring;
//
//
//import com.taotao.cloud.core.sensitive.word.bs.SensitiveWordBs;
//import com.taotao.cloud.core.sensitive.word.spring.database.MyDdWordAllow;
//import com.taotao.cloud.core.sensitive.word.spring.database.MyDdWordDeny;
//import com.taotao.cloud.core.sensitive.word.support.allow.WordAllows;
//@AutoConfiguration
//public class SpringSensitiveWordConfig {
//
//    @Autowired
//    private MyDdWordAllow myDdWordAllow;
//
//    @Autowired
//    private MyDdWordDeny myDdWordDeny;
//
//    /**
//     * 初始化引导类
//     * @return 初始化引导类
//     */
//    @Bean
//    public SensitiveWordBs sensitiveWordBs() {
//        SensitiveWordBs sensitiveWordBs = SensitiveWordBs.newInstance()
//                .wordAllow(WordAllows.chains(WordAllows.system(), myDdWordAllow))
//                .wordDeny(myDdWordDeny)
//                // 各种其他配置
//                .init();
//
//        return sensitiveWordBs;
//    }
//
//}
