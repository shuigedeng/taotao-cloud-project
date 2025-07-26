package com.taotao.cloud.idea.plugin.toolkit.domain.executor;

import com.intellij.openapi.actionSystem.DataContext;
import com.taotao.cloud.idea.plugin.toolkit.domain.ToolkitCommand;

public interface ToolkitCommandExecutor {
    boolean support(ToolkitCommand command);

    void execute(ToolkitCommand command, DataContext dataContext);
}
