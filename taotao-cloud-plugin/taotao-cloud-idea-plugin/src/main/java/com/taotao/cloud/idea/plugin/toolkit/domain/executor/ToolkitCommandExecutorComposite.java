package com.taotao.cloud.idea.plugin.toolkit.domain.executor;

import com.intellij.openapi.actionSystem.DataContext;

import com.taotao.cloud.idea.plugin.toolkit.domain.ToolkitCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ToolkitCommandExecutorComposite
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class ToolkitCommandExecutorComposite implements ToolkitCommandExecutor {

    private List<ToolkitCommandExecutor> toolkitCommandExecutors = new ArrayList<>();

    public ToolkitCommandExecutorComposite() {
        Arrays.stream(ToolkitCommand.values())
                .forEach(command -> {
                    try {
                        this.addExecutor(command.getExecutor().newInstance());
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public boolean support( ToolkitCommand command ) {
        return true;
    }

    @Override
    public void execute( ToolkitCommand command, DataContext dataContext ) {
        for (ToolkitCommandExecutor executor : this.toolkitCommandExecutors) {
            if (executor.support(command)) {
                executor.execute(command, dataContext);
                return;
            }
        }
    }

    public ToolkitCommandExecutorComposite addExecutor( ToolkitCommandExecutor executor ) {
        this.toolkitCommandExecutors.add(executor);
        return this;
    }
}
