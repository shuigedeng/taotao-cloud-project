package com.taotao.cloud.sys.biz.modules.versioncontrol.controller;

import com.sanri.tools.maven.service.MavenPluginService;
import com.sanri.tools.maven.service.MavenSettingsResolve;
import com.sanri.tools.maven.service.dtos.ExecuteMavenPluginParam;
import com.sanri.tools.maven.service.dtos.GoalExecuteResult;
import com.sanri.tools.modules.compiler.dtos.CompileResult;
import com.sanri.tools.modules.core.service.file.TreeFile;
import com.sanri.tools.modules.core.utils.OnlyPath;
import com.sanri.tools.modules.core.utils.OnlyPaths;
import com.sanri.tools.modules.versioncontrol.dtos.*;
import com.sanri.tools.modules.versioncontrol.git.GitDiffService;
import com.sanri.tools.modules.versioncontrol.git.RepositoryMetaService;
import com.sanri.tools.modules.versioncontrol.git.dtos.DiffChanges;
import com.sanri.tools.modules.versioncontrol.git.dtos.TarFileParam;
import com.sanri.tools.modules.versioncontrol.project.JavacCompileService;
import com.sanri.tools.modules.versioncontrol.project.MavenProjectService;
import com.sanri.tools.modules.versioncontrol.git.GitRepositoryService;
import com.sanri.tools.modules.versioncontrol.project.dtos.*;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@RequestMapping("/project/maven")
@RestController
@Validated
public class MavenProjectController {
    @Autowired
    private MavenProjectService mavenProjectService;
    @Autowired
    private GitRepositoryService gitRepositoryService;
    @Autowired
    private GitDiffService gitDiffService;
    @Autowired
    private MavenPluginService mavenPluginService;
    @Autowired
    private JavacCompileService javacCompileService;
    @Autowired
    private RepositoryMetaService repositoryMetaService;
    @Autowired
    private MavenSettingsResolve mavenSettingsResolve;

    /**
     * 获取项目模块列表
     * @param group 分组
     * @param repository 仓库
     * @param path 相对于仓库的路径, 为解决一个仓库下有多个项目的情况
     * @return
     */
    @GetMapping("/modules")
    public List<Module> modules(@Validated ProjectLocation projectLocation) throws IOException {
        final List<PomFile> pomFiles = mavenProjectService.pomFiles(projectLocation);
        final List<Module> modules = mavenProjectService.modules(pomFiles);
        return modules;
    }

    /**
     * 猜测编译模块
     * @param choseCommits 选择的提交记录信息
     * @return
     */
    @PostMapping("/guessCompileModules")
    public List<Module> guessCompileModules(@RequestBody @Validated ChoseCommits choseCommits) throws IOException, GitAPIException {
        final ProjectLocation projectLocation = choseCommits.getProjectLocation();
        final String group = projectLocation.getGroup();
        final String repository = projectLocation.getRepository();
        final DiffChanges diffChanges = gitDiffService.parseDiffChanges(group, repository, choseCommits.getCommitIds());
        final File projectDir = gitRepositoryService.loadProjectDir(projectLocation);
        return mavenProjectService.guessCompileModules(projectDir,diffChanges);
    }

    /**
     * 执行多个 maven 目标命令
     * 可以执行编译指令
     * @param mavenGoalsParam
     * @return
     */
    @PostMapping("/execute/goals")
    public MavenPluginService.MavenExecuteLogFiles sendMavenGoals(@RequestBody @Validated MavenGoalsParam mavenGoalsParam) throws MavenInvocationException, XmlPullParserException, IOException {
        final File projectDir = gitRepositoryService.loadProjectDir(mavenGoalsParam.getProjectLocation());
        final File pomFile = new File(projectDir, mavenGoalsParam.getRelativePomFile());
        ExecuteMavenPluginParam executeMavenPluginParam = new ExecuteMavenPluginParam(pomFile,mavenGoalsParam.getGoals().toArray(new String[]{}));
        final GoalExecuteResult goalExecuteResult = mavenPluginService.executeMavenPluginGoals(mavenGoalsParam.getSettingsName(), executeMavenPluginParam);
        mavenProjectService.listenToUpdateProjectMeta(goalExecuteResult.getInvocationResultFuture(),mavenGoalsParam);
        return goalExecuteResult.getMavenExecuteLogFiles();
    }


