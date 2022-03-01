package com.taotao.cloud.sys.biz.tools.codepatch.controller;

import com.taotao.cloud.sys.biz.tools.codepatch.controller.dtos.GroupRepository;
import com.taotao.cloud.sys.biz.tools.codepatch.service.GitService;
import com.taotao.cloud.sys.biz.tools.codepatch.service.dtos.BatchCommitIdPatch;
import com.taotao.cloud.sys.biz.tools.codepatch.service.dtos.Branchs;
import com.taotao.cloud.sys.biz.tools.codepatch.service.dtos.ChangeFiles;
import com.taotao.cloud.sys.biz.tools.codepatch.service.dtos.Commit;
import com.taotao.cloud.sys.biz.tools.codepatch.service.dtos.PomFile;
import com.taotao.cloud.sys.biz.tools.core.service.file.FileManager;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/git")
public class GitController {

	@Autowired
	private GitService gitService;
	@Autowired
	private FileManager fileManager;

	@GetMapping("/cloneRepository")
	public void cloneRepository(String group, String url)
		throws IOException, GitAPIException, URISyntaxException {
		gitService.cloneRepository(group, url);
	}

	@GetMapping("/groups")
	public List<String> groups() {
		return gitService.groups();
	}

	@GetMapping("/poms")
	public List<PomFile> poms(String group, String repository) {
		return gitService.loadAllPomFile(group, repository);
	}

	@GetMapping("/modules")
	public List<Module> modules(String group, String repository) throws IOException {
		final List<PomFile> pomFiles = gitService.loadAllPomFile(group, repository);
		return gitService.modules(group, repository, pomFiles);
	}

//    @GetMapping("/compile")
//    public void compile(HttpServletRequest request,String websocketId,String group, String repository, String pomRelativePath) throws IOException, InterruptedException {
//        gitService.compile(request.getRemoteAddr(),websocketId,group,repository,pomRelativePath);
//    }

	@GetMapping("/repositorys")
	public String[] repositorys(String group) {
		return gitService.repositorys(group);
	}

	/**
	 * 分组和仓库列表, 这个是 groups 和 repositorys 接口的合并
	 */
	@GetMapping("/compose/groupAndRepo")
	public List<GroupRepository> groupRepositorys() {
		List<GroupRepository> groupRepositories = new ArrayList<>();

		final List<String> groups = gitService.groups();
		for (String group : groups) {
			final String[] repositorys = gitService.repositorys(group);
			if (repositorys != null) {
				groupRepositories.add(new GroupRepository(group, Arrays.asList(repositorys)));
			}
		}
		return groupRepositories;
	}

	/**
	 * 分支列表
	 *
	 * @param group      分组名
	 * @param repository 仓库名
	 * @return
	 * @throws IOException
	 * @throws GitAPIException
	 */
	@GetMapping("/branchs")
	public Branchs branchs(String group, String repository) throws IOException, GitAPIException {
		final List<Branchs.Branch> branchs = gitService.branchs(group, repository);
		final String currentBranch = gitService.currentBranch(group, repository);
		return new Branchs(branchs, currentBranch);
	}

	/**
	 * 切换分支
	 *
	 * @param group      分组名
	 * @param repository 仓库名
	 * @param branchName 分支名
	 * @return
	 * @throws IOException
	 * @throws GitAPIException
	 * @throws URISyntaxException
	 */
	@GetMapping("/switchBranch")
	public String switchBranch(String group, String repository, String branchName)
		throws IOException, GitAPIException, URISyntaxException {
		return gitService.switchBranch(group, repository, branchName);
	}

	/**
	 * 更新提交记录
	 *
	 * @param group      分组名
	 * @param repository 仓库名
	 * @throws GitAPIException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@GetMapping("/pull")
	public void pull(String group, String repository)
		throws GitAPIException, IOException, URISyntaxException {
		gitService.pull(group, repository);
	}

	/**
	 * 提交记录列表
	 *
	 * @param group      分组名
	 * @param repository 仓库名
	 * @return
	 * @throws IOException
	 * @throws GitAPIException
	 */
	@GetMapping("/commits")
	public List<Commit> commits(String group, String repository)
		throws IOException, GitAPIException {
		return gitService.listCommits(group, repository, 1000);
	}

