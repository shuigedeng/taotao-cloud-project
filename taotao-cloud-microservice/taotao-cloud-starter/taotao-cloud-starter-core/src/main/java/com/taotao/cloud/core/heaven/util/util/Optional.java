package com.taotao.cloud.core.heaven.util.util;


import com.taotao.cloud.core.heaven.util.lang.ObjectUtil;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Optional 工具类
 *
 * A container object which may or may not contain a non-null value.
 * If a value is present, {@code isPresent()} will return {@code true} and
 * {@code get()} will return the value.
 *
 * 此处简化 jdk8 Optional，提供一个简化版的 Optional提供给 jdk7 使用。便于过渡使用。
 * @since 0.0.9
 */
public final class Optional<T> {
    /**
     * Common instance for {@code empty()}.
     */
    private static final Optional<?> EMPTY = new Optional<>();

    /**
     * If non-null, the value; if null, indicates no value is present
     */
    private final T value;

    /**
     * Constructs an empty instance.
     *
     * Generally only one empty instance, {@link Optional#EMPTY},
     * should exist per VM.
     */
    private Optional() {
        this.value = null;
    }

    /**
     * Returns an empty {@code Optional} instance.  No value is present for this
     * Optional.
     *
     * Though it may be tempting to do so, avoid testing if an object
     * is empty by comparing with {@code ==} against instances returned by
     * {@code Option.empty()}. There is no guarantee that it is a singleton.
     * Instead, use {@link #isPresent()}.
     *
     * @param <T> Type of the non-existent value
     * @return an empty {@code Optional}
     */
    public static<T> Optional<T> empty() {
        @SuppressWarnings("unchecked")
        Optional<T> t = (Optional<T>) EMPTY;
        return t;
    }

    /**
     * Constructs an instance with the value present.
     *
     * @param value the non-null value to be present
     * @throws NullPointerException if value is null
     */
    private Optional(T value) {
        this.value = Objects.requireNonNull(value);
    }

    /**
     * Returns an {@code Optional} with the specified present non-null value.
     *
     * @param <T> the class of the value
     * @param value the value to be present, which must be non-null
     * @return an {@code Optional} with the value present
     * @throws NullPointerException if value is null
     */
    public static <T> Optional<T> of(T value) {
        return new Optional<>(value);
    }

    /**
     * Returns an {@code Optional} describing the specified value, if non-null,
     * otherwise returns an empty {@code Optional}.
     *
     * @param <T> the class of the value
     * @param value the possibly-null value to describe
     * @return an {@code Optional} with a present value if the specified value
     * is non-null, otherwise an empty {@code Optional}
     */
    public static <T> Optional<T> ofNullable(T value) {
        if(value == null) {
            return empty();
        }
        return of(value);
    }

    /**
     * If a value is present in this {@code Optional}, returns the value,
     * otherwise throws {@code NoSuchElementException}.
     *
     * @return the non-null value held by this {@code Optional}
     * @throws NoSuchElementException if there is no value present
     *
     * @see Optional#isPresent()
     */
    public T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    /**
     * 获取转换后的类型。
     * 如果为空，则直接返回 null
     * @param rClass 类型
     * @param <R> 泛型
     * @return 结果
     * @since 0.1.41
     */
    @SuppressWarnings("unchecked")
    public <R> R getCastOrNull(final Class<R> rClass) {
        if(ObjectUtil.isNotNull(value)) {
            return (R)value;
        }
        return null;
    }

    /**
     * Return {@code true} if there is a value present, otherwise {@code false}.
     *
     * @return {@code true} if there is a value present, otherwise {@code false}
     */
    public boolean isPresent() {
        return value != null;
    }

    /**
     * Return {@code true} if there is a value not present, otherwise {@code false}.
     *
     * @return {@code true} if there is a value not present, otherwise {@code false}
     * @since 0.1.6
     */
    public boolean isNotPresent() {
        return value == null;
    }

    /**
     * Return the contained value, if present, otherwise throw an exception
     * to be created by the provided supplier.
     *
     * A method reference to the exception constructor with an empty
     * argument list can be used as the supplier. For example,
     * {@code IllegalStateException::new}
     *
     * @param throwable Type of the exception to be thrown be thrown
     * @param <X> 泛型
     * @return the present value
     * @throws X if there is no value present
     * @throws NullPointerException if no value is present and
     * {@code exceptionSupplier} is null
     */
    public <X extends Throwable> T orElseThrow(X throwable) throws X {
        if (value != null) {
            return value;
        } else {
            throw throwable;
        }
    }

    /**
     * Return the contained value, if present, otherwise return null.
     * @return the present value or null
     * @since 0.1.5
     */
    public T orElseNull() {
        if (value != null) {
            return value;
        } else {
            return null;
        }
    }

    /**
     * Return the contained value, if present, otherwise return default value.
     * @param defaultVal default value
     * @return the present value or default value
     * @since 0.1.15
     */
    public T orDefault(final T defaultVal) {
        if (value != null) {
            return value;
        } else {
            return defaultVal;
        }
    }

    /**
     * Indicates whether some other object is "equal to" this Optional. The
     * other object is considered equal if:
     * <ul>
     * <li>it is also an {@code Optional} and;
     * <li>both instances have no value present or;
     * <li>the present values are "equal to" each other via {@code equals()}.
     * </ul>
     *
     * @param obj an object to be tested for equality
     * @return {code true} if the other object is "equal to" this object
     * otherwise {@code false}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Optional)) {
            return false;
        }

        Optional<?> other = (Optional<?>) obj;
        return Objects.equals(value, other.value);
    }

    /**
     * Returns the hash code value of the present value, if any, or 0 (zero) if
     * no value is present.
     *
     * @return hash code value of the present value or 0 if no value is present
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    /**
     * Returns a non-empty string representation of this Optional suitable for
     * debugging. The exact presentation format is unspecified and may vary
     * between implementations and versions.
     *
     * If a value is present the result must include its string
     * representation in the result. Empty and present Optionals must be
     * unambiguously differentiable.
     *
     * @return the string representation of this instance
     */
    @Override
    public String toString() {
        return value != null
                ? String.format("Optional[%s]", value)
                : "Optional.empty";
    }

}

