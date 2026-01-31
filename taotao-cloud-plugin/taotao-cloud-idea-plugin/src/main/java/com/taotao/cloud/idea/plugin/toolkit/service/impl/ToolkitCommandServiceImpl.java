package com.taotao.cloud.idea.plugin.toolkit.service.impl;

import com.intellij.openapi.actionSystem.DataContext;
import com.taotao.cloud.idea.plugin.toolkit.domain.ToolkitCommand;
import com.taotao.cloud.idea.plugin.toolkit.domain.executor.ToolkitCommandExecutor;
import com.taotao.cloud.idea.plugin.toolkit.domain.executor.ToolkitCommandExecutorComposite;
import com.taotao.cloud.idea.plugin.toolkit.service.ToolkitCommandService;

/**
 * ToolkitCommandServiceImpl
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class ToolkitCommandServiceImpl implements ToolkitCommandService {

    private ToolkitCommandExecutor toolkitCommandExecutor = new ToolkitCommandExecutorComposite();

    @Override
    public void execute( ToolkitCommand command, DataContext dataContext ) {
        toolkitCommandExecutor.execute(command, dataContext);
    }
}
