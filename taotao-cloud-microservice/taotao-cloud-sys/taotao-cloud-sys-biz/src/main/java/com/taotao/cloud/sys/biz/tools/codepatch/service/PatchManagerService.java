package com.taotao.cloud.sys.biz.tools.codepatch.service;

import com.taotao.cloud.sys.biz.tools.codepatch.service.dtos.PatchEntity;
import com.taotao.cloud.sys.biz.tools.core.security.UserService;
import com.taotao.cloud.sys.biz.tools.core.service.file.FileManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PatchManagerService implements InitializingBean {
    @Autowired
    private FileManager fileManager;

    @Autowired(required = false)
    private UserService userService;
    /**
     * 增量列表
     */
    private List<PatchEntity> patchEntities = new ArrayList<>();

    /**
     * 添加一个增量
     * @param patchEntity
     * @throws IOException
     */
    public void addPatch(PatchEntity patchEntity) throws IOException {
        patchEntity.setEffect(true);

        patchEntities.add(patchEntity);

        serializer();
    }

    /**
     * 清空无效项目
     */
    public void cleanNotEffectPatch() throws IOException {
        checkEffect();

        final Iterator<PatchEntity> iterator = patchEntities.iterator();
        while (iterator.hasNext()){
            final PatchEntity next = iterator.next();
            if (!next.isEffect()){
                iterator.remove();
            }
        }

        serializer();
    }

    /**
     * 删除一个补丁记录
     * @param filePath
     * @throws IOException
     */
    public void deletePatch(String filePath) throws IOException {
        final Iterator<PatchEntity> iterator = patchEntities.iterator();
        while (iterator.hasNext()){
            final PatchEntity next = iterator.next();
            if (next.isEffect()){
                final File tmpDir = fileManager.mkTmpDir("");
                final File file = new File(tmpDir, filePath);
                if (file.exists()){
                    FileUtils.forceDelete(file);
                }
            }
            if (next.getFilePath().equals(filePath)){
                iterator.remove();
                break;
            }
        }
        serializer();
    }

    /**
     * 列出所有增量列表
     * 先排出有效的, 然后再按时间倒序
     * @return
     */
    public List<PatchEntity> listPatch(String group,String repository){
        checkEffect();

        final List<PatchEntity> filterPatchs = patchEntities.stream().filter(patchEntity ->
                (StringUtils.isNotBlank(group) && group.equals(patchEntity.getGroup())) ||
                        (StringUtils.isNotBlank(repository) && repository.equals(patchEntity.getRepository()))
        ).collect(Collectors.toList());

        Comparator<? super PatchEntity> comparator = (a,b) -> {
            if (!a.isEffect() && b.isEffect()){
                return -1;
            }
            if (a.isEffect() && !b.isEffect()){
                return 1;
            }
            return new Date(b.getTime()).compareTo(new Date(a.getTime()));
        };
        Collections.sort(filterPatchs,comparator);
        return filterPatchs;
    }

    /**
     * 序列化到文件, 检查文件是否可用
     */
    public void serializer() throws IOException {
        final File dir = fileManager.mkDataDir("gitpatch");
        final File patchs = new File(dir, "patchs");

        checkEffect();

        final List<String> collect = patchEntities.stream().map(PatchEntity::toString).collect(Collectors.toList());
        FileUtils.writeLines(patchs,collect);
    }

    /**
     * 检查是否所有增量都还有效
     */
    private void checkEffect() {
        final File tmpDir = fileManager.mkTmpDir("");

        for (PatchEntity patchEntity : patchEntities) {
            if (patchEntity.isEffect()) {
                final String filePath = patchEntity.getFilePath();
                final File file = new File(tmpDir, filePath);
                if (!file.exists()) {
                    patchEntity.setEffect(false);
                }
            }
        }
    }

    /**
     *
     * $dataDir[Dir]
     *   gitpatch[Dir]
     *     patchs[File]
     *      title:group:repository:branch:time:user:filePath:effect
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        final File dir = fileManager.mkDataDir("gitpatch");
        final File patchs = new File(dir, "patchs");
        if (patchs.exists()) {
            final List<String> lines = FileUtils.readLines(patchs, StandardCharsets.UTF_8);
            for (String line : lines) {
                final String[] patch = StringUtils.splitPreserveAllTokens(line, ":");
                final PatchEntity patchEntity = new PatchEntity(patch[0], patch[1], patch[2], patch[3], NumberUtils.toLong(patch[4]), patch[5], patch[6], Boolean.parseBoolean(patch[7]));
                patchEntities.add(patchEntity);
            }
        }
    }
}
