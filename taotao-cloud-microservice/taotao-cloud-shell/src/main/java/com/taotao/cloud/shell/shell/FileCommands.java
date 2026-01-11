package com.taotao.cloud.shell.shell;

import org.springframework.stereotype.Component;
import org.springframework.shell.core.command.annotation.Command;

@Component
public class FileCommands {
    
    @Command(name = "ls", description = "列出目录内容", group = "文件操作")
    public String ls(String path) {
        // 实现列出目录内容的逻辑
        return "列出 " + path + " 的内容";
    }
    
    @Command(name = "mkdir", description = "创建新目录", group = "文件操作")
    public String mkdir(String dirName) {
        // 实现创建目录的逻辑
        return "创建目录: " + dirName;
    }
}
