package com.taotao.cloud.backend.graphql;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLScalarType;
import graphql.schema.idl.SchemaDirectiveWiring;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.data.query.QuerydslDataFetcher;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.awt.print.Book;
import java.security.SecureRandom;
import java.util.Arrays;

@Configuration
public class GraphQlConfig {

    @Bean
    // public RuntimeWiringConfigurer runtimeWiringConfigurer(BookRepository repository) {
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {

        // GraphQLScalarType scalarType = ... ;
        // SchemaDirectiveWiring directiveWiring = ... ;
        // DataFetcher dataFetcher = QuerydslDataFetcher.builder(repository).single();
		//
        // return wiringBuilder -> wiringBuilder
        //         .scalar(scalarType)
        //         .directiveWiring(directiveWiring)
        //         .type("Query", builder -> builder.dataFetcher("book", dataFetcher));

		return  builder -> {
			builder
				.type("Query", typeWiring -> {
				// 查询greeting,返回hello
				typeWiring.dataFetcher("greeting", env -> "hello");
				// 查询作者
				typeWiring.dataFetcher("author", env -> {
					Integer id = Integer.valueOf(env.getArgument("id"));
					// Author author = new Author();
					// author.setId(id);
					// author.setName("小明");
					// author.setSex(SexEnum.man);
					// Book book1 = new Book();
					// book1.setBookName("无敌拳法十三式");
					// book1.setPublish(false);
					// book1.setPrice(new SecureRandom().nextFloat());
					// Book book2 = new Book();
					// book2.setBookName("独孤十三剑");
					// book2.setPublish(true);
					// book2.setPrice(new SecureRandom().nextFloat());
					// author.setBooks(Arrays.asList(book1, book2));
					return id;
				});

				return typeWiring;
			});
		};
    }
}

