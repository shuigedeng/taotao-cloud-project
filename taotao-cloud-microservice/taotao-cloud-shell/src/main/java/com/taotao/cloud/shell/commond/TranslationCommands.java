package com.taotao.cloud.shell.commond;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Locale;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class TranslationCommands {

//	private final TranslationService service;
//
//	@Autowired
//	public TranslationCommands(TranslationService service) {
//		this.service = service;
//	}

	@ShellMethod("Translate text from one language to another.")
	public String translate(
		@ShellOption(optOut = true) String text,
		@ShellOption(optOut = true, defaultValue = "en_US") Locale from
//		@ShellOption(optOut = true) Locate to
	) {

		// invoke service
//		return service.translate(text, from, to);
		return "";
	}
}
