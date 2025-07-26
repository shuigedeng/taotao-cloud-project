package com.taotao.cloud.idea.plugin.toolkit.ui;

import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.fileTypes.FileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorSettingsProvider;
import com.intellij.ui.EditorTextField;
import com.taotao.cloud.idea.plugin.toolkit.domain.ToolkitCommand;
import com.taotao.cloud.idea.plugin.toolkit.listener.action.CopyContentActionListener;
import com.taotao.cloud.idea.plugin.toolkit.listener.document.Base64EncodeAndDecodeDocumentListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Base64EncodeAndDecodeUI {

	private JPanel panel;
	private JButton copy;
	private EditorTextField textField;
	private EditorTextField resultTextField;

	private Project project;

	public Base64EncodeAndDecodeUI(Project project, ToolkitCommand command) {
		this.project = project;
		copy.addActionListener(new CopyContentActionListener(this.resultTextField));

		this.textField.addDocumentListener(
			new Base64EncodeAndDecodeDocumentListener(command, textField, resultTextField));
	}

	private void createUIComponents() {
		// TODO: place custom component creation code here
		this.textField = new EditorTextField(EditorFactory.getInstance().createDocument(""),
			project, FileTypes.PLAIN_TEXT, false, false);
		this.textField.addSettingsProvider(getEditorSettingsProvider());

		this.resultTextField = new EditorTextField(EditorFactory.getInstance().createDocument(""),
			project, FileTypes.PLAIN_TEXT, true, false);
		this.resultTextField.addSettingsProvider(getEditorSettingsProvider());
	}

	private EditorSettingsProvider getEditorSettingsProvider() {
		return editor -> {
			EditorSettings settings = editor.getSettings();
			settings.setUseSoftWraps(true);
			settings.setLineNumbersShown(true);
		};
	}

	public JPanel getPanel() {
		return panel;
	}

}
