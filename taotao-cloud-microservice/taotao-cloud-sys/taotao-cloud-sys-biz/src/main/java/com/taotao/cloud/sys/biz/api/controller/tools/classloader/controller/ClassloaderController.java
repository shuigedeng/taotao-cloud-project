package com.taotao.cloud.sys.biz.api.controller.tools.classloader.controller;

import com.taotao.cloud.sys.biz.api.controller.tools.classloader.ClassloaderService;
import com.taotao.cloud.sys.biz.api.controller.tools.classloader.DeCompileService;
import com.taotao.cloud.sys.biz.api.controller.tools.classloader.dtos.LoadClassResponse;
import com.taotao.cloud.sys.biz.api.controller.tools.classloader.dtos.LoadedClass;
import com.taotao.cloud.sys.biz.api.controller.tools.classloader.dtos.LoadedTreeFile;
import com.taotao.cloud.sys.biz.api.controller.tools.core.controller.dtos.ClassMethodInfo;
import com.taotao.cloud.sys.biz.api.controller.tools.core.controller.dtos.UploadClassInfo;
import com.taotao.cloud.sys.biz.api.controller.tools.core.service.data.RandomDataService;
import com.taotao.cloud.sys.biz.api.controller.tools.core.service.file.FileManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RegExUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @resourceName 类加载器
 * @parentMenu menu_level_1_basedata
 */
@RestController
@RequestMapping("/classloader")
@Validated
@Slf4j
public class ClassloaderController {
    @Autowired
    private ClassloaderService classloaderService;
    @Autowired
    private FileManager fileManager;
    @Autowired
    private DeCompileService deCompileService;
    @Autowired
    private RandomDataService randomDataService;

    /**
     * 类加载器文件系统结构
     * @param classloaderName
     * @return
     */
    @GetMapping("/loaderFilesTree")
    public LoadedTreeFile loaderFilesTree(String classloaderName){
        return classloaderService.loaderFilesTree(classloaderName);
    }

    /**
     * 加载类
     * @param classloaderName
     * @param className
     */
    @GetMapping("/loadClass")
    public void loadClass(String classloaderName,String className) throws ClassNotFoundException {
        classloaderService.loadClass(classloaderName,className);
    }

    /**
     * 上传单文件
     * @param classloaderName 类加载器名称
     * @param file 上传的 pom 文件
     * @return 加载类的结果
     */
    @PostMapping("/{classloaderName}/upload/single")
    public LoadClassResponse uploadSingle(@PathVariable("classloaderName") String classloaderName, MultipartFile file) throws IOException, ClassNotFoundException {
        final String originalFilename = file.getOriginalFilename();
        // 移动文件到临时目录
        final File uploadTemp = fileManager.mkTmpDir("uploadTemp/" + System.currentTimeMillis());
        final File targetFile = new File(uploadTemp, originalFilename);
        FileCopyUtils.copy(file.getInputStream(),new FileOutputStream(targetFile));

        // 上传类到类加载器
        List<File> files = new ArrayList<>();
        files.add(targetFile);
        try {
           return classloaderService.uploadClass(classloaderName, files,null);
        }finally {
            // 清空临时目录
            FileUtils.forceDelete(uploadTemp);
        }
    }

    /**
     * 多个文件一起上传到类加载器
     * @param uploadClassInfo 上传的类文件信息
     * @return
     */
    @PostMapping("/upload/multi")
    public LoadClassResponse uploadMulti(UploadClassInfo uploadClassInfo) throws IOException, ClassNotFoundException {
        final File uploadTemp = fileManager.mkTmpDir("uploadTemp/" + System.currentTimeMillis());
        List<File> files = new ArrayList<>();

        // 移动上传的文件
        if (ArrayUtils.isNotEmpty(uploadClassInfo.getFiles())) {
            for (MultipartFile file : uploadClassInfo.getFiles()) {
                final File moveTo = new File(uploadTemp, file.getOriginalFilename());
                files.add(moveTo);
                FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(moveTo));
            }
        }

