package com.taotao.cloud.idea.plugin.toolset;

import cn.hutool.http.HttpUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.components.JBCheckBox;
import com.taotao.cloud.idea.plugin.component.TextEditor;
import com.taotao.cloud.idea.plugin.utils.JsonFormatter;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class Sql2DslPanel extends AbstractPanel implements ActionListener {
	private static final String URL = "http://www.ischoolbar.com/EsParser/convert.php";
	private final TextEditor sqlTextEditor = new TextEditor(8, 20);
	private final TextEditor dslTextEditor = new TextEditor(8, 20);
	private final JBCheckBox format = new JBCheckBox("格式化DSL");

	public Sql2DslPanel(Project project) {
		this.add(createSqlArea(), BorderLayout.NORTH);
		this.add(createDslArea(), BorderLayout.CENTER);
		this.add(createConvertButton(), BorderLayout.SOUTH);
	}

	private Component createConvertButton() {
		JButton convertButton = new JButton("转换");
		convertButton.addActionListener(this);

		Box horizontalBox = Box.createHorizontalBox();
		horizontalBox.add(format);
		horizontalBox.add(Box.createHorizontalStrut(10));
		horizontalBox.add(convertButton);
		horizontalBox.add(Box.createHorizontalStrut(10));
		horizontalBox.add(exceptionMessageLabel);
		return horizontalBox;
	}

	private JComponent createSqlArea() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel("输入SQL:"), BorderLayout.NORTH);
		panel.add(this.sqlTextEditor, BorderLayout.CENTER);
		return panel;
	}

	private JComponent createDslArea() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel("转换后的DSL:"), BorderLayout.NORTH);
		panel.add(this.dslTextEditor, BorderLayout.CENTER);
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		setFailureStyle();

		String sqlText = this.sqlTextEditor.getTextValue();
		if (StringUtil.isEmpty(sqlText)) {
			exceptionMessageLabel.setText("SQL必填");
			return;
		}
		String dslText;
		try {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("mysqlStr", sqlText);
			dslText = HttpUtil.post(URL, paramMap);
			dslText = StringEscapeUtils.unescapeJava(dslText.substring(1, dslText.length() - 1));
		} catch (Exception ex) {
			exceptionMessageLabel.setText("转换失败，请检查SQL，异常信息：" + ex.getMessage());
			return;
		}

		if (format.isSelected()) {
			try {
				dslText = JsonFormatter.format(dslText);
			} catch (Exception ex) {
				exceptionMessageLabel.setText("格式化失败，去掉勾选格式化再试");
				return;
			}
		}
		this.dslTextEditor.setTextValue(dslText);

		setSuccessStyle("转换完成");
	}


}
