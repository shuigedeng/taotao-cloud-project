package com.taotao.cloud.sys.biz.modules.versioncontrol.git;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sanri.tools.modules.core.exception.ToolException;
import com.sanri.tools.modules.core.service.file.FileManager;
import com.sanri.tools.modules.core.utils.OnlyPath;
import com.sanri.tools.modules.versioncontrol.dtos.ProjectLocation;
import com.sanri.tools.modules.versioncontrol.git.dtos.TarFileParam;

import lombok.extern.slf4j.Slf4j;

/**
 * 用于 git 的仓库管理:
 * <ul>
 *  <li>克隆仓库</li>
 *  <li>删除仓库</li>
 *  <li>判断本地仓库是否存在</li>
 *  <li>获取一个 GIT 本地仓库</li>
 *  <li>获取分组下的仓库名列表</li>
 *  <li>pull</li>
 * </ul>
 *
 * 仓库存储结构为 <br/>
 * <pre>
 *  $dataDir[Dir]
 *    gitrepositorys[Dir]
 *      group1[Dir]
 *        repository1[Dir]
 *        repository2[Dir]
 *      group2[Dir]
 *        repository3[Dir]
 * </pre>

 */
@Service
@Slf4j
public class GitRepositoryService {
    @Autowired
    private FileManager fileManager;
    @Autowired
    private GitSecurityService gitSecurityService;

    /**
     * 仓库基础路径  $dataDir/gitrepositorys
     */
    private static final String baseDirName = "gitrepositorys";

    /**
     * clone 一个仓库
     * @param group
     * @param url 示例: git@gitee.com:sanri/sanri-tools-maven.git
     *            示例2: https://gitee.com/sanri/sanri-tools-maven.git
     */
    public void cloneRepository(String group, String url) throws IOException, URISyntaxException, GitAPIException {
        // 根据 url 获取仓库名
        final String repositoryName = FilenameUtils.getBaseName(new File(url).getName());

        // 检查仓库是否已经存在
        final File repositoryDir = checkRepositoryExist(group, repositoryName);

        // 仓库不存在时, clone 仓库
        final CloneCommand cloneCommand = Git.cloneRepository().setURI(url).setDirectory(repositoryDir);
        gitSecurityService.addAuth(new GitSecurityService.AuthDto(cloneCommand,url,group));

        cloneCommand.call();
    }

    /**
     * 列出分组下的仓库列表
     * @param group
     * @return
     */
    public List<String> listGroupRepositorys(String group){
        final File baseDir = fileManager.mkDataDir(baseDirName);
        final File groupFile = new File(baseDir, group);
        List<String> repositorys = new ArrayList<>();
        final File[] files = groupFile.listFiles();
        for (File file : files) {
            if (file.isDirectory()){
                repositorys.add(file.getName());
            }
        }
        return repositorys;
    }

    /**
     * 删除一个仓库
     * @param group
     * @param repository
     */
    public void deleteRepository(String group,String repository) throws IOException {
        final File repositoryDir = loadRepositoryDir(group, repository);
        FileUtils.deleteDirectory(repositoryDir);
    }

    /**
     * 打开一个本地仓库
     * @param group
     * @param repository
     * @return
     * @throws IOException
     */
    public Git openGit(String group, String repository) throws IOException {
        final File repositoryDir = loadRepositoryDir(group, repository);
        return Git.open(repositoryDir);
    }

    /**
     * pull
     * @param group
     * @param repository
     */
    public void pull(String group,String repository) throws GitAPIException, IOException, URISyntaxException {
        Git git = openGit(group, repository);
        final PullCommand pullCommand = git.pull();
        gitSecurityService.addAuth(new GitSecurityService.AuthDto(pullCommand,group));
        final PullResult pullResult = pullCommand.call();
        log.info("拉取数据结果: {}",pullResult);
    }

    /**
     * 检查某个给定的仓库是否已经存在
     * @param group
     * @param repository
     * @return
     */
    public File checkRepositoryExist(String group,String repository) throws IOException {
        final File repositoryDir = loadRepositoryDir(group, repository);
        final File[] list = repositoryDir.listFiles();
        if (list.length == 0){
            return repositoryDir;
        }

        if (list.length == 1 && ".git".equals(list[0].getName())){
            // 如果仓库中只有 .git 隐藏文件, 则为上次下载失败, 删除文件并认为仓库不存在
            FileUtils.deleteDirectory(list[0]);
            return repositoryDir;
        }

        log.error("仓库[{}/{}]已经存在",group,repository);
        throw new ToolException("仓库" + repository + "已经存在");
    }

    /**
     * 获取仓库存储位置, 如果不存在则创建
     * @return
     */
    public File loadRepositoryDir(String group,String repository){
        final File baseDir = fileManager.mkDataDir(baseDirName);
        final File repositoryDir = new File(baseDir, group + "/" + repository);
        if (!repositoryDir.exists()){
            repositoryDir.mkdirs();
        }
        return repositoryDir;
    }

    /**
     * 获取项目路径
     * @param projectLocation
     * @return
     */
    public File loadProjectDir(ProjectLocation projectLocation){
        final File repositoryDir = loadRepositoryDir(projectLocation.getGroup(), projectLocation.getRepository());
        return new File(repositoryDir,projectLocation.getPath());
    }

    /**
     * 源码文件打包
     * @param tarFileParam
     * @return
     */
    public OnlyPath tarSourceFile(TarFileParam tarFileParam) throws IOException {
        final File targetDir = fileManager.mkTmpDir("code/gitpatch/source/" + System.currentTimeMillis());

        final File repositoryDir = loadRepositoryDir(tarFileParam.getGroup(), tarFileParam.getRepository());
        final List<OnlyPath> onlyPaths = tarFileParam.getRelativePaths().stream().map(OnlyPath::new).collect(Collectors.toList());

        // 根据是否需要层级结构决定不同的复制方式
        if (tarFileParam.isParallel()){
            // 如果是平级复制, 则直接复制所有文件到目标目录即可
            for (OnlyPath onlyPath : onlyPaths) {
                final File file = onlyPath.resolveFile(repositoryDir);
                if (file.isFile()){
                    FileUtils.copyFileToDirectory(file,targetDir);
                    continue;
                }
                final Collection<File> listFiles = FileUtils.listFiles(file, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
                for (File listFile : listFiles) {
                    FileUtils.copyFileToDirectory(listFile,targetDir);
                }
            }
        }else if (tarFileParam.isStruct()){
            // 如果是层级结构, 则需要在目标目录构建相应目录并进行复制
            for (OnlyPath onlyPath : onlyPaths) {
                final File file = onlyPath.resolveFile(repositoryDir);
                final OnlyPath parent = onlyPath.getParent();
                if (parent == null){
                   // 如果没有父级, 则属于仓库根路径下文件或目录, 直接复制即可
                    copyFileOrDir(file,targetDir);
                    continue;
                }
                final File realTargetDir = parent.resolveFile(targetDir);
                realTargetDir.mkdirs();
                copyFileOrDir(file,realTargetDir);
            }
        }else {
            throw new ToolException("不支持的文件结构, 请重新指定下载文件结构");
        }

        return fileManager.relativePath(new OnlyPath(targetDir));
    }

    private void copyFileOrDir(File sourceFile,File targetDir) throws IOException {
        if (sourceFile.isFile()){
            FileUtils.copyFileToDirectory(sourceFile, targetDir);
            return ;
        }
        FileUtils.copyDirectoryToDirectory(sourceFile, targetDir);
    }

}
