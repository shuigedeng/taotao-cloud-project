package com.taotao.cloud.ccsr.core.storage;

import com.taotao.cloud.ccsr.spi.SPI;

import java.util.List;
import java.util.Map;


@SPI
public interface Storage<T> {

    String put(T data);

    T get(String key);

    T delete(String key);

    List<T> list();
}
