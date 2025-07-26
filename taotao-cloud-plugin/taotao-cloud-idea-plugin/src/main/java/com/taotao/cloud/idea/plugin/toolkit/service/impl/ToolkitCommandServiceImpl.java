package com.taotao.cloud.idea.plugin.toolkit.service.impl;

import com.intellij.openapi.actionSystem.DataContext;
import com.taotao.cloud.idea.plugin.toolkit.domain.ToolkitCommand;
import com.taotao.cloud.idea.plugin.toolkit.domain.executor.ToolkitCommandExecutor;
import com.taotao.cloud.idea.plugin.toolkit.domain.executor.ToolkitCommandExecutorComposite;
import com.taotao.cloud.idea.plugin.toolkit.service.ToolkitCommandService;

public class ToolkitCommandServiceImpl implements ToolkitCommandService {
    private ToolkitCommandExecutor toolkitCommandExecutor = new ToolkitCommandExecutorComposite();

    @Override
    public void execute(ToolkitCommand command, DataContext dataContext) {
        toolkitCommandExecutor.execute(command, dataContext);
    }
}
