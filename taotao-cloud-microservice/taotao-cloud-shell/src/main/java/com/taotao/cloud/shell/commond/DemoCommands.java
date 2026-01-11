/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.shell.commond;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.springframework.stereotype.Component;
import org.springframework.shell.core.command.annotation.Command;

/**
 * 时间命令
 */
@Component
public class DemoCommands {

    @Command(name = "changePassword", description = "查询当前时间")
    public String date() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    @Command(name = "readDemo", description = "读取指定文件夹内容")
    public String readDemo() {
        File file = new File("D:\\shell.txt");
        StringBuffer stringBuffer = new StringBuffer();
        try (FileInputStream fis = new FileInputStream(file);
                InputStreamReader inRead = new InputStreamReader(fis)) {
            BufferedReader br = new BufferedReader(inRead);
            String line;
            while ((line = br.readLine()) != null) {
                stringBuffer.append(line);
            }
        } catch (FileNotFoundException e) {
            // LogUtils.error(e);
        } catch (IOException e) {
            // LogUtils.error(e);
        }
        return stringBuffer.toString();
    }

    @Command(name = "cloneGit", description = "读取指定文件夹内容")
    public String cloneGit(String url) {
        // 克隆代码库命令
        CloneCommand cloneCommand = Git.cloneRepository();

        Git git = null;
        try {
            git =
                    cloneCommand
                            .setURI(url) // 设置远程URI
                            .setBranch("master") // 设置clone下来的分支
                            .setDirectory(new File("D:\\shell-git")) // 设置下载存放路径
                            .call();
        } catch (GitAPIException e) {
            return "error";
        }
        return "finish";
    }

    @Command(name = "getBranch", description = "获取指定仓库的分支")
    public String getBranch(String url) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            Collection<Ref> refList = Git.lsRemoteRepository().setRemote(url).call();
            List<String> branchnameList = new ArrayList<>(4);
            for (Ref ref : refList) {
                String refName = ref.getName();
                if (refName.startsWith("refs/heads/")) { // 须要进行筛选
                    String branchName = refName.replace("refs/heads/", "");
                    branchnameList.add(branchName);
                }
            }
            stringBuffer.append("共有分支" + branchnameList.size() + "个。");
            for (String item : branchnameList) {
                stringBuffer.append("[").append(item).append("]");
            }
        } catch (Exception e) {
            return "error";
        }
        return stringBuffer.toString();
    }
}
