package com.taotao.cloud.idea.plugin.toolset;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UI;
import com.intellij.util.ui.components.BorderLayoutPanel;
import com.taotao.cloud.idea.plugin.component.TextEditor;
import com.taotao.cloud.idea.plugin.toolset.model.RegularExample;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressionPanel extends AbstractPanel {
	private final TextEditor sourceTextEditor = new TextEditor(8, 10);
	private final JBTextField regularTextField = new JBTextField(20);
	private final TextEditor resultTextEditor = new TextEditor(false);

	private DefaultListModel<RegularExample> model = new DefaultListModel<>();
	private JBList<RegularExample> regularExampleList = new JBList<>(model);
	private JBCheckBox ignore = new JBCheckBox("忽略大小写");

	public RegularExpressionPanel(Project project) {
		super(10, 10);
		initJBList();

		this.add(UI.Panels.simplePanel()
				.addToTop(new JLabel("常用正则表达式:  "))
				.addToCenter(regularExampleList),
			BorderLayout.WEST);

		BorderLayoutPanel centerPanel = UI.Panels.simplePanel(10, 10)
			.addToTop(createSourceTextEditor())
			.addToCenter(
				UI.Panels.simplePanel(10, 10)
					.addToTop(createRegularTextField())
					.addToCenter(createResultTextEditor())
			);
		this.add(centerPanel, BorderLayout.CENTER);
	}

	private void initJBList() {
		model.addElement(new RegularExample("匹配邮箱", "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}"));
		model.addElement(new RegularExample("匹配手机号", "(13\\d|14[579]|15[^4\\D]|17[^49\\D]|18\\d)\\d{8}"));
		model.addElement(new RegularExample("匹配IP（IPV4）", "\\d{0,3}\\.\\d{0,3}\\.\\d{0,3}\\.\\d{0,3}"));
		model.addElement(new RegularExample("匹配中文", "[\\u4e00-\\u9fa5]+"));
		model.addElement(new RegularExample("匹配双字节字符（包含汉字）", "[^\\x00-\\xff]+"));
		model.addElement(new RegularExample("匹配时间（时:分:秒）", "([01]?\\d|2[0-3]):[0-5]?\\d:[0-5]?\\d"));
		model.addElement(new RegularExample("匹配身份证", "\\d{17}[0-9Xx]|\\d{15}"));
		model.addElement(new RegularExample("匹配日期（年-月-日）", "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)"));
		model.addElement(new RegularExample("匹配正整数", "[1-9]\\d*"));
		model.addElement(new RegularExample("匹配负整数", "-[1-9]\\d*"));
		model.addElement(new RegularExample("空白行", "\\s"));
		model.addElement(new RegularExample("腾讯QQ", "[1-9]([0-9]{5,11})"));
		model.addElement(new RegularExample("邮政编码", "\\d{6}"));


		regularExampleList.setBorder(JBUI.Borders.customLine(JBColor.border(), 1));
		regularExampleList.setCellRenderer(new ColoredListCellRenderer<RegularExample>() {
			@Override
			protected void customizeCellRenderer(@NotNull JList<? extends RegularExample> list, RegularExample regularExample,
												 int index, boolean selected, boolean hasFocus) {
				this.append(regularExample.getName());
			}
		});

		regularExampleList.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				RegularExample regularExample = regularExampleList.getSelectedValue();
				regularTextField.setText(regularExample.getRegular());
			}
		});
	}

	private JComponent createRegularTextField() {
		JPanel panel = new JPanel(new BorderLayout(5, 10));
		panel.add(new JLabel("正则表达式:"), BorderLayout.WEST);
		panel.add(this.regularTextField, BorderLayout.CENTER);

		JPanel panel2 = new JPanel(new BorderLayout(2, 10));

		JButton matchButton = new JButton("匹配");
		matchButton.addActionListener(matchButtonListener());
		panel2.add(ignore, BorderLayout.WEST);
		panel2.add(matchButton, BorderLayout.EAST);

		panel.add(panel2, BorderLayout.EAST);
		return panel;
	}

	private JComponent createSourceTextEditor() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel("匹配文本:"), BorderLayout.NORTH);
		panel.add(this.sourceTextEditor, BorderLayout.CENTER);
		return panel;
	}

	private JComponent createResultTextEditor() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel("匹配结果:"), BorderLayout.NORTH);
		panel.add(this.resultTextEditor, BorderLayout.CENTER);
		return panel;
	}

	private ActionListener matchButtonListener() {
		return e -> {
			this.resultTextEditor.setTextValue("");
			String sourceText = this.sourceTextEditor.getTextValue();
			if (StringUtil.isEmpty(sourceText)) {
				this.resultTextEditor.setTextValue("匹配文本必填");
				return;
			}
			String regularText = regularTextField.getText();
			if (StringUtil.isEmpty(regularText)) {
				this.resultTextEditor.setTextValue("正则表达式必填");
				return;
			}
			String result = this.patternMatch(sourceText, regularText, ignore.isSelected());
			if (StringUtil.isEmpty(result)) {
				this.resultTextEditor.setTextValue("未匹配出任何结果");
				return;
			}
			this.resultTextEditor.setTextValue(result);
		};
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
