package com.taotao.cloud.idea.plugin.toolset.listener;

import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBTextField;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class JBTextFieldHintListener implements FocusListener {
	private String hintText;
	private JTextField textField;

	public JBTextFieldHintListener(JBTextField JBTextField, String hintText) {
		this.textField = JBTextField;
		this.hintText = hintText;
		JBTextField.setText(hintText);  //默认直接显示
		JBTextField.setForeground(JBColor.GRAY);
	}

	@Override
	public void focusGained(FocusEvent e) {
		//获取焦点时，清空提示内容
		String temp = textField.getText();
		if (temp.equals(hintText)) {
			textField.setText("");
			textField.setForeground(JBColor.GRAY);
		}

	}

	@Override
	public void focusLost(FocusEvent e) {
		//失去焦点时，没有输入内容，显示提示内容
		String temp = textField.getText();
		if ("".equals(temp)) {
			textField.setForeground(JBColor.GRAY);
			textField.setText(hintText);
		}
	}

}
