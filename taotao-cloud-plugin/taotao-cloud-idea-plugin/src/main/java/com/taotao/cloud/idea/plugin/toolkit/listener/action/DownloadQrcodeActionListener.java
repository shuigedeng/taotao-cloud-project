package com.taotao.cloud.idea.plugin.toolkit.listener.action;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.taotao.cloud.idea.plugin.toolkit.notification.ToolkitNotifier;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class DownloadQrcodeActionListener implements ActionListener {
    private JLabel imageLabel;
    private Project project;

    public DownloadQrcodeActionListener(Project project, JLabel imageLabel) {
        this.project = project;
        this.imageLabel = imageLabel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileChooserDescriptor chooserDescriptor = new FileChooserDescriptor(false, true, false, false, false, false);
        VirtualFile virtualFile = FileChooser.chooseFile(chooserDescriptor, this.project, null);
        if (virtualFile == null) {
            ToolkitNotifier.error("请选择下载目录");
            return;
        }
        String destPath = virtualFile.getPath();
        String file = new Random().nextInt(99999999) + ".jpg";
        try {

            ImageIcon imageIcon = (ImageIcon) imageLabel.getIcon();
            ImageIO.write((RenderedImage) imageIcon.getImage(), "JPG", new File(destPath + "/" + file));
            ToolkitNotifier.success("Download image success");
        } catch (Exception ex) {
            ToolkitNotifier.error("Download image fail");
        }
    }

}
