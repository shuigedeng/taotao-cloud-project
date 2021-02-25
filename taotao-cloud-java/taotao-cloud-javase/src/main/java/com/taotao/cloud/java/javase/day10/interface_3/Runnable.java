package com.taotao.cloud.java.javase.day10.interface_3;

import java.io.Serializable;

public interface Runnable extends Swimable,Serializable,Cloneable{
	void run();
}
