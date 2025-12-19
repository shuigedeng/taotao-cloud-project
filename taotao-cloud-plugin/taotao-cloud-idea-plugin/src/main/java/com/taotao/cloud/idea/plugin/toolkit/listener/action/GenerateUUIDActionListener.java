package com.taotao.cloud.idea.plugin.toolkit.listener.action;

import cn.hutool.core.util.IdUtil;
import com.intellij.ui.EditorTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * GenerateUUIDActionListener
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class GenerateUUIDActionListener implements ActionListener {

    private EditorTextField lowerCaseTextField;
    private EditorTextField upperCaseTextField;
    private EditorTextField lowerCaseExt;
    private EditorTextField upperCaseExt;

    public GenerateUUIDActionListener( EditorTextField lowerCaseTextField,
            EditorTextField upperCaseTextField, EditorTextField lowerCaseExt,
            EditorTextField upperCaseExt ) {
        this.lowerCaseTextField = lowerCaseTextField;
        this.upperCaseTextField = upperCaseTextField;
        this.lowerCaseExt = lowerCaseExt;
        this.upperCaseExt = upperCaseExt;
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
        String uuid = IdUtil.randomUUID();
        this.lowerCaseTextField.setText(uuid.toLowerCase());
        this.upperCaseTextField.setText(uuid.toUpperCase());
        this.lowerCaseExt.setText(uuid.replaceAll("-", "").toLowerCase());
        this.upperCaseExt.setText(uuid.replaceAll("-", "").toUpperCase());
    }
}
