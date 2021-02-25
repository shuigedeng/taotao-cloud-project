package com.taotao.cloud.idea.plugin.toolset;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.Base64;
import com.taotao.cloud.idea.plugin.component.TextEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;

public class Base64Panel extends AbstractPanel {
    private final TextEditor sourceTextEditor = new TextEditor(8, 20);
    private final TextEditor targetTextEditor = new TextEditor(8, 20);

    public Base64Panel(Project project) {
        super();
        this.add(createSourceTextEditor(), BorderLayout.NORTH);
        this.add(createTargetTextEditor(), BorderLayout.CENTER);
        this.add(createConvertButton(), BorderLayout.SOUTH);
    }

    private Component createConvertButton() {
        JButton encodeButton = new JButton("编码");
        encodeButton.addActionListener(encodeButtonListener());

        JButton decodeButton = new JButton("解码");
        decodeButton.addActionListener(decodeButtonListener());

        Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.add(encodeButton);
        horizontalBox.add(Box.createHorizontalStrut(10));
        horizontalBox.add(decodeButton);
        horizontalBox.add(Box.createHorizontalStrut(10));
        horizontalBox.add(exceptionMessageLabel);
        return horizontalBox;
    }

    private JComponent createSourceTextEditor() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("输入源:"), BorderLayout.NORTH);
        panel.add(this.sourceTextEditor, BorderLayout.CENTER);
        return panel;
    }

    private JComponent createTargetTextEditor() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("结果:"), BorderLayout.NORTH);
        panel.add(this.targetTextEditor, BorderLayout.CENTER);
        return panel;
    }


    @NotNull
    private ActionListener encodeButtonListener() {
        return e -> {
            setFailureStyle();
            String sourceText = this.sourceTextEditor.getTextValue();
            if (StringUtil.isEmpty(sourceText)) {
                exceptionMessageLabel.setText("输入源必填");
                return;
            }

            String result = Base64.encode(sourceText.getBytes(StandardCharsets.UTF_8));
            targetTextEditor.setTextValue(result);
            setSuccessStyle("编码完成");
        };
    }

    @NotNull
    private ActionListener decodeButtonListener() {
        return e -> {
            setFailureStyle();
            String sourceText = this.sourceTextEditor.getTextValue();
            if (StringUtil.isEmpty(sourceText)) {
                exceptionMessageLabel.setText("输入源必填");
                return;
            }

            String result = new String(Base64.decode(sourceText), StandardCharsets.UTF_8);
            this.targetTextEditor.setTextValue(result);
            setSuccessStyle("解码完成");
        };
    }

}
