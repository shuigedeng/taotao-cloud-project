package com.taotao.cloud.ccsr.common.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * GsonUtils.
 */
public class GsonUtils {

    /**
     * logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(GsonUtils.class);

    private static final GsonUtils INSTANCE = new GsonUtils();

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter())
            .registerTypeHierarchyAdapter(Pair.class, new PairTypeAdapter())
            .create();

    private static final Gson GSON_MAP = new GsonBuilder().serializeNulls().registerTypeHierarchyAdapter(new TypeToken<Map<String, Object>>() {
    }.getRawType(), new MapDeserializer<String, Object>()).create();

    private static final String DOT = ".";

    private static final String E = "e";

    private static final String LEFT = "left";

    private static final String RIGHT = "right";

    private static final String LEFT_ANGLE_BRACKETS = "{";

    private static final String RIGHT_ANGLE_BRACKETS = "}";

    private static final String EMPTY = "";

    private static final String EQUAL_SIGN = "=";

    private static final String AND = "&";

    /**
     * Get gson instance.
     *
     * @return the instance
     */
    public static Gson getGson() {
        return GsonUtils.GSON;
    }

    /**
     * Get instance.
     *
     * @return the instance
     */
    public static GsonUtils getInstance() {
        return INSTANCE;
    }

    /**
     * To json string.
     *
     * @param object the object
     * @return the string
     */
    public String toJson(final Object object) {
        return GSON.toJson(object);
    }

    /**
     * From json t.
     *
     * @param <T>    the type parameter
     * @param json   the json
     * @param tClass the t class
     * @return the t
     */
    public <T> T fromJson(final String json, final Class<T> tClass) {
        return GSON.fromJson(json, tClass);
    }

    /**
     * From json t.
     *
     * @param <T>         the type parameter
     * @param jsonElement the json element
     * @param tClass      the t class
     * @return the t
     */
    public <T> T fromJson(final JsonElement jsonElement, final Class<T> tClass) {
        return GSON.fromJson(jsonElement, tClass);
    }

    /**
     * From list.
     *
     * @param <T>   the type parameter
     * @param json  the json
     * @param clazz the clazz
     * @return the list
     */
    public <T> List<T> fromList(final String json, final Class<T> clazz) {
        return GSON.fromJson(json, TypeToken.getParameterized(List.class, clazz).getType());
    }

    /**
     * From current list.
     *
     * @param <T>   the type parameter
     * @param json  the json
     * @param clazz the clazz
     * @return the list
     */
    public <T> List<T> fromCurrentList(final String json, final Class<T> clazz) {
        return GSON.fromJson(json, TypeToken.getParameterized(CopyOnWriteArrayList.class, clazz).getType());
    }


    /**
     * to Map.
     *
     * @param json json
     * @return hashMap map
     */
    private Map<String, String> toStringMap(final String json) {
        return GSON.fromJson(json, new TypeToken<Map<String, String>>() {
        }.getType());
    }

    /**
     * to List Map.
     *
     * @param json json
     * @return hashMap list
     */
    public List<Map<String, Object>> toListMap(final String json) {
        return GSON.fromJson(json, new TypeToken<List<Map<String, Object>>>() {
        }.getType());
    }

    /**
     * To object map.
     *
     * @param json the json
     * @return the map
     */
    public Map<String, Object> toObjectMap(final String json) {
        return GSON_MAP.fromJson(json, new TypeToken<LinkedHashMap<String, Object>>() {
        }.getType());
    }

    /**
     * To object map.
     *
     * @param <T>   the class
     * @param json  the json
     * @param clazz the class
     * @return the map
     */
    public <T> Map<String, T> toObjectMap(final String json, final Class<T> clazz) {
        return GSON.fromJson(json, TypeToken.getParameterized(Map.class, String.class, clazz).getType());
    }

    /**
     * To object map list.
     *
     * @param <T>   the class
     * @param json  the json
     * @param clazz the class
     * @return the map
     */
    public <T> Map<String, List<T>> toObjectMapList(final String json, final Class<T> clazz) {
        return GSON.fromJson(json, TypeToken.getParameterized(Map.class, String.class, TypeToken.getParameterized(List.class, clazz).getType()).getType());
    }

    /**
     * To tree map.
     *
     * @param json the json
     * @return the tree map
     */
    public ConcurrentNavigableMap<String, Object> toTreeMap(final String json) {
        return GSON_MAP.fromJson(json, new TypeToken<ConcurrentSkipListMap<String, Object>>() {
        }.getType());
    }

