package com.taotao.cloud.sys.biz.modules.compiler;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.tools.*;

import com.taotao.cloud.sys.biz.modules.classloader.ClassloaderService;
import com.taotao.cloud.sys.biz.modules.classloader.ExtendClassloader;
import com.taotao.cloud.sys.biz.modules.compiler.dtos.CompileResult;
import com.taotao.cloud.sys.biz.modules.compiler.dtos.ModuleCompileConfig;
import com.taotao.cloud.sys.biz.modules.compiler.dtos.SourceCompileConfig;
import com.taotao.cloud.sys.biz.modules.compiler.dtos.SourceJavaFileObject;
import com.taotao.cloud.sys.biz.modules.core.service.file.FileManager;
import com.taotao.cloud.sys.biz.modules.core.utils.URLUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JavaCompilerService {

    @Autowired
    private ClassloaderService classloaderService;

    @Autowired
    private FileManager fileManager;

    /**
     * 源码编译
     * @param sourceCompileConfig
     * @return
     */
    public CompileResult compileSourceFile(SourceCompileConfig sourceCompileConfig) throws URISyntaxException, IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        final DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();

        // 创建编译输出目录
        final File output = fileManager.mkTmpDir("compile/" + System.currentTimeMillis() + "/classes/");

        // 加载类加载器的 classpath
        List<File> classpaths = new ArrayList<>();
        final String classloaderName = sourceCompileConfig.getClassloaderName();
        if (StringUtils.isNotBlank(classloaderName)) {
            final ClassLoader classloader = classloaderService.getClassloader(classloaderName);
            if (classloader == ClassLoader.getSystemClassLoader()) {
                log.error("没有找到指定的类加载器:{}",classloaderName);
//                throw new ToolException("找不到类加载器:"+classloaderName);
            }else {
                ExtendClassloader extendClassloader = (ExtendClassloader) classloader;
                URL[] urLs = extendClassloader.getURLs();
                classpaths = URLUtil.convertFiles(Arrays.asList(urLs.clone()));
            }
        }
        // 获取 lombok , 目前只有这一个注解处理器
        final List<File> lombok = classpaths.stream().filter(file -> file.getName().contains("lombok")).collect(Collectors.toList());

        final StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(diagnosticCollector, null, null);
        standardFileManager.setLocation(StandardLocation.CLASS_PATH, classpaths);
        standardFileManager.setLocation(StandardLocation.ANNOTATION_PROCESSOR_PATH,lombok);
        standardFileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(output));

        // 获取 javaFileObject
        Iterable<? extends JavaFileObject> javaFileObjects = Arrays.asList(new SourceJavaFileObject(sourceCompileConfig.getClassName(), sourceCompileConfig.getSource()));

        List<String> options = Arrays.asList("-encoding", "utf-8","-source","1.8");
        JavaCompiler.CompilationTask task = compiler.getTask(null, standardFileManager, diagnosticCollector, options, null, javaFileObjects);
        final Boolean call = task.call();
        final CompileResult compileResult = new CompileResult(diagnosticCollector, call,output);

        // 获取编译后的文件列表, 如果成功编译的话
        final Collection<File> listFiles = FileUtils.listFiles(output, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        compileResult.setClassFiles(listFiles);
        return compileResult;
    }

    /**
     * 模块文件编译
     * @param moduleCompileConfig 模块编译配置, 如果不填写编译文件列表时, 将编译整个模块
     * @return
     */
    public CompileResult compileModuleFiles(ModuleCompileConfig moduleCompileConfig) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        final DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();

        final List<File> classpaths = moduleCompileConfig.getClasspaths();
        // classpath 需要添加输出目录的 class , 如果没有输出目录, 需要编译整个模块
        boolean compileModule = true;
        final File output = moduleCompileConfig.getOutput();
        if (output.exists()) {
            // 当输出目录存在时, 默认不需要编译整个模块
            classpaths.add(output);
            compileModule = false;
        }
        // 如果没有填写编译文件列表, 也是编译整个模块
        if (CollectionUtils.isEmpty(moduleCompileConfig.getCompileFiles())){
            compileModule = true;
        }

        // 获取 lombok , 目前只有这一个注解处理器
        final List<File> lombok = classpaths.stream().filter(file -> file.getName().contains("lombok")).collect(Collectors.toList());

        final StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(diagnosticCollector, null, null);
        standardFileManager.setLocation(StandardLocation.CLASS_PATH, classpaths);
        standardFileManager.setLocation(StandardLocation.ANNOTATION_PROCESSOR_PATH,lombok);
        if (output.exists()) {
            standardFileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(output));
        }
        standardFileManager.setLocation(StandardLocation.SOURCE_PATH,Arrays.asList(moduleCompileConfig.getSourcePath()));

        Iterable<? extends JavaFileObject> javaFileObjects = null;
        if (!compileModule) {
            final List<File> compileFiles = moduleCompileConfig.getCompileFiles();
            // 仅过滤出 java 文件进行编译, 如果没有一个文件, 直接响应编译成功
            final List<File> javaFiles = compileFiles.stream().filter(file -> {
                final String extension = FilenameUtils.getExtension(file.getName());
                if (StringUtils.equalsIgnoreCase(extension, "java")) {
                    return true;
                }
                return false;
            }).collect(Collectors.toList());

            if (CollectionUtils.isEmpty(javaFiles)){
                log.warn("没有可以编译的 java 文件列表");
                return new CompileResult(diagnosticCollector, true,output);
            }

            javaFileObjects = standardFileManager.getJavaFileObjects(javaFiles.toArray(new File[]{}));
        }else {
            // 如果需要编译整个模块, 获取模块所有代码进行编译
            final File sourcePath = moduleCompileConfig.getSourcePath();
            IOFileFilter ioFileFilter = new SuffixFileFilter("java");
            final Collection<File> listFiles = FileUtils.listFiles(sourcePath, ioFileFilter, TrueFileFilter.INSTANCE);
            javaFileObjects = standardFileManager.getJavaFileObjectsFromFiles(listFiles);
        }

        List<String> options = Arrays.asList("-encoding", "utf-8","-source","1.8");
        JavaCompiler.CompilationTask task = compiler.getTask(null, standardFileManager, diagnosticCollector, options, null, javaFileObjects);
        final Boolean call = task.call();
        final CompileResult compileResult = new CompileResult(diagnosticCollector, call,output);
        if (output.exists()) {
            final Collection<File> listFiles = FileUtils.listFiles(output, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
            compileResult.setClassFiles(listFiles);
        }
        return compileResult;
    }
}
