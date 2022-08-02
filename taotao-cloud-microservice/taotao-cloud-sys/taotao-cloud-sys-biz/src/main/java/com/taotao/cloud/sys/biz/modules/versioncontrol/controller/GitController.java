package com.taotao.cloud.sys.biz.modules.versioncontrol.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import com.taotao.cloud.sys.biz.modules.core.utils.OnlyPath;
import com.taotao.cloud.sys.biz.modules.versioncontrol.dtos.ChoseCommits;
import com.taotao.cloud.sys.biz.modules.versioncontrol.dtos.FilterBranchParam;
import com.taotao.cloud.sys.biz.modules.versioncontrol.dtos.PageBranches;
import com.taotao.cloud.sys.biz.modules.versioncontrol.dtos.ProjectLocation;
import com.taotao.cloud.sys.biz.modules.versioncontrol.git.GitBranchService;
import com.taotao.cloud.sys.biz.modules.versioncontrol.git.GitDiffService;
import com.taotao.cloud.sys.biz.modules.versioncontrol.git.GitRepositoryService;
import com.taotao.cloud.sys.biz.modules.versioncontrol.git.RepositoryMetaService;
import com.taotao.cloud.sys.biz.modules.versioncontrol.git.dtos.Branches;
import com.taotao.cloud.sys.biz.modules.versioncontrol.git.dtos.Commit;
import com.taotao.cloud.sys.biz.modules.versioncontrol.git.dtos.DiffChanges;
import com.taotao.cloud.sys.biz.modules.versioncontrol.git.dtos.DiffChangesTree;
import com.taotao.cloud.sys.biz.modules.versioncontrol.git.dtos.TarFileParam;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/git")
@Validated
public class GitController {
    @Autowired
    private GitRepositoryService gitRepositoryService;
    @Autowired
    private GitBranchService gitBranchService;
    @Autowired
    private RepositoryMetaService repositoryMetaService;
    @Autowired
    private GitDiffService gitDiffService;

    /**
     * 锁定一个仓库
     * @param group
     * @param repository
     * @throws IOException
     */
    @PostMapping("/lock")
    public void lock(@NotBlank String group, @NotBlank String repository) throws IOException {
        repositoryMetaService.lock(group,repository);
    }

    /**
     * 解锁仓库
     * @param group
     * @param repository
     * @param force
     * @throws IOException
     */
    @PostMapping("/unLock")
    public void unLock(@NotBlank String group, @NotBlank String repository,String force) throws IOException {
        repositoryMetaService.unLock(group,repository,Boolean.parseBoolean(force));
    }

    /**
     * 克隆仓库
     * @param group 分组
     * @param url 仓库地址
     */
    @GetMapping("/cloneRepository")
    public void cloneRepository(@NotBlank String group,@NotBlank String url) throws IOException, GitAPIException, URISyntaxException, GitAPIException, IOException, URISyntaxException {
        gitRepositoryService.cloneRepository(group,url);
    }

    /**
     * 获取仓库列表
     * @param group 分组
     * @return
     */
    @GetMapping("/repositories")
    public List<String> repositories(@NotBlank String group){
        return repositoryMetaService.listSortGroupRepository(group);
    }

    /**
     * 分支列表
     * @param group 分组名
     * @param repository 仓库名
     * @return
     * @throws IOException
     * @throws GitAPIException
     */
    @GetMapping("/branches")
    public Branches branches(@NotBlank String group, @NotBlank String repository) throws IOException, GitAPIException {
        final List<Branches.Branch> branches = gitBranchService.branches(group, repository);
        final String currentBranch = gitBranchService.currentBranch(group, repository);
        return new Branches(branches, currentBranch);
    }

    /**
     * 分页查询分支信息
     * @param filterBranchParam
     * @param pageParam
     * @return
     */
    @GetMapping("/filterBranchesPage")
    public PageBranches filterBranchesPage(FilterBranchParam filterBranchParam, PageParam pageParam) throws IOException, GitAPIException {
        final String group = filterBranchParam.getGroup();
        final String repository = filterBranchParam.getRepository();

        final List<Branches.Branch> branches = gitBranchService.branches(group, repository);

        // 过滤出需要的分支
        final List<Branches.Branch> filterBranches = branches.stream()
                .filter(branch -> branch.getBranchName().contains(filterBranchParam.getFilterValue()))
                .collect(Collectors.toList());
        final List<Branches.Branch> collect = filterBranches.stream().limit(pageParam.getPageSize() * pageParam.getPageNo()).collect(Collectors.toList());
        final String currentBranch = gitBranchService.currentBranch(group, repository);
        return new PageBranches(collect,filterBranches.size(),currentBranch);
    }


    /**
     * 当前分支名
     * @param group
     * @param repository
     * @return
     * @throws IOException
     */
    @GetMapping("/currentBranch")
    public String currentBranch(@NotBlank String group, @NotBlank String repository) throws IOException {
        return gitBranchService.currentBranch(group,repository);
    }

    /**
     * 切换分支
     * @param group 分组名
     * @param repository 仓库名
     * @param branchName 分支名
     */
    @GetMapping("/switchBranch")
    public String switchBranch(@NotBlank String group, @NotBlank String repository, @NotBlank String branchName) throws IOException, GitAPIException, URISyntaxException {
        return gitBranchService.switchBranch(group, repository, branchName);
    }

    /**
     * 更新提交记录
     * @param group 分组名
     * @param repository 仓库名
     */
    @GetMapping("/pull")
    public void pull(@NotBlank String group, @NotBlank String repository) throws GitAPIException, IOException, URISyntaxException {
        gitRepositoryService.pull(group,repository);
    }

    /**
     * 提交记录列表
     * @param group 分组名
     * @param repository 仓库名
     */
    @GetMapping("/commits")
    public List<Commit> commits(@NotBlank String group, @NotBlank String repository) throws IOException, GitAPIException {
        return gitBranchService.commits(group,repository,200);
    }

    /**
     * 获取变更文件记录列表
     * @param choseCommits
     * @return
     */
    @PostMapping("/diffChanges")
    public DiffChanges diffChanges(@RequestBody @Validated ChoseCommits choseCommits) throws IOException {
        final ProjectLocation projectLocation = choseCommits.getProjectLocation();
        return gitDiffService.parseDiffChanges(projectLocation.getGroup(), projectLocation.getRepository(), choseCommits.getCommitIds());
    }

    /**
     * 获取提交记录树状变更列表
     * @param choseCommits 选中的提交记录列表
     * @return
     */
    @PostMapping("/diffTree")
    public DiffChangesTree.TreeFile diffTree(@RequestBody @Validated ChoseCommits choseCommits) throws IOException {
        final ProjectLocation projectLocation = choseCommits.getProjectLocation();
        final DiffChangesTree.TreeFile treeFile = gitDiffService.parseDiffChangesTree(projectLocation, choseCommits.getCommitIds());
        return treeFile;
    }

    /**
     * 下载一个文件或目录(源码)
     * @return
     */
    @PostMapping("/source/download")
    public String downloadFile(@RequestBody @Validated TarFileParam tarFileParam) throws IOException {
        final OnlyPath onlyPath = gitRepositoryService.tarSourceFile(tarFileParam);
        return onlyPath.toString();
    }
}
