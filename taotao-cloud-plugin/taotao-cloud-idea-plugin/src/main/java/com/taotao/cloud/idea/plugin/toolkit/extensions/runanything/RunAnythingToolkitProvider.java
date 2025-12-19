package com.taotao.cloud.idea.plugin.toolkit.extensions.runanything;

import com.intellij.icons.AllIcons;
import com.intellij.ide.IdeBundle;
import com.intellij.ide.actions.runAnything.activity.RunAnythingAnActionProvider;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.util.ObjectUtils;
import com.taotao.cloud.idea.plugin.toolkit.domain.ToolkitCommand;
import com.taotao.cloud.idea.plugin.toolkit.actions.ToolkitCommandAction;
import com.taotao.cloud.idea.plugin.toolkit.service.CacheService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.Icon;

/**
 * RunAnythingToolkitProvider
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class RunAnythingToolkitProvider extends RunAnythingAnActionProvider<AnAction> {

    private static final String CACHE_KEY = "ToolkitCommandActionInstances";

    private CacheService cacheService;

    public RunAnythingToolkitProvider() {
        cacheService = ServiceManager.getService(CacheService.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<AnAction> getValues( DataContext dataContext, String pattern ) {
        Object value = cacheService.get(CACHE_KEY);
        if (Objects.nonNull(value)) {
            return (Collection<AnAction>) value;
        }
        Collection<AnAction> actions = createActions();
        cacheService.put(CACHE_KEY, actions);
        return actions;
    }

    private Collection<AnAction> createActions() {
        return Arrays.stream(ToolkitCommand.values())
                .map(ToolkitCommandAction::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getCommand( AnAction value ) {
        return this.getHelpCommand() + " " + ObjectUtils.notNull(
                value.getTemplatePresentation().getText(),
                IdeBundle.message("run.anything.actions.undefined"));
    }

    @Override
    public String getHelpGroupTitle() {
        return "Toolkit";
    }


    @Override
    public String getHelpCommand() {
        return "toolkit";
    }

    @Override
    public String getHelpCommandPlaceholder() {
        return "toolkit <command name>";
    }

    @Override
    public Icon getHelpIcon() {
        return AllIcons.General.ExternalTools;
    }

    @Override
    public String getCompletionGroupTitle() {
        return "toolkit"; //过滤界面中的名称
    }

}
