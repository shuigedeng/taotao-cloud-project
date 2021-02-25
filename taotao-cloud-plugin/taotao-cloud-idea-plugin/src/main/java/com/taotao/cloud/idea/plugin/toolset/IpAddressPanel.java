package com.taotao.cloud.idea.plugin.toolset;

import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IpAddressPanel extends AbstractPanel {
	private static final String URL = "http://whois.pconline.com.cn/ipJson.jsp";
	private final JBTextField ipAddressTextField = new JBTextField(20);
	private DefaultTableModel model = new DefaultTableModel();
	private final JBTable jbTable = new JBTable(model);

	public IpAddressPanel(Project project) {
		super();
		this.add(createIpAddressTextEditor(), BorderLayout.NORTH);
		this.add(createJBTable(), BorderLayout.CENTER);
		this.add(exceptionMessageLabel, BorderLayout.SOUTH);
	}

	private JComponent createJBTable() {
		jbTable.setBorder(JBUI.Borders.customLine(JBColor.border(), 1));
		jbTable.setGridColor(JBColor.border());
		return jbTable;
	}

	private JComponent createIpAddressTextEditor() {
		JButton queryButton = new JButton("查询");
		queryButton.addActionListener(queryIpButtonListener());

		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.add(new JLabel("请输入IP地址:"), BorderLayout.WEST);
		panel.add(this.ipAddressTextField, BorderLayout.CENTER);
		panel.add(queryButton, BorderLayout.EAST);
		return panel;
	}

	@NotNull
	private ActionListener queryIpButtonListener() {
		return e -> {
			setFailureStyle();
			String text = this.ipAddressTextField.getText();
			if (StringUtil.isEmpty(text)) {
				exceptionMessageLabel.setText("IP地址必填");
				return;
			}
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("json", true);
			paramMap.put("ip", text);
			String body = HttpUtil.post(URL, paramMap);

			ObjectMapper objectMapper = new ObjectMapper();
			try {
				JsonNode data = objectMapper.readTree(body.trim());
				model.setDataVector(
					new Object[][]{
						{"省份", data.get("pro")},
						{"城市", data.get("city")},
						{"IP归属地", data.get("addr")}
					}, new Object[]{"属性", "值"});
				setSuccessStyle("IP查询完成");
			} catch (IOException ex) {
				exceptionMessageLabel.setText("解析失败");
				return;
			}
		};
	}


}
