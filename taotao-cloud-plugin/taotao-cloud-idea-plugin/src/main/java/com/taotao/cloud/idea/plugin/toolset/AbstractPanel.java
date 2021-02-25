package com.taotao.cloud.idea.plugin.toolset;

import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;

public class AbstractPanel extends JPanel {
	protected JLabel exceptionMessageLabel = new JLabel();

	public AbstractPanel() {
		this(0, 10, 10);
	}

	public AbstractPanel(int hgap, int vgap) {
		this(hgap, vgap, 10);
	}

	public AbstractPanel(int hgap, int vgap, int borderGrap) {
		super(new BorderLayout(hgap, vgap));
		this.setBorder(JBUI.Borders.empty(borderGrap));
	}

	protected void setFailureStyle() {
		exceptionMessageLabel.setText("");
		exceptionMessageLabel.setForeground(JBColor.RED);
	}

	protected void setSuccessStyle(String message) {
		exceptionMessageLabel.setForeground(JBColor.GREEN);
		exceptionMessageLabel.setText(message);
	}
}
