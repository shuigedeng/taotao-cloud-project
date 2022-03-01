package com.taotao.cloud.sys.biz.tools.core.dtos.param;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

public class KafkaConnectParam extends AbstractConnectParam{
    private KafkaProperties kafka;
    private String chroot = "/";        // kafka 在 zookeeper 上的数据路径,默认为 /

	public KafkaProperties getKafka() {
		return kafka;
	}

	public void setKafka(KafkaProperties kafka) {
		this.kafka = kafka;
	}

	public String getChroot() {
		return chroot;
	}

	public void setChroot(String chroot) {
		this.chroot = chroot;
	}
}
