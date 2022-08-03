package com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.git;

import com.alibaba.fastjson.JSON;
import com.taotao.cloud.sys.biz.api.controller.tools.core.exception.ToolException;
import com.taotao.cloud.sys.biz.api.controller.tools.core.security.UserService;
import com.taotao.cloud.sys.biz.api.controller.tools.core.utils.NetUtil;
import com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.project.dtos.ProjectMeta;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 仓库元数据信息管理 <br/>
 * <ul>
 *     <li>管理仓库锁信息,因为使用本地文件夹, 同一时刻只能一个人访问</li>
 * </ul>
 *
 * 存储位置 <br/>
 * <pre>
 * $dataDir[Dir]
 *   gitrepositorys[Dir]
 *     group1[Dir]
 *       repository1[Dir]
 *       repository2[Dir]
 *       repository1_lock[File]
 *       repository1_info[File]
 *     group2[Dir]
 *       repository3[Dir]
 * </pre>

 */
@Service
@Slf4j
public class RepositoryMetaService {
    @Autowired(required = false)
    private UserService userService;

    @Autowired
    private GitRepositoryService gitRepositoryService;

    /**
     * 仓库信息, 包含上次访问时间等数据
     * group => repository => Info
     */
    private Map<String, Map<String, RepositoryMeta>> repositoryInfos = new ConcurrentHashMap<>();

    /**
     * 锁的最大空闲时间, 超过指定时间时将自动解锁
     * 10 分钟
     */
    public static final int LOCK_MAX_IDLE_TIME_MILLIS = 600000;

    /**
     * 分组仓库列表功能增强, 添加排序的能力
     * @param group
     * @return
     */
    public List<String> listSortGroupRepository(String group){
        final List<String> repositorys = gitRepositoryService.listGroupRepositorys(group);
        final Map<String, RepositoryMeta> repositoryInfo = repositoryInfos.computeIfAbsent(group, k -> new ConcurrentHashMap<>());
        Comparator<String> comparator = (a,b) -> {
            final RepositoryMeta infoA = repositoryInfo.computeIfAbsent(a, k -> new RepositoryMeta());
            final RepositoryMeta infoB = repositoryInfo.computeIfAbsent(b, k -> new RepositoryMeta());
            return (int) (infoA.lastAccessTime - infoB.lastAccessTime);
        };
        return repositorys.stream().sorted(comparator).collect(Collectors.toList());
    }

    /**
     * 给某个项目上锁, 这时其它人就只能读, 不能改, 需要支持可重入, 长时间未使用自动解锁
     * 使用文件锁, 存储的信息包含: 拥有者, 上锁时间
     * @param group
     * @param repository
     */
    public void lock(String group,String repository) throws IOException {
        String lockUser = NetUtil.request().getRemoteAddr();
        // 如果存在权限系统, 则根据使用者上锁
        if (userService != null){
            lockUser = userService.username();
        }

        // 查看锁文件是否存在
        final File repositoryDir = gitRepositoryService.loadRepositoryDir(group, repository);
        final File file = new File(repositoryDir.getParentFile(), repository + "_lock");
        if (file.exists()){
            // 如果文件存在, 解析内容, 看是否自己加的锁
            final String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            final LockDto lockDto = JSON.parseObject(content, LockDto.class);
            if (lockDto.owner.equals(lockUser)){
                log.debug("仓库 [{}/{}] 成功被 {} 锁定(重入)",group,repository,lockUser);
                lockDto.lastAccessTime = System.currentTimeMillis();
                FileUtils.writeStringToFile(file,JSON.toJSONString(lockDto),StandardCharsets.UTF_8);
                return ;
            }

            // 如果上一个使用者已经很长时间没有使用了, 则可以强制解锁, 否则提示被锁定
            if (System.currentTimeMillis() - lockDto.lastAccessTime < LOCK_MAX_IDLE_TIME_MILLIS){
                throw new ToolException("当前仓库 "+group+" - "+repository+" 已经被" + lockUser + " 锁定");
            }

            FileUtils.forceDelete(file);
        }

        final LockDto lockDto = new LockDto().init(lockUser);
        FileUtils.writeStringToFile(file,JSON.toJSONString(lockDto),StandardCharsets.UTF_8);
        log.info("仓库 [{}/{}] 成功被 {} 锁定",group,repository,lockUser);
    }

