package com.taotao.cloud.sys.biz.tools.core.service.classloader;

import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.sys.biz.tools.core.exception.ToolException;
import com.taotao.cloud.sys.biz.tools.core.service.classloader.dtos.LoadClassResponse;
import com.taotao.cloud.sys.biz.tools.core.service.classloader.dtos.LoadedClass;
import com.taotao.cloud.sys.biz.tools.core.service.file.FileManager;
import com.taotao.cloud.sys.biz.tools.core.utils.ZipUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.*;


@Service
public class ClassloaderService implements InitializingBean {
    // classloaderName ==> ExtendClassloader
    private static final Map<String,ExtendClassloader> CACHED_CLASSLOADER = new HashMap<>();

    @Autowired
    private FileManager fileManager;

    @Autowired
    private PomFileParser pomFileParser;

    /**
     * 获取类加载器列表
     */
    public Set<String> classloaders(){
        return CACHED_CLASSLOADER.keySet();
    }


    /**
     * @param classloaderName 类加载器名称
     * @param files 前面需要预先把文件存储在临时目录
     * @return 加载成功的 class 和 jar 包
     * @throws IOException
     * @throws ClassNotFoundException
     *
     * 这个是
     */
    public LoadClassResponse uploadClass(String classloaderName, List<File> files) throws IOException, ClassNotFoundException {
        LoadClassResponse loadClassResponse = new LoadClassResponse();
        // 清除无效文件
        final Iterator<File> iterator = files.iterator();
        while (iterator.hasNext()){
            final File file = iterator.next();
            final String extension = FilenameUtils.getExtension(file.getName());
            if (StringUtils.isBlank(extension)){
                LogUtil.warn("[{}]文件无效",file.getName());
                loadClassResponse.addRemoveFile(file.getName());
                iterator.remove();
                continue;
            }
            boolean effective = "xml".equalsIgnoreCase(extension) || "zip".equals(extension) || "class".equals(extension) || "jar".equals(extension);
            if (!effective){
	            LogUtil.warn("[{}]文件无效",file.getName());
                loadClassResponse.addRemoveFile(file.getName());
                iterator.remove();
            }
        }

        // 获取 classLoader
        final File classesDir = fileManager.mkDataDir("classloaders/"+classloaderName+"/classes");
        final URL url = classesDir.toURI().toURL();
        final ExtendClassloader extendClassloader = CACHED_CLASSLOADER.computeIfAbsent(classloaderName, key -> new ExtendClassloader(classloaderName, url));

        // 对 files 进行分类
        MultiValueMap<String,File> fileMultiValueMap = new LinkedMultiValueMap<>();
        for (File file : files) {
            final String extension = FilenameUtils.getExtension(file.getName());
            fileMultiValueMap.add(extension.toLowerCase(),file);
        }

        // 总共的 jar 文件列表
        final List<File> classifyJarFiles = fileMultiValueMap.get("jar");
        Set<File> allJarFiles = new HashSet<>();
        if (classifyJarFiles != null){
            allJarFiles.addAll(classifyJarFiles);
        }

        // 加载 pom 文件列表
        long startTime = System.currentTimeMillis();
        final List<File> pomFiles = fileMultiValueMap.get("xml");
        if (pomFiles != null) {
            for (File pomFile : pomFiles) {
	            LogUtil.info("当前加载 pom 文件[{}]", pomFile.getName());
                try {
                    final List<File> jarFiles = pomFileParser.loadDependencies(pomFile);
                    allJarFiles.addAll(jarFiles);
                } catch (Exception e) {
	                LogUtil.info("当前加载 pom 文件失败[{}],原因:{}", pomFile.getName(), e.getMessage());
                } finally {
	                LogUtil.info("加载文件[{}]的依赖项当前耗时:{} ms", pomFile.getName(), (System.currentTimeMillis() - startTime));
                }
            }
        }

        // 处理 jar 文件列表
        if (!CollectionUtils.isEmpty(allJarFiles)) {
            final File jarsDir = fileManager.mkDataDir("classloaders/" + classloaderName + "/jars");
            for (File jarFile : allJarFiles) {
                ReflectionUtils.invokeMethod(addURLMethod, extendClassloader, jarFile.toURI().toURL());
                // 需要把 jar 包移动到类加载器的数据目录
                FileCopyUtils.copy(jarFile,new File(jarsDir,jarFile.getName()));
            }
            loadClassResponse.addJars(allJarFiles.stream().map(File::getName).collect(Collectors.toList()));
        }

        // 加载零散的 class 文件, 先把文件放到临时目录, 进行类加载; 如果类加载失败, 则所有零散的类都不会放在类加载器的文件夹中
        final long currentLoadClassId = System.currentTimeMillis();
        final File loadClassesDir = fileManager.mkTmpDir("classloaderTemp/" + currentLoadClassId +"/classes");

        // 处理 class 文件
        List<ClassFile> targetClassFiles = new ArrayList<>();
        final List<File> classesFiles = fileMultiValueMap.get("class");
        if (classesFiles != null) {
            final List<ClassFile> targetPartClassFiles = moveClassFilesToTarget(loadClassesDir, classesFiles);
            targetClassFiles.addAll(targetPartClassFiles);
        }

        // 处理 zip 文件
        final File unZipDir = new File(loadClassesDir.getParentFile(),"zip");
        unZipDir.mkdirs();
        final List<File> zipFiles = fileMultiValueMap.get("zip");
        if (zipFiles != null) {
            for (File zipFile : zipFiles) {
                ZipUtil.unzip(zipFile, unZipDir.getAbsolutePath());
            }
        }
        // 解压完毕后, 查找所有的 class 文件进行移动
        final SuffixFileFilter classFileFilter = new SuffixFileFilter("class", IOCase.INSENSITIVE);
        final Collection<File> unzipClassFiles = FileUtils.listFiles(unZipDir, classFileFilter, TrueFileFilter.INSTANCE);
        final List<ClassFile> targetPartZipClassFiles = moveClassFilesToTarget(loadClassesDir, unzipClassFiles);
        targetClassFiles.addAll(targetPartZipClassFiles);

        // 处理 java 文件; 暂不支持, 依赖包不好弄
        final File compileDir = new File(loadClassesDir.getParentFile(), "compile");
        compileDir.mkdirs();
        final List<File> javaFiles = fileMultiValueMap.get("java");
        if (javaFiles != null) {
            for (File javaFile : javaFiles) {
                // 编译 java 文件, 并移动到 classes 目录
            }
        }

        // 最后, 添加一个扫描路径来加载新添加的类
        ReflectionUtils.invokeMethod(addURLMethod,extendClassloader,loadClassesDir.toURI().toURL());
        try {
            for (ClassFile targetClassFile : targetClassFiles) {
                final String className = targetClassFile.getClassName();
                extendClassloader.loadClass(className);
            }
            // 如果所有类加载成功,则移动类到类加载器目录, 把新建的类加载器目录的类删除
            for (ClassFile targetClassFile : targetClassFiles) {
                final String relativePath = RegExUtils.replaceAll(targetClassFile.getPackageName(), "\\.", "/") + "/"+targetClassFile.getClassFile().getName();
                final File file = new File(classesDir, relativePath);
                file.getParentFile().mkdirs();
                FileCopyUtils.copy(targetClassFile.getClassFile(),file);
            }

            // 添加所有加载成功的类
            final List<String> collect = targetClassFiles.stream().map(ClassFile::getClassName).collect(Collectors.toList());
            loadClassResponse.addClasses(collect);

            // 后面如果添加太多的 url 不行的话, 打开这一步, 新建一个类加载器, 重新加载类, 替换旧的加载器
//            final ExtendClassloader newExtendClassLoader = new ExtendClassloader(classloaderName, classesDir.toURI().toURL());
//            final Collection<File> classLoaderClassFiles = FileUtils.listFiles(classesDir, classFileFilter, TrueFileFilter.INSTANCE);
//            for (File classLoaderClassFile : classLoaderClassFiles) {
//                final Path relativize = classesDir.toPath().relativize(classLoaderClassFile.toPath());
//                List<String> paths = new ArrayList<>();
//                for (int i = 0; i < relativize.getNameCount(); i++) {
//                    paths.add(relativize.getName(i).toString());
//                }
//                String className = FilenameUtils.getBaseName(StringUtils.join(paths, '.'));
//                try {
//                    newExtendClassLoader.loadClass(className);
//                } catch (Exception e) {
//                    log.info("当前类加载器[{}]加载类[{}]失败,原因为:{}", classloaderName, className, e.getMessage());
//                }
//            }
//            CACHED_CLASSLOADER.put(classloaderName,newExtendClassLoader);

        }catch (ClassNotFoundException e){
            // 只要有一个类加载不成功,则所有零散的类将在本次加载失败
	        LogUtil.info("本次类加载[{}]失败,所有文件都不会进入类加载器{}:{}",currentLoadClassId,classloaderName,e.getMessage(),e);
            throw e;
        } finally {
            // 临时目录清空
            final File parentFile = loadClassesDir.getParentFile();
	        LogUtil.info("清空临时目录:{}",parentFile);
            FileUtils.forceDelete(parentFile);
        }

        return loadClassResponse;
    }

