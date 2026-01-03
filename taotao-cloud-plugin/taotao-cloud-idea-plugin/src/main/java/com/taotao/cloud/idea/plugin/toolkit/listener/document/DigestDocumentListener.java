package com.taotao.cloud.idea.plugin.toolkit.listener.document;

import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.ui.EditorTextField;

import javax.swing.JRadioButton;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * DigestDocumentListener
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class DigestDocumentListener implements DocumentListener {

    private EditorTextField textField;
    private EditorTextField md5TextField;
    private EditorTextField sha1TextField;
    private EditorTextField sha224TextField;
    private EditorTextField sha384TextField;
    private EditorTextField sha512TextField;
    private JRadioButton lowerCase;

    public DigestDocumentListener( JRadioButton lowerCase, EditorTextField textField,
            EditorTextField md5TextField, EditorTextField sha1TextField,
            EditorTextField sha224TextField, EditorTextField sha384TextField,
            EditorTextField sha512TextField ) {
        this.textField = textField;
        this.md5TextField = md5TextField;
        this.sha1TextField = sha1TextField;
        this.sha224TextField = sha224TextField;
        this.sha384TextField = sha384TextField;
        this.sha512TextField = sha512TextField;
        this.lowerCase = lowerCase;
    }

    @Override
    public void documentChanged( DocumentEvent event ) {
        String text = this.textField.getText();
        if (StringUtils.isBlank(text)) {
            return;
        }
        try {
            boolean upperCaseB = true;
            if (lowerCase.isSelected()) {
                upperCaseB = false;
            }

            this.md5TextField.setText(upperCaseB ? DigestUtils.md5Hex(text).toUpperCase()
                    : DigestUtils.md5Hex(text).toLowerCase());
            this.sha1TextField.setText(upperCaseB ? DigestUtils.sha1Hex(text).toUpperCase()
                    : DigestUtils.sha1Hex(text).toLowerCase());
            this.sha224TextField.setText(upperCaseB ? DigestUtils.sha384Hex(text).toUpperCase()
                    : DigestUtils.sha384Hex(text).toLowerCase());
            this.sha384TextField.setText(upperCaseB ? DigestUtils.sha384Hex(text).toUpperCase()
                    : DigestUtils.sha384Hex(text).toLowerCase());
            this.sha512TextField.setText(upperCaseB ? DigestUtils.sha512Hex(text).toUpperCase()
                    : DigestUtils.sha512Hex(text).toLowerCase());
        } catch (Exception ignored) {
            this.md5TextField.setText("");
            this.sha1TextField.setText("");
            this.sha224TextField.setText("");
            this.sha384TextField.setText("");
            this.sha512TextField.setText("");
        }
    }


}
