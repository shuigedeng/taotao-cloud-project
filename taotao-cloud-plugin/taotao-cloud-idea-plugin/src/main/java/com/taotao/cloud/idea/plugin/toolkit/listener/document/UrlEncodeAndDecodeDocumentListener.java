package com.taotao.cloud.idea.plugin.toolkit.listener.document;

import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.ui.EditorTextField;
import com.taotao.cloud.idea.plugin.toolkit.domain.ToolkitCommand;
import java.net.URLDecoder;
import java.net.URLEncoder;
import com.taotao.boot.common.utils.lang.StringUtils;

public class UrlEncodeAndDecodeDocumentListener implements DocumentListener {

	private EditorTextField textField;
	private EditorTextField resultTextField;
	private ToolkitCommand command;

	public UrlEncodeAndDecodeDocumentListener(ToolkitCommand command, EditorTextField textField,
		EditorTextField resultTextField) {
		this.command = command;
		this.textField = textField;
		this.resultTextField = resultTextField;
	}

	@Override
	public void documentChanged(DocumentEvent event) {
		String text = this.textField.getText();
		if (StringUtils.isBlank(text)) {
			return;
		}
		String result;
		if (ToolkitCommand.URLEncode.equals(command)) {
			result = URLEncoder.encode(text);
		} else {
			result = URLDecoder.decode(text);
		}
		this.resultTextField.setText(result);
	}

}
