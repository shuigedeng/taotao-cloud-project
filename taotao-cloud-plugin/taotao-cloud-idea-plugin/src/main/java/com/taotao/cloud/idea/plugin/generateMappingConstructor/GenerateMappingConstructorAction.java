package com.taotao.cloud.idea.plugin.generateMappingConstructor;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;

/**
 * GenerateMappingConstructorAction
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class GenerateMappingConstructorAction extends AnAction {

    @Override
    public void actionPerformed( AnActionEvent e ) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);

        if (project == null || editor == null || !( psiFile instanceof PsiJavaFile )) {
            return;
        }

        PsiElement element = psiFile.findElementAt(editor.getCaretModel().getOffset());
        PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);

        if (psiClass == null) {
            return;
        }

        ClassChooserUtil.ClassChoiceResult result = ClassChooserUtil.chooseClass(project);
        if (result == null) {
            return;
        }

        generateConstructor(project, psiClass, result.getSelectedClass(), result.getDtoParamName());
    }

    private void generateConstructor( Project project, PsiClass psiClass, PsiClass inputClass, String dtoParamName ) {
        PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
        StringBuilder constructorBuilder = new StringBuilder();

        constructorBuilder.append("public ").append(psiClass.getName()).append("(")
                .append(inputClass.getName()).append(" ").append(dtoParamName).append(") {\n");

        boolean hasLombokData = hasLombokAnnotation(inputClass, "Data");
        boolean hasLombokGetter = hasLombokAnnotation(inputClass, "Getter");

        for (PsiField currentField : psiClass.getAllFields()) {
            String fieldName = currentField.getName();
            PsiField inputField = inputClass.findFieldByName(fieldName, true);

            if (inputField != null) {
                String getterName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

                boolean hasGetter = hasLombokData || hasLombokGetter ||
                        hasLombokAnnotation(inputField, "Getter") ||
                        inputClass.findMethodsByName(getterName, true).length > 0;

                if (hasGetter) {
                    constructorBuilder.append("    this.").append(fieldName)
                            .append(" = ").append(dtoParamName).append(".").append(getterName).append("();\n");
                } else {
                    constructorBuilder.append("    this.").append(fieldName)
                            .append(" = ").append(dtoParamName).append(".").append(fieldName).append(";\n");
                }
            } else {
                constructorBuilder.append("    // this.").append(fieldName)
                        .append(" = // No matching field found in input class\n");
            }
        }

        constructorBuilder.append("}");

        PsiMethod constructor = factory.createMethodFromText(constructorBuilder.toString(), psiClass);

        WriteCommandAction.runWriteCommandAction(project, () -> {
            psiClass.add(constructor);
        });
    }

    private boolean hasLombokAnnotation( PsiModifierListOwner element, String annotationName ) {
        PsiModifierList modifierList = element.getModifierList();
        return modifierList != null && modifierList.findAnnotation("lombok." + annotationName) != null;
    }
}
