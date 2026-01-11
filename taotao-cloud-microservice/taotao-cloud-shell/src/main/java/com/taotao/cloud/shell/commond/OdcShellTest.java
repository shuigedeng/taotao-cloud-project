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

import jakarta.validation.constraints.Size;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.stereotype.Component;
import org.springframework.shell.core.command.annotation.Command;
import org.springframework.shell.core.command.annotation.Option;

/**
 * OdcShellTest Description
 *
 * @author lsj
 * @version odc-manage 1.0.0.RELEASE
 * <b>Creation Time:</b> 2021/7/29 14:18
 */
@Component
public class OdcShellTest {

    //	@Autowired
    //	private NodeService nodeService;
    //
    //	/**
    //	 * 基础的命令
    //	 *
    //	 * @return
    //	 */
    //	@Command(value = "输入两个整数，获取相加结果")
    //	//    @Command(name = "changePassword", description = "输入两个整数，获取相加结果")
    //	public List<Node> findAll() {
    //		return nodeService.findAll();
    //	}

    /**
     * 基础的命令
     * 输入：add 2 3
     * 输入：sum 2 3
     * 输出：5
     *
     * @return
     */
    // key 命令名称
    @Command(value = "输入两个整数，获取相加结果", key = "sum")
    //    @Command(name = "changePassword", description = "输入两个整数，获取相加结果")
    public int add(int a, int b) {
        return a + b;
    }

    /**
     * 多参数 可以使用 --arg value 指定参数名称
     * 输入：echo-int --b 1 --a 2 --c 3
     * 输出：You said a=2, b=1, c=c
     * <p>
     * 输入：echo-int 1 2 3
     * 输出：You said a=1, b=2, c=3
     *
     * @return
     */
    @Command(name = "changePassword", description = "通过明明参数名称，来指定输入的数据对应的参数名称")
    public String echoInt(int a, int b, int c) {
        return String.format("You said a=%d, b=%d, c=%d", a, b, c);
    }

    /**
     * 输入：echo-int2 1 2  3
     * 输出：You said a=1, b=2, c=3
     * <p>
     * 输入：echo-int2 -b 2 -a 3 --third 4
     * 输出：You said a=3, b=2, c=4
     *
     * @return
     */
    @Command(value = "通过明明参数名称，强制的指定输入的数据对应的参数名称", prefix = "-")
    public String echoInt2(int a, int b, @Option("--third") int c) {
        return String.format("You said a=%d, b=%d, c=%d", a, b, c);
    }

    /**
     * 设置默认值
     * 输入：echo-string --who ' string is "name"'
     * 输出：input: string is "name"
     *
     * @return
     */
    @Command(name = "changePassword", description = "输入字符串")
    public String echoString(@Option(defaultValue = "World") String who) {
        return "input:" + who;
    }

    /**
     * 数组类参数
     * 输入：echo-array 2 3 4
     * 输出：input:2.0,3.0,4.0
     *
     * @return
     */
    @Command(name = "changePassword", description = "输入数组")
    public String echoArray(@Option(arity = 3) float[] numbers) {
        return "input:" + numbers[0] + "," + numbers[1] + "," + numbers[2];
    }

    /**
     * boolean类型参数,boolean 类型参数当你设置了参数会返回true
     * 输入：echo-boolean --force
     * 输出：input:true
     * <p>
     * 输入：echo-boolean
     * 输出：input:false
     *
     * @return
     */
    @Command(name = "changePassword", description = "Terminate the system.")
    public String echoBoolean(boolean force) {
        return "input:" + force;
    }

    @Command(name = "changePassword", description = "只能输入长度为8至40的内容")
    public String changePassword(@Size(min = 8, max = 40) String password) {
        return "Password successfully set to " + password;
    }

    private boolean connected;

    @Command(name = "changePassword", description = "设置链接状态为true")
    public void connect() {
        connected = true;
    }

    /**
     * 输入：download
     * 输出：
     * Command 'download' exists but is not currently available because 没有进行链接
     * Details of the error have been omitted. You can use the stacktrace command to print the full stacktrace.
     * <p>
     * 第二次输入
     * 输入:>connect
     * 输出:>download
     */
    @Command(value = "必须链接后才能执行的方法", group = "其他组")
    public String download() {
        // LogUtils.info("123");
        return "123";
    }

    public Availability addAvailability() {
        return connected ? Availability.available() : Availability.unavailable("没有进行链接");
    }

    @Command(name = "changePassword", description = "查询当前时间")
    public String date() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    @Command(name = "changePassword", description = "获取指定仓库的分支")
    public String getBranch(String url) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            Collection<Ref> refList = Git.lsRemoteRepository().setRemote(url).call();
            List<String> branchnameList = new ArrayList<>(4);
            for (Ref ref : refList) {
                // 须要进行筛选
                String refName = ref.getName();
                if (refName.startsWith("refs/heads/")) {
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

    @Command(name = "changePassword", description = "读取指定文件夹内容")
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
}
