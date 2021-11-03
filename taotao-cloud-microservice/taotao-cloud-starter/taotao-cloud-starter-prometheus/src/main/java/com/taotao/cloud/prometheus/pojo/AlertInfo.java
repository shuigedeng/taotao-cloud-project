/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.prometheus.pojo;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * 告警模型
 *
 * @author L.cm
 */
public class AlertInfo implements Serializable {

	/**
	 * 状态 resolved|firing
	 */
	private String status;
	/**
	 * 标签集合
	 */
	private Map<String, String> labels;
	/**
	 * 注释集合
	 */
	private Map<String, String> annotations;
	/**
	 * 开始时间
	 */
	private OffsetDateTime startsAt;
	/**
	 * 结束时间
	 */
	private OffsetDateTime endsAt;
	/**
	 * identifies the entity that caused the alert
	 */
	private String generatorURL;
	/**
	 * fingerprint to identify the alert
	 */
	private String fingerprint;
	public AlertInfo(){};
	public AlertInfo(String status, Map<String, String> labels,
		Map<String, String> annotations, OffsetDateTime startsAt, OffsetDateTime endsAt,
		String generatorURL, String fingerprint) {
		this.status = status;
		this.labels = labels;
		this.annotations = annotations;
		this.startsAt = startsAt;
		this.endsAt = endsAt;
		this.generatorURL = generatorURL;
		this.fingerprint = fingerprint;
	}

	@Override
	public String toString() {
		return "AlertInfo{" +
			"status='" + status + '\'' +
			", labels=" + labels +
			", annotations=" + annotations +
			", startsAt=" + startsAt +
			", endsAt=" + endsAt +
			", generatorURL='" + generatorURL + '\'' +
			", fingerprint='" + fingerprint + '\'' +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		AlertInfo alertInfo = (AlertInfo) o;
		return Objects.equals(status, alertInfo.status) && Objects.equals(labels,
			alertInfo.labels) && Objects.equals(annotations, alertInfo.annotations)
			&& Objects.equals(startsAt, alertInfo.startsAt) && Objects.equals(
			endsAt, alertInfo.endsAt) && Objects.equals(generatorURL,
			alertInfo.generatorURL) && Objects.equals(fingerprint, alertInfo.fingerprint);
	}

	@Override
	public int hashCode() {
		return Objects.hash(status, labels, annotations, startsAt, endsAt, generatorURL,
			fingerprint);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Map<String, String> getLabels() {
		return labels;
	}

	public void setLabels(Map<String, String> labels) {
		this.labels = labels;
	}

	public Map<String, String> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(Map<String, String> annotations) {
		this.annotations = annotations;
	}

	public OffsetDateTime getStartsAt() {
		return startsAt;
	}

	public void setStartsAt(OffsetDateTime startsAt) {
		this.startsAt = startsAt;
	}

	public OffsetDateTime getEndsAt() {
		return endsAt;
	}

	public void setEndsAt(OffsetDateTime endsAt) {
		this.endsAt = endsAt;
	}

	public String getGeneratorURL() {
		return generatorURL;
	}

	public void setGeneratorURL(String generatorURL) {
		this.generatorURL = generatorURL;
	}

	public String getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}
}