    /**
     * 移动类文件到目标目录
     * @param loadClassesDir 目标目录
     * @param classesFiles 所有类文件列表
     * @return
     * @throws IOException
     */
    private List<ClassFile> moveClassFilesToTarget(File loadClassesDir, Collection<File> classesFiles) throws IOException {
        List<ClassFile> targetClassFiles = new ArrayList<>();

        for (File classFile : classesFiles) {
            try(final FileInputStream fileInputStream = new FileInputStream(classFile)){
                ClassReader reader = new ClassReader(fileInputStream);
                //创建ClassNode,读取的信息会封装到这个类里面
                ClassNode classNode = new ClassNode();
                reader.accept(classNode, 0);
                final String className = classNode.name.replaceAll("/",".");
                final String packageName = FilenameUtils.getBaseName(className);
                final String classSimpleName = FilenameUtils.getExtension(className);
                if (StringUtils.isBlank(packageName)){
	                LogUtil.warn("类[{}]没有包名",className);
                    final File targetClassFile = new File(loadClassesDir, classFile.getName());
                    FileCopyUtils.copy(classFile,targetClassFile);
                    targetClassFiles.add(new ClassFile(packageName,className,classSimpleName,targetClassFile));
                    continue;

                }
                final File targetDir = new File(loadClassesDir, RegExUtils.replaceAll(packageName, "\\.", "/"));
                targetDir.mkdirs();
                final File targetClassFile = new File(targetDir, classFile.getName());
                FileCopyUtils.copy(classFile,targetClassFile);
                targetClassFiles.add(new ClassFile(packageName,className,classSimpleName,targetClassFile));
            }
        }

        return targetClassFiles;
    }

