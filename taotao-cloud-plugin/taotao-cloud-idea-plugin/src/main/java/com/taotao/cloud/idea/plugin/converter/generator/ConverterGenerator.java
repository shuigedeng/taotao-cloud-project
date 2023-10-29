package com.taotao.cloud.idea.plugin.converter.generator;

import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

public interface ConverterGenerator {

    /**
     * Generate code
     * @param element method
     */
    void generate(@NotNull PsiMethod element);
}
