package com.taotao.cloud.idea.plugin.toolkit.listener.document;

import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.ui.EditorTextField;
import com.intellij.util.Base64;
import com.taotao.cloud.idea.plugin.toolkit.domain.ToolkitCommand;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;

/**
 * Base64EncodeAndDecodeDocumentListener
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class Base64EncodeAndDecodeDocumentListener implements DocumentListener {

    private EditorTextField urlTextField;
    private EditorTextField resultTextField;
    private ToolkitCommand command;

    public Base64EncodeAndDecodeDocumentListener( ToolkitCommand command,
            EditorTextField urlTextField, EditorTextField resultTextField ) {
        this.command = command;
        this.urlTextField = urlTextField;
        this.resultTextField = resultTextField;
    }

    @Override
    public void documentChanged( DocumentEvent event ) {
        String text = this.urlTextField.getText();
        if (StringUtils.isBlank(text)) {
            return;
        }
        String result;
        if (ToolkitCommand.Base64Decode.equals(command)) {
            result = new String(Base64.decode(text), StandardCharsets.UTF_8);
        } else {
            result = Base64.encode(text.getBytes(StandardCharsets.UTF_8));
        }
        this.resultTextField.setText(result);
    }

}