    /**
     * Convert to map.
     *
     * @param json the json
     * @return the map
     */
    public Map<String, Object> convertToMap(final String json) {
        Map<String, Object> map = GSON_MAP.fromJson(json, new TypeToken<Map<String, Object>>() {
        }.getType());

        if (MapUtils.isEmpty(map)) {
            return map;
        }

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                String valueStr = ((String) value).trim();
                if (valueStr.startsWith(LEFT_ANGLE_BRACKETS) && valueStr.endsWith(RIGHT_ANGLE_BRACKETS)) {
                    Map<String, Object> mv = convertToMap(value.toString());
                    map.put(key, mv);
                }
            } else if (value instanceof JsonObject) {
                map.put(key, convertToMap(value.toString()));
            } else if (value instanceof JsonArray) {
                JsonArray jsonArray = (JsonArray) value;
                map.put(key, jsonArrayToListInConvertToMap(jsonArray));
            } else if (value instanceof JsonNull) {
                map.put(key, null);
            }
        }

        return map;
    }

    /**
     * translate JsonArray in covertToMap of Method.
     *
     * @param jsonArray the Gson's Object {@link JsonArray}
     * @return list about translating jsonArray
     */
    private List<Object> jsonArrayToListInConvertToMap(final JsonArray jsonArray) {
        List<Object> list = new ArrayList<>(jsonArray.size());
        for (JsonElement jsonElement : jsonArray) {
            if (jsonElement.isJsonNull()) {
                list.add(null);
                continue;
            }
            String objStr;
            if (jsonElement instanceof JsonObject) {
                JsonObject asJsonObject = jsonElement.getAsJsonObject();
                objStr = toJson(asJsonObject);
            } else {
                objStr = jsonElement.getAsString();
            }
            if (objStr.startsWith(LEFT_ANGLE_BRACKETS) && objStr.endsWith(RIGHT_ANGLE_BRACKETS)) {
                list.add(convertToMap(jsonElement.toString()));
            } else {
                list.add(objStr);
            }
        }

        return list;
    }

    private static class MapDeserializer<T, U> implements JsonDeserializer<Map<T, U>> {
        @SuppressWarnings("unchecked")
        @Override
        public Map<T, U> deserialize(final JsonElement json, final Type type, final JsonDeserializationContext context) {
            if (!json.isJsonObject()) {
                return null;
            }
            String className = ((ParameterizedType) type).getRawType().getTypeName();
            Class<Map<?, ?>> mapClass = null;
            try {
                mapClass = (Class<Map<?, ?>>) Class.forName(className);
            } catch (ClassNotFoundException e) {
                LOG.error("failed to get class", e);
            }

            Map<T, U> resultMap = null;
            assert mapClass != null;
            if (Objects.requireNonNull(mapClass).isInterface()) {
                resultMap = new LinkedHashMap<>();
            } else {
                try {
                    resultMap = (Map<T, U>) mapClass.getConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    LOG.error("failed to get constructor", e);
                }
            }
            JsonObject jsonObject = json.getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> jsonEntrySet = jsonObject.entrySet();
            for (Map.Entry<String, JsonElement> entry : jsonEntrySet) {
                if (entry.getValue().isJsonNull()) {
                    if (Objects.nonNull(resultMap)) {
                        resultMap.put((T) entry.getKey(), null);
                    }
                } else {
                    U value = context.deserialize(entry.getValue(), this.getType(entry.getValue()));
                    if (Objects.nonNull(resultMap)) {
                        resultMap.put((T) entry.getKey(), value);
                    }
                }
            }
            return resultMap;
        }

        /**
         * Get JsonElement class type.
         *
         * @param element the element
         * @return Class the class
         */
        public Class<?> getType(final JsonElement element) {
            if (!element.isJsonPrimitive()) {
                return element.getClass();
            }

            final JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isString()) {
                return String.class;
            }
            if (primitive.isNumber()) {
                String numStr = primitive.getAsString();
                if (numStr.contains(DOT) || numStr.contains(E)
                        || numStr.contains(E.toUpperCase())) {
                    return Double.class;
                }
                return Long.class;
            }
            if (primitive.isBoolean()) {
                return Boolean.class;
            }
            return element.getClass();
        }
    }

    private static class PairTypeAdapter extends TypeAdapter<Pair<String, String>> {

        @Override
        public void write(final JsonWriter out, final Pair<String, String> value) throws IOException {
            out.beginObject();
            out.name(LEFT).value(value.getLeft());
            out.name(RIGHT).value(value.getRight());
            out.endObject();
        }

        @Override
        public Pair<String, String> read(final JsonReader in) throws IOException {
            in.beginObject();

            String left = null;
            String right = null;

            while (in.hasNext()) {
                switch (in.nextName()) {
                    case LEFT:
                        left = in.nextString();
                        break;
                    case RIGHT:
                        right = in.nextString();
                        break;
                    default:
                        break;
                }
            }

            in.endObject();

            return Pair.of(left, right);
        }
    }

    private static class TimestampTypeAdapter implements JsonSerializer<Timestamp>, JsonDeserializer<Timestamp> {

        private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        public Timestamp deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
            if (!(json instanceof JsonPrimitive)) {
                throw new JsonParseException("The date should be a string value");
            }
            try {
                Date date = format.parse(json.getAsString());
                return new Timestamp(date.getTime());
            } catch (ParseException e) {
                throw new JsonParseException(e);
            }
        }

        @Override
        public JsonElement serialize(final Timestamp src, final Type typeOfSrc, final JsonSerializationContext context) {
            String dfString = format.format(new Date(src.getTime()));
            return new JsonPrimitive(dfString);
        }
    }
}
