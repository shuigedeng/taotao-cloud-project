package com.taotao.cloud.shell.jcommand;

import java.util.List;

/**
 * git add file1 file2
 *
 */
@Parameters(commandDescription = "暂存文件", commandNames = "add", separators = " ")
public class GitCommandAdd {
    public static final String COMMAND = "add";
    @Parameter(description = "暂存文件列表")
    private List<String> files;

    public List<String> getFiles() {
        return files;
    }
}