    /**
     * 模块依赖 jar 包解析(将写入模块的 classpath )
     * @param moduleResolveDepParam
     * @return 响应 jar 文件树, 相对于仓库路径
     */
    @PostMapping("/resolve/dependencies")
    public TreeFile resolveDependencies(@RequestBody @Valid ModuleResolveDepParam moduleResolveDepParam) throws DependencyCollectionException, XmlPullParserException, DependencyResolutionException, ModelBuildingException, IOException {
        final ProjectLocation projectLocation = moduleResolveDepParam.getProjectLocation();
        final File projectDir = gitRepositoryService.loadProjectDir(projectLocation);

        final Collection<File> files = mavenProjectService.resolveModuleDependencies(projectLocation, moduleResolveDepParam.getSettings(), projectDir, moduleResolveDepParam.getRelativePomFile());
        final List<OnlyPath> collect = files.stream().map(OnlyPath::new).collect(Collectors.toList());
        final File localRepository = mavenSettingsResolve.getLocalRepository(moduleResolveDepParam.getSettings());
        final OnlyPath root = new OnlyPath(localRepository);
        return OnlyPaths.treeFiles(collect,root);
    }

    /**
     * 模块上次解析 classpath 时间
     * @param moduleResolveDepParam
     * @return
     */
    @PostMapping("/module/classpath/lastResolveTime")
    public Long lastResolveDependenciesTime(ModuleResolveDepParam moduleResolveDepParam) throws IOException {
        return mavenProjectService.readResolveDependenciesTime(moduleResolveDepParam.getProjectLocation(),moduleResolveDepParam.getRelativePomFile());
    }

    /**
     * 少量文件编译(javac 编译)
     * @param javacCompileFiles 编译文件列表
     * @return 编译结果列表
     */
    @PostMapping("/compileLittleFiles")
    public Map<String, CompileResult> compileLittleFiles(@RequestBody @Validated JavacCompileFiles javacCompileFiles) throws IOException {
        final ProjectLocation projectLocation = javacCompileFiles.getProjectLocation();
        final File projectDir = gitRepositoryService.loadProjectDir(projectLocation);
        final List<File> fileList = javacCompileFiles.getRelativePaths().stream().map(relativePath -> new File(projectDir, relativePath)).collect(Collectors.toList());
        final RepositoryMetaService.RepositoryMeta repositoryInfo = repositoryMetaService.repositoryMeta(projectLocation.getGroup(), projectLocation.getRepository());
        return javacCompileService.compileLittleFiles(projectLocation,projectDir,fileList);
    }

    /**
     * 使用提交记录来编译(javac 编译 )
     * @param javacCompileCommits
     * @return 编译结果列表
     */
    @PostMapping("/compile/commits")
    public Map<String, CompileResult> compileLittleFilesByCommits(@RequestBody @Validated ChoseCommits choseCommits) throws IOException {
        final ProjectLocation projectLocation = choseCommits.getProjectLocation();

        final DiffChanges diffChanges = gitDiffService.parseDiffChanges(projectLocation.getGroup(), projectLocation.getRepository(), choseCommits.getCommitIds());
        final File projectDir = gitRepositoryService.loadProjectDir(projectLocation);
        final List<File> relativePaths = diffChanges.getChangeFiles().stream()
                .map(DiffChanges.DiffFile::path)
                .map(relativePath-> new File(projectDir,relativePath))
                .collect(Collectors.toList());
        return javacCompileService.compileLittleFiles(projectLocation,projectDir, relativePaths);
    }

    /**
     * 使用 maven 进行编译(这个未测试)
     * @param choseCommits
     */
    @PostMapping("/compile/maven")
    public void compileMultiFilesByMaven(@RequestBody @Validated MavenCompileParam mavenCompileParam) throws IOException, MavenInvocationException, ExecutionException, XmlPullParserException, TimeoutException, InterruptedException {
        final ChoseCommits choseCommits = mavenCompileParam.getChoseCommits();
        final ProjectLocation projectLocation = choseCommits.getProjectLocation();
        final File projectDir = gitRepositoryService.loadProjectDir(projectLocation);

        final DiffChanges diffChanges = gitDiffService.parseDiffChanges(projectLocation.getGroup(), projectLocation.getRepository(), choseCommits.getCommitIds());
        final List<String> relativePaths = diffChanges.getChangeFiles().stream()
                .map(DiffChanges.DiffFile::path)
                .collect(Collectors.toList());
        mavenProjectService.compile(mavenCompileParam.getSettings(), projectDir, relativePaths);
    }

    /**
     * 解析出编译后文件元数据
     * @param tarFileParam
     * @return
     */
    @PostMapping("/bin/metaParse")
    public TarBinFileResult.BinFileMeta tarBinFileParse(@RequestBody @Validated TarFileParam tarFileParam) throws IOException {
        return gitDiffService.tarBinFileParse(tarFileParam);
    }

    /**
     * 编译后文件下载
     * @param tarFileParam
     * @return
     */
    @PostMapping("/bin/download")
    public TarBinFileResult downloadBinFiles(@RequestBody @Validated TarFileParam tarFileParam) throws IOException {
        return gitDiffService.tarBinFile(tarFileParam);
    }
}
