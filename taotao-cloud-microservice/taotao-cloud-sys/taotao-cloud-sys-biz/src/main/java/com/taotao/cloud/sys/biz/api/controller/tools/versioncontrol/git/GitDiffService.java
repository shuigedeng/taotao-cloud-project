package com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.git;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.taotao.cloud.sys.biz.api.controller.tools.core.dtos.RelativeFile;
import com.taotao.cloud.sys.biz.api.controller.tools.core.security.UserService;
import com.taotao.cloud.sys.biz.api.controller.tools.core.service.file.FileManager;
import com.taotao.cloud.sys.biz.api.controller.tools.core.utils.OnlyPath;
import com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.dtos.CompileFiles;
import com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.dtos.ProjectLocation;
import com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.git.dtos.Commit;
import com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.git.dtos.DiffChanges;
import com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.git.dtos.DiffChangesTree;
import com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.git.dtos.TarFileParam;
import com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.project.MavenProjectService;
import com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.project.PatchManager;
import com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.project.dtos.PatchEntity;
import com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.project.dtos.TarBinFileResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import lombok.extern.slf4j.Slf4j;

/**
 * git 获取提交记录变更文件服务, 主要用于提供变更文件列表
 */
@Service
@Slf4j
public class GitDiffService {
    @Autowired
    private GitRepositoryService gitRepositoryService;
    @Autowired
    private GitBranchService gitBranchService;
    @Autowired
    private MavenProjectService mavenProjectService;
    @Autowired
    private FileManager fileManager;
    @Autowired
    private PatchManager patchManager;
    @Autowired(required = false)
    private UserService userService;

    /**
     * bin 文件打包之前分析编译后文件元数据
     * @param tarFileParam
     */
    public TarBinFileResult.BinFileMeta tarBinFileParse(TarFileParam tarFileParam) throws IOException {
        final ProjectLocation projectLocation = tarFileParam.getProjectLocation();
        final DiffChanges diffChanges = filterDiffChanges(tarFileParam);

        final File repositoryDir = gitRepositoryService.loadRepositoryDir(projectLocation.getGroup(),projectLocation.getRepository());
        final RelativeFile relativeFile = new RelativeFile(repositoryDir, new OnlyPath(projectLocation.getPath()));
        final CompileFiles compileFiles = mavenProjectService.resolveDiffCompileFiles(relativeFile, diffChanges);

        TarBinFileResult.BinFileMeta binFileMeta = new TarBinFileResult.BinFileMeta(compileFiles.getDiffCompileFiles());

        // 开始数量统计
        int deleteFileCount = 0, errorFileCount = 0, binFileCount = 0;
        for (CompileFiles.DiffCompileFile diffCompileFile : compileFiles.getDiffCompileFiles()) {
            final DiffChanges.DiffFile diffFile = diffCompileFile.getDiffFile();
            if (diffFile.getChangeType() == DiffEntry.ChangeType.DELETE){
                // 删除文件场景
                deleteFileCount++;
                continue;
            }
            final Collection<RelativeFile> binFiles = diffCompileFile.getCompileFiles();

            if (CollectionUtils.isEmpty(binFiles)){
                // 丢失编译文件场景
                errorFileCount++;
                continue;
            }

            // 正常场景
            binFileCount += binFiles.size();
        }
        binFileMeta.setErrorFileCount(errorFileCount);
        binFileMeta.setDeleteFileCount(deleteFileCount);
        binFileMeta.setBinFileCount(binFileCount);
        return binFileMeta;
    }

