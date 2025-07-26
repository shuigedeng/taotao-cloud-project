package com.taotao.cloud.idea.plugin.toolkit.listener.action;

import com.intellij.ui.EditorTextField;
import com.taotao.cloud.idea.plugin.toolkit.notification.ToolkitNotifier;
import com.taotao.cloud.idea.plugin.toolkit.utils.JsonUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.apache.commons.lang3.StringUtils;

/**
 * MinifyJsonAction
 */
public class MinifyJsonActionListener implements ActionListener {

    private EditorTextField editorTextField;

    public MinifyJsonActionListener() {
    }

    public MinifyJsonActionListener(EditorTextField editorTextField) {
        this.editorTextField = editorTextField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String text = editorTextField.getText();
        if (StringUtils.isBlank(text)) {
            return;
        }

        try {
            String minifiedJson = JsonUtils.minifyJson(text);
            editorTextField.setText(minifiedJson);
        } catch (Exception ex) {
            ToolkitNotifier.error("Json minify fail, please check data.");
        }
    }
}
