package com.taotao.cloud.idea.plugin.toolkit.listener.action;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.EditorTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JCheckBox;
import com.taotao.boot.common.utils.lang.StringUtils;

public class RegularMatchListener implements ActionListener {
    private EditorTextField textField;
    private EditorTextField regularTextField;
    private EditorTextField resultTextField;
    private JCheckBox ignore;

    public RegularMatchListener(EditorTextField textField, EditorTextField regularTextField, EditorTextField resultTextField, JCheckBox ignore) {
        this.textField = textField;
        this.regularTextField = regularTextField;
        this.resultTextField = resultTextField;
        this.ignore = ignore;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String text = this.textField.getText();
        if (StringUtils.isBlank(text)) {
            return;
        }

        String regularText = regularTextField.getText();
        if (StringUtils.isBlank(regularText)) {
            return;
        }
        String result = this.patternMatch(text, regularText, ignore.isSelected());
        if (StringUtil.isEmpty(result)) {
            this.resultTextField.setText("未匹配出任何结果");
            return;
        }
        this.resultTextField.setText(result);
    }


    private String patternMatch(String text, String pattern, boolean ignoreCase) {
        Pattern r;
        if (ignoreCase) {
            r = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        } else {
            r = Pattern.compile(pattern);
        }
        Matcher m = r.matcher(text);
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            sb.append(m.group()).append("\n");
        }
        return sb.toString();
    }


}
