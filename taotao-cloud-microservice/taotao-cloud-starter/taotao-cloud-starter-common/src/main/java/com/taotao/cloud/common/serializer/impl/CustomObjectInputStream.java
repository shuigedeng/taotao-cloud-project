package com.taotao.cloud.common.serializer.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class CustomObjectInputStream extends ObjectInputStream {
    private ClassLoader classLoader;

    public CustomObjectInputStream(InputStream in,ClassLoader classLoader) throws IOException {
        super(in);
        this.classLoader = classLoader;
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        String name = desc.getName();
        Class<?> aClass = classLoader.loadClass(name);
        if(aClass != null){
            return aClass;
        }
        return super.resolveClass(desc);
    }
}
