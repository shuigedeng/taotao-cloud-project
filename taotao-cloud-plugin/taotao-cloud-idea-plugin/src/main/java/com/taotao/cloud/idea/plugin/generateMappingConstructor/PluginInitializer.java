package com.taotao.cloud.idea.plugin.generateMappingConstructor;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;

/**
 * PluginInitializer
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class PluginInitializer implements StartupActivity {

    @Override
    public void runActivity( @NotNull Project project ) {
        ClassChooserUtil.initialize(project);
    }
}
