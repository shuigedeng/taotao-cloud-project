package com.taotao.cloud.idea.plugin.toolkit.ui;

import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.project.Project;
import com.intellij.ui.LanguageTextField;

import com.taotao.cloud.idea.plugin.toolkit.listener.action.CopyContentActionListener;
import com.taotao.cloud.idea.plugin.toolkit.listener.action.FormatJsonActionListener;
import com.taotao.cloud.idea.plugin.toolkit.listener.action.MinifyJsonActionListener;

import javax.swing.*;

/**
 * JsonFormatterUI
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class JsonFormatterUI {

    private JPanel panel;
    private LanguageTextField textField;
    private JButton format;
    private JButton copy;
    private JButton minify;

    private Project project;

    /**
     * 编辑器对象
     */
    public JsonFormatterUI( Project project ) {
        this.project = project;
        format.addActionListener(new FormatJsonActionListener(this.textField));
        copy.addActionListener(new CopyContentActionListener(this.textField));
        minify.addActionListener(new MinifyJsonActionListener(this.textField));
    }

    private void createUIComponents() {
        this.textField = new LanguageTextField(JavaLanguage.INSTANCE, project, "", false);
        this.textField.addSettingsProvider(editor -> {
            EditorSettings settings = editor.getSettings();
            settings.setFoldingOutlineShown(true);
            settings.setLineNumbersShown(true);
            settings.setLineMarkerAreaShown(true);
            settings.setIndentGuidesShown(true);
            settings.setWheelFontChangeEnabled(true);
            editor.setHorizontalScrollbarVisible(true);
            editor.setVerticalScrollbarVisible(true);
        });
    }

    //或者主面板
    public JPanel getPanel() {
        return panel;
    }

}
