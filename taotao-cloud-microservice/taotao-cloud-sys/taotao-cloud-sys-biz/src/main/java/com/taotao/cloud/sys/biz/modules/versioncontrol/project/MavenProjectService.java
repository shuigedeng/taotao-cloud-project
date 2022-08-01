package com.taotao.cloud.sys.biz.modules.versioncontrol.project;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.sanri.tools.modules.core.dtos.RelativeFile;
import com.sanri.tools.modules.versioncontrol.project.dtos.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.apache.maven.shared.utils.cli.CommandLineException;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Service;

import com.google.common.util.concurrent.ListenableFuture;
import com.sanri.tools.maven.service.MavenCommonUsageService;
import com.sanri.tools.maven.service.MavenJarResolve;
import com.sanri.tools.maven.service.MavenPluginService;
import com.sanri.tools.maven.service.dtos.ExecuteMavenPluginParam;
import com.sanri.tools.maven.service.dtos.GoalExecuteResult;
import com.sanri.tools.maven.service.dtos.JarCollect;
import com.sanri.tools.modules.core.exception.ToolException;
import com.sanri.tools.modules.core.utils.OnlyPath;
import com.sanri.tools.modules.core.utils.OnlyPaths;
import com.sanri.tools.modules.versioncontrol.dtos.CompileFiles;
import com.sanri.tools.modules.versioncontrol.dtos.ProjectLocation;
import com.sanri.tools.modules.versioncontrol.git.GitDiffService;
import com.sanri.tools.modules.versioncontrol.git.GitRepositoryService;
import com.sanri.tools.modules.versioncontrol.git.dtos.DiffChanges;
import com.sanri.tools.modules.versioncontrol.project.compile.CompileResolve;

import lombok.extern.slf4j.Slf4j;

/**
 * maven 项目服务, 提供的能力有 <br/>
 * <ul>
 *     <li>获取项目所有 pom 文件</li>
 *     <li>项目模块列表树</li>
 *     <li>模块扩展元数据信息(上次编译时间)</li>
 * </ul>
 *
 * 存储结构为 <br/>
 * 大多数情况下,一个仓库只包含一个项目,所以 repository1 == project1 <br/>
 * <pre>
 * $dataDir[Dir]
 *   gitrepositorys[Dir]
 *     group1[Dir]
 *       repository1[Dir]
 *         project1_meta[File]
 *         repository1_info[File]
 * </pre>
 *
 */
@Service
@Slf4j
public class MavenProjectService implements InitializingBean {

    @Autowired
    private MavenPluginService mavenPluginService;
    @Autowired
    private MavenCommonUsageService mavenCommonUsageService;
    @Autowired
    private MavenJarResolve mavenJarResolve;
    @Autowired
    private ProjectMetaService projectMetaService;
    @Autowired
    private GitDiffService gitDiffService;
    @Autowired
    private GitRepositoryService gitRepositoryService;

    @Autowired(required = false)
    private List<CompileResolve> compileResolves = new ArrayList<>();

    /**
     * 加载项目所有的 pom 文件列表
     * @param root 项目根目录
     * @return 所有的 pom 文件
     */
    public List<PomFile> pomFiles(ProjectLocation projectLocation) throws IOException {
        final File root = gitRepositoryService.loadProjectDir(projectLocation);

        IOFileFilter fileFilter = new NameFileFilter("pom.xml");
        final Collection<File> listFiles = FileUtils.listFiles(root, fileFilter, TrueFileFilter.INSTANCE);
        if (CollectionUtils.isEmpty(listFiles)){
            return new ArrayList<>();
        }
        final List<PomFile> pomFiles = listFiles.stream().map(file -> new PomFile(root, file)).collect(Collectors.toList());

        // 获取模块最后编译时间
        final ProjectMeta projectMeta = projectMetaService.computeIfAbsent(projectLocation);
        final Map<String, ProjectMeta.ModuleCompileMeta> moduleCompileMetas = projectMeta.getModuleCompileMetas();
        for (PomFile pomFile : pomFiles) {
            final ProjectMeta.ModuleCompileMeta moduleCompileMeta = moduleCompileMetas.get(pomFile.getModuleName());
            if (moduleCompileMeta != null){
                pomFile.setLastCompileTime(new Date(moduleCompileMeta.getLastCompileTime()));
            }
        }

        return pomFiles;
    }

