package com.taotao.cloud.idea.plugin.converter.generator.impl;

import com.intellij.lang.java.JavaLanguage;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiTypesUtil;
import com.taotao.cloud.idea.plugin.converter.exception.ConverterException;
import com.taotao.cloud.idea.plugin.converter.model.FieldMappingResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * MethodGenerator
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class MethodGenerator extends AbstractGenerator {

    @Override
    void generateCode( PsiClass psiClass, PsiMethod psiMethod ) throws ConverterException {

        if (psiMethod.isConstructor()) {
            throw new ConverterException("Method is the constructor");
        }

        if (null == psiMethod.getBody()) {
            throw new ConverterException("Method body is null");
        }

        PsiClass fromClass = getParamPsiClass(psiMethod);
        PsiClass toClass = getReturnPsiClass(psiMethod);

        // camel
        String toName = StringUtils.uncapitalize(toClass.getName());

        String fromName = psiMethod.getParameterList().getParameters()[0].getName();

        List<String> statementList = new ArrayList<>();
        statementList.add("if(" + fromName + " == null) {\nreturn null;\n}");

        statementList.add(buildNewStatement(toClass, toName));

        FieldMappingResult mappingResult = new FieldMappingResult();
        processToFields(mappingResult, fromClass, toClass);
        processFromFields(mappingResult, fromClass);

        statementList.addAll(writeMappedFields(fromName, toName, mappingResult));
        statementList.addAll(writeMappedListFields(psiClass, fromName, toName, mappingResult));
        statementList.addAll(writeMappedObjectFields(psiClass, fromName, toName, mappingResult));
        statementList.addAll(writeNotMappedFields(mappingResult.getNotMappedToFieldList(), "TO"));
        statementList.addAll(writeNotMappedFields(mappingResult.getNotMappedFromFieldList(), "FROM"));
        statementList.add("return " + toName + ";");

        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(psiClass.getProject());
        PsiCodeBlock codeBlock = elementFactory.createCodeBlockFromText("{" + String.join("\n", statementList) + "}",
                psiClass);

        for (int i = 1; i < codeBlock.getChildren().length - 1; i++) {
            PsiElement psiElement = codeBlock.getChildren()[i];
            psiMethod.getBody().add(psiElement);
        }

        JavaCodeStyleManager.getInstance(psiClass.getProject()).shortenClassReferences(psiMethod);
    }

    private void processToFields( FieldMappingResult mappingResult, PsiClass from, PsiClass to ) {
        for (PsiField toField : to.getAllFields()) {
            String toFieldName = toField.getName();
            if (toFieldName != null && !toField.getModifierList().hasExplicitModifier(PsiModifier.STATIC)) {
                PsiMethod toSetter = findSetter(to, toFieldName);
                PsiMethod fromGetter = findGetter(from, toFieldName);
                if (toSetter == null || fromGetter == null) {
                    mappingResult.addNotMappedToField(toFieldName);
                    continue;
                }

                if (isMatchingFieldType(toField, fromGetter)) {
                    mappingResult.addMappedField(toSetter, fromGetter);
                    continue;
                }

                if (toField.getType().getCanonicalText().startsWith("java.util.List")) {

                    PsiClass paramClass = getGenericParamPsiClass(toSetter);
                    String paramName = paramClass.getName();
                    PsiClass returnClass = getGenericReturnPsiClass(fromGetter);
                    String returnName = returnClass.getName();

                    if (removeSuffix(paramName).equals(removeSuffix(returnName))) {
                        mappingResult.addMappedListField(toSetter, fromGetter);
                        continue;
                    }
                }

                if (isMatchingNearbyFieldType(toField, fromGetter)) {
                    mappingResult.addMappedObjectField(toSetter, fromGetter);
                    continue;
                }

                mappingResult.addNotMappedToField(toFieldName);
            }
        }
    }

    private String removeSuffix( String name ) {
        List<String> suffixList = Arrays.asList("DTO", "JO", "DO", "VO");
        for (String suffix : suffixList) {
            if (name.endsWith(suffix)) {
                return name.substring(0, name.length() - suffix.length());
            }
        }
        return name;
    }

    private void processFromFields( FieldMappingResult mappingResult, PsiClass from ) {
        for (PsiField fromField : from.getAllFields()) {
            String fromFieldName = fromField.getName();
            if (fromFieldName != null && !fromField.getModifierList().hasExplicitModifier(PsiModifier.STATIC)) {
                PsiMethod fromGetter = findGetter(from, fromFieldName);
                boolean isMapped = mappingResult.getMappedFieldMap().containsValue(fromGetter)
                        || mappingResult.getMappedObjectMap().containsValue(fromGetter)
                        || mappingResult.getMappedListMap().containsValue(fromGetter);
                if (fromGetter == null || !isMapped) {
                    mappingResult.addNotMappedFromField(fromFieldName);
                }
            }
        }
    }

    private String buildNewStatement( PsiClass to, String toName ) {

        return to.getQualifiedName() + " " + toName + " = new " + to.getQualifiedName() + "();";
    }

    private List<String> writeMappedFields( String fromName, String toName, FieldMappingResult mappingResult ) {
        List<String> builder = new ArrayList<>();

        for (Map.Entry<PsiMethod, PsiMethod> entry : mappingResult.getMappedFieldMap().entrySet()) {
            PsiMethod toSetter = entry.getKey();
            PsiMethod fromGetter = entry.getValue();

            builder.add(toName + "." + toSetter.getName() + "(" + fromName + "." + ( fromGetter.getName() ) + "());");
        }

        return builder;
    }

    private List<String> writeMappedListFields( PsiClass psiClass, String fromName, String toName,
            FieldMappingResult mappingResult ) {
        List<String> builder = new ArrayList<>();

        for (Map.Entry<PsiMethod, PsiMethod> entry : mappingResult.getMappedObjectMap().entrySet()) {
            PsiMethod toSetter = entry.getKey();
            PsiMethod fromGetter = entry.getValue();

            PsiClass fromClass = PsiTypesUtil.getPsiClass(fromGetter.getReturnType());
            PsiClass toClass = PsiTypesUtil.getPsiClass(toSetter.getParameterList().getParameters()[0].getType());
            String converterName = removeSuffix(fromClass.getName()) + "Converter";

            String converter =
                    converterName + ".to" + toClass.getName() + "(" + fromName + "." + ( fromGetter.getName() ) + "())";
            builder.add(toName + "." + toSetter.getName() + "(" + converter + ");");

            createFile(psiClass, converterName, fromClass, toClass);
        }

        return builder;
    }

    private List<String> writeMappedObjectFields( PsiClass psiClass, String fromName, String toName,
            FieldMappingResult mappingResult ) {
        List<String> builder = new ArrayList<>();

        for (Map.Entry<PsiMethod, PsiMethod> entry : mappingResult.getMappedListMap().entrySet()) {
            PsiMethod toSetter = entry.getKey();
            PsiMethod fromGetter = entry.getValue();

            PsiClass fromClass = getGenericParamPsiClass(toSetter);
            PsiClass toClass = getGenericReturnPsiClass(fromGetter);
            String converterName = removeSuffix(fromClass.getName()) + "Converter";

            String converter =
                    converterName + ".to" + fromClass.getName() + "List(" + fromName + "." + ( fromGetter.getName() )
                            + "())";
            builder.add(toName + "." + toSetter.getName() + "(" + converter + ");");

            createFile(psiClass, converterName, fromClass, toClass);
        }

        return builder;
    }

    private void createFile( PsiClass psiClass, String converterName, PsiClass paramClass, PsiClass returnClass ) {

        PsiJavaFile javaFile = (PsiJavaFile) psiClass.getContainingFile();
        String packagePath = javaFile.getPackageName();
        PsiFile converterFile = psiClass.getContainingFile().getContainingDirectory().findFile(converterName + ".java");

        if (converterFile == null) {
            String converterContent = "package " + packagePath + ";\npublic class " + converterName + "{}";
            converterFile = PsiFileFactory.getInstance(psiClass.getProject())
                    .createFileFromText(converterName + ".java", JavaLanguage.INSTANCE, converterContent);
            PsiDirectory dir = psiClass.getContainingFile().getContainingDirectory();
            converterFile = (PsiFile) dir.add(converterFile);
        }
        final PsiClass converterClass = PsiTreeUtil.findChildOfAnyType(converterFile.getOriginalElement(),
                PsiClass.class);

        ClassGenerator classGenerator = new ClassGenerator();

        classGenerator.generateCode(converterClass, paramClass, returnClass);

        JavaCodeStyleManager.getInstance(psiClass.getProject()).shortenClassReferences(converterClass);
    }

    private List<String> writeNotMappedFields( List<String> notMappedFields, String sourceType ) {
        List<String> builder = new ArrayList<>();
        if (!notMappedFields.isEmpty()) {
            builder.add("// Not mapped " + sourceType + " fields: ");
        }
        for (String notMappedField : notMappedFields) {
            builder.add("// " + notMappedField + "");
        }
        return builder;
    }

    private PsiMethod findSetter( PsiClass psiClass, String fieldName ) {
        PsiMethod[] setters = psiClass.findMethodsByName(
                "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), true);
        if (setters.length == 1) {
            return setters[0];
        }
        return null;
    }

    private PsiMethod findGetter( PsiClass psiClass, String fieldName ) {
        String methodSuffix = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        PsiMethod[] getters = psiClass.findMethodsByName("get" + methodSuffix, true);
        if (getters.length > 0) {
            return getters[0];
        }
        getters = psiClass.findMethodsByName("is" + methodSuffix, false);
        if (getters.length > 0) {
            return getters[0];
        }
        return null;
    }

    private boolean isMatchingFieldType( PsiField toField, PsiMethod fromGetter ) {
        PsiType fromGetterReturnType = fromGetter.getReturnType();
        PsiType toFieldType = toField.getType();
        return fromGetterReturnType != null && toFieldType.isAssignableFrom(fromGetterReturnType);
    }

    private boolean isMatchingNearbyFieldType( PsiField toField, PsiMethod fromGetter ) {
        PsiClass fromGetterReturnType = PsiTypesUtil.getPsiClass(fromGetter.getReturnType());
        PsiClass toFieldType = PsiTypesUtil.getPsiClass(toField.getType());
        if (fromGetterReturnType == null || toFieldType == null) {
            return false;
        }
        String fromTypeName = removeSuffix(fromGetter.getReturnType().getCanonicalText());
        String toTypeName = removeSuffix(toField.getType().getCanonicalText());
        return toTypeName.equals(fromTypeName);
    }

    private PsiClass getGenericParamPsiClass( PsiMethod method ) {

        PsiParameter[] parameters = method.getParameterList().getParameters();
        if (parameters.length == 0) {
            return null;
        }

        PsiType paramPsiType = ( (PsiClassType) parameters[0].getType() ).getParameters()[0];
        return PsiTypesUtil.getPsiClass(paramPsiType);
    }

    private PsiClass getGenericReturnPsiClass( PsiMethod method ) {

        final PsiType returnType = method.getReturnType();
        if (null == returnType) {
            return null;
        }

        if (PsiType.VOID.equals(returnType)) {
            return null;
        }

        PsiType returnPsiType = ( (PsiClassType) returnType ).getParameters()[0];
        return PsiTypesUtil.getPsiClass(returnPsiType);
    }
}
