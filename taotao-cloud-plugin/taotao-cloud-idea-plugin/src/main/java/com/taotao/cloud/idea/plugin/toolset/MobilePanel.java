package com.taotao.cloud.idea.plugin.toolset;

import cn.hutool.http.HttpUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBUI;
import com.taotao.cloud.idea.plugin.utils.ParseJsonStrUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Map;

public class MobilePanel extends AbstractPanel {
    private static final String URL = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=";
    private final JBTextField mobileTextField = new JBTextField(20);
    private final DefaultTableModel model = new DefaultTableModel();
    private final JBTable jbTable = new JBTable(model);

    public MobilePanel(Project project) {
        super();
        this.add(createMobileTextEditor(), BorderLayout.NORTH);
        this.add(createJBTable(), BorderLayout.CENTER);
        this.add(exceptionMessageLabel, BorderLayout.SOUTH);
    }

    private JComponent createJBTable() {
        jbTable.setBorder(JBUI.Borders.customLine(JBColor.border(), 1));
        jbTable.setGridColor(JBColor.border());
        return jbTable;
    }

    private JComponent createMobileTextEditor() {
        JButton queryButton = new JButton("查询");
        queryButton.addActionListener(queryMobileButtonListener());

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(new JLabel("请输入手机号码:"), BorderLayout.WEST);
        panel.add(this.mobileTextField, BorderLayout.CENTER);
        panel.add(queryButton, BorderLayout.EAST);
        return panel;
    }

    @NotNull
    private ActionListener queryMobileButtonListener() {
        return e -> {
            setFailureStyle();
            String mobile = this.mobileTextField.getText();
            if (StringUtil.isEmpty(mobile)) {
                exceptionMessageLabel.setText("手机号必填");
                return;
            }
            String body = HttpUtil.get(URL + mobile);
            try {
                Map<String, Object> json = (Map<String, Object>) ParseJsonStrUtils.parseJson(body.replace("__GetZoneResult_ =", "").trim());
//                model.setRowCount(0);

                model.setDataVector(
                        new Object[][]{
                                {"手机号码段", json.get("mts")},
                                {"省份", json.get("province")},
                                {"卡号归属地", json.get("carrier")},
                                {"服务商", json.get("catName")}
                        }, new Object[]{"属性", "值"});
            } catch (Exception ex) {
                exceptionMessageLabel.setText("查询出错");
			}
        };
    }


}
