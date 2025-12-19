package com.taotao.cloud.idea.plugin.convertClassFields;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * ConvertClassFieldsAction
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class ConvertClassFieldsAction extends AnAction {

    private static final List<String> LIST_TYPES = Arrays.asList(
            "List", "ArrayList", "LinkedList", "Vector", "Stack", "CopyOnWriteArrayList"
    );

    @Override
    public void actionPerformed( AnActionEvent e ) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);

        if (project == null || editor == null || psiFile == null) {
            showErrorMessage(project, "Invalid context", "Unable to perform the action in the current context.");
            return;
        }

        SelectionModel selectionModel = editor.getSelectionModel();
        if (!selectionModel.hasSelection()) {
            showErrorMessage(project, "No Selection", "Please select a variable to convert.");
            return;
        }

        int startOffset = selectionModel.getSelectionStart();
        PsiElement element = psiFile.findElementAt(startOffset);
        if (element == null) {
            showErrorMessage(project, "Invalid Selection", "Unable to find a valid element at the cursor position.");
            return;
        }
        PsiVariable variable = PsiTreeUtil.getParentOfType(element, PsiVariable.class);
        if (variable == null) {
            showErrorMessage(project, "Invalid Selection", "Please select a variable of a class type.");
            return;
        }

        PsiType type = variable.getType();
        PsiClass sourceClass = null;
        String sourceTypeName = "";

        if (type instanceof PsiClassType) {
            PsiClassType classType = (PsiClassType) type;
            String className = classType.getClassName();
            if (LIST_TYPES.contains(className)) {
                PsiType[] parameters = classType.getParameters();
                if (parameters.length > 0 && parameters[0] instanceof PsiClassType) {
                    sourceClass = ( (PsiClassType) parameters[0] ).resolve();
                    sourceTypeName = parameters[0].getPresentableText();
                }
            } else {
                sourceClass = classType.resolve();
                sourceTypeName = classType.getClassName();
            }
        }

        if (sourceClass == null) {
            showErrorMessage(project, "Invalid Selection", "Please select a variable of a valid class type.");
            return;
        }

        CombinedConversionDialog dialog = new CombinedConversionDialog(project, variable.getName(), sourceClass);
        if (dialog.showAndGet()) {
            PsiClass targetClass = dialog.getTargetClass();
            boolean isList = dialog.isList();
            String sourceName = dialog.getSourceName();
            String targetName = dialog.getTargetName();
            Map<String, String> fieldMapping = dialog.getFieldMapping();

            generateConversionCode(project, editor, sourceClass, targetClass, isList, sourceName, targetName,
                    sourceTypeName, fieldMapping);
        }
    }

    private void showErrorMessage( Project project, String title, String message ) {
        Messages.showErrorDialog(project, message, title);
    }


    private void generateSetterCalls( PsiClass targetClass, PsiClass sourceClass, String targetName, String sourceName,
            List<String> conversionCode, String indent, Map<String, String> fieldMapping ) {
        if (sourceClass == null) {
            return;
        }
        for (PsiField targetField : targetClass.getFields()) {
            String targetFieldName = targetField.getName();
            if (fieldMapping.containsKey(targetFieldName)) {
                PsiMethod setter = findSetter(targetClass, targetField);
                if (setter != null) {
                    String sourceFieldName = fieldMapping.get(targetFieldName);
                    PsiField sourceField = sourceClass.findFieldByName(sourceFieldName, false);
                    if (sourceField != null) {
                        PsiMethod getter = findGetter(sourceClass, sourceField);
                        if (getter != null) {
                            String setterCall = indent + targetName + "." + setter.getName() + "(" +
                                    sourceName + "." + getter.getName() + "());";
                            conversionCode.add(setterCall);
                        }
                    }
                }
            }
        }
    }

    private void generateConversionCode( Project project, Editor editor, PsiClass sourceClass, PsiClass targetClass,
            boolean isList, String sourceVariableName, String targetName, String sourceTypeName,
            Map<String, String> fieldMapping ) {
        WriteCommandAction.runWriteCommandAction(project, () -> {
            List<String> conversionCode = new ArrayList<>();
            if (isList) {
                String sourceElementTypeName = sourceTypeName.replaceFirst("^List<", "").replaceFirst(">$", "");

                conversionCode.add("List<" + targetClass.getName() + "> " + targetName + " = new ArrayList<>();");
                conversionCode.add("for (" + sourceElementTypeName + " item : " + sourceVariableName + ") {");
                conversionCode.add(
                        "    " + targetClass.getName() + " " + targetName + "Item = new " + targetClass.getName()
                                + "();");
                generateSetterCalls(targetClass, sourceClass, targetName + "Item", "item", conversionCode, "    ",
                        fieldMapping);
                conversionCode.add("    " + targetName + ".add(" + targetName + "Item);");
                conversionCode.add("}");
            } else {
                conversionCode.add(
                        targetClass.getName() + " " + targetName + " = new " + targetClass.getName() + "();");
                generateSetterCalls(targetClass, sourceClass, targetName, sourceVariableName, conversionCode, "",
                        fieldMapping);
            }

            CodeInsertionUtil.insertCodeBelowCursor(project, editor, conversionCode);
        });
    }

    private PsiMethod findSetter( PsiClass psiClass, PsiField field ) {
        String setterName = "set" + capitalize(field.getName());
        PsiMethod[] setters = psiClass.findMethodsByName(setterName, false);
        return setters.length > 0 ? setters[0] : null;
    }

    private PsiMethod findGetter( PsiClass psiClass, PsiField field ) {
        String getterName = "get" + capitalize(field.getName());
        PsiMethod[] getters = psiClass.findMethodsByName(getterName, false);
        return getters.length > 0 ? getters[0] : null;
    }


    private String capitalize( String str ) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
