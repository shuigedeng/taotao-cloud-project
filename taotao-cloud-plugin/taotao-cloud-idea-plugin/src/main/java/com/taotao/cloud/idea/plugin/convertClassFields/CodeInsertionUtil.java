package com.taotao.cloud.idea.plugin.convertClassFields;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;

import java.util.List;

public class CodeInsertionUtil {

    /**
     * 在当前光标位置的下一行插入代码并格式化。
     *
     * @param project 当前项目
     * @param editor 当前编辑器
     * @param codeLines 要插入的代码行列表
     */
    public static void insertCodeBelowCursor(Project project, Editor editor, List<String> codeLines) {
        WriteCommandAction.runWriteCommandAction(project, () -> {
            Document document = editor.getDocument();
            int currentOffset = editor.getCaretModel().getOffset();
            int currentLine = document.getLineNumber(currentOffset);
            int nextLineStartOffset = document.getLineStartOffset(currentLine + 1);

            // 生成要插入的文本
            String insertText = String.join("\n", codeLines) + "\n";
            
            // 插入文本
            document.insertString(nextLineStartOffset, insertText);

            // 格式化插入的代码
            PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
            if (psiFile != null) {
                CodeStyleManager codeStyleManager = CodeStyleManager.getInstance(project);
                codeStyleManager.reformatText(psiFile, nextLineStartOffset, nextLineStartOffset + insertText.length());
            }
        });
    }
}