    /**
     * 项目解锁, 直接删除文件即可, 可以将 force 设置为 true 来强制解锁
     * @param group
     * @param repository
     * @param force
     */
    public void unLock(String group,String repository,boolean force) throws IOException {
        // 锁文件
        final File repositoryDir = gitRepositoryService.loadRepositoryDir(group, repository);
        final File file = new File(repositoryDir.getParentFile(), repository + "_lock");

        if (!file.exists()){
            log.debug("仓库 [{}/{}] 没有被锁定,无需解锁",group,repository);
            return ;
        }

        String lockUser = NetUtil.request().getRemoteAddr();
        // 如果存在权限系统, 则根据使用者上锁
        if (userService != null){
            lockUser = userService.username();
        }

        // 非使用者, 无法解锁
        final String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        final LockDto lockDto = JSON.parseObject(content, LockDto.class);
        if (!lockUser.equals(lockDto.owner) && !force){
            throw new ToolException("无法解锁,联系:"+ lockDto.owner);
        }

        // 解锁
        FileUtils.forceDelete(file);
    }

    /**
     * 访问仓库
     * @param group
     * @param repository
     */
    public void visitRepository(String group,String repository) throws IOException {
        final RepositoryMeta info = repositoryMeta(group, repository);
        info.setLastAccessTime(System.currentTimeMillis());
    }

    /**
     * 获取仓库信息
     * @param group
     * @param repository
     * @return
     */
    public RepositoryMeta repositoryMeta(String group, String repository) throws IOException {
        final Map<String, RepositoryMeta> repositoryInfo = repositoryInfos.computeIfAbsent(group, k -> new ConcurrentHashMap<>());
        final RepositoryMeta info = repositoryInfo.computeIfAbsent(repository, k -> {
            // 如果没有仓库信息, 从文件进行读取,文件没有的话,就设置一个默认
            final File repositoryDir = gitRepositoryService.loadRepositoryDir(group, repository);
            final File file = new File(repositoryDir.getParentFile(), repository + "_info");
            if (file.exists()) {
                try {
                    final String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                    return JSON.parseObject(content, RepositoryMeta.class);
                } catch (IOException e) {
                    log.error("读取仓库信息失败, 将使用默认值:{}", e.getMessage());
                }
            }
            return new RepositoryMeta();
        });
        return info;
    }

    /**
     * 每 10 分钟写入一次仓库信息
     */
    @Scheduled(fixedDelay = 600000,initialDelay = 10000)
    public void serializer(){
        final Iterator<Map.Entry<String, Map<String, RepositoryMeta>>> iterator = repositoryInfos.entrySet().iterator();
        while (iterator.hasNext()){
            final Map.Entry<String, Map<String, RepositoryMeta>> entry = iterator.next();
            final String group = entry.getKey();
            final Map<String, RepositoryMeta> repositoryInfo = entry.getValue();
            final Iterator<Map.Entry<String, RepositoryMeta>> entryIterator = repositoryInfo.entrySet().iterator();
            while (entryIterator.hasNext()){
                final Map.Entry<String, RepositoryMeta> infoEntry = entryIterator.next();
                final String repository = infoEntry.getKey();
                final File parentFile = gitRepositoryService.loadRepositoryDir(group, repository).getParentFile();
                final File file = new File(parentFile, repository + "_info");
                final RepositoryMeta info = infoEntry.getValue();
                try {
                    FileUtils.writeStringToFile(file, JSON.toJSONString(info), StandardCharsets.UTF_8);
                }catch (Exception e){
                    // ignore
                    log.warn("写入仓库信息[{}/{}]配置出错: {}",group,repository,e.getMessage());
                }
            }
        }
    }

    /**
     * 锁信息
     */
    @Data
    static final class LockDto{
        /**
         * 上锁者
         */
        private String owner;
        /**
         * 上锁时间
         */
        private long lockTime;

        /**
         * 最后一次访问时间
         */
        private long lastAccessTime;

        public LockDto() {
        }

        public LockDto init(String owner){
            this.owner = owner;
            this.lockTime = System.currentTimeMillis();
            this.lastAccessTime = this.lockTime;
            return this;
        }
    }

    /**
     * 仓库信息
     */
    @Data
    public static final class RepositoryMeta {
        /**
         * 上次访问时间
         */
        private long lastAccessTime;

        /**
         * 项目元数据信息
         */
        private Map<String, ProjectMeta> projectMetaMap = new HashMap<>();

        public RepositoryMeta() {
        }

        public RepositoryMeta(long lastAccessTime) {
            this.lastAccessTime = lastAccessTime;
        }

        /**
         * 添加一个项目元数据
         * @param projectMeta
         */
        public void addProjectMeta(ProjectMeta projectMeta){
            projectMetaMap.put(projectMeta.getProjectName(), projectMeta);
        }

        public Set<String> projects(){
            return projectMetaMap.keySet();
        }
    }
}
