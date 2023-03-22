package com.taotao.cloud.log.api.api.common;

import java.io.Serializable;

public class Range<T> implements Serializable {

	private T lower;
	private T upper;

	public T getLower() {
		return this.lower;
	}

	public T getUpper() {
		return this.upper;
	}

	public boolean hasLowerBound() {
		return this.lower != null;
	}

	public boolean hasUpperBound() {
		return this.upper != null;
	}

	private Range() {
	}

	public static <T> Builder<T> newBuilder() {
		return new Builder();
	}

	public static class Builder<T> {

		private Range<T> range;

		private Builder() {
			this.range = new Range();
		}

		public Builder<T> lower(T lower) {
			this.range.lower = lower;
			return this;
		}

		public Builder<T> upper(T upper) {
			this.range.upper = upper;
			return this;
		}

		public Range<T> build() {
			return this.range;
		}
	}
}
