package com.taotao.cloud.agent.bytebuddyother.core.loader;


import com.taotao.cloud.agent.demo.common.Logger;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by pphh on 2022/8/4.
 */
public class AgentPluginClassLoader extends ClassLoader {

    private static AgentPluginClassLoader DEFAULT_LOADER;
    private static String pluginFilePath = "/tmp/phantom-agent-plugin.jar";

    private List<File> classpath;
    private List<Jar> allJars = new LinkedList<>();

    public static void initDefaultLoader() {
        if (DEFAULT_LOADER == null) {
            synchronized (AgentPluginClassLoader.class) {
                if (DEFAULT_LOADER == null) {
                    DEFAULT_LOADER = new AgentPluginClassLoader(AgentPluginClassLoader.class.getClassLoader());
                }
            }
        }
    }

    public AgentPluginClassLoader(ClassLoader parent) {
        super(parent);
        classpath = new LinkedList<>();
        classpath.add(new File(pluginFilePath));
    }


    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (allJars.size() == 0) {
            try {
                File file = new File(pluginFilePath);
                Jar jar = new Jar(new JarFile(file), file);
                allJars.add(jar);
            } catch (IOException e) {
                Logger.error("failed to load the plugin file, msg = %s", e.getMessage());
            }
        }

        String path = name.replace('.', '/').concat(".class");
        for (Jar jar : allJars) {
            JarEntry entry = jar.jarFile.getJarEntry(path);
            if (entry == null) {
                continue;
            }
            try {
                URL classFileUrl = new URL("jar:file:" + jar.sourceFile.getAbsolutePath() + "!/" + path);
                byte[] data;
                try (final BufferedInputStream is = new BufferedInputStream(
                        classFileUrl.openStream()); final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    int ch;
                    while ((ch = is.read()) != -1) {
                        baos.write(ch);
                    }
                    data = baos.toByteArray();
                }
                return defineClass(name, data, 0, data.length);
            } catch (IOException e) {
                Logger.error("find class fail, msg = %s", e.getMessage());
            }
        }

        throw new ClassNotFoundException("Can't find " + name);
    }

    private static class Jar {
        private final JarFile jarFile;
        private final File sourceFile;

        public Jar(JarFile jarFile, File sourceFile) {
            this.jarFile = jarFile;
            this.sourceFile = sourceFile;
        }
    }

}
