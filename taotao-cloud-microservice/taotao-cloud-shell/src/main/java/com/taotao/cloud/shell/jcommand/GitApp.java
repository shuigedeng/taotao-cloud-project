package com.taotao.cloud.shell.jcommand;

public class GitApp {

    public static void main(String[] args) {
        //args = new String[] {"clone", "ht2tp://www.wdbyte.com/test.git"};
        GitCommandOptions gitCommandOptions = new GitCommandOptions();
        GitCommandCommit commandCommit = new GitCommandCommit();
        GitCommandAdd commandAdd = new GitCommandAdd();
        JCommander commander = JCommander.newBuilder()
            .programName("git-app")
            .addObject(gitCommandOptions)
            .addCommand(commandCommit)
            .addCommand(commandAdd)
            .build();
        commander.parse(args);
        // 打印帮助信息
        if (gitCommandOptions.isHelp()) {
            commander.usage();
            return;
        }
        if (gitCommandOptions.isVersion()) {
            System.out.println("git version 2.24.3 (Apple Git-128)");
            return;
        }
        if (gitCommandOptions.getCloneUrl() != null) {
            System.out.println("clone " + gitCommandOptions.getCloneUrl());
        }
        String parsedCommand = commander.getParsedCommand();
        if ("commit".equals(parsedCommand)) {
            System.out.println(commandCommit.getComment());
        }
        if ("add".equals(parsedCommand)) {
            for (String file : commandAdd.getFiles()) {
                System.out.println("暂存文件：" + file);
            }
        }
    }
}