    /**
     * 根据 pom 文件列表得到模块树
     * @param pomFiles
     * @return
     */
    public List<Module> modules(Collection<PomFile> pomFiles){
        if (CollectionUtils.isEmpty(pomFiles)){
            return new ArrayList<>();
        }

        // 先把 pomFiles 映射成 Module
        final List<Module> modules = pomFiles.stream().map(Module::new).collect(Collectors.toList());

        // 映射成 map 结构
        Map<OnlyPath,Module> onlyPathModuleMap = new HashMap<>();
        for (Module module : modules) {
            final String relativePath = module.getPomFile().getRelativePath();
            final OnlyPath modulePath = new OnlyPath(relativePath).getParent();
            onlyPathModuleMap.put(modulePath,module);
        }

        // 生成树形结构
        for (Module module : modules) {
            final String relativePath = module.getPomFile().getRelativePath();
            OnlyPath modulePath = new OnlyPath(relativePath).getParent();
            final Module topModule = onlyPathModuleMap.get(modulePath.getParent());
            if (topModule != null) {
                topModule.getChildren().add(module);

                // 如果子模块没有编译时间, 则取父模块的编译时间
                if (module.getLastCompileTime() == null){
                    module.getPomFile().setLastCompileTime(topModule.getPomFile().getLastCompileTime());
                }
            }
        }

        // 找到顶层节点, 找路径最短的节点列表
        final List<OnlyPath> topPaths = OnlyPaths.filterShorterPaths(onlyPathModuleMap.keySet());

        // 再将路径信息映射成模块信息
        final List<Module> topModules = topPaths.stream().map(onlyPathModuleMap::get).collect(Collectors.toList());

        return topModules;
    }

    /**
     * 模块依赖 jar 文件解析(耗时)
     * @param pomFile
     * @param repositoryMeta
     * @param projectLocation
     * @param settingsName
     * @param projectDir
     * @return
     */
    public Collection<File> resolveModuleDependencies(ProjectLocation projectLocation, String settingsName, File projectDir, String relativePomFile) throws DependencyCollectionException, XmlPullParserException, DependencyResolutionException, ModelBuildingException, IOException {
        final File pomFile = new File(projectDir, relativePomFile);

        final ProjectMeta projectMeta = projectMetaService.computeIfAbsent(projectLocation);
        final ProjectMeta.ModuleCompileMeta moduleCompileMeta = projectMetaService.resolveModuleCompileMeta(projectLocation, relativePomFile);
        projectMeta.addModuleCompileMeta(moduleCompileMeta);

        // 解析依赖项
        final JarCollect jarCollect = mavenJarResolve.resolveJarFiles(settingsName, pomFile);
        final String classpath = jarCollect.getClasspath();
        projectMetaService.writeModuleClasspath(moduleCompileMeta,classpath);
        return jarCollect.getFiles();
    }

    /**
     * 读取模块上次解析 classpath 的时间
     * @param projectLocation
     * @param relativePomFile
     * @return
     */
    public Long readResolveDependenciesTime(ProjectLocation projectLocation, String relativePomFile) throws IOException {
        final ProjectMeta projectMeta = projectMetaService.computeIfAbsent(projectLocation);
        final ProjectMeta.ModuleCompileMeta moduleCompileMeta = projectMetaService.resolveModuleCompileMeta(projectLocation, relativePomFile);

        return projectMetaService.readModuleClassPathLastUpdateTime(moduleCompileMeta);
    }

