package com.taotao.cloud.shell.shell;

import org.springframework.shell.core.command.annotation.Command;
import org.springframework.shell.core.command.annotation.Option;
import org.springframework.stereotype.Component;
import org.springframework.shell.core.command.annotation.Command;
import org.springframework.shell.core.command.annotation.Option;
import org.springframework.stereotype.Component;

@Component
public class AdvancedCommands {
    
    @Command(name = "changePassword", description = "使用高级参数选项的示例")
    public String greet(
        @Option(defaultValue = "世界") String name,
        @Option(description = "决定是否使用大写", defaultValue = "false") boolean uppercase
    ) {
        String greeting = "你好, " + name + "!";
        return uppercase ? greeting.toUpperCase() : greeting;
    }
}
