/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.kubernetes;

import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.LabelSelector;
import io.fabric8.kubernetes.api.model.ListOptions;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceBuilder;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.PodSpec;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.SecurityContext;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import io.fabric8.kubernetes.client.AppsAPIGroupClient;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.NonNamespaceOperation;
import io.fabric8.kubernetes.client.dsl.PodResource;
import io.fabric8.kubernetes.client.dsl.RollableScalableResource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * KubernetesClientBuilder
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/03/08 14:35
 */
public class KubernetesClientBuilder {

	public static void createSource() {
		KubernetesClient client = createKubernetesClient();
		Namespace namespace = new NamespaceBuilder()
			.withNewMetadata()
			.withName("pkslow")
			.addToLabels("reason", "pkslow-sample")
			.endMetadata()
			.build();
		client.namespaces().createOrReplace(namespace);

		Pod pod = new PodBuilder()
			.withNewMetadata()
			.withName("nginx")
			.addToLabels("app", "nginx")
			.endMetadata()
			.withNewSpec()
			.addNewContainer()
			.withName("nginx")
			.withImage("nginx:1.19.5")
			.endContainer()
			.endSpec()
			.build();
		client.pods().inNamespace("pkslow").createOrReplace(pod);
	}

	public static void showSource() {
		KubernetesClient client = createKubernetesClient();

		// 查看命名空间
		NamespaceList namespaceList = client.namespaces().list();
		namespaceList.getItems()
			.forEach(namespace ->
				System.out.println(
					namespace.getMetadata().getName() + ":" + namespace.getStatus().getPhase()));
		// 查看Pod
		ListOptions options = new ListOptions();
		options.setLabelSelector("app=nginx");
		Pod nginx = client.pods().inNamespace("pkslow")
			.list(options)
			.getItems()
			.get(0);
		System.out.println(nginx);
	}

	public static void editSource() {
		KubernetesClient client = createKubernetesClient();
// 修改命名空间
		client.namespaces().withName("pkslow")
			.edit(n -> new NamespaceBuilder(n)
				.editMetadata()
				.addToLabels("project", "pkslow")
				.endMetadata()
				.build()
			);
// 修改Pod
		client.pods().inNamespace("pkslow").withName("nginx")
			.edit(p -> new PodBuilder(p)
				.editMetadata()
				.addToLabels("app-version", "1.0.1")
				.endMetadata()
				.build()
			);
	}

	public static void deleteSource() {
		KubernetesClient client = createKubernetesClient();

		client.pods().inAnyNamespace().watch(new Watcher<Pod>() {
			@Override
			public void eventReceived(Action action, Pod pod) {
				System.out.println("event " + action.name() + " " + pod.toString());
			}

			@Override
			public void onClose(WatcherException e) {
				System.out.println("Watcher close due to " + e);
			}
		});

		client.pods().inNamespace("pkslow")
			.withName("nginx")
			.delete();
	}

	public static void showPodInfo() {
		//查看Pod
		MixedOperation<Pod, PodList, PodResource<Pod>> operation = createKubernetesClient().pods();
		//创建Pod，获取资源处理类，在传入组装号的Pod类
		NonNamespaceOperation<Pod, PodList, PodResource<Pod>> pods = createKubernetesClient().pods().inNamespace("default");
		//配置Pod，还可以通过 pod 类组装，想要运行 这里的参数是不够的，仅作演示
		Pod pod1 = new PodBuilder().withNewMetadata().withName("pod1").withNamespace("default")
			.and().build();
		pods.create(pod1);
		//删除同上
		pods.delete(pod1);
	}

	public static void showDeployment() {
		//将基础Client转换为AppsAPIGroupClient，用于操作deployment
		AppsAPIGroupClient oclient = createKubernetesClient().adapt(AppsAPIGroupClient.class);
		MixedOperation<Deployment, DeploymentList, RollableScalableResource<Deployment>> operation1
			= oclient.deployments();
		//将资源转换为JSON 查看
		DeploymentList deploymentList = operation1.list();
		List<Deployment> deployments = deploymentList.getItems();
		//创建Deployment，返回创建好的Deployment文件
		oclient.deployments()
			.create(getDepandDeployment("appName", "image", "nodeName"));
		//删除同上，返回结果为boolean类型数据
		oclient.deployments()
			.delete(getDepandDeployment("appName", "image", "nodeName"));
	}

	public static Deployment getDepandDeployment(String appName, String image, String nodeName) {
		//参数传递
		String appGroup = "appGroup";
		//参数
		Map<String, String> labels = new HashMap<String, String>();
		labels.put("app", appGroup);
		Map<String, String> nodeSelector = new HashMap<String, String>();
		nodeSelector.put("name", nodeName);
		//mataData 数据组装
		ObjectMeta mataData = new ObjectMeta();
		mataData.setName(appName);
		mataData.setLabels(labels);
		//镜像设置
		Container container = new Container();
		container.setName(appName);
		container.setImage(image);
		container.setImagePullPolicy("IfNotPresent");
		SecurityContext sc = new SecurityContext();
		sc.setPrivileged(true);
		container.setSecurityContext(sc);
		List<Container> containers = new ArrayList<>();
		containers.add(container);
		//Spec 数据组装
		//1.selector
		LabelSelector ls = new LabelSelector();
		ls.setMatchLabels(labels);
		//2.template
		ObjectMeta empMataData = new ObjectMeta();
		empMataData.setLabels(labels);
		PodSpec pods = new PodSpec();
		pods.setHostNetwork(true);
		pods.setNodeSelector(nodeSelector);
		pods.setContainers(containers);
		//2.2 组装
		PodTemplateSpec pt = new PodTemplateSpec();
		pt.setMetadata(empMataData);
		pt.setSpec(pods);
		//3.spec 组合
		DeploymentSpec ds = new DeploymentSpec();
		ds.setReplicas(1);
		ds.setSelector(ls);
		ds.setTemplate(pt);
		//Deployment 设置
		Deployment deployment = new Deployment();
		deployment.setApiVersion("apps/v1");
		deployment.setKind("Deployment");
		deployment.setMetadata(mataData);
		deployment.setSpec(ds);
		return deployment;
	}

	public static KubernetesClient createKubernetesClient() {
		//1.通过安全证书访问
		Config config = new ConfigBuilder()
			.withMasterUrl("https://localhost:6443")
			.withCaCertData("ca.crt内容")
			.withClientCertData("apiserver-kubelet-client.crt内容")
			.withClientKeyData("apiserver-kubelet-client.key内容")
			.build();

		System.out.println(config);

		//2.通过配置文件访问
//		Config config = Config.fromKubeconfig("admin.conf内容");

		// 3.kube-proxy 配置后通过 HTTP 直接访问
		// 首先在服务器中开启kube-proxy代理暴露8080端口：nohup kubectl proxy --port=8080 &
//		Config config = new ConfigBuilder().withMasterUrl("http://127.0.0.1:8080").build();

		return new DefaultKubernetesClient(config);
	}


}
