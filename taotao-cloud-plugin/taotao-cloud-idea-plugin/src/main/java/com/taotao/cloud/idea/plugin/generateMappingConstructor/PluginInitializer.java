package com.taotao.cloud.idea.plugin.generateMappingConstructor;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;

public class PluginInitializer implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {
        ClassChooserUtil.initialize(project);
    }
}
