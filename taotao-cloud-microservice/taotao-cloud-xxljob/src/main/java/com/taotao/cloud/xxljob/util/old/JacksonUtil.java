package com.taotao.cloud.xxljob.util.old;//package com.xxl.job.admin.util.old;
//
//import tools.jackson.core.JsonGenerationException;
//import tools.jackson.core.JsonParseException;
//import tools.jackson.databind.JavaType;
//import tools.jackson.databind.JsonMappingException;
//import tools.jackson.databind.json.JsonMapper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//
///**
// * Jackson util
// *
// * 1、obj need private and set/get；
// * 2、do not support inner class；
// *
// * @author xuxueli 2015-9-25 18:02:56
// */
//public class JacksonUtil {
//	private static Logger logger = LoggerFactory.getLogger(JacksonUtil.class);
//
//    private final static JsonMapper objectMapper = new JsonMapper();
//    public static JsonMapper getInstance() {
//        return objectMapper;
//    }
//
//    /**
//     * bean、array、List、Map --> json
//     *
//     * @param obj
//     * @return json string
//     * @throws Exception
//     */
//    public static String writeValueAsString(Object obj) {
//    	try {
//			return getInstance().writeValueAsString(obj);
//		} catch (JsonGenerationException e) {
//			logger.error(e.getMessage(), e);
//		} catch (JsonMappingException e) {
//			logger.error(e.getMessage(), e);
//		} catch (IOException e) {
//			logger.error(e.getMessage(), e);
//		}
//        return null;
//    }
//
//    /**
//     * string --> bean、Map、List(array)
//     *
//     * @param jsonStr
//     * @param clazz
//     * @return obj
//     * @throws Exception
//     */
//    public static <T> T readValue(String jsonStr, Class<T> clazz) {
//    	try {
//			return getInstance().readValue(jsonStr, clazz);
//		} catch (JsonParseException e) {
//			logger.error(e.getMessage(), e);
//		} catch (JsonMappingException e) {
//			logger.error(e.getMessage(), e);
//		} catch (IOException e) {
//			logger.error(e.getMessage(), e);
//		}
//    	return null;
//    }
//
//	/**
//	 * string --> List<Bean>...
//	 *
//	 * @param jsonStr
//	 * @param parametrized
//	 * @param parameterClasses
//	 * @param <T>
//	 * @return
//	 */
//	public static <T> T readValue(String jsonStr, Class<?> parametrized, Class<?>... parameterClasses) {
//		try {
//			JavaType javaType = getInstance().getTypeFactory().constructParametricType(parametrized, parameterClasses);
//			return getInstance().readValue(jsonStr, javaType);
//		} catch (JsonParseException e) {
//			logger.error(e.getMessage(), e);
//		} catch (JsonMappingException e) {
//			logger.error(e.getMessage(), e);
//		} catch (IOException e) {
//			logger.error(e.getMessage(), e);
//		}
//		return null;
//	}
//}
