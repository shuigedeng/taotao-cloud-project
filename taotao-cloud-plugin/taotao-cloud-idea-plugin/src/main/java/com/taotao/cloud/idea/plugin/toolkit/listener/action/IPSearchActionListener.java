package com.taotao.cloud.idea.plugin.toolkit.listener.action;

import tools.jackson.databind.JsonNode;
import com.intellij.ui.EditorTextField;
import com.taotao.cloud.idea.plugin.toolkit.notification.ToolkitNotifier;
import com.taotao.cloud.idea.plugin.toolkit.utils.JsonUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import com.taotao.boot.common.utils.lang.StringUtils;


public class IPSearchActionListener implements ActionListener {
    private static final String URL = "http://whois.pconline.com.cn/ipJson.jsp";

    private EditorTextField editorTextField;
    private DefaultTableModel tableModel;

    public IPSearchActionListener(EditorTextField editorTextField, DefaultTableModel tableModel) {
        this.editorTextField = editorTextField;
        this.tableModel = tableModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String text = editorTextField.getText().trim();
        if (StringUtils.isBlank(text)) {
            return;
        }

        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("json", true);
            paramMap.put("ip", text);
            String body = HttpUtil.post(URL, paramMap);
            JsonNode node = JsonUtils.read(body);
            tableModel.setDataVector(
                    new Object[][]{
                            {"省份", node.get("pro")},
                            {"城市", node.get("city")},
                            {"IP归属地", node.get("addr")}
                    }, new Object[]{"属性", "值"});
        } catch (Exception ex) {
            ToolkitNotifier.error("Search ip address fail");
        }

    }
}
