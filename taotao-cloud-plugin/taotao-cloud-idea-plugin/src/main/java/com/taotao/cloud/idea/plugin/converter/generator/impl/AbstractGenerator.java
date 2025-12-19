package com.taotao.cloud.idea.plugin.converter.generator.impl;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiTypesUtil;
import com.taotao.cloud.idea.plugin.converter.exception.ConverterException;
import com.taotao.cloud.idea.plugin.converter.generator.ConverterGenerator;
import org.jetbrains.annotations.NotNull;

/**
 * AbstractGenerator
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public abstract class AbstractGenerator implements ConverterGenerator {

    @Override
    public void generate( @NotNull PsiMethod psiMethod ) {

        PsiClass psiClass = (PsiClass) psiMethod.getParent();

        new WriteCommandAction.Simple(psiClass.getProject(), psiClass.getContainingFile()) {

            @Override
            protected void run() {
                try {
                    generateCode(psiClass, psiMethod);
                } catch (ConverterException e) {
                    Messages.showErrorDialog(e.getMessage(), "Converter Plugin");
                }
            }

        }.execute();

    }

    /**
     * Generate code
     *
     * @param psiClass class
     * @param psiMethod method
     * @throws ConverterException exception
     */
    abstract void generateCode( PsiClass psiClass, PsiMethod psiMethod ) throws ConverterException;

    PsiClass getParamPsiClass( PsiMethod method ) throws ConverterException {

        PsiParameter[] parameters = method.getParameterList().getParameters();

        if (parameters.length == 0) {
            throw new ConverterException("The method does not have any parameter");
        }

        return PsiTypesUtil.getPsiClass(parameters[0].getType());
    }

    PsiClass getReturnPsiClass( PsiMethod method ) throws ConverterException {

        final PsiType returnType = method.getReturnType();
        if (PsiType.VOID.equals(returnType)) {
            throw new ConverterException("The return of method is void");
        }

        return PsiTypesUtil.getPsiClass(returnType);
    }

}