    /**
     * 获取变更模块列表, 找到所有模块的编译顺序, 照顺序编译模块
     * @param settingsName maven settings 文件配置
     * @param projectDir 项目目录
     * @param relativePaths 变更文件的 pom 相对路径列表
     */
    public void compile(String settingsName,File projectDir,List<String> relativePaths) throws MavenInvocationException, XmlPullParserException, IOException, InterruptedException, ExecutionException, TimeoutException {
        // 查询项目模块顺序信息
        final List<String> moduleOrder = mavenCommonUsageService.validate(settingsName, new File(projectDir, "pom.xml"));

        // pom 文件映射成模块数据
        final Map<String, PomFile> pomFileMap = relativePaths.stream()
                .map(relativePath -> new PomFile(projectDir, relativePath))
                .collect(Collectors.toMap(PomFile::getModuleName, Function.identity()));

        // 过滤出真实编译模块列表和顺序
        List<String> needCompileModuleOrder = new ArrayList<>();
        for (String moduleName : moduleOrder) {
            if (!pomFileMap.containsKey(moduleName)){
                // 如果没有模块, 则跳过不需要编译
                continue;
            }
            needCompileModuleOrder.add(moduleName);
        }

        log.info("将顺序编译模块: {}", StringUtils.join(needCompileModuleOrder,","));
        // 对于需要编译的模块进行顺序编译
        for (String moduleName : needCompileModuleOrder) {
            final File pomFile = pomFileMap.get(moduleName).getPomFile();
            final ExecuteMavenPluginParam executeMavenPluginParam = new ExecuteMavenPluginParam(pomFile, "clean", "install");
            final GoalExecuteResult goalExecuteResult = mavenPluginService.executeMavenPluginGoals(settingsName, executeMavenPluginParam);

            // 需要提前把目前正在执行的数据和日志文件, 响应前端, 让前端可以实时知道编译情况(websocket)

            // 需要同步执行编译(阻塞)
            final InvocationResult invocationResult = goalExecuteResult.getInvocationResultFuture().get();
            if (invocationResult.getExitCode() == 0){
                continue;
            }

            final CommandLineException executionException = invocationResult.getExecutionException();
            log.error("编译模块 {} 时出错 {}",moduleName,invocationResult.getExitCode(),executionException);

        }

    }

    /**
     * 猜测编译模块
     * @param projectDir
     * @param diffChanges
     * @return
     */
    public List<Module> guessCompileModules(File projectDir,DiffChanges diffChanges){
        Set<PomFile> pomFiles = new HashSet<>();
        for (DiffChanges.DiffFile diffFile : diffChanges.getChangeFiles()) {
            final String relativePath = diffFile.path();
            final File file = new File(projectDir, relativePath);
            // 一直往上找到 pom 文件, 映射成 pomFile 对象
            File parent = file;
            while (!(parent = parent.getParentFile()).equals(projectDir)){
                final boolean isModuleDir = ArrayUtils.contains(parent.list(), "pom.xml");
                if (isModuleDir){
                    break;
                }
            }
            final File pomXmlFile = new File(parent, "pom.xml");
            final PomFile pomFile = new PomFile(projectDir, pomXmlFile);
            pomFiles.add(pomFile);
        }

        return modules(pomFiles);
    }

    /**
     * 解析出编译后文件列表
     * @param projectDir 项目目录
     * @param diffChanges 修改项
     */
    public CompileFiles resolveDiffCompileFiles(RelativeFile projectRelativeFile,DiffChanges diffChanges){
        final File projectDir = projectRelativeFile.relativeFile();
        CompileFiles compileFiles = new CompileFiles(diffChanges);
        A:for (DiffChanges.DiffFile modifyFile : diffChanges.getChangeFiles()) {
            // 找到当前文件的编译输出路径
            final File file = new File(projectDir, modifyFile.path());
            final File compilePath = findCompilePath(file, projectDir);

            if (!compilePath.exists()){
                log.warn("编译路径[{}]不存在",compilePath);
                continue ;
            }

            // 模块路径
            final File moduleDir = compilePath.getParentFile().getParentFile();
            final OnlyPath modulePath = new OnlyPath(projectDir).relativize(new OnlyPath(moduleDir));
            final RelativeFile relativeFile = new RelativeFile(projectDir, modulePath);

            // 使用特定的解析器, 进行编译解析
            for (CompileResolve compileResolve : compileResolves) {
                if (compileResolve.support(modifyFile)){
                    final CompileFiles.DiffCompileFile diffCompileFile = compileResolve.resolve(modifyFile,relativeFile,projectRelativeFile.path());
                    compileFiles.addDiffCompileFile(diffCompileFile);
                    continue A;
                }
            }
        }
        return compileFiles;
    }

