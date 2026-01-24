package com.taotao.cloud.gateway.predicates;

import org.springframework.cloud.gateway.server.mvc.predicate.PredicateSupplier;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

public class CustomPredicateSupplier implements PredicateSupplier {
    @Override
    public Collection<Method> get() {
        return Arrays.asList(SampleRequestPredicates.class.getMethods());
    }
}

