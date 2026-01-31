package com.taotao.cloud.idea.plugin.converter.model;

import com.intellij.psi.PsiMethod;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * FieldMappingResult
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class FieldMappingResult {

    /**
     * Key is a Setter method for To class field Value is a Getter method for From class field
     */
    private final Map<PsiMethod, PsiMethod> mappedFieldMap = new LinkedHashMap<>();

    private final Map<PsiMethod, PsiMethod> mappedListMap = new LinkedHashMap<>();

    private final Map<PsiMethod, PsiMethod> mappedObjectMap = new LinkedHashMap<>();

    private final List<String> notMappedToFieldList = new ArrayList<>();

    private final List<String> notMappedFromFieldList = new ArrayList<>();

    public Map<PsiMethod, PsiMethod> getMappedListMap() {
        return mappedListMap;
    }

    public Map<PsiMethod, PsiMethod> getMappedObjectMap() {
        return mappedObjectMap;
    }

    public Map<PsiMethod, PsiMethod> getMappedFieldMap() {
        return mappedFieldMap;
    }

    public List<String> getNotMappedToFieldList() {
        return notMappedToFieldList;
    }

    public List<String> getNotMappedFromFieldList() {
        return notMappedFromFieldList;
    }

    public void addMappedField( PsiMethod toSetter, PsiMethod fromGetter ) {
        mappedFieldMap.put(toSetter, fromGetter);
    }

    public void addMappedListField( PsiMethod toSetter, PsiMethod fromGetter ) {
        mappedListMap.put(toSetter, fromGetter);
    }

    public void addMappedObjectField( PsiMethod toSetter, PsiMethod fromGetter ) {
        mappedObjectMap.put(toSetter, fromGetter);
    }

    public void addNotMappedToField( String toField ) {
        notMappedToFieldList.add(toField);
    }

    public void addNotMappedFromField( String fromField ) {
        notMappedFromFieldList.add(fromField);
    }

}
