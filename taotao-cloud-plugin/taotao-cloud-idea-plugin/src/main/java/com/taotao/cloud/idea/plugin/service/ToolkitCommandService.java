package com.taotao.cloud.idea.plugin.service;

import com.intellij.openapi.actionSystem.DataContext;
import com.taotao.cloud.idea.plugin.domain.ToolkitCommand;

public interface ToolkitCommandService {
    void execute(ToolkitCommand command, DataContext dataContext);
}
