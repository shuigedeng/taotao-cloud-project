package com.taotao.cloud.common.utils.reflect;

import com.taotao.cloud.common.exception.CommonRuntimeException;
import com.taotao.cloud.common.support.tuple.impl.Pair;
import com.taotao.cloud.common.utils.collection.ArrayUtil;
import com.taotao.cloud.common.utils.lang.ObjectUtil;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Type 工具类
 *
 * @see ClassGenericUtil 泛型工具类
 * @see ClassUtil 类
 * @see ClassTypeUtil 类型工具类
 */
public final class TypeUtil {

	private TypeUtil() {
	}

	/**
	 * 创建集合
	 *
	 * @param type 类型
	 * @return 集合
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static Collection createCollection(Type type) {
		return createCollection(type, 8);
	}

	/**
	 * 创建一个 map
	 *
	 * @param type 类型
	 * @return 结果
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static Map<Object, Object> createMap(final Type type) {
		if (type == Properties.class) {
			return new Properties();
		}

		if (type == Hashtable.class) {
			return new Hashtable();
		}

		if (type == IdentityHashMap.class) {
			return new IdentityHashMap();
		}

		if (type == SortedMap.class || type == TreeMap.class) {
			return new TreeMap();
		}

		if (type == ConcurrentMap.class || type == ConcurrentHashMap.class) {
			return new ConcurrentHashMap();
		}

		if (type == HashMap.class) {
			return new HashMap();
		}

		if (type == LinkedHashMap.class) {
			return new LinkedHashMap();
		}

		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;

			Type rawType = parameterizedType.getRawType();
			if (EnumMap.class.equals(rawType)) {
				Type[] actualArgs = parameterizedType.getActualTypeArguments();
				return new EnumMap((Class) actualArgs[0]);
			}

			return createMap(rawType);
		}

		Class<?> clazz = (Class<?>) type;
		if (clazz.isInterface()) {
			throw new CommonRuntimeException("unsupport type " + type);
		}

		if ("java.util.Collections$UnmodifiableMap".equals(clazz.getName())) {
			return new HashMap();
		}

		try {
			return (Map<Object, Object>) clazz.newInstance();
		} catch (Exception e) {
			throw new CommonRuntimeException(e);
		}
	}

	/**
	 * 根据 map 的类型，获取对应的 key/value 类型
	 *
	 * @param mapType map 类型
	 * @return 结果
	 */
	public static Pair<Type, Type> getMapKeyValueType(final Type mapType) {
		if (mapType instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) mapType;
			Type keyType = parameterizedType.getActualTypeArguments()[0];
			Type valueType = null;
			if (mapType.getClass().getName()
				.equals("org.springframework.util.LinkedMultiValueMap")) {
				valueType = List.class;
			} else {
				valueType = parameterizedType.getActualTypeArguments()[1];
			}
			return Pair.of(keyType, valueType);
		}
		final Type objectType = Object.class;
		return Pair.of(objectType, objectType);
	}

	/**
	 * 创建集合
	 *
	 * @param type 类型
	 * @param size 集合大小
	 * @return 集合
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static Collection createCollection(Type type,
		final int size) {
		Class<?> rawClass = getRawClass(type);
		Collection list;
		if (rawClass == AbstractCollection.class
			|| rawClass == Collection.class) {
			list = new ArrayList(size);
		} else if (rawClass.isAssignableFrom(HashSet.class)) {
			list = new HashSet(size);
		} else if (rawClass.isAssignableFrom(LinkedHashSet.class)) {
			list = new LinkedHashSet(size);
		} else if (rawClass.isAssignableFrom(TreeSet.class)) {
			list = new TreeSet();
		} else if (rawClass.isAssignableFrom(ArrayList.class)) {
			list = new ArrayList(size);
		} else if (rawClass.isAssignableFrom(EnumSet.class)) {
			Type itemType = getGenericType(type);
			list = EnumSet.noneOf((Class<Enum>) itemType);
		} else if (rawClass.isAssignableFrom(Queue.class)) {
			list = new LinkedList();
		} else {
			try {
				list = (Collection) rawClass.newInstance();
			} catch (Exception e) {
				throw new CommonRuntimeException(e);
			}
		}
		return list;
	}

	/**
	 * 获取对应的 raw 类型
	 *
	 * @param type 类型
	 * @return 结果
	 */
	private static Class<?> getRawClass(Type type) {
		if (type instanceof Class<?>) {
			return (Class<?>) type;
		} else if (type instanceof ParameterizedType) {
			return getRawClass(((ParameterizedType) type).getRawType());
		} else {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * 获取泛型类型
	 *
	 * @param type 类型
	 * @return 元素类型
	 */
	public static Class getGenericType(final Type type) {
		Type itemType;
		if (type instanceof ParameterizedType) {
			itemType = ((ParameterizedType) type).getActualTypeArguments()[0];
		} else {
			itemType = Object.class;
		}

		return (Class) itemType;
	}

	/**
	 * 获取集合元素类型
	 *
	 * @param collectionType 集合类型
	 * @return 类型
	 */
	public static Type getCollectionItemType(Type collectionType) {
		if (collectionType instanceof ParameterizedType) {
			return getCollectionItemType((ParameterizedType) collectionType);
		}
		if (collectionType instanceof Class<?>) {
			return getCollectionItemType((Class<?>) collectionType);
		}
		return Object.class;
	}

	/**
	 * 获取集合元素类型
	 *
	 * @param clazz 类型
	 * @return 结果
	 */
	private static Type getCollectionItemType(Class<?> clazz) {
		return clazz.getName().startsWith("java.")
			? Object.class
			: getCollectionItemType(getCollectionSuperType(clazz));
	}

	private static Type getCollectionItemType(ParameterizedType parameterizedType) {
		Type rawType = parameterizedType.getRawType();
		Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
		if (rawType == Collection.class) {
			return getWildcardTypeUpperBounds(actualTypeArguments[0]);
		}
		Class<?> rawClass = (Class<?>) rawType;
		Map<TypeVariable, Type> actualTypeMap = createActualTypeMap(rawClass.getTypeParameters(),
			actualTypeArguments);
		Type superType = getCollectionSuperType(rawClass);
		if (superType instanceof ParameterizedType) {
			Class<?> superClass = getRawClass(superType);
			Type[] superClassTypeParameters = ((ParameterizedType) superType).getActualTypeArguments();
			return superClassTypeParameters.length > 0
				? getCollectionItemType(
				makeParameterizedType(superClass, superClassTypeParameters, actualTypeMap))
				: getCollectionItemType(superClass);
		}
		return getCollectionItemType((Class<?>) superType);
	}

	private static Type getWildcardTypeUpperBounds(Type type) {
		if (type instanceof WildcardType) {
			WildcardType wildcardType = (WildcardType) type;
			Type[] upperBounds = wildcardType.getUpperBounds();
			return upperBounds.length > 0 ? upperBounds[0] : Object.class;
		}
		return type;
	}

	private static Map<TypeVariable, Type> createActualTypeMap(TypeVariable[] typeParameters,
		Type[] actualTypeArguments) {
		int length = typeParameters.length;
		Map<TypeVariable, Type> actualTypeMap = new HashMap<TypeVariable, Type>(length);
		for (int i = 0; i < length; i++) {
			actualTypeMap.put(typeParameters[i], actualTypeArguments[i]);
		}
		return actualTypeMap;
	}

	private static Type getCollectionSuperType(Class<?> clazz) {
		Type assignable = null;
		for (Type type : clazz.getGenericInterfaces()) {
			Class<?> rawClass = getRawClass(type);
			if (rawClass == Collection.class) {
				return type;
			}
			if (Collection.class.isAssignableFrom(rawClass)) {
				assignable = type;
			}
		}
		return assignable == null ? clazz.getGenericSuperclass() : assignable;
	}

	private static ParameterizedType makeParameterizedType(Class<?> rawClass, Type[] typeParameters,
		Map<TypeVariable, Type> actualTypeMap) {
		int length = typeParameters.length;
		Type[] actualTypeArguments = new Type[length];
		for (int i = 0; i < length; i++) {
			actualTypeArguments[i] = getActualType(typeParameters[i], actualTypeMap);
		}
		return new ParameterizedTypeImpl(actualTypeArguments, null, rawClass);
	}

	private static Type getActualType(Type typeParameter, Map<TypeVariable, Type> actualTypeMap) {
		if (typeParameter instanceof TypeVariable) {
			return actualTypeMap.get(typeParameter);
		} else if (typeParameter instanceof ParameterizedType) {
			return makeParameterizedType(getRawClass(typeParameter),
				((ParameterizedType) typeParameter).getActualTypeArguments(), actualTypeMap);
		} else if (typeParameter instanceof GenericArrayType) {
			return new GenericArrayTypeImpl(
				getActualType(((GenericArrayType) typeParameter).getGenericComponentType(),
					actualTypeMap));
		}
		return typeParameter;
	}


	/**
	 * 获取泛型参数类型
	 *
	 * @param genericType 类型
	 * @param paramIndex  泛型参数下标
	 * @return 结果
	 */
	public static Class getGenericParamType(final Type genericType, final int paramIndex) {
		if (ObjectUtil.isNull(genericType)) {
			return null;
		}

		// 如果是泛型参数的类型
		if (genericType instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) genericType;
			//得到泛型里的class类型对象
			Type[] types = parameterizedType.getActualTypeArguments();
			if (ArrayUtil.isEmpty(types)) {
				return null;
			}

			// 判断是否为通配符(?)
			Type type = types[paramIndex];
			if (ClassTypeUtil.isWildcardGenericType(type)) {
				//WildcardTypeImpl wildcardType = (WildcardTypeImpl)type;
				//
				////lower
				//Type[] lowerBounds = wildcardType.getLowerBounds();
				//if(ArrayUtil.isNotEmpty(lowerBounds)) {
				//    return (Class<?>)lowerBounds[0];
				//}
				//
				////upper
				//Type[] upperBounds = wildcardType.getUpperBounds();
				//if(ArrayUtil.isNotEmpty(upperBounds)) {
				//    return (Class<?>)upperBounds[0];
				//}

				// 默认返回 object 对象类型
				return Object.class;
			}

			return (Class<?>) type;
		}

		// 默认返回 object 对象类型
		return null;
	}

	/**
	 * 获取泛型参数类型
	 *
	 * @param genericType 类型
	 * @return 结果
	 */
	public static Class getGenericParamType(final Type genericType) {
		return getGenericParamType(genericType, 0);
	}

	/**
	 * 转换为指定的数据类型
	 *
	 * @param obj  原始数据
	 * @param type 预期类型
	 * @param <T>  结果泛型
	 * @return 结果
	 */
	@SuppressWarnings("unchecked")
	public static <T> T cast(Object obj, Type type) {
		//8 大基本类型的默认值处理
		if (obj == null) {
			if (ClassTypeUtil.isPrimitive((Class) type)) {
				return (T) PrimitiveUtil.getDefaultValue((Class<T>) type);
			}
			return null;
		}
		if (type instanceof ParameterizedType) {
			return (T) cast(obj, (ParameterizedType) type);
		}
		if (obj instanceof String) {
			String strVal = (String) obj;
			if (strVal.length() == 0
				|| "null".equals(strVal)
				|| "NULL".equals(strVal)) {
				return null;
			}

			return (T) strVal;
		}

		// 其他情况，直接类型强转。
		// 后期可以丰富这里的特性。
		return (T) obj;
	}

	/**
	 * 类型转换
	 *
	 * @param obj  原始结果
	 * @param type 类型
	 * @param <T>  泛型
	 * @return 转换后的结果
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	private static <T> T cast(Object obj, ParameterizedType type) {
		Type rawTye = type.getRawType();

		if (rawTye == List.class || rawTye == ArrayList.class) {
			Type itemType = type.getActualTypeArguments()[0];
			if (obj instanceof List) {
				List listObj = (List) obj;
				List arrayList = new ArrayList(listObj.size());

				for (Object item : listObj) {
					Object itemValue;
					if (itemType instanceof Class) {
						itemValue = cast(item, (Class<T>) itemType);
					} else {
						itemValue = cast(item, itemType);
					}

					arrayList.add(itemValue);
				}
				return (T) arrayList;
			}
		}

		if (rawTye == Set.class || rawTye == HashSet.class
			|| rawTye == TreeSet.class
			|| rawTye == Collection.class
			|| rawTye == List.class
			|| rawTye == ArrayList.class) {
			Type itemType = type.getActualTypeArguments()[0];
			if (obj instanceof Iterable) {
				Collection collection;
				if (rawTye == Set.class || rawTye == HashSet.class) {
					collection = new HashSet();
				} else if (rawTye == TreeSet.class) {
					collection = new TreeSet();
				} else {
					collection = new ArrayList();
				}
				for (Object item : (Iterable) obj) {
					Object itemValue;
					if (itemType instanceof Class) {
						itemValue = cast(item, (Class<T>) itemType);
					} else {
						itemValue = cast(item, itemType);
					}

					collection.add(itemValue);
				}
				return (T) collection;
			}
		}

		if (rawTye == Map.class || rawTye == HashMap.class) {
			Type keyType = type.getActualTypeArguments()[0];
			Type valueType = type.getActualTypeArguments()[1];
			if (obj instanceof Map) {
				Map map = new HashMap();
				for (Map.Entry entry : ((Map<?, ?>) obj).entrySet()) {
					Object key = cast(entry.getKey(), keyType);
					Object value = cast(entry.getValue(), valueType);
					map.put(key, value);
				}
				return (T) map;
			}
		}
		if (obj instanceof String) {
			String strVal = (String) obj;
			if (strVal.length() == 0) {
				return null;
			}
		}
		if (type.getActualTypeArguments().length == 1) {
			Type argType = type.getActualTypeArguments()[0];
			if (argType instanceof WildcardType) {
				return (T) cast(obj, rawTye);
			}
		}

		if (rawTye == Map.Entry.class && obj instanceof Map && ((Map) obj).size() == 1) {
			Map.Entry entry = (Map.Entry) ((Map) obj).entrySet().iterator().next();
			return (T) entry;
		}

		throw new CommonRuntimeException("无法转换为类型： " + type);
	}

}
