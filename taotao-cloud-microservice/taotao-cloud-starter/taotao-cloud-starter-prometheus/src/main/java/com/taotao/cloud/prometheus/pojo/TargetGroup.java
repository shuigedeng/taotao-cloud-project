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


import java.util.List;
import java.util.Map;

/**
 * prometheus http sd 模型
 *
 * @author L.cm
 */
public class TargetGroup {

	private final List<String> targets;
	private final Map<String, String> labels;

	public TargetGroup(List<String> targets, Map<String, String> labels) {
		this.targets = targets;
		this.labels = labels;
	}


	public List<String> getTargets() {
		return targets;
	}

	public Map<String, String> getLabels() {
		return labels;
	}
}
