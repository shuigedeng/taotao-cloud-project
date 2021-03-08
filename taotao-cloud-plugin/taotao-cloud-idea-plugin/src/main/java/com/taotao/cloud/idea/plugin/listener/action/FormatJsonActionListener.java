package com.taotao.cloud.idea.plugin.listener.action;

import com.intellij.ui.EditorTextField;
import com.taotao.cloud.idea.plugin.notification.ToolkitNotifier;
import com.taotao.cloud.idea.plugin.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * FormatJsonAction
 */
public class FormatJsonActionListener implements ActionListener {
    private EditorTextField editorTextField;

    public FormatJsonActionListener(EditorTextField editorTextField) {
        this.editorTextField = editorTextField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String text = editorTextField.getText();
        if (StringUtils.isBlank(text)) {
            return;
        }
        try {
            String formattedJson = JsonUtils.formatJson(text);
            editorTextField.setText(formattedJson);
        } catch (Exception ex) {
            ToolkitNotifier.error("Json format fail, please check the data.");
        }
    }
}