    /**
     * 从变更文件中, 找到所有变更文件所在模块
     * @param changePaths
     * @return
     */
    public Set<OnlyPath> findPomFilesFromPaths(File repository, List<OnlyPath> changePaths){
        final String findFileName = "pom.xml";

        Set<OnlyPath> pomPaths = new HashSet<>();
        for (OnlyPath changePath : changePaths) {
            final File resolveFile = changePath.resolveFile(repository);
            if (findFileName.equals(resolveFile.getName())){
                pomPaths.add(changePath);
                continue;
            }

            OnlyPath parent = changePath;
            while (parent != null){
                File dir = parent.resolveFile(repository);
                final String[] list = dir.list();
                if (ArrayUtils.contains(list,findFileName)){
                    // 找到了父级模块, 不需要继续往上找了
                    pomPaths.add(parent.resolve(findFileName));
                    break;
                }
                parent = parent.getParent();
            }
        }
        return pomPaths;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        OrderComparator.sort(compileResolves);
    }

    /**
     * 查询文件的模块信息, 一级一级往上找, 直到找到 pom.xml 文件为止
     * @param file
     * @param stop
     * @return
     */
    public PomFile findPomFile(File file,File stop){
        if ("pom.xml".equalsIgnoreCase(file.getName())){
            return new PomFile(stop,file);
        }
        File parent = file;
        while (!parent.equals(stop) && !ArrayUtils.contains(parent.list(),"pom.xml")){
            parent = parent.getParentFile();
        }
        if (parent.equals(stop)){
            // 如果找到顶了
            if (ArrayUtils.contains(parent.list(),"pom.xml")){
                return new PomFile(stop,new File(stop,"pom.xml"));
            }
            throw new ToolException("是否不是 maven 项目, 找到根路径了, 还是没有 pom.xml 文件");
        }
        return new PomFile(parent,new File(parent,"pom.xml"));
    }

    /**
     * 根据文件,一级一级往上找, 直到找到 target 目录
     * @param file
     * @return
     */
    private File findCompilePath(File file,File stop){
        String compilePath = "target/classes";
        if ("pom.xml".equals(file.getName())){
            return new File(file.getParent(),compilePath);
        }
        File parent = file;
        while (!parent.equals(stop) && !ArrayUtils.contains(parent.list(),"target")){
            parent = parent.getParentFile();
        }
        if (parent.equals(stop)){
            if (ArrayUtils.contains(parent.list(),"target")){
                return new File(parent,compilePath);
            }
            throw new IllegalStateException("是否还未编译, 没有找到 target 目录,在文件:" + file);
        }
        return new File(parent,compilePath);
    }

    /**
     * 监听 maven 命令, 如果遇到编译命令, 需要获取模块编译成功时间
     * @param invocationResultFuture
     * @param mavenGoalsParam
     */
    public void listenToUpdateProjectMeta(ListenableFuture<InvocationResult> invocationResultFuture, MavenGoalsParam mavenGoalsParam) {
        final CompileTimeUpdateFutureListener compileTimeUpdateFutureListener = new CompileTimeUpdateFutureListener(mavenGoalsParam,invocationResultFuture);
        invocationResultFuture.addListener(compileTimeUpdateFutureListener,Executors.newSingleThreadExecutor());
    }

    public class CompileTimeUpdateFutureListener implements Runnable {
        private MavenGoalsParam mavenGoalsParam;
        private ListenableFuture<InvocationResult> invocationResultListenableFuture;

        public CompileTimeUpdateFutureListener(MavenGoalsParam mavenGoalsParam, ListenableFuture<InvocationResult> invocationResultFuture) {
            this.mavenGoalsParam = mavenGoalsParam;
            this.invocationResultListenableFuture = invocationResultFuture;
        }

        @Override
        public void run() {
            final List<String> goals = mavenGoalsParam.getGoals();
            if (goals.contains("compile")){
                try {
                    InvocationResult invocationResult = invocationResultListenableFuture.get();
                    if (invocationResult.getExitCode() == 0){
                        // 如果编译成功, 记录编译时间
                        final String moduleName = new OnlyPath(mavenGoalsParam.getRelativePomFile()).getParent().getFileName();
                        final ProjectMeta projectMeta = projectMetaService.computeIfAbsent(mavenGoalsParam.getProjectLocation());
                        final Map<String, ProjectMeta.ModuleCompileMeta> moduleCompileMetas = projectMeta.getModuleCompileMetas();
                        final ProjectMeta.ModuleCompileMeta moduleCompileMeta = moduleCompileMetas.computeIfAbsent(moduleName, k -> new ProjectMeta.ModuleCompileMeta(moduleName, mavenGoalsParam.getRelativePomFile()));
                        moduleCompileMeta.setLastCompileTime(System.currentTimeMillis());
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(),e);
                }
            }
        }
    }
}