	/**
	 * 获取变更的文件列表
	 *
	 * @param batchCommitIdPatch 提交记录
	 * @return
	 * @throws IOException
	 * @throws GitAPIException
	 */
	@PostMapping("/v2/changeFiles")
	public ChangeFiles changeFilesV2(@RequestBody BatchCommitIdPatch batchCommitIdPatch)
		throws IOException, GitAPIException {
		final String group = batchCommitIdPatch.getGroup();
		final String repository = batchCommitIdPatch.getRepository();
		return gitService.createPatch(group, repository, batchCommitIdPatch.getCommitIds());
	}

	/**
	 * 获取变更的文件列表 弃用  {@link GitController#changeFilesV2(com.sanri.tools.modules.codepatch.service.dtos.BatchCommitIdPatch)}
	 *
	 * @param commitIdPatch 提交记录
	 * @return
	 * @throws IOException
	 * @throws GitAPIException
	 */
	@Deprecated
	@PostMapping("/changeFiles")
	public ChangeFiles changeFiles(@RequestBody BatchCommitIdPatch commitIdPatch)
		throws IOException, GitAPIException {
		final String group = commitIdPatch.getGroup();
		final String repository = commitIdPatch.getRepository();
		final ChangeFiles changeFiles = gitService.createPatch(group, repository,
			commitIdPatch.getCommitBeforeId(), commitIdPatch.getCommitAfterId());
		return changeFiles;
	}

	/**
	 * 下载补丁, 弃用 {@link GitController#createPatchV2(com.sanri.tools.modules.codepatch.service.dtos.BatchCommitIdPatch)}
	 *
	 * @param commitIdPatch
	 * @return
	 * @throws IOException
	 * @throws GitAPIException
	 */
	@Deprecated
	@PostMapping("/createPatch")
	public String createPatch(@RequestBody BatchCommitIdPatch commitIdPatch)
		throws IOException, GitAPIException {
		final String group = commitIdPatch.getGroup();
		final String repository = commitIdPatch.getRepository();
		final ChangeFiles changeFiles = gitService.createPatch(group, repository,
			commitIdPatch.getCommitBeforeId(), commitIdPatch.getCommitAfterId());
		final File compileFile = gitService.findCompileFiles(group, repository, changeFiles,
			commitIdPatch.getTitle());
		final Path path = fileManager.relativePath(compileFile.toPath());

		return path.toString();
	}

	/**
	 * 下载补丁
	 *
	 * @param batchCommitIdPatch
	 * @return
	 * @throws IOException
	 * @throws GitAPIException
	 */
	@PostMapping("/v2/createPatch")
	public String createPatchV2(@RequestBody BatchCommitIdPatch batchCommitIdPatch)
		throws IOException, GitAPIException {
		final String group = batchCommitIdPatch.getGroup();
		final String repository = batchCommitIdPatch.getRepository();
		final ChangeFiles changeFiles = gitService.createPatch(group, repository,
			batchCommitIdPatch.getCommitIds());
		final File compileFile = gitService.findCompileFiles(group, repository, changeFiles,
			batchCommitIdPatch.getTitle());
		final Path path = fileManager.relativePath(compileFile.toPath());

		return path.toString();
	}

	/**
	 * 猜测编译模块
	 *
	 * @param batchCommitIdPatch 提交记录信息
	 * @return
	 */
	@PostMapping("/guessCompileModules")
	public List<Module> guessCompileModules(@RequestBody BatchCommitIdPatch batchCommitIdPatch)
		throws IOException, GitAPIException {
		final String group = batchCommitIdPatch.getGroup();
		final String repository = batchCommitIdPatch.getRepository();
		return gitService.guessCompileModules(group, repository, batchCommitIdPatch.getCommitIds());
	}

	/**
	 * 仓库锁定
	 *
	 * @param request    request
	 * @param group      分组
	 * @param repository 仓库名
	 * @throws IOException
	 */
	@GetMapping("/lock")
	public void lock(HttpServletRequest request, String group, String repository)
		throws IOException {
		gitService.lock(request.getRemoteAddr(), group, repository);
	}

	/**
	 * 解锁仓库
	 *
	 * @param group      分组
	 * @param repository 仓库名
	 * @param force      是否强制解锁
	 * @throws IOException
	 */
	@GetMapping("/unLock")
	public void unLock(String group, String repository, String force) throws IOException {
		gitService.unLock(group, repository, Boolean.parseBoolean(force));
	}

	/**
	 * 获取仓库最新的编译时间
	 *
	 * @param group      分组名
	 * @param repository 仓库名
	 * @return
	 * @throws IOException
	 */
	@GetMapping("/newCompileTime")
	public long newCompileTime(String group, String repository) throws IOException {
		return gitService.newCompileTime(group, repository);
	}
}
