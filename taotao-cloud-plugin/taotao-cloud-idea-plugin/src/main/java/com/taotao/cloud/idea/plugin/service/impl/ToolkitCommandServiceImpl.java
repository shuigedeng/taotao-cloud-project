package com.taotao.cloud.idea.plugin.service.impl;

import com.intellij.openapi.actionSystem.DataContext;
import com.taotao.cloud.idea.plugin.domain.ToolkitCommand;
import com.taotao.cloud.idea.plugin.domain.executor.ToolkitCommandExecutor;
import com.taotao.cloud.idea.plugin.domain.executor.ToolkitCommandExecutorComposite;
import com.taotao.cloud.idea.plugin.service.ToolkitCommandService;

public class ToolkitCommandServiceImpl implements ToolkitCommandService {
    private ToolkitCommandExecutor toolkitCommandExecutor = new ToolkitCommandExecutorComposite();

    @Override
    public void execute(ToolkitCommand command, DataContext dataContext) {
        toolkitCommandExecutor.execute(command, dataContext);
    }
}