    /**
     * 将多个相对路径进行打包
     * @param group
     * @param repository
     * @param relativePath
     * @return
     */
    public TarBinFileResult tarBinFile(TarFileParam tarFileParam) throws IOException {
        final ProjectLocation projectLocation = tarFileParam.getProjectLocation();
        final DiffChanges diffChanges = filterDiffChanges(tarFileParam);

        // 查找编译后文件
        final File repositoryDir = gitRepositoryService.loadRepositoryDir(projectLocation.getGroup(),projectLocation.getRepository());
        final RelativeFile relativeFile = new RelativeFile(repositoryDir, new OnlyPath(projectLocation.getPath()));
        final CompileFiles compileFiles = mavenProjectService.resolveDiffCompileFiles(relativeFile, diffChanges);

        // 将编译后文件写入临时路径并打包
        final File targetDir = fileManager.mkTmpDir("code/gitpatch/bin/" + System.currentTimeMillis());
        final TarBinFileResult tarBinFileResult = new TarBinFileResult(fileManager.relativePath(new OnlyPath(targetDir)));

        // 写入总计信息
        final File allChange = new File(targetDir, "allchange.txt");
        final File deleteChange = new File(targetDir, "delete.txt");
        List<String> deleteLines = new ArrayList<>();

        List<String> lines = new ArrayList<>();
        lines.add("项目:"+projectLocation.getRepository());
        // 获取项目当前分支
        final String currentBranch = gitBranchService.currentBranch(projectLocation.getGroup(), projectLocation.getRepository());
        lines.add("增量分支:"+currentBranch);
        // 提交记录列表
        final List<Commit> commits = loadCommitInfos(projectLocation.getGroup(), projectLocation.getRepository(), tarFileParam.getCommitIds());
        lines.add("提交记录列表");
        // 因为前面提交记录是反过来的, 所以这里得倒序输出
        if (CollectionUtils.isNotEmpty(commits)) {
            for (int i = commits.size(); i > 0; i--) {
                Commit commit = commits.get(i - 1);
                lines.add(commit.toInfo());
            }
        }
        lines.add("当前打包变更文件信息");
        for (DiffChanges.DiffFile changeFile : diffChanges.getChangeFiles()) {
            lines.add(changeFile.toString());

            if (changeFile.getChangeType() == DiffEntry.ChangeType.DELETE){
                deleteLines.add(changeFile.path());
            }
        }
        FileUtils.writeLines(allChange,lines);
        if (CollectionUtils.isNotEmpty(deleteLines)) {
            FileUtils.writeLines(deleteChange, deleteLines);
        }

        // 写入编译后文件
        final File modifyFileDir = new File(targetDir, "modify");
        for (CompileFiles.DiffCompileFile diffCompileFile : compileFiles.getDiffCompileFiles()) {
            final DiffChanges.DiffFile diffFile = diffCompileFile.getDiffFile();
            if (diffFile.getChangeType() == DiffEntry.ChangeType.DELETE){
                // 删除的文件不做处理, 由实施人员查看删除文件列表来删除
                continue;
            }
            final Collection<RelativeFile> compileBinFiles = diffCompileFile.getCompileFiles();
            if (CollectionUtils.isEmpty(compileBinFiles)){
                // 如果没有 bin 文件, 说明未编译或者文件丢失
                continue;
            }

            if (tarFileParam.isStruct()){
                // 如果需要树形结构, 则以树形结构打包(模块路径取最后一级路径)
                final OnlyPath modulePath = diffCompileFile.getModulePath().path();
                final File parentFile = modulePath.lastPath().resolveFile(modifyFileDir);
                parentFile.mkdirs();

                for (RelativeFile compileBinFile : compileBinFiles) {
                    final OnlyPath path = compileBinFile.path();
                    final File targetFile = path.resolveFile(parentFile);
                    targetFile.getParentFile().mkdirs();

                    FileUtils.copyFileToDirectory(compileBinFile.relativeFile(),targetFile.getParentFile());
                }
            }else {
                // 平级结构打包
                for (RelativeFile compileBinFile : compileBinFiles) {
                    final File file = compileBinFile.relativeFile();
                    FileUtils.copyFileToDirectory(file,modifyFileDir);
                }
            }

        }

        // 添加到增量管理
        String username = userService != null ? userService.username() : null;
        String title = tarFileParam.getTitle();
        final OnlyPath path = tarBinFileResult.path();
        if (StringUtils.isBlank(title)){
            title = path.getFileName();
        }
        patchManager.addPatch(new PatchEntity(title, projectLocation.getGroup(), projectLocation.getRepository(), currentBranch,System.currentTimeMillis(),username,path.toString(),true));

        return tarBinFileResult;
    }


    /**
     * 过滤出变更文件
     * @param tarFileParam
     * @return
     * @throws IOException
     */
    public DiffChanges filterDiffChanges(TarFileParam tarFileParam) throws IOException {
        // 先根据提交记录列表, 找到变更文件列表
        final DiffChanges diffChanges = parseDiffChanges(tarFileParam.getGroup(), tarFileParam.getRepository(), tarFileParam.getCommitIds());

        // 过滤出需要打包的变更记录
        final List<String> relativePaths = tarFileParam.getRelativePaths();
        final List<DiffChanges.DiffFile> changeFiles = diffChanges.getChangeFiles();
        final Iterator<DiffChanges.DiffFile> iterator = changeFiles.iterator();
        while (iterator.hasNext()){
            final DiffChanges.DiffFile diffFile = iterator.next();
            if (!relativePaths.contains(diffFile.path())){
                iterator.remove();
            }
        }
        return diffChanges;
    }

