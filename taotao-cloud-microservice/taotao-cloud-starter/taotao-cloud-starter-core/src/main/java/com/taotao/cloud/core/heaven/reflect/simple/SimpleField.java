package com.taotao.cloud.core.heaven.reflect.simple;


import com.taotao.cloud.core.heaven.reflect.api.IField;
import com.taotao.cloud.core.heaven.util.common.ArgUtil;
import com.taotao.cloud.core.heaven.util.util.CollectionUtil;
import com.taotao.cloud.core.heaven.util.util.Optional;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 简单字段
 */
public class SimpleField implements IField {

    /**
     * 原始字段信息
     */
    private Field field;

    private List<Annotation> annotations;

    private String name;

    private String fullName;

    private Class type;

    private int access;

    /**
     * 字段对应得值
     */
    private Object value;

    @Override
    public List<Annotation> annotations() {
        return annotations;
    }

    @Override
    public Optional<Annotation> annotationOpt(Class type) {
        ArgUtil.notNull(type, "type");

        if(CollectionUtil.isEmpty(this.annotations)) {
            return Optional.empty();
        }
        for(Annotation annotation : annotations) {
            if(type.equals(annotation.annotationType())) {
                return Optional.of(annotation);
            }
        }
        return Optional.empty();
    }

    @Override
    public Annotation annotation(Class type) {
        ArgUtil.notNull(type, "type");

        Optional<Annotation> annotationOptional = this.annotationOpt(type);
        return annotationOptional.orElseNull();
    }

    public SimpleField annotations(List<Annotation> annotations) {
        this.annotations = annotations;
        return this;
    }

    @Override
    public String name() {
        return name;
    }

    public SimpleField name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String fullName() {
        return fullName;
    }

    public SimpleField fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    @Override
    public Class type() {
        return type;
    }

    public SimpleField type(Class type) {
        this.type = type;
        return this;
    }

    @Override
    public int access() {
        return access;
    }

    public SimpleField access(int access) {
        this.access = access;
        return this;
    }

    @Override
    public Object value() {
        return value;
    }

    @Override
    public SimpleField value(Object value) {
        this.value = value;
        return this;
    }

    @Override
    public Field field() {
        return field;
    }

    public SimpleField field(Field field) {
        this.field = field;
        return this;
    }

    @Override
    public int compareTo(IField o) {
        return this.name.compareTo(o.name());
    }
}
