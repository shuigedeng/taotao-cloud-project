package com.taotao.cloud.idea.plugin.toolset;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.taotao.cloud.idea.plugin.component.TextEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class JSONPanel extends AbstractPanel {
    private final TextEditor textEditor = new TextEditor();

    public JSONPanel(Project project) {
        super();
        this.add(createTextEditor(), BorderLayout.CENTER);
        this.add(createFormatButton(), BorderLayout.SOUTH);
    }

    private Component createFormatButton() {
        JButton formatButton = new JButton("格式化");
        formatButton.addActionListener(actionListener());

        Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.add(formatButton);
        horizontalBox.add(Box.createHorizontalStrut(10));
        horizontalBox.add(exceptionMessageLabel);
        return horizontalBox;
    }

    private JComponent createTextEditor() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("JSON字符串:"), BorderLayout.NORTH);
        panel.add(this.textEditor, BorderLayout.CENTER);
        return panel;
    }

    @NotNull
    private ActionListener actionListener() {
        return e -> {
            setFailureStyle();
            String text = this.textEditor.getTextValue();
            if (StringUtil.isEmpty(text)) {
                exceptionMessageLabel.setText("JSON字符串必填");
                return;
            }
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(text);
                this.textEditor.setTextValue(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode));
            } catch (Exception ex) {
                exceptionMessageLabel.setText("格式化失败，请检查JSON字符串");
                return;
            }
            setSuccessStyle("格式化成功");
        };
    }

}
