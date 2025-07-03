package com.taotao.cloud.shell.commond;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

/**
 * OdcPromptProvider Description
 */
@Component
public class OdcPromptProvider implements PromptProvider {
	@Override
	public AttributedString getPrompt() {
		// 定制命令提示符为红色的“odc-shell:>”
		return new AttributedString("ttc-shell:>", AttributedStyle.DEFAULT.foreground(AttributedStyle.RED));
	}
}
