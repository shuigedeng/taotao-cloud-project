package com.taotao.cloud.idea.plugin.toolkit.notification;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;

/**
 * Toolkit Notifier
 */
public class ToolkitNotifier {

    private static final NotificationGroup NOTIFICATION_GROUP = new NotificationGroup("Toolkit Information", NotificationDisplayType.BALLOON, false);

    private ToolkitNotifier() {
    }

    public static void success(String message) {
        final Notification notification = NOTIFICATION_GROUP.createNotification("Toolkit Information", message, NotificationType.INFORMATION, null);
        notification.notify(null);
    }

    public static void error(String message) {
        final Notification notification = NOTIFICATION_GROUP.createNotification("Toolkit Information", message, NotificationType.ERROR, null);
        notification.notify(null);
    }
}
