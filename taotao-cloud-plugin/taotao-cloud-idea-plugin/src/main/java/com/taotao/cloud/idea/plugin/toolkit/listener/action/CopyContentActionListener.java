package com.taotao.cloud.idea.plugin.toolkit.listener.action;

import com.intellij.ui.EditorTextField;
import com.taotao.cloud.idea.plugin.toolkit.notification.ToolkitNotifier;
import com.taotao.cloud.idea.plugin.toolkit.utils.SystemUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.taotao.boot.common.utils.lang.StringUtils;

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
