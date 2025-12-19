package com.taotao.cloud.idea.plugin.toolkit.ui;

import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.fileTypes.FileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorSettingsProvider;
import com.intellij.ui.EditorTextField;
import com.taotao.cloud.idea.plugin.toolkit.listener.action.CopyContentActionListener;
import com.taotao.cloud.idea.plugin.toolkit.listener.action.GenerateUUIDActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * UuidUI
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class UuidUI {

    private JPanel panel;
    private JButton lowerCaseCopy;
    private JButton lowerCaseExtCopy;
    private JButton upperCaseExtCopy;
    private JButton upperCaseCopy;
    private EditorTextField lowerCaseTextField;
    private EditorTextField upperCaseTextField;
    private EditorTextField lowerCaseExt;
    private EditorTextField upperCaseExt;
    private JButton generate;

    private Project project;

    public UuidUI( Project project ) {
        this.project = project;
        this.lowerCaseCopy.addActionListener(new CopyContentActionListener(lowerCaseTextField));
        this.upperCaseCopy.addActionListener(new CopyContentActionListener(upperCaseTextField));
        this.lowerCaseExtCopy.addActionListener(new CopyContentActionListener(lowerCaseExt));
        this.upperCaseExtCopy.addActionListener(new CopyContentActionListener(upperCaseExt));
        this.generate.addActionListener(
                new GenerateUUIDActionListener(this.lowerCaseTextField, this.upperCaseTextField,
                        this.lowerCaseExt, this.upperCaseExt));
        this.generate.doClick();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        this.lowerCaseTextField = createPlainTextEditor();
        this.lowerCaseTextField.addSettingsProvider(getEditorSettingsProvider());

        this.upperCaseTextField = createPlainTextEditor();
        this.upperCaseTextField.addSettingsProvider(getEditorSettingsProvider());

        this.lowerCaseExt = createPlainTextEditor();
        this.lowerCaseExt.addSettingsProvider(getEditorSettingsProvider());

        this.upperCaseExt = createPlainTextEditor();
        this.upperCaseExt.addSettingsProvider(getEditorSettingsProvider());
    }

    private EditorTextField createPlainTextEditor() {
        return new EditorTextField(EditorFactory.getInstance().createDocument(""), project,
                FileTypes.PLAIN_TEXT, true, true);
    }

    private EditorSettingsProvider getEditorSettingsProvider() {
        return editor -> {
            EditorSettings settings = editor.getSettings();
            settings.setIndentGuidesShown(true);
            settings.setWheelFontChangeEnabled(true);
        };
    }

    public JPanel getPanel() {
        return panel;
    }
}
