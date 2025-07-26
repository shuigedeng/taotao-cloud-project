package com.taotao.cloud.idea.plugin.toolkit.ui;

import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.fileTypes.FileTypes;
import com.intellij.openapi.fileTypes.PlainTextLanguage;
import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorSettingsProvider;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.LanguageTextField;
import com.taotao.cloud.idea.plugin.toolkit.listener.action.CopyContentActionListener;
import com.taotao.cloud.idea.plugin.toolkit.listener.document.DigestDocumentListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class DigestUI {

	private JRadioButton upperCase;
	private JRadioButton lowerCase;
	private JPanel panel;
	private EditorTextField textField;
	private EditorTextField md5TextField;
	private EditorTextField sha1TextField;
	private EditorTextField sha224TextField;
	private EditorTextField sha384TextField;
	private EditorTextField sha512TextField;
	private JButton copyMd5;
	private JButton copySha1;
	private JButton copySha224;
	private JButton copySha384;
	private JButton copySha512;

	private Project project;

	public DigestUI(Project project) {
		this.project = project;

		this.copyMd5.addActionListener(new CopyContentActionListener(md5TextField));
		this.copySha1.addActionListener(new CopyContentActionListener(sha1TextField));
		this.copySha224.addActionListener(new CopyContentActionListener(sha224TextField));
		this.copySha384.addActionListener(new CopyContentActionListener(sha384TextField));
		this.copySha512.addActionListener(new CopyContentActionListener(sha512TextField));

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(upperCase);
		buttonGroup.add(lowerCase);

		this.textField.addDocumentListener(new DigestDocumentListener(this.lowerCase,
			this.textField, this.md5TextField,
			this.sha1TextField, this.sha224TextField, this.sha384TextField, this.sha512TextField));
	}

	private void createUIComponents() {
		// TODO: place custom component creation code here
		this.textField = new LanguageTextField(PlainTextLanguage.INSTANCE, project, "", false);
		this.textField.setPlaceholder("输入字符串");
		this.textField.addSettingsProvider(editor -> {
			EditorSettings settings = editor.getSettings();
			settings.setIndentGuidesShown(true);
			settings.setLineNumbersShown(true);
			settings.setWheelFontChangeEnabled(true);
		});

		this.md5TextField = createPlainTextEditor();
		this.md5TextField.addSettingsProvider(getEditorSettingsProvider());

		this.sha1TextField = createPlainTextEditor();
		this.sha1TextField.addSettingsProvider(getEditorSettingsProvider());

		this.sha224TextField = createPlainTextEditor();
		this.sha224TextField.addSettingsProvider(getEditorSettingsProvider());

		this.sha384TextField = createPlainTextEditor();
		this.sha384TextField.addSettingsProvider(getEditorSettingsProvider());

		this.sha512TextField = createPlainTextEditor();
		this.sha512TextField.addSettingsProvider(getEditorSettingsProvider());
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
