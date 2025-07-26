package com.taotao.cloud.idea.plugin.toolkit.domain.executor;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.util.ui.JBDimension;

import com.taotao.cloud.idea.plugin.toolkit.domain.ToolkitCommand;
import com.taotao.cloud.idea.plugin.toolkit.ui.Base64EncodeAndDecodeUI;
import javax.swing.*;

public class Base64EncodeAndDecodeToolkitCommandExecutor extends AbstractToolkitCommandExecutor {

    @Override
    public boolean support(ToolkitCommand command) {
        return ToolkitCommand.Base64Encode.equals(command) || ToolkitCommand.Base64Decode.equals(command);
    }

    @Override
    public void execute(ToolkitCommand command, DataContext dataContext) {
        Project project = getProject(dataContext);

        JPanel panel = new Base64EncodeAndDecodeUI(project, command).getPanel();

        JBDimension dimension = new JBDimension(400, 300);
        JBPopup popup = createPopup(command.getDescription(), dimension, AllIcons.Toolwindows.Documentation, panel);
        popup.show(getRelativePoint(dataContext, dimension));
    }
}
