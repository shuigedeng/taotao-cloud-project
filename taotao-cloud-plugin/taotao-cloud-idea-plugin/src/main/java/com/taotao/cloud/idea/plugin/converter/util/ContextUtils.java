package com.taotao.cloud.idea.plugin.converter.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;

/**
 * ContextUtils
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class ContextUtils {

    public static PsiClass getPsiClass( AnActionEvent e ) {
        return getPsiElement(e, PsiClass.class);
    }

    public static PsiMethod getPsiMethod( AnActionEvent e ) {
        return getPsiElement(e, PsiMethod.class);
    }

    private static PsiElement getPsiElement( AnActionEvent e ) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (psiFile == null || editor == null) {
            return null;
        }
        int offset = editor.getCaretModel().getOffset();
        return psiFile.findElementAt(offset);

    }

    private static <T extends PsiElement> T getPsiElement( AnActionEvent e, Class<T> clazz ) {

        PsiElement elementAt = getPsiElement(e);
        return PsiTreeUtil.getParentOfType(elementAt, clazz);
    }

}