    public List<LoadedClass> listLoadedClasses(String classloaderName) {
        final ExtendClassloader extendClassloader = CACHED_CLASSLOADER.get(classloaderName);
        if (extendClassloader == null){
            throw new ToolException("指定的类加载器不存在");
        }
        List<LoadedClass> loadedClasses = new ArrayList<>();
        final Vector<Class<?>> loadClasses = extendClassloader.getLoadClasses();
        for (Class<?> loadClass : loadClasses) {
            int methods = 0,fields = 0;
            try {
                Field[] declaredFields = loadClass.getDeclaredFields();
                Method[] declaredMethods = loadClass.getDeclaredMethods();

                fields = declaredFields.length;
                methods = declaredMethods.length;
            } catch (Exception e) {
	            LogUtil.error("获取指定类[{}] 方法数和字段数出错:{}",loadClass.getName(),e.getMessage());
            } catch (Error e){
	            LogUtil.error("获取指定类[{}] 方法数和字段数出错:{}",loadClass.getName(),e.getMessage());
            }finally {
                loadedClasses.add(new LoadedClass(loadClass.getName(),fields,methods));
            }
        }
        return loadedClasses;
    }

    public static final class ClassFile{
        private String packageName;
        private String className;
        private String classSimpleName;
        private File classFile;

        public ClassFile(String packageName, String className, String classSimpleName, File classFile) {
            this.packageName = packageName;
            this.className = className;
            this.classSimpleName = classSimpleName;
            this.classFile = classFile;
        }

	    public String getPackageName() {
		    return packageName;
	    }

	    public void setPackageName(String packageName) {
		    this.packageName = packageName;
	    }

	    public String getClassName() {
		    return className;
	    }

	    public void setClassName(String className) {
		    this.className = className;
	    }

	    public String getClassSimpleName() {
		    return classSimpleName;
	    }

