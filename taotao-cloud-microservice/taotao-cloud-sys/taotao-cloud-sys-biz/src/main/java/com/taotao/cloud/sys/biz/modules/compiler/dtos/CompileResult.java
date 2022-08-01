package com.taotao.cloud.sys.biz.modules.compiler.dtos;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sanri.tools.modules.core.utils.OnlyPath;

import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

public class CompileResult {
    private DiagnosticCollector<JavaFileObject> diagnosticCollector;
    private boolean result;
    private OnlyPath classes;
    @JsonIgnore
    private Collection<File> classFiles = new ArrayList<>();

    public CompileResult() {
    }

    public CompileResult(DiagnosticCollector<JavaFileObject> diagnosticCollector, boolean result,File classes) {
        this.diagnosticCollector = diagnosticCollector;
        this.result = result;
        this.classes = new OnlyPath(classes);
    }

    public void setClassFiles(Collection<File> classFiles) {
        this.classFiles = classFiles;
    }

    public Collection<File> getClassFiles() {
        return classFiles;
    }

    public boolean isResult() {
        return result;
    }

    /**
     * 获取编译错误列表
     * @return
     */
    public List<String> getCompileErrors(){
        if (diagnosticCollector != null){
            List<String> compileErrors = new ArrayList<>();
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnosticCollector.getDiagnostics()) {
//                System.out.println(diagnostic);
                compileErrors.add(diagnostic.toString());
            }
            return compileErrors;
        }
        return new ArrayList<>();
    }
}
