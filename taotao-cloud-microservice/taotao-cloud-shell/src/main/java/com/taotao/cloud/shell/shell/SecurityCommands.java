package com.taotao.cloud.shell.shell;

import org.springframework.context.annotation.Bean;
import org.springframework.shell.core.command.ExitStatus;
import org.springframework.shell.core.command.availability.Availability;
import org.springframework.shell.core.command.availability.AvailabilityProvider;
import org.springframework.shell.core.command.completion.CompletionProposal;
import org.springframework.shell.core.command.completion.CompletionProvider;
import org.springframework.shell.core.command.completion.CompositeCompletionProvider;
import org.springframework.shell.core.command.completion.EnumCompletionProvider;
import org.springframework.shell.core.command.exit.ExitStatusExceptionMapper;
import org.springframework.stereotype.Component;
import org.springframework.shell.core.command.annotation.Command;

import java.util.List;

@Component
public class SecurityCommands {
    
    private boolean loggedIn = false;
    
    @Command(name = "login", description = "登录系统")
    public String login(String username, String password) {
        // 实现登录逻辑
        this.loggedIn = true;
        return "用户 " + username + " 已登录";
    }
    
    @Command(name = "query", description = "查看敏感信息")
    public String query() {
        return "这是敏感信息，只有登录后才能查看";
    }
    
    @Bean
    public AvailabilityProvider viewSecretInfoAvailability() {
		return () -> loggedIn
			? Availability.available()
			: Availability.unavailable("您需要先登录才能查看敏感信息");
    }
	public enum Gender {
		MALE, FEMALE
	}
	@Bean
	public CompletionProvider helloCompletionProvider() {
		EnumCompletionProvider genderCompletionProvider = new EnumCompletionProvider(Gender.class, "--gender");
		CompletionProvider nameCompletionProvider = completionContext -> List.of(new CompletionProposal("--name=Bob"), new CompletionProposal("--name=Alice"));
		return new CompositeCompletionProvider(nameCompletionProvider, genderCompletionProvider);
	}

	@Bean
	public ExitStatusExceptionMapper myCustomExceptionMapper() {
		return exception -> new ExitStatus(42, "42! The answer to life, the universe and everything!");
	}


}
