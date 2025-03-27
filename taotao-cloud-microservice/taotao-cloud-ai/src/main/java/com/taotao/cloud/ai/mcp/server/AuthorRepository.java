package com.taotao.cloud.ai.mcp.server;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import java.util.List;
public class AuthorRepository {
	//MCP Server注册我们的作者工具
	@Bean
	ToolCallbackProvider authorTools() {
		return MethodToolCallbackProvider
			.builder()
			.toolObjects(new AuthorRepository())
			.build();
	}


	@Tool(description = "Get Baeldung author details using an article title")
	Author getAuthorByArticleTitle(String articleTitle) {
		return new Author("John Doe", "john.doe@baeldung.com");
	}

	@Tool(description = "Get highest rated Baeldung authors")
	List<Author> getTopAuthors() {
		return List.of(
			new Author("John Doe", "john.doe@baeldung.com"),
			new Author("Jane Doe", "jane.doe@baeldung.com")
		);
	}

	record Author(String name, String email) {
	}

}
