package com.taotao.cloud.idea.plugin.toolset;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UI;
import com.taotao.cloud.idea.plugin.toolset.listener.JBTextFieldHintListener;
import com.taotao.cloud.idea.plugin.toolset.model.TimeUnit;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Date;

public class TimestampPanel extends AbstractPanel {
	private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
	private final JBTextField timeStampTextField1 = new JBTextField();
	private final JBTextField timeStampTextField2 = new JBTextField();

	private final ComboBox<TimeUnit> timeUnitBox1 = new ComboBox<>(TimeUnit.values());
	private final ComboBox<TimeUnit> timeUnitBox2 = new ComboBox<>(TimeUnit.values());

	private final JBTextField dateTextField1 = new JBTextField();
	private final JBTextField dateTextField2 = new JBTextField();

	public TimestampPanel(Project project) {
		super();
		initialComponent();
		this.add(UI.PanelFactory.grid()
				.add(UI.PanelFactory.panel(addTimestampToDate()))
				.add(UI.PanelFactory.panel(addDateToTimestamp()))
				.createPanel(),
			BorderLayout.NORTH);
	}

	private void initialComponent() {
		timeUnitBox1.setRenderer(this.cellRenderer());
		timeUnitBox2.setRenderer(this.cellRenderer());
		dateTextField1.addFocusListener(new JBTextFieldHintListener(dateTextField1, FORMAT));
		dateTextField2.addFocusListener(new JBTextFieldHintListener(dateTextField2, FORMAT));

		timeStampTextField1.setText(String.valueOf(System.currentTimeMillis()));
		dateTextField2.setText(DateUtil.format(new Date(), FORMAT));
	}

	private JComponent addDateToTimestamp() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};  //设置了总共有4列
		gridBagLayout.rowHeights = new int[]{0};  //设置了总共有2行
		gridBagLayout.columnWeights = new double[]{0.48, 0.02, 0.48, 0.02};  //设置了列的宽度为容器宽度
		gridBagLayout.rowWeights = new double[]{1.0};  //第一行的高度占了容器的2份，第二行的高度占了容器的8份

		JPanel jPanel = new JPanel();
		jPanel.setLayout(gridBagLayout);

		JButton convertButton = new JButton("转换");
		convertButton.addActionListener(dateToTimestampListener());


		GridBagConstraints gbc0 = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, JBUI.emptyInsets(), 0, 0);
		jPanel.add(UI.Panels.simplePanel()
				.addToLeft(new JBLabel("日   期："))
				.addToCenter(dateTextField2)
			, gbc0);

		GridBagConstraints gbc1 = new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, JBUI.emptyInsets(), 0, 0);
		jPanel.add(convertButton, gbc1);

		GridBagConstraints gbc2 = new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, JBUI.emptyInsets(), 0, 0);
		jPanel.add(UI.Panels.simplePanel()
				.addToLeft(new JBLabel("时间戳："))
				.addToCenter(timeStampTextField2)
			, gbc2);
		GridBagConstraints gbc3 = new GridBagConstraints(3, 0, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, JBUI.emptyInsets(), 0, 0);
		jPanel.add(timeUnitBox2, gbc3);

		return jPanel;
	}

	private ActionListener timestampToDateListener() {
		return e -> {
			String text = timeStampTextField1.getText();
			try {
				TimeUnit timeUnit = (TimeUnit) timeUnitBox1.getSelectedItem();
				Date date = timeUnit.getDate(Long.valueOf(text));
				dateTextField1.setText(DateUtil.format(date, FORMAT));
			} catch (Exception ex) {
				dateTextField1.setText("转换失败，请检查时间戳");
			}
		};
	}

	private ActionListener dateToTimestampListener() {
		return e -> {
			String text = dateTextField2.getText();
			try {
				DateTime dateTime = DateUtil.parse(text, FORMAT);
				TimeUnit timeUnit = (TimeUnit) timeUnitBox2.getSelectedItem();
				timeStampTextField2.setText(timeUnit.getLongValue(dateTime));
			} catch (Exception ex) {
				timeStampTextField2.setText("转换失败，请检查日期格式");
			}
		};
	}

	private JComponent addTimestampToDate() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};  //设置了总共有4列
		gridBagLayout.rowHeights = new int[]{0};  //设置了总共有2行
		gridBagLayout.columnWeights = new double[]{0.48, 0.02, 0.02, 0.48};  //设置了列的宽度为容器宽度
		gridBagLayout.rowWeights = new double[]{1.0};  //第一行的高度占了容器的2份，第二行的高度占了容器的8份

		JPanel jPanel = new JPanel();
		jPanel.setLayout(gridBagLayout);

		JButton convertButton = new JButton("转换");
		convertButton.addActionListener(timestampToDateListener());

		GridBagConstraints gbc0 = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, JBUI.emptyInsets(), 0, 0);
		jPanel.add(UI.Panels.simplePanel()
				.addToLeft(new JBLabel("时间戳："))
				.addToCenter(timeStampTextField1)
			, gbc0);

		GridBagConstraints gbc2 = new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, JBUI.emptyInsets(), 0, 0);
		jPanel.add(timeUnitBox1, gbc2);

		GridBagConstraints gbc3 = new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, JBUI.emptyInsets(), 0, 0);
		jPanel.add(convertButton, gbc3);

		GridBagConstraints gbc4 = new GridBagConstraints(3, 0, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, JBUI.emptyInsets(), 0, 0);
		jPanel.add(UI.Panels.simplePanel()
				.addToLeft(new JBLabel("日期："))
				.addToCenter(dateTextField1)
			, gbc4);

		return jPanel;
	}

	private ColoredListCellRenderer<TimeUnit> cellRenderer() {
		return new ColoredListCellRenderer<TimeUnit>() {
			@Override
			protected void customizeCellRenderer(@NotNull JList<? extends TimeUnit> list, TimeUnit timeUnit,
												 int index, boolean selected, boolean hasFocus) {
				this.append(timeUnit.getLabel());
			}
		};
	}

}
