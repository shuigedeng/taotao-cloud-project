package com.taotao.cloud.data.elasticsearch.esearchx;

import java.util.function.Consumer;

/**
 * es全球
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-16 16:09:24
 */
public class EsGlobal {

	private static Consumer<EsCommandHolder> onCommandBefore;
	private static Consumer<EsCommandHolder> onCommandAfter;

	public static void onCommandBefore(Consumer<EsCommandHolder> event) {
		onCommandBefore = event;
	}

	public static void onCommandAfter(Consumer<EsCommandHolder> event) {
		onCommandAfter = event;
	}


	public static void applyCommandBefore(EsCommandHolder cmd) {
		if (onCommandBefore != null) {
			onCommandBefore.accept(cmd);
		}
	}

	public static void applyCommandAfter(EsCommandHolder cmd) {
		if (onCommandAfter != null) {
			onCommandAfter.accept(cmd);
		}
	}
}
