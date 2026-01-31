package com.taotao.cloud.idea.plugin.beanConvert;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.*;

/**
 * BeanConvertAction
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class BeanConvertAction extends AnAction {

    @Override
    public void actionPerformed( AnActionEvent event ) {
        PsiMethod psiMethod = getPsiMethodFromContext(event);
        beanConvertMethod(psiMethod);
    }

    /**
     * 启动写线程
     */
    private void beanConvertMethod( final PsiMethod psiMethod ) {
        WriteCommandAction.runWriteCommandAction(psiMethod.getProject(), () -> {
            beanConvert(psiMethod);
        });
    }

    private void beanConvert( PsiMethod psiMethod ) {
        PsiType returnType = psiMethod.getReturnType();
        if (returnType == null) {
            return;
        }
        PsiParameter psiParameter = psiMethod.getParameterList().getParameters()[0];
        Project project = psiMethod.getProject();
        List<String> methodTextList = new ArrayList<>();
        doGetMethodText(returnType, psiParameter.getType(), methodTextList, psiMethod.getContainingClass(),
                new HashSet<>());
        PsiMethod last = psiMethod;
        List<PsiMethod> shortList = new ArrayList<>();
        for (String methodText : methodTextList) {
            PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);
            PsiMethod toMethod = elementFactory.createMethodFromText(methodText, null);
            JavaCodeStyleManager.getInstance(psiMethod.getProject())
                    .shortenClassReferences(toMethod, JavaCodeStyleManager.INCOMPLETE_CODE);
            CodeStyleManager.getInstance(psiMethod.getProject()).reformat(toMethod);

            psiMethod.getContainingClass().addAfter(toMethod, last);

            shortList.add(toMethod);
        }
        psiMethod.delete();

    }

    private Map<String, PsiType> getGenericMap( PsiClass psiClass, PsiType psiType ) {
        Map<String, PsiType> map = new HashMap<>();
        PsiTypeParameterList typeParameterList = psiClass.getTypeParameterList();
        PsiType[] typeParameters1 = ( (PsiClassReferenceType) psiType ).getReference().getTypeParameters();
        if (typeParameterList != null) {
            PsiTypeParameter[] typeParameters = typeParameterList.getTypeParameters();
            for (int i = 0; i < typeParameters.length; i++) {
                map.put(typeParameters[i].getName(), typeParameters1[i]);
            }
        }
        return map;
    }

    private PsiType convertGenericMap( Map<String, PsiType> genericMap, PsiType psiType ) {
        return genericMap.get(psiType.getPresentableText()) == null ? psiType
                : genericMap.get(psiType.getPresentableText());
    }


    private void doGetMethodText( PsiType returnClassType, PsiType paramClassType, List<String> result,
            PsiClass psiClass, Set<String> set ) {
        String returnClassName = returnClassType.getInternalCanonicalText();
        String returnObjName = "result";
        String paramClassName = paramClassType.getInternalCanonicalText();
        PsiClass returnClass = ( (PsiClassReferenceType) returnClassType ).resolve();
        PsiClass paramClass = ( (PsiClassReferenceType) paramClassType ).resolve();

        Map<String, PsiType> returnGenericMap = getGenericMap(returnClass, returnClassType);
        Map<String, PsiType> paramGenericMap = getGenericMap(paramClass, paramClassType);

        StringBuilder builder = new StringBuilder("public static " + returnClassName + " convertTo"
                + ( (PsiClassReferenceType) returnClassType ).getClassName() + " (");
        if (set.contains(builder.toString())) {
            return;
        }
        set.add(builder.toString());
        builder.append(paramClassName + " item) {\n");

        builder.append("if (item == null){\n").append("return null;\n}\n");

        Optional<PsiMethod> first = Arrays.stream(returnClass.getConstructors())
                .filter(it -> it.getParameterList().getParameters().length != 0)
                .max(Comparator.comparing(it -> it.getParameterList().getParameters().length));
        Optional<PsiMethod> noArgs = Arrays.stream(returnClass.getConstructors())
                .filter(it -> it.getParameterList().getParameters().length == 0).findFirst();

        if (hasAnnotation(returnClass, "lombok.Builder")) {
            PsiField[] returnFields = returnClass.getAllFields();
            StringBuilder parameterBuilder = new StringBuilder("");
            StringBuilder topBuilder = new StringBuilder("");

            for (PsiField field : returnFields) {
                PsiModifierList modifierList = field.getModifierList();
                if (modifierList == null || modifierList.hasModifierProperty(PsiModifier.STATIC) || modifierList
                        .hasModifierProperty(PsiModifier.FINAL) || modifierList
                        .hasModifierProperty(PsiModifier.SYNCHRONIZED)) {
                    continue;
                }
                PsiField paramField = paramClass.findFieldByName(field.getNameIdentifier().getText(), false);
                if (paramField == null) {
                    parameterBuilder.append("." + field.getNameIdentifier().getText() + "(item.get"
                            + getFirstUpperCase(field.getNameIdentifier().getText()) + "())\n");
                    continue;
                }
                PsiType returnFieldType = convertGenericMap(returnGenericMap, field.getType());
                PsiType paramFieldType = convertGenericMap(paramGenericMap, paramField.getType());

                if (isListOrSet(returnFieldType, paramFieldType)) {
                    if (isSameSubType(returnFieldType, paramFieldType)) {
                        parameterBuilder.append(
                                "." + field.getNameIdentifier().getText() + "(item.get" + getFirstUpperCase(
                                        field.getNameIdentifier().getText()) + "()");
                    } else {

                        PsiType genericType = null;
                        for (PsiType parameter : ( (PsiClassReferenceType) returnFieldType ).getParameters()) {
                            genericType = parameter;
                        }
                        PsiType fromGenericType = null;
                        for (PsiType parameter : ( (PsiClassReferenceType) paramFieldType ).getParameters()) {
                            fromGenericType = parameter;
                        }

                        topBuilder.append(returnFieldType.getCanonicalText() + " " + field.getNameIdentifier().getText()
                                + "Resp = java.util.Optional.ofNullable(item.get" + getFirstUpperCase(
                                field.getNameIdentifier().getText()) + "())\n.map(it -> it.stream().map("
                                + psiClass.getName() + "::convertTo" + genericType.getPresentableText()
                                + ").collect(java.util.stream.Collectors.toList()))\n.orElse(null);\n");
                        parameterBuilder.append(
                                "." + field.getNameIdentifier().getText() + "(" + field.getNameIdentifier().getText()
                                        + "Resp)\n");

                        doGetMethodText(genericType, fromGenericType, result, psiClass, set);
                    }


                } else if (isDiffObjAndNotPrimitiveType(returnFieldType, paramFieldType)) {
                    parameterBuilder.append("." + field.getNameIdentifier().getText() + "(" + "convertTo"
                            + returnFieldType.getPresentableText() + "(item.get"
                            + getFirstUpperCase(field.getNameIdentifier().getText()) + "()))\n");
                    doGetMethodText(returnFieldType, paramFieldType, result, psiClass, set);
                } else {
                    parameterBuilder.append("." + field.getNameIdentifier().getText() + "(item.get"
                            + getFirstUpperCase(field.getNameIdentifier().getText()) + "())\n");
                }

            }
            builder.append(topBuilder);
            String parameterString = parameterBuilder.toString();
            builder.append("return " + returnClassName + ".builder()\n" + parameterString + ".build();\n");

        } else if (first.isPresent() && !noArgs.isPresent()) {
            PsiParameter[] parameters = first.get().getParameterList().getParameters();
            StringBuilder parameterBuilder = new StringBuilder("");
            StringBuilder topBuilder = new StringBuilder("");

            for (PsiParameter psiParameter : parameters) {
                PsiModifierList modifierList = psiParameter.getModifierList();
                if (modifierList == null || modifierList.hasModifierProperty(PsiModifier.STATIC) || modifierList
                        .hasModifierProperty(PsiModifier.FINAL) || modifierList
                        .hasModifierProperty(PsiModifier.SYNCHRONIZED)) {
                    continue;
                }
                String returnFileName = null;
                if (psiParameter.getNameIdentifier() != null) {
                    returnFileName = psiParameter.getNameIdentifier().getText();
                } else {
                    returnFileName = psiParameter.getText().substring(psiParameter.getText().indexOf(" ") + 1);
                }
                PsiField paramField = paramClass.findFieldByName(returnFileName, false);
                if (paramField == null) {
                    parameterBuilder.append(", ");
                    continue;
                }
                PsiType returnFieldType = convertGenericMap(returnGenericMap, psiParameter.getType());
                PsiType paramFieldType = convertGenericMap(paramGenericMap, paramField.getType());

                if (isListOrSet(returnFieldType, paramFieldType)) {
                    if (isSameSubType(returnFieldType, paramFieldType)) {
                        parameterBuilder.append(",item.get" + getFirstUpperCase(returnFileName) + "()");
                    } else {

                        PsiType genericType = null;
                        for (PsiType parameter : ( (PsiClassReferenceType) returnFieldType ).getParameters()) {
                            genericType = parameter;
                        }
                        PsiType fromGenericType = null;
                        for (PsiType parameter : ( (PsiClassReferenceType) paramFieldType ).getParameters()) {
                            fromGenericType = parameter;
                        }

                        topBuilder.append(returnFieldType.getCanonicalText() + " " + returnFileName
                                + "Resp = java.util.Optional.ofNullable(item.get" + getFirstUpperCase(returnFileName)
                                + "())\n.map(it -> it.stream().map(" + psiClass.getName() + "::convertTo"
                                + genericType.getPresentableText()
                                + ").collect(java.util.stream.Collectors.toList()))\n.orElse(null);\n");

                        parameterBuilder.append("," + returnFileName + "Resp");
                        doGetMethodText(genericType, fromGenericType, result, psiClass, set);
                    }


                } else if (isDiffObjAndNotPrimitiveType(returnFieldType, paramFieldType)) {
                    parameterBuilder.append(",convertTo" + returnFieldType.getPresentableText() + "(item.get"
                            + getFirstUpperCase(returnFileName) + "())");
                    doGetMethodText(returnFieldType, paramFieldType, result, psiClass, set);
                } else {
                    parameterBuilder.append(",item.get"
                            + getFirstUpperCase(returnFileName) + "()");
                }

            }
            builder.append(topBuilder);
            String parameterString = parameterBuilder.substring(1);
            builder.append("return new " + returnClassName + "(" + parameterString + ");\n");

        } else {
            String newObject =
                    returnClassName.contains("<") ? returnClassName.substring(0, returnClassName.indexOf("<") + 1) + ">"
                            : returnClassName;
            builder.append(returnClassName + " " + returnObjName + "= new " + newObject + "();\n");
            PsiField[] returnFields = returnClass.getAllFields();
            for (PsiField field : returnFields) {
                PsiModifierList modifierList = field.getModifierList();
                if (modifierList == null || modifierList.hasModifierProperty(PsiModifier.STATIC) || modifierList
                        .hasModifierProperty(PsiModifier.FINAL) || modifierList
                        .hasModifierProperty(PsiModifier.SYNCHRONIZED)) {
                    continue;
                }
                PsiField paramField = paramClass.findFieldByName(field.getNameIdentifier().getText(), false);
                if (paramField == null) {
                    builder.append(
                            returnObjName + ".set" + getFirstUpperCase(field.getNameIdentifier().getText()) + "();\n");
                    continue;
                }
                PsiType returnFieldType = convertGenericMap(returnGenericMap, field.getType());
                PsiType paramFieldType = convertGenericMap(paramGenericMap, paramField.getType());

                if (isListOrSet(returnFieldType, paramFieldType)) {
                    if (isSameSubType(returnFieldType, paramFieldType)) {
                        builder.append(returnObjName + ".set" + getFirstUpperCase(field.getNameIdentifier().getText())
                                + "(item.get"
                                + getFirstUpperCase(field.getNameIdentifier().getText()) + "());\n");
                    } else {
                        builder.append(
                                paramFieldType.getCanonicalText() + " " + paramField.getNameIdentifier().getText()
                                        + " = item.get" + getFirstUpperCase(field.getNameIdentifier().getText())
                                        + "();\n");
                        builder.append("if(" + paramField.getNameIdentifier().getText() + " == null){\n");
                        builder.append(returnObjName + ".set" + getFirstUpperCase(field.getNameIdentifier().getText())
                                + "(null);\n}\n");
                        builder.append("else {\n");
                        PsiType genericType = null;
                        for (PsiType parameter : ( (PsiClassReferenceType) returnFieldType ).getParameters()) {
                            genericType = parameter;
                        }
                        PsiType fromGenericType = null;
                        for (PsiType parameter : ( (PsiClassReferenceType) paramFieldType ).getParameters()) {
                            fromGenericType = parameter;
                        }

                        builder.append(
                                returnObjName + ".set" + getFirstUpperCase(field.getNameIdentifier().getText()) + "("
                                        + paramField.getNameIdentifier().getText() + ".stream().map("
                                        + psiClass.getName() + "::convertTo" + genericType.getPresentableText()
                                        + ").collect(java.util.stream.Collectors.toList()));\n");
                        builder.append("}\n");
                        doGetMethodText(genericType, fromGenericType, result, psiClass, set);
                    }


                } else if (isDiffObjAndNotPrimitiveType(returnFieldType, paramFieldType)) {
                    builder.append(returnObjName + ".set" + getFirstUpperCase(field.getNameIdentifier().getText())
                            + "(convertTo" + returnFieldType.getPresentableText() + "(item.get"
                            + getFirstUpperCase(field.getNameIdentifier().getText()) + "()));\n");
                    doGetMethodText(returnFieldType, paramFieldType, result, psiClass, set);
                } else {
                    builder.append(returnObjName + ".set" + getFirstUpperCase(field.getNameIdentifier().getText())
                            + "(item.get"
                            + getFirstUpperCase(field.getNameIdentifier().getText()) + "());\n");
                }

            }
            builder.append("return " + returnObjName + ";\n");

        }
        builder.append("}\n");
        result.add(builder.toString());
    }

    private boolean hasAnnotation( PsiClass returnClass, String annotation ) {

        PsiModifierList psiModifierList = returnClass.getModifierList();
        if (psiModifierList != null) {
            PsiAnnotation[] annotations = psiModifierList.getAnnotations();
            for (PsiAnnotation psiAnnotation : annotations) {
                if (Objects.equals(psiAnnotation.getQualifiedName(), annotation)) {
                    return true;
                }
            }
        }
        return false;

    }

    Set<String> set = new HashSet<>();

    {
        set.add("LocalDateTime");
        set.add("LocalDate");
        set.add("Date");
        set.add("String");
        set.add("BigDecimal");
    }

    boolean isPrimitiveType( PsiType psiType ) {
        return psiType instanceof PsiPrimitiveType || set.contains(psiType.getPresentableText());
    }

    boolean isDiffObjAndNotPrimitiveType( PsiType to, PsiType from ) {
        return !( to instanceof PsiPrimitiveType ) && !set.contains(to.getPresentableText())
                && !to.getInternalCanonicalText().equals(from.getInternalCanonicalText())
                && !( from instanceof PsiPrimitiveType );
    }


    boolean isListOrSet( PsiType to, PsiType from ) {
        String toPresentableText = to.getPresentableText();
        String fromPresentableText = from.getPresentableText();
        if (( toPresentableText.startsWith("List") || toPresentableText.startsWith("ArrayList") ) && (
                fromPresentableText.startsWith("List") || fromPresentableText.startsWith("ArrayList") )) {
            return true;
        }

        if (( toPresentableText.startsWith("Set") || toPresentableText.startsWith("HashSet") ) && (
                fromPresentableText.startsWith("Set") || fromPresentableText.startsWith("HashSet") )) {

            return true;
        }
        return false;
    }

    boolean isSameSubType( PsiType to, PsiType from ) {
        String toPresentableText = to.getCanonicalText();
        String fromPresentableText = from.getCanonicalText();
        String toSubType = toPresentableText.substring(toPresentableText.indexOf("<") + 1,
                toPresentableText.lastIndexOf(">"));
        String fromSubType = fromPresentableText.substring(fromPresentableText.indexOf("<") + 1,
                fromPresentableText.lastIndexOf(">"));
        return toSubType.equals(fromSubType);
    }


    PsiClass getPsiClass( PsiType psiType, Project project ) {
        String parameterClassWithPackage = psiType.getInternalCanonicalText().replaceAll("\\<.*?\\>", "");
        //为了解析字段，这里需要加载参数的class
        JavaPsiFacade facade = JavaPsiFacade.getInstance(project);
        return facade
                .findClass(parameterClassWithPackage, GlobalSearchScope.allScope(project));
    }


    private String getFirstUpperCase( String oldStr ) {
        return oldStr.substring(0, 1).toUpperCase() + oldStr.substring(1);
    }

    private PsiMethod getPsiMethodFromContext( AnActionEvent e ) {
        PsiElement elementAt = getPsiElement(e);
        if (elementAt == null) {
            return null;
        }
        return PsiTreeUtil.getParentOfType(elementAt, PsiMethod.class);
    }

    private PsiElement getPsiElement( AnActionEvent e ) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (psiFile == null || editor == null) {
            e.getPresentation().setEnabled(false);
            return null;
        }
        //用来获取当前光标处的PsiElement
        int offset = editor.getCaretModel().getOffset();
        return psiFile.findElementAt(offset);
    }
}
