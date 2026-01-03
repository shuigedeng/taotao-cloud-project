package com.taotao.cloud.idea.plugin.toolkit.listener.action;

import cn.hutool.http.HttpUtil;
import com.intellij.ui.EditorTextField;
import com.taotao.cloud.idea.plugin.toolkit.notification.ToolkitNotifier;
import com.taotao.cloud.idea.plugin.toolkit.utils.JsonFormatter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JCheckBox;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * SQL2DSLConvertActionListener
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class SQL2DSLConvertActionListener implements ActionListener {

    private static final String URL = "http://www.ischoolbar.com/EsParser/convert.php";
    private EditorTextField sqlTextField;
    private EditorTextField dslTextField;
    private JCheckBox formatCheckBox;

    public SQL2DSLConvertActionListener( EditorTextField sqlTextField, EditorTextField dslTextField,
            JCheckBox formatCheckBox ) {
        this.sqlTextField = sqlTextField;
        this.dslTextField = dslTextField;
        this.formatCheckBox = formatCheckBox;
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
        String text = sqlTextField.getText();
        if (StringUtils.isBlank(text)) {
            return;
        }
        String dslText;
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("mysqlStr", text);
            dslText = HttpUtil.post(URL, paramMap);
            dslText = StringEscapeUtils.unescapeJava(dslText.substring(1, dslText.length() - 1));
        } catch (Exception ex) {
            ToolkitNotifier.error("SQL convert to ElasticSearch DSL fail, please check SQL.");
            return;
        }

        if (formatCheckBox.isSelected()) {
            try {
                dslText = JsonFormatter.format(dslText);
            } catch (Exception ex) {
                ToolkitNotifier.error("Format DSL fail");
            }
        }
        this.dslTextField.setText(dslText);
    }
}