    /**
     * 文件变更树
     * @param group
     * @param repositoryName
     * @param commitIds
     * @throws IOException
     * @return
     */
    public DiffChangesTree.TreeFile parseDiffChangesTree(ProjectLocation projectLocation, List<String> commitIds) throws IOException {
        final String group = projectLocation.getGroup();
        final String repositoryName = projectLocation.getRepository();
        final DiffChanges diffChanges = parseDiffChanges(group, repositoryName, commitIds);

        // 映射成 TreeDiffFile 列表
        List<DiffChangesTree.TreeFile> treeFiles = new ArrayList<>();
        for (DiffChanges.DiffFile changeFile : diffChanges.getChangeFiles()) {
            final DiffChangesTree.TreeFile treeDiffFile = new DiffChangesTree.TreeFile(changeFile);
            treeFiles.add(treeDiffFile);
        }

        // 映射成 map 结构
        Map<OnlyPath, DiffChangesTree.TreeFile> treeFileMap = new HashMap<>();
        for (DiffChangesTree.TreeFile treeFile : treeFiles) {
            treeFileMap.put(treeFile.getRelativePath(),treeFile);
        }

        // 将仓库路径做为模块添加到树, 用于非模块变更文件的挂载
        final File repositoryDir = gitRepositoryService.loadRepositoryDir(group, repositoryName);
        final OnlyPath repositoryPath = new OnlyPath(repositoryDir);
        final DiffChangesTree.TreeFile repositoryTreeFile = new DiffChangesTree.TreeFile(new OnlyPath("/pom.xml"));
        repositoryTreeFile.setParentRelativePath(new OnlyPath(repositoryDir.getName()));
        treeFileMap.put(repositoryPath,repositoryTreeFile);

        // 找到所有变更文件所在模块, 将模块添加到树
        final List<OnlyPath> chagePaths = diffChanges.getChangeFiles().stream().map(DiffChanges.DiffFile::path).map(OnlyPath::new).collect(Collectors.toList());
        final Set<OnlyPath> pomFilesFromPaths = mavenProjectService.findPomFilesFromPaths(repositoryDir, chagePaths);
        for (OnlyPath pomFilesFromPath : pomFilesFromPaths) {
            if (pomFilesFromPath.getParent() != null) {
                final DiffChangesTree.TreeFile treeFile = new DiffChangesTree.TreeFile(pomFilesFromPath);
                repositoryTreeFile.getChildren().add(treeFile);
                treeFileMap.put(pomFilesFromPath.getParent(), treeFile);

                // 获取模块上次 classpath 解析时间
                final Long resolveDependenciesTime = mavenProjectService.readResolveDependenciesTime(projectLocation, pomFilesFromPath.toString());
                treeFile.setClasspathResolveTime(resolveDependenciesTime);

                // 获取模块上次编译时间
            }
            // 如果 parent 为 null, 则模块为项目模块, 在前面已经添加到树, 不需要重复添加
        }

        // 生成树形结构
        for (DiffChangesTree.TreeFile treeFile : treeFiles) {
            final OnlyPath parentPath = treeFile.getRelativePath().getParent();
            if (parentPath == null){
                // 如果就是顶层路径, 则是挂载到项目下的
                repositoryTreeFile.getChildren().add(treeFile);
                treeFile.setParentRelativePath(repositoryTreeFile.getRelativePath());
                continue;
            }

            DiffChangesTree.TreeFile parentTreeFile = treeFileMap.get(parentPath);
            if (parentTreeFile != null){
                parentTreeFile.getChildren().add(treeFile);
                treeFile.setParentRelativePath(parentTreeFile.getRelativePath());
                continue;
            }

            // 如果父级不存在, 则创建父级并添加到树
            parentTreeFile = new DiffChangesTree.TreeFile(parentPath);
            parentTreeFile.getChildren().add(treeFile);
            treeFile.setParentRelativePath(parentTreeFile.getRelativePath());
            treeFileMap.put(parentPath,parentTreeFile);

            // 将创建的父级进行挂载, 一直往上, 直到找到模块进行挂载; 如果当前变更模块不属于模块, 则挂载到项目上
            boolean mount = false;
            OnlyPath top = parentPath.getParent();
            while (top != null){
                final DiffChangesTree.TreeFile topTreeFile = treeFileMap.get(top);
                if (topTreeFile != null){
                    topTreeFile.getChildren().add(parentTreeFile);
                    parentTreeFile.setParentRelativePath(topTreeFile.getRelativePath());
                    mount = true;
                    break;
                }
                top = top.getParent();
            }

            if (!mount){
                // 如果未能挂载,则挂载到项目模块
//                log.error("路径未挂载 :{}",parentPath);
                repositoryTreeFile.getChildren().add(parentTreeFile);
                parentTreeFile.setParentRelativePath(repositoryTreeFile.getRelativePath());
            }
        }

        return repositoryTreeFile;
    }

