package com.taotao.cloud.idea.plugin.listener.action;

import com.intellij.ui.EditorTextField;
import com.taotao.cloud.idea.plugin.notification.ToolkitNotifier;
import com.taotao.cloud.idea.plugin.utils.SystemUtils;
import org.apache.commons.lang3.StringUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * CopyContentAction
 */
public class CopyContentActionListener implements ActionListener {
    private EditorTextField editorTextField;

    public CopyContentActionListener(EditorTextField editorTextField) {
        this.editorTextField = editorTextField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String editorContent = editorTextField.getText();

        if (StringUtils.isNotBlank(editorContent)) {
            SystemUtils.copyToClipboard(editorContent);
            ToolkitNotifier.success("Copy content to Clipboard success.");
        }
    }
}
