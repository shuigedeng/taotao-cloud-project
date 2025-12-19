package com.taotao.cloud.idea.plugin.toolkit.listener.action;

import com.intellij.ui.EditorTextField;
import com.taotao.cloud.idea.plugin.toolkit.notification.ToolkitNotifier;
import com.taotao.cloud.idea.plugin.toolkit.utils.QRCodeUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * GenerateQrcodeActionListener
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class GenerateQrcodeActionListener implements ActionListener {

    private EditorTextField contentTextField;
    private EditorTextField logoTextField;
    private JLabel imageLabel;

    public GenerateQrcodeActionListener( EditorTextField contentTextField, EditorTextField logoTextField,
            JLabel imageLabel ) {
        this.contentTextField = contentTextField;
        this.logoTextField = logoTextField;
        this.imageLabel = imageLabel;
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
        String text = this.contentTextField.getText();
//        if (StringUtils.isBlank(text)) {
//            return;
//        }

        try {
            String logoPath = logoTextField.getText();
            BufferedImage image = QRCodeUtils.createImage(text, logoPath, true);
            imageLabel.setIcon(new ImageIcon(image));
        } catch (Exception ex) {
            ToolkitNotifier.error("Generate Qrcode Fail");
        }
    }
}
