//package com.taotao.cloud.idea.plugin.toolkit.utils;
//
//import tools.jackson.core.util.DefaultIndenter;
//import tools.jackson.core.util.DefaultPrettyPrinter;
//import tools.jackson.databind.JsonNode;
//import tools.jackson.databind.json.JsonMapper;
//import java.io.IOException;
//
///**
// * JsonUtil
// */
//public class JsonUtils {
//
//	private JsonUtils() {
//	}
//
//	public static String formatJson(String jsonStr) throws IOException {
//		Object jsonObject = Holder.MAPPER.readValue(jsonStr, Object.class);
//		return Holder.MAPPER.writer(Holder.DEFAULT_PRETTY_PRINTER).writeValueAsString(jsonObject);
//	}
//
//	public static String minifyJson(String jsonStr) throws IOException {
//		Object jsonObject = Holder.MAPPER.readValue(jsonStr, Object.class);
//		return Holder.MAPPER.writeValueAsString(jsonObject);
//	}
//
//	public static void verifyJson(String jsonStr) throws IOException {
//		Holder.MAPPER.readValue(jsonStr, Object.class);
//	}
//
//	public static JsonNode read(String jsonStr) throws IOException {
//		return Holder.MAPPER.readTree(jsonStr);
//	}
//
//	private static final class Holder {
//
//		public static final JsonMapper MAPPER = new JsonMapper();
//		public static final DefaultPrettyPrinter DEFAULT_PRETTY_PRINTER = new CustomPrettyPrinter();
//	}
//
//	private static final class CustomPrettyPrinter extends DefaultPrettyPrinter {
//
//		private static final DefaultIndenter UNIX_LINE_FEED_INSTANCE = new DefaultIndenter("  ",
//			"\n");
//
//		public CustomPrettyPrinter() {
//			super._objectFieldValueSeparatorWithSpaces = ": ";
//			super._objectIndenter = UNIX_LINE_FEED_INSTANCE;
//			super._arrayIndenter = UNIX_LINE_FEED_INSTANCE;
//		}
//
//		@Override
//		public DefaultPrettyPrinter createInstance() {
//			return new CustomPrettyPrinter();
//		}
//	}
//}