	    public void setClassSimpleName(String classSimpleName) {
		    this.classSimpleName = classSimpleName;
	    }

	    public File getClassFile() {
		    return classFile;
	    }

	    public void setClassFile(File classFile) {
		    this.classFile = classFile;
	    }
    }

    /**
     * 根据名称获取一个类加载器
     * @param classloaderName 类加载器名称
     * @return
     */
    public ClassLoader getClassloader(String classloaderName) {
        ClassLoader extendClassloader = CACHED_CLASSLOADER.get(classloaderName);
        if (extendClassloader == null){
            extendClassloader = ClassLoader.getSystemClassLoader();
        }
        return extendClassloader;
    }

    /**
     * 获取类加载器中的一个类
     * @param classloaderName 类加载器名称
     * @param className 类全路径
     * @return
     */
    public Class getClass(String classloaderName,String className) throws ClassNotFoundException {
        ClassLoader extendClassloader = CACHED_CLASSLOADER.get(classloaderName);
        if (extendClassloader == null){
            throw new ToolException("类加载器不存在:"+classloaderName);
        }
        return extendClassloader.loadClass(className);
    }

    /**
     * $dataDir[Dir]
     *   classloaders[Dir]
     *     mmclassloader[Dir]
     *       jars[Dir]   maven 下载的 jar 也放到这个里面,下次加载的时候直接放到类加载器的 url 中
     *         xx.jar
     *         commons.jar
     *       classes[Dir]       这里放零散的类
     *        com/sanri/xx.class
     *        com/sanri/domain/xx.class
     *     twoclassloader[Dir]
     *      ....
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // 加载之前上传的类信息
        final File dir = fileManager.mkDataDir("classloaders");
//        final Collection<File> classloadersFile = FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        final List<File> classloadersFile = Arrays.stream(dir.listFiles()).filter(File::isDirectory).collect(Collectors.toList());

        final SuffixFileFilter suffixFileFilter = new SuffixFileFilter("class", IOCase.INSENSITIVE);
        for (File classloaderDir : classloadersFile) {
            final String classloaderName = classloaderDir.getName();
            final File jarsDir = new File(classloaderDir, "jars");
            final File classesDir = new File(classloaderDir, "classes");

            final ExtendClassloader extendClassloader = new ExtendClassloader(classloaderName, classesDir.toURI().toURL());
            CACHED_CLASSLOADER.put(classloaderName,extendClassloader);

            // 加载 jar 包
            if (jarsDir.exists()) {
                loadJars(extendClassloader, jarsDir);
            }

            // 加载所有 classes
            if (classesDir.exists()) {
                final Collection<File> classesFiles = FileUtils.listFiles(classesDir, suffixFileFilter, TrueFileFilter.INSTANCE);
                for (File classesFile : classesFiles) {
                    final Path relativize = classesDir.toPath().relativize(classesFile.toPath());
                    List<String> paths = new ArrayList<>();
                    for (int i = 0; i < relativize.getNameCount(); i++) {
                        paths.add(relativize.getName(i).toString());
                    }
                    String className = FilenameUtils.getBaseName(StringUtils.join(paths, '.'));
                    try {
                        extendClassloader.loadClass(className);
                    } catch (Exception e) {
	                    LogUtil.info("当前类加载器[{}]加载类[{}]失败,原因为:{}", classloaderName, className, e.getMessage());
                    }
                }
            }
        }
    }

    private static final Field classesField;
    private static final Method addURLMethod;
    static {
        classesField = FieldUtils.getField(ClassLoader.class, "classes", true);
        addURLMethod = MethodUtils.getMatchingMethod(URLClassLoader.class,"addURL",URL.class);
        addURLMethod.setAccessible(true);
    }

    /**
     * 加载所有的 jar 包
     * @param extendClassloader
     * @param jarDir
     */
    private void loadJars(ExtendClassloader extendClassloader,File jarDir) throws MalformedURLException {
        final SuffixFileFilter suffixFileFilter = new SuffixFileFilter(".jar", IOCase.INSENSITIVE);
        final Collection<File> files = FileUtils.listFiles(jarDir, suffixFileFilter, TrueFileFilter.INSTANCE);
        for (File file : files) {
            final URL url = file.toURI().toURL();
            ReflectionUtils.invokeMethod(addURLMethod,extendClassloader,url);
        }
    }
}
