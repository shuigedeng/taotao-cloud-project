package com.taotao.cloud.idea.plugin.toolkit.domain.executor;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.util.ui.JBDimension;

import com.taotao.cloud.idea.plugin.toolkit.domain.ToolkitCommand;
import com.taotao.cloud.idea.plugin.toolkit.ui.DigestUI;

import javax.swing.*;
import java.util.Arrays;

/**
 * DigestToolkitCommandExecutor
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class DigestToolkitCommandExecutor extends AbstractToolkitCommandExecutor {

    @Override
    public boolean support( ToolkitCommand command ) {
        return Arrays.asList(ToolkitCommand.MD5, ToolkitCommand.SHA1, ToolkitCommand.SHA224,
                ToolkitCommand.SHA256, ToolkitCommand.SHA384, ToolkitCommand.SHA512).contains(command);
    }

    @Override
    public void execute( ToolkitCommand command, DataContext dataContext ) {
        Project project = getProject(dataContext);

        JPanel panel = new DigestUI(project).getPanel();

        JBDimension dimension = new JBDimension(550, 350);
        JBPopup popup = createPopup("字符串加密", dimension, AllIcons.Toolwindows.Documentation, panel);
        popup.show(getRelativePoint(dataContext, dimension));
    }
}
