package com.taotao.cloud.idea.plugin.toolkit.listener.document;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.ui.EditorTextField;
import com.taotao.cloud.idea.plugin.toolkit.domain.ToolkitCommand;
import org.apache.commons.lang3.StringUtils;


/**
 * DateTimestampDocumentListener
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class DateTimestampDocumentListener implements DocumentListener {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private EditorTextField textField;
    private EditorTextField localTimeTextField;
    private EditorTextField localDateTextField;
    private EditorTextField secondTextField;
    private EditorTextField millisTextField;
    private EditorTextField utcTimeTextField;
    private ToolkitCommand command;

    public DateTimestampDocumentListener( ToolkitCommand command, EditorTextField textField,
            EditorTextField localTimeTextField,
            EditorTextField localDateTextField,
            EditorTextField secondTextField,
            EditorTextField millisTextField,
            EditorTextField utcTimeTextField ) {
        this.command = command;
        this.textField = textField;
        this.localTimeTextField = localTimeTextField;
        this.localDateTextField = localDateTextField;
        this.secondTextField = secondTextField;
        this.millisTextField = millisTextField;
        this.utcTimeTextField = utcTimeTextField;
    }

    @Override
    public void documentChanged( DocumentEvent event ) {
        String text = this.textField.getText();
        if (StringUtils.isBlank(text)) {
            return;
        }
        try {
            DateTime dateTime = buildDateTime(command, text);

            this.localTimeTextField.setText(dateTime.toString(DATE_TIME_FORMAT));
            this.localDateTextField.setText(dateTime.toString(DATE_FORMAT));
            this.secondTextField.setText(String.valueOf(dateTime.getTime() / 1000));
            this.millisTextField.setText(String.valueOf(dateTime.getTime()));
            this.utcTimeTextField.setText(
                    DateUtil.offsetHour(dateTime, -8).toString(DATE_TIME_FORMAT));
        } catch (Exception ignored) {
            this.localTimeTextField.setText("");
            this.localDateTextField.setText("");
            this.secondTextField.setText("");
            this.millisTextField.setText("");
            this.utcTimeTextField.setText("");
        }

    }

    private DateTime buildDateTime( ToolkitCommand command, String text ) {
        if (ToolkitCommand.Timestamp.equals(command)) {
            return new DateTime(Long.valueOf(text));
        } else if (ToolkitCommand.Date.equals(command)) {
            return DateUtil.parse(text, DATE_TIME_FORMAT);
        }
        throw new UnsupportedOperationException();
    }

}