    /**
     * 从 commitIds 列表加载
     * @param commitIds 提交记录列表
     * @return
     */
    public List<Commit> loadCommitInfos(String group, String repositoryName, List<String> commitIds) throws IOException {
        final Git git = gitRepositoryService.openGit(group, repositoryName);

        final Repository repository = git.getRepository();

        List<Commit> commits = new ArrayList<>();

        try(TreeWalk treeWalk = new TreeWalk(repository);) {
            for (String commitId : commitIds) {
                final RevCommit revCommit = repository.parseCommit(repository.resolve(commitId));

                final Commit commit = new Commit(revCommit.getShortMessage(), revCommit.getAuthorIdent().getName(), new String(revCommit.getId().name()), new Date(((long)revCommit.getCommitTime()) * 1000));
                commits.add(commit);
            }
        }
        return commits;
    }

    /**
     * 分析出提交记录列表的文件修改信息
     * @param group
     * @param repository
     * @param commitIds
     * @return
     */
    public DiffChanges parseDiffChanges(String group, String repositoryName, List<String> commitIds) throws IOException {
        final File repositoryDir = gitRepositoryService.loadRepositoryDir(group, repositoryName);
        Git git = Git.open(repositoryDir);

        final Repository repository = git.getRepository();

        Collections.reverse(commitIds);

        List<List<DiffEntry>> allDiffEntries = new ArrayList<>();
        try(TreeWalk treeWalk = new TreeWalk(repository);){
            for (String commitId : commitIds) {
                final RevCommit revCommit = repository.parseCommit(repository.resolve(commitId));
                try(RevWalk revWalk = new RevWalk(repository);){
                    revWalk.markStart(revCommit);
                    revWalk.next();
                    final RevCommit nextCommit = revWalk.next();
                    if (nextCommit != null){
                        treeWalk.reset(nextCommit.getTree());
                        treeWalk.setRecursive(true);
                        treeWalk.addTree(revCommit.getTree());
                        final List<DiffEntry> diffEntries = DiffEntry.scan(treeWalk);
                        allDiffEntries.add(diffEntries);
                    }else{
                        treeWalk.reset(revCommit.getTree());
                        treeWalk.setRecursive(true);
                        List<DiffEntry> diffEntries = new ArrayList<>();
                        while (treeWalk.next()){
                            final String pathString = treeWalk.getPathString();
                            final DiffEntryAdd diffEntryAdd = new DiffEntryAdd(pathString);
                            diffEntries.add(diffEntryAdd);
                        }
                        allDiffEntries.add(diffEntries);
                    }
                }
            }
        }

        // 得到最后的记录变更(相同路径, 后面的变更覆盖前面的)
        Map<String,DiffEntry> diffEntryMap = new LinkedHashMap();
        for (List<DiffEntry> diffEntries : allDiffEntries) {
            for (DiffEntry diffEntry : diffEntries) {
                final DiffEntry.ChangeType changeType = diffEntry.getChangeType();
                switch (changeType){
                    case DELETE:
                        final String oldPath = diffEntry.getOldPath();
                        diffEntryMap.put(oldPath,diffEntry);
                        break;
                    case MODIFY:
                    case ADD:
                    case COPY:
                    case RENAME:
                        final String newPath = diffEntry.getNewPath();
                        diffEntryMap.put(newPath,diffEntry);
                        break;
                    default:
                }
            }
        }

        // 生成文件变更记录列表
        final List<DiffEntry> diffEntries = new ArrayList<>(diffEntryMap.values());
        DiffChanges diffChanges = new DiffChanges(commitIds);
        for (DiffEntry diffEntry : diffEntries) {
            switch (diffEntry.getChangeType()){
                case MODIFY:
                case ADD:
                case COPY:
                case RENAME:
                    diffChanges.addChangeFile(new DiffChanges.DiffFile(diffEntry.getChangeType(), new RelativeFile(repositoryDir,diffEntry.getNewPath())));
                    break;
                case DELETE:
                    diffChanges.addChangeFile(new DiffChanges.DiffFile(diffEntry.getChangeType(), new RelativeFile(repositoryDir,diffEntry.getOldPath())));
                    break;
                default:
            }
        }

        return diffChanges;
    }

    public static final class DiffEntryAdd extends DiffEntry {
        public DiffEntryAdd(String path) {
            this.changeType = ChangeType.ADD;
            this.newPath = path;
        }
    }

}
