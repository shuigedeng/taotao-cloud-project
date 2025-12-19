package com.taotao.cloud.idea.plugin.toolkit.domain.executor;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.util.ui.JBDimension;

import com.taotao.cloud.idea.plugin.toolkit.domain.ToolkitCommand;
import com.taotao.cloud.idea.plugin.toolkit.ui.IPAndPhoneAddressUI;

import javax.swing.*;

/**
 * IPAndPhoneToolkitCommandExecutor
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class IPAndPhoneToolkitCommandExecutor extends AbstractToolkitCommandExecutor {

    @Override
    public boolean support( ToolkitCommand command ) {
        return ToolkitCommand.Phone.equals(command) || ToolkitCommand.IP.equals(command);
    }

    @Override
    public void execute( ToolkitCommand command, DataContext dataContext ) {
        Project project = getProject(dataContext);

        JPanel panel = new IPAndPhoneAddressUI(project, command).getPanel();

        JBDimension dimension = new JBDimension(400, 250);
        JBPopup popup = createPopup(command.getDescription(), dimension, AllIcons.Actions.Search, panel);
        popup.show(getRelativePoint(dataContext, dimension));
    }
}
