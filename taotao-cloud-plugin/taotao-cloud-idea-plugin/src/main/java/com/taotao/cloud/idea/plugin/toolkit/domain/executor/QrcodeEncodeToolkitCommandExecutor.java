package com.taotao.cloud.idea.plugin.toolkit.domain.executor;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.util.ui.JBDimension;
import com.taotao.cloud.idea.plugin.toolkit.domain.ToolkitCommand;
import com.taotao.cloud.idea.plugin.toolkit.ui.QrcodeEncodeUI;

import javax.swing.*;

/**
 * QrcodeEncodeToolkitCommandExecutor
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class QrcodeEncodeToolkitCommandExecutor extends AbstractToolkitCommandExecutor {

    @Override
    public boolean support( ToolkitCommand command ) {
        return ToolkitCommand.QRCodeEncode.equals(command);
    }

    @Override
    public void execute( ToolkitCommand command, DataContext dataContext ) {
        Project project = getProject(dataContext);

        JPanel panel = new QrcodeEncodeUI(project).getPanel();

        JBDimension dimension = new JBDimension(530, 500);
        JBPopup popup = createPopup(command.getDescription(), dimension, AllIcons.Actions.Search, panel);
        popup.show(getRelativePoint(dataContext, dimension));
    }
}
