package com.taotao.cloud.idea.plugin.toolkit.domain.executor;

import com.intellij.icons.AllIcons.FileTypes;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.util.ui.JBDimension;

import com.taotao.cloud.idea.plugin.toolkit.domain.ToolkitCommand;
import com.taotao.cloud.idea.plugin.toolkit.ui.RegularExpressionUI;
import javax.swing.*;

public class RegularToolkitCommandExecutor extends AbstractToolkitCommandExecutor {

    @Override
    public boolean support(ToolkitCommand command) {
        return ToolkitCommand.Regular.equals(command);
    }

    @Override
    public void execute(ToolkitCommand command, DataContext dataContext) {
        Project project = getProject(dataContext);

        JPanel panel = new RegularExpressionUI(project).getPanel();

        JBDimension dimension = new JBDimension(650, 620);
        JBPopup popup = createPopup(command.getDescription(), dimension, FileTypes.Text, panel);
        popup.show(getRelativePoint(dataContext, dimension));
    }
}