        // 写入默认 pom 的内容
        final File pomFile = new File(uploadTemp, "defaultPom-" + System.currentTimeMillis() + ".xml");
        FileUtils.writeStringToFile(pomFile,uploadClassInfo.getPomContent(), StandardCharsets.UTF_8);
        files.add(pomFile);
        try {
            return classloaderService.uploadClass(uploadClassInfo.getClassloaderName(), files,uploadClassInfo.getSettings());
        }finally {
            // 清空临时目录
            try {
                FileUtils.forceDelete(uploadTemp);
            }catch (IOException e){
                //ignore
            }
        }
    }

    /**
     * 上传默认的 pom 信息, 前端界面可修改 pom 文件, 把这个 pom 文件内容上传, 将解析依赖, 并放到类加载器中
     * @param content
     * @return
     */
    @PostMapping("/{classloaderName}/upload/content")
    public LoadClassResponse uploadContent(@PathVariable("classloaderName") String classloaderName,String settings, @RequestBody String content) throws IOException, ClassNotFoundException {
        final File uploadTemp = fileManager.mkTmpDir("uploadTemp/" + System.currentTimeMillis());
        final File pomFile = new File(uploadTemp, "defaultPom-" + System.currentTimeMillis() + ".xml");
        FileUtils.writeStringToFile(pomFile,content, StandardCharsets.UTF_8);

        List<File> files = new ArrayList<>();
        files.add(pomFile);
        try{
            return classloaderService.uploadClass(classloaderName,files,settings);
        }finally {
            // 清空临时目录
            FileUtils.forceDelete(uploadTemp);
        }
    }

    /**
     * 获取类加载器列表
     * @return
     */
    @GetMapping("/classloaders")
    public Set<String> classloaders(){
        return classloaderService.classloaders();
    }

    /**
     * 获取类加载器加载的类列表
     * @param classloaderName 类加载器名称
     */
    @GetMapping("listLoadedClasses")
    public List<LoadedClass> listLoadedClasses(String classloaderName){
        return classloaderService.listLoadedClasses(classloaderName);
    }

    /**
     * 获取反编译的源文件
     * @param classloaderName 类加载器名称
     * @param className 类全路径
     * @return
     */
    @GetMapping("/deCompileClass")
    public String deCompileClass(String classloaderName,String className){
        final File dir = fileManager.mkDataDir("classloaders/" + classloaderName+"/classes");
        final String path = RegExUtils.replaceAll(className, "\\.", "/");
        final File classFile = new File(dir, path + ".class");
        return deCompileService.deCompile(classFile);
    }

    /**
     * 获取某个类的所有方法名
     * @param classloaderName 类加载器名称
     * @param className 类名称
     * @return
     */
    @GetMapping("/{classloaderName}/{className}/methodNames")
    public List<String> methodNames(@PathVariable("classloaderName") String classloaderName,@PathVariable("className") String className) throws ClassNotFoundException {
        final Class serviceClass = classloaderService.getClass(classloaderName, className);
        final Method[] declaredMethods = serviceClass.getDeclaredMethods();
        return Arrays.stream(declaredMethods).map(Method::getName).collect(Collectors.toList());
    }

    private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    /**
     * 获取某个类的某个方法信息
     * @param classloaderName 类加载器名称
     * @param className       类名称
     * @param methodName      方法名称
     *
     * 因为方法可能有多个, 所以这里会返回所有同名的方法
     */
    @GetMapping("/{classloaderName}/{className}/{methodName}/methodInfo")
    public List<ClassMethodInfo> methodInfo(@PathVariable("classloaderName") String classloaderName, @PathVariable("className") String className, @PathVariable("methodName") String methodName) throws ClassNotFoundException {
        final Class serviceClass = classloaderService.getClass(classloaderName, className);
        final Method[] declaredMethods = serviceClass.getDeclaredMethods();

        List<ClassMethodInfo> classMethodInfos = new ArrayList<>();
        for (Method declaredMethod : declaredMethods) {
            final String name = declaredMethod.getName();
            if (name.equals(methodName)){
                // 获取方法参数
                final Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
                final String[] parameterNames = parameterNameDiscoverer.getParameterNames(declaredMethod);
                List<ClassMethodInfo.Arg> args = new ArrayList<>();
                for (int i = 0; i < parameterNames.length; i++) {
                    final Class<?> parameterType = parameterTypes[i];
                    final ClassMethodInfo.JavaType javaType = new ClassMethodInfo.JavaType(parameterType.getName(), parameterType.getSimpleName());
                    final ClassMethodInfo.Arg arg = new ClassMethodInfo.Arg(javaType, parameterNames[i]);
                    args.add(arg);
                }
                final Class<?> returnType = declaredMethod.getReturnType();
                final ClassMethodInfo.JavaType javaType = new ClassMethodInfo.JavaType(returnType.getName(), returnType.getSimpleName());
                final ClassMethodInfo classMethodInfo = new ClassMethodInfo(name, args, javaType);
                classMethodInfos.add(classMethodInfo);
            }
        }
        return classMethodInfos;
    }

    /**
     * 构建方法参数,需要保证方法名是唯一的,如果有重载方法,将只取第一个重载方法的参数
     * @param classloaderName 类加载器名称
     * @param className 类名
     * @param methodName 方法名
     * @return
     */
    @GetMapping("/{classloaderName}/{className}/{methodName}/buildParams")
    public List<Object> buildParams(@PathVariable("classloaderName") String classloaderName, @PathVariable("className") String className,@PathVariable("methodName") String methodName) throws ClassNotFoundException {
        Class clazz = classloaderService.getClass(classloaderName,className);
        Method[] declaredMethods = clazz.getDeclaredMethods();
        Method method = null;
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.getName().equals(methodName)){
                method = declaredMethod;
            }
        }
        List<Object> paramValues = new ArrayList<>();
        if (method != null){
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (Class<?> parameterType : parameterTypes) {
                ClassLoader classLoader = parameterType.getClassLoader();
                if (classLoader == null){
                    Object populateData = randomDataService.populateDataStart(parameterType);
                    paramValues.add(populateData);
                }else {
                    Object randomData = randomDataService.randomData(parameterType.getName(), classLoader);
                    paramValues.add(randomData);
                }
            }
        }
        return paramValues;
    }
}
