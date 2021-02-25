package com.taotao.cloud.idea.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import com.taotao.cloud.idea.plugin.toolset.Base64Panel;
import com.taotao.cloud.idea.plugin.toolset.IpAddressPanel;
import com.taotao.cloud.idea.plugin.toolset.JSONPanel;
import com.taotao.cloud.idea.plugin.toolset.MobilePanel;
import com.taotao.cloud.idea.plugin.toolset.RegularExpressionPanel;
import com.taotao.cloud.idea.plugin.toolset.Sql2DslPanel;
import com.taotao.cloud.idea.plugin.toolset.TimestampPanel;
import com.taotao.cloud.idea.plugin.toolset.URLPanel;
import java.awt.BorderLayout;
import javax.swing.JPanel;

public class ToolSetContainer {

	private final Project project;

	private JPanel content;

	public ToolSetContainer(Project project) {
		this.project = project;
		init();
	}

	@SuppressWarnings("checkstyle:Indentation")
	private void init() {
		JBTabsImpl tabs = new JBTabsImpl(project);

		TabInfo Sql2DslTabInfo = new TabInfo(new Sql2DslPanel(project));
		Sql2DslTabInfo.setText("Sql2Dsl");
		tabs.addTab(Sql2DslTabInfo);

		TabInfo base64TabInfo = new TabInfo(new Base64Panel(project));
		base64TabInfo.setText("Base64");
		tabs.addTab(base64TabInfo);

		TabInfo jsonTabInfo = new TabInfo(new JSONPanel(project));
		jsonTabInfo.setText("JSON格式化");
		tabs.addTab(jsonTabInfo);

		TabInfo ipAddressTabInfo = new TabInfo(new IpAddressPanel(project));
		ipAddressTabInfo.setText("Ip查询");
		tabs.addTab(ipAddressTabInfo);

		TabInfo mobileTabInfo = new TabInfo(new MobilePanel(project));
		mobileTabInfo.setText("手机归属地");
		tabs.addTab(mobileTabInfo);

		TabInfo urlTabInfo = new TabInfo(new URLPanel(project));
		urlTabInfo.setText("URL编解码");
		tabs.addTab(urlTabInfo);

		TabInfo timeTabInfo = new TabInfo(new TimestampPanel(project));
		timeTabInfo.setText("时间戳");
		tabs.addTab(timeTabInfo);

		TabInfo regularTabInfo = new TabInfo(new RegularExpressionPanel(project));
		regularTabInfo.setText("正则表达式");
		tabs.addTab(regularTabInfo);

		content = new JPanel();
		content.setLayout(new BorderLayout());
		content.add(tabs.getComponent(), BorderLayout.CENTER);
	}

	public JPanel getContent() {
		return content;
	}

}
