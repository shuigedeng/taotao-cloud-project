package com.taotao.cloud.idea.plugin.listener.document;

import cn.hutool.core.util.URLUtil;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.ui.EditorTextField;
import com.taotao.cloud.idea.plugin.domain.ToolkitCommand;
import org.apache.commons.lang3.StringUtils;

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
			result = URLUtil.encode(text);
		} else {
			result = URLUtil.decode(text);
		}
		this.resultTextField.setText(result);
	}

}
