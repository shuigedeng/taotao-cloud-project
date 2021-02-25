package com.taotao.cloud.idea.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.sun.istack.NotNull;

public class ProgrammerToolsetWindowFactory implements ToolWindowFactory, Condition<Project> {

	@Override
	public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
		ToolSetContainer toolSetContainer = new ToolSetContainer(project);
		ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
		Content content = contentFactory.createContent(toolSetContainer.getContent(), "", false);
		toolWindow.getContentManager().addContent(content);
	}

	@Override
	public boolean value(Project project) {
		return false;
	}
}
