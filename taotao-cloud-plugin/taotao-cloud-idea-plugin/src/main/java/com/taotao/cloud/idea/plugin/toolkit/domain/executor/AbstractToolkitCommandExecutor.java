package com.taotao.cloud.idea.plugin.toolkit.domain.executor;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.ActiveIcon;
import com.intellij.openapi.ui.popup.IconButton;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;

import org.jetbrains.annotations.Nullable;

/**
 * AbstractToolkitCommandExecutor
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public abstract class AbstractToolkitCommandExecutor implements ToolkitCommandExecutor {

    protected Project getProject( DataContext dataContext ) {
        return (Project) dataContext.getData(DataKey.create("project"));
    }

    protected JBPopup createPopup( String title, Dimension dimension, @Nullable Icon titleIcon,
            JComponent content ) {
        return JBPopupFactory.getInstance()
                .createComponentPopupBuilder(content, content)
                .setTitle(title)
                .setTitleIcon(new ActiveIcon(titleIcon))
                .setCancelButton(new IconButton("Close", AllIcons.Actions.Close))
                .setMovable(true)
                .setMinSize(dimension)
                .setCancelOnClickOutside(false)
                .setCancelOnWindowDeactivation(false)
                .setCancelOnOtherWindowOpen(false)
                .setResizable(true)
                .setRequestFocus(true)
                .createPopup();
    }

    protected RelativePoint getRelativePoint( DataContext dataContext, Dimension childDimension ) {
        Component component = PlatformDataKeys.CONTEXT_COMPONENT.getData(dataContext);
        JComponent focusOwner = component instanceof JComponent ? (JComponent) component : null;
        if (focusOwner == null) {
            Project project = CommonDataKeys.PROJECT.getData(dataContext);
            JFrame frame = project == null ? null : WindowManager.getInstance().getFrame(project);
            focusOwner = frame == null ? null : frame.getRootPane();
            if (focusOwner == null) {
                throw new IllegalArgumentException("focusOwner cannot be null");
            }
        }

        final Rectangle visibleRect = focusOwner.getVisibleRect();
        Point popupMenuPoint = new Point(visibleRect.x + visibleRect.width - childDimension.width,
                visibleRect.y);
        return new RelativePoint(focusOwner, popupMenuPoint);
    }

}
