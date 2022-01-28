package com.taotao.cloud.web.schedule;

/**
 * @author jitwxs
 * @date 2021年03月27日 21:54
 */
public class SchedulerTaskInfo implements IDSTaskInfo {

	private long id;

	private String cron;

	private boolean isValid;

	private String reference;

	public SchedulerTaskInfo() {
	}

	public SchedulerTaskInfo(long id, String cron, boolean isValid, String reference) {
		this.id = id;
		this.cron = cron;
		this.isValid = isValid;
		this.reference = reference;
	}

	@Override
	public boolean isChange(IDSTaskInfo oldTaskInfo) {
		if (oldTaskInfo instanceof SchedulerTaskInfo) {
			final SchedulerTaskInfo obj = (SchedulerTaskInfo) oldTaskInfo;
			return !this.cron.equals(obj.cron) || this.isValid != obj.isValid
				|| !this.reference.equals(obj.getReference());
		} else {
			throw new IllegalArgumentException("Not Support SchedulerTestTaskInfo classType");
		}
	}

	@Override
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	@Override
	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean valid) {
		isValid = valid;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public static SchedulerTestTaskInfoBuilder builder() {
		return new SchedulerTestTaskInfoBuilder();
	}

	public static final class SchedulerTestTaskInfoBuilder {

		private long id;
		private String cron;
		private boolean isValid;
		private String reference;

		private SchedulerTestTaskInfoBuilder() {
		}

		public SchedulerTestTaskInfoBuilder id(long id) {
			this.id = id;
			return this;
		}

		public SchedulerTestTaskInfoBuilder cron(String cron) {
			this.cron = cron;
			return this;
		}

		public SchedulerTestTaskInfoBuilder isValid(boolean isValid) {
			this.isValid = isValid;
			return this;
		}

		public SchedulerTestTaskInfoBuilder reference(String reference) {
			this.reference = reference;
			return this;
		}

		public SchedulerTaskInfo build() {
			return new SchedulerTaskInfo(id, cron, isValid, reference);
		}
	}
}
