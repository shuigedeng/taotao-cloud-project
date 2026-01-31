package com.taotao.cloud.idea.plugin.generateJavaBean;

import com.intellij.lang.jvm.JvmParameter;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.CollectionListModel;
import com.taotao.cloud.idea.plugin.generateJavaBean.uitls.FormatSetting;

import java.util.HashSet;
import java.util.List;

/**
 * GenerateJavaBeanAction
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class GenerateJavaBeanAction extends AnAction {

    private FormatSetting formatSetting;
    private static final String GET = "get";
    private static final String SET = "set";

    public GenerateJavaBeanAction() {
    }

    public void actionPerformed( AnActionEvent e ) {
        this.formatSetting = new FormatSetting();
        this.generateJavaBean(this.getPsiClass(e));
    }

    private void generateJavaBean( final PsiClass psiClass ) {
        PsiFile[] psiFiles = new PsiFile[]{psiClass.getContainingFile()};
        ( new WriteCommandAction.Simple(psiClass.getProject(), psiFiles) {
            protected void run() throws Throwable {
                GenerateJavaBeanAction.this.createJavaBean(psiClass);
            }
        } ).execute();
    }

    private void createJavaBean( PsiClass psiClass ) {
        List<PsiField> fields = ( new CollectionListModel(psiClass.getFields()) ).getItems();
        PsiField[] psiFields = new PsiField[fields.size()];
        fields.toArray(psiFields);
        if (fields != null) {
            List<PsiMethod> methodList = ( new CollectionListModel(psiClass.getMethods()) ).getItems();
            HashSet<String> methodSet = new HashSet();

            for (PsiMethod m : methodList) {
                methodSet.add(m.getName());
            }

            PsiElementFactory elementFactory1 = JavaPsiFacade.getElementFactory(psiClass.getProject());
            boolean needNoParamConstructor = true;
            boolean needFullParamConstructor = true;
            PsiMethod[] constructors = psiClass.getConstructors();

            for (PsiMethod constructor : constructors) {
                JvmParameter[] parameters = constructor.getParameters();
                if (parameters.length == 0) {
                    needNoParamConstructor = false;
                } else if (parameters.length == fields.size()) {
                    needFullParamConstructor = false;
                }
            }

//            if (needNoParamConstructor) {
//                String contructorText = this.buildConstructor(psiClass);
//                System.out.println("contructorText = " + contructorText);
//                PsiMethod contructor = elementFactory1.createMethodFromText(contructorText, psiClass);
//                psiClass.add(contructor);
//            }
//
//            if (needFullParamConstructor) {
//                String contructorText = this.buildConstructor(psiClass, psiFields);
//                System.out.println("contructorText = " + contructorText);
//                PsiMethod contructor = elementFactory1.createMethodFromText(contructorText, psiClass);
//                psiClass.add(contructor);
//            }

            for (PsiField field : fields) {
                if (!field.getModifierList().hasModifierProperty("final")) {
                    String methodText = this.buildGet(field);
                    PsiMethod toMethod = elementFactory1.createMethodFromText(methodText, psiClass);
                    if (!methodSet.contains(toMethod.getName())) {
                        psiClass.add(toMethod);
                    }

                    methodText = this.buildSet(psiClass, field);
                    toMethod = elementFactory1.createMethodFromText(methodText, psiClass);
                    if (!methodSet.contains(toMethod.getName())) {
                        psiClass.add(toMethod);
                    }
                }
            }

            String toStringText = this.buildToString(psiClass, psiFields);
            PsiMethod toStringMethod = elementFactory1.createMethodFromText(toStringText, psiClass);
            if (!methodSet.contains(toStringMethod.getName())) {
                psiClass.add(toStringMethod);
            }
        }

    }

    private String buildToString( PsiClass psiClass, PsiField... fields ) {
        StringBuilder sb = new StringBuilder();
        sb.append("public String toString() {\n return ");
        sb.append("\"" + psiClass.getName());
        sb.append("{");
        int length = fields.length;

        for (int i = 0; i < length; ++i) {
            PsiField field = fields[i];
            sb.append(field.getName() + " = \" + " + field.getName() + " + \"");
            if (i < length - 1) {
                sb.append(", ");
            }
        }

        sb.append("}\";\n}");
        return sb.toString();
    }

    private String buildConstructor( PsiClass psiClass, PsiField... fields ) {
        StringBuilder sb = new StringBuilder();
        sb.append("public ");
        String name = psiClass.getName();
        sb.append(name + "(");
        int length = fields.length;

        for (int i = 0; i < length; ++i) {
            PsiField field = fields[i];
            sb.append(field.getType().getPresentableText() + " " + field.getName());
            if (i < length - 1) {
                sb.append(", ");
            }
        }

        sb.append(") {\n");

        for (int i = 0; i < length; ++i) {
            PsiField field = fields[i];
            sb.append("this." + field.getName() + " = " + field.getName() + ";");
        }

        sb.append("}");
        return sb.toString();
    }

    private String buildGet( PsiField field ) {
        StringBuilder sb = new StringBuilder();
        String doc = this.format("get", field);
        if (doc != null) {
            sb.append(doc);
        }

        sb.append("public ");
        if (field.getModifierList().hasModifierProperty("static")) {
            sb.append("static ");
        }

        sb.append(field.getType().getPresentableText() + " ");
        if (field.getType().getPresentableText().equals("boolean")) {
            sb.append("is");
        } else {
            sb.append("get");
        }

        sb.append(this.getFirstUpperCase(field.getName()));
        sb.append("(){\n");
        sb.append(" return " + field.getName() + ";}\n");
        return sb.toString();
    }

    private String buildSet( PsiClass psiClass, PsiField field ) {
        StringBuilder sb = new StringBuilder();
        String doc = this.format("set", field);
        if (doc != null) {
            sb.append(doc);
        }

        boolean isStatic = false;
        sb.append("public ");
        if (field.getModifierList().hasModifierProperty("static")) {
            sb.append("static ");
            isStatic = true;
        }

        sb.append("void ");
        sb.append("set" + this.getFirstUpperCase(field.getName()));
        sb.append("(" + field.getType().getPresentableText() + " " + field.getName() + "){\n");
        if (isStatic) {
            sb.append(psiClass.getName() + "." + field.getName() + " = " + field.getName() + ";");
        } else {
            sb.append("this." + field.getName() + " = " + field.getName() + ";");
        }

        sb.append("}");
        return sb.toString();
    }

    private String getFirstUpperCase( String oldStr ) {
        return oldStr.substring(0, 1).toUpperCase() + oldStr.substring(1);
    }

    private PsiClass getPsiClass( AnActionEvent e ) {
        PsiElement elementAt = this.getPsiElement(e);
        return elementAt == null ? null : (PsiClass) PsiTreeUtil.getParentOfType(elementAt, PsiClass.class);
    }

    private PsiElement getPsiElement( AnActionEvent e ) {
        PsiFile psiFile = (PsiFile) e.getData(LangDataKeys.PSI_FILE);
        Editor editor = (Editor) e.getData(PlatformDataKeys.EDITOR);
        if (psiFile != null && editor != null) {
            int offset = editor.getCaretModel().getOffset();
            return psiFile.findElementAt(offset);
        } else {
            e.getPresentation().setEnabled(false);
            return null;
        }
    }

    private String format( String string, PsiField field ) {
        String oldContent;
        if (field.getDocComment() == null) {
            oldContent = field.getText().substring(0, field.getText().lastIndexOf("\n") + 1);
        } else {
            oldContent = field.getDocComment().getText();
        }

        oldContent = oldContent.substring(0, oldContent.length()).replaceAll("[\n,\r,*,/,\t]", "").trim();
        if ("get".equals(string)) {
            oldContent = this.formatSetting.getGetFormat().replaceAll("#\\{bare_field_comment}", oldContent)
                    .replaceAll("\\$\\{field.name}", field.getName());
        } else if ("set".equals(string)) {
            oldContent = this.formatSetting.getSetFormat().replaceAll("#\\{bare_field_comment}", oldContent)
                    .replaceAll("\\$\\{field.name}", field.getName());
        }

        return oldContent;
    }
}
