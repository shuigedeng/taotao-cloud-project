package com.taotao.cloud.idea.plugin.toolkit.ui;

import cn.hutool.core.date.DateTime;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.fileTypes.FileTypes;
import com.intellij.openapi.fileTypes.PlainTextLanguage;
import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorSettingsProvider;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.LanguageTextField;
import com.taotao.cloud.idea.plugin.toolkit.domain.ToolkitCommand;
import com.taotao.cloud.idea.plugin.toolkit.listener.action.CopyContentActionListener;
import com.taotao.cloud.idea.plugin.toolkit.listener.document.DateTimestampDocumentListener;


import javax.swing.JButton;
import javax.swing.JPanel;

public class DateTimestampUI {

	private JPanel panel;
	private EditorTextField textField;
	private EditorTextField localTimeTextField;
	private EditorTextField localDateTextField;
	private EditorTextField secondTextField;
	private EditorTextField millisTextField;
	private EditorTextField utcTimeTextField;

	private JButton localTimeCopy;
	private JButton localDateCopy;
	private JButton secondCopy;
	private JButton millisCopy;
	private JButton utcCopy;

	private Project project;

	public DateTimestampUI(Project project, ToolkitCommand command) {
		this.project = project;
		this.localTimeCopy.addActionListener(new CopyContentActionListener(localTimeTextField));
		this.localDateCopy.addActionListener(new CopyContentActionListener(localDateTextField));
		this.secondCopy.addActionListener(new CopyContentActionListener(secondTextField));
		this.millisCopy.addActionListener(new CopyContentActionListener(millisTextField));
		this.utcCopy.addActionListener(new CopyContentActionListener(utcTimeTextField));

		this.textField.addDocumentListener(new DateTimestampDocumentListener(command,
			this.textField, this.localTimeTextField,
			this.localDateTextField, this.secondTextField,
			this.millisTextField, this.utcTimeTextField));

		initTextField(command);
	}

	private void initTextField(ToolkitCommand command) {
		if (ToolkitCommand.Date.equals(command)) {
			this.textField.setText(DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
		} else if (ToolkitCommand.Timestamp.equals(command)) {
			this.textField.setText(String.valueOf(DateTime.now().getTime()));
		}
	}

	public JPanel getPanel() {
		return panel;
	}

	private void createUIComponents() {
		// TODO: place custom component creation code here
		this.textField = new LanguageTextField(PlainTextLanguage.INSTANCE, project, "", true);
		this.textField.setPlaceholder("yyyy-MM-dd HH:mm:ss");
		this.textField.addSettingsProvider(getEditorSettingsProvider());

		this.localTimeTextField = createPlainTextEditor();
		this.localTimeTextField.addSettingsProvider(getEditorSettingsProvider());

		this.localDateTextField = createPlainTextEditor();
		this.localDateTextField.addSettingsProvider(getEditorSettingsProvider());

		this.secondTextField = createPlainTextEditor();
		this.secondTextField.addSettingsProvider(getEditorSettingsProvider());

		this.millisTextField = createPlainTextEditor();
		this.millisTextField.addSettingsProvider(getEditorSettingsProvider());

		this.utcTimeTextField = createPlainTextEditor();
		this.utcTimeTextField.addSettingsProvider(getEditorSettingsProvider());
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
}
