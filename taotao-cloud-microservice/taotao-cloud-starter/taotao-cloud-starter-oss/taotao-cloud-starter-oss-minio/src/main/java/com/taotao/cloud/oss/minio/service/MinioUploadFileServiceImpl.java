/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.oss.minio.service;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.servlet.RequestUtils;
import com.taotao.cloud.oss.common.model.UploadFileInfo;
import com.taotao.cloud.oss.common.service.AbstractUploadFileService;
import com.taotao.cloud.oss.common.util.FileUtil;
import com.taotao.cloud.oss.minio.properties.MinioProperties;
import io.minio.BucketExistsArgs;
import io.minio.CopyObjectArgs;
import io.minio.CopySource;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PostPolicy;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.SetBucketPolicyArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * MinioUploadFileServiceImpl
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-09-23 11:04:00
 */
public class MinioUploadFileServiceImpl extends AbstractUploadFileService {

	private final MinioProperties properties;
	private final MinioClient minioClient;

	/**
	 * minio上传文件服务实现类
	 *
	 * @param properties  属性
	 * @param minioClient minio客户
	 * @since 2022-09-23 11:04:00
	 */
	public MinioUploadFileServiceImpl(MinioProperties properties, MinioClient minioClient) {
		this.properties = properties;
		this.minioClient = minioClient;
	}

	@Override
	protected UploadFileInfo uploadFile(MultipartFile file, UploadFileInfo uploadFileInfo) {
		return null;
	}

	@Override
	protected UploadFileInfo uploadFile(File file, UploadFileInfo uploadFileInfo) {
		return null;
	}

	@Override
	public UploadFileInfo delete(UploadFileInfo uploadFileInfo) {
		return null;
	}

	/**
	 * 获取上传临时签名
	 *
	 * @param fileName 文件名称
	 * @param time     时间
	 * @return {@link Map }
	 * @since 2022-09-23 11:04:01
	 */
	public Map getPolicy(String fileName, ZonedDateTime time) {
		PostPolicy postPolicy = new PostPolicy(properties.getBucketName(), time);
		postPolicy.addEqualsCondition("key", fileName);
		try {
			Map<String, String> map = minioClient.getPresignedPostFormData(postPolicy);
			HashMap<String, String> map1 = new HashMap<>();
			map.forEach((k, v) -> {
				map1.put(k.replaceAll("-", ""), v);
			});
			map1.put("host", properties.getUrl() + "/" + properties.getBucketName());
			return map1;
		} catch (ErrorResponseException | InsufficientDataException | InternalException |
				 InvalidResponseException | InvalidKeyException | IOException |
				 NoSuchAlgorithmException | ServerException | XmlParserException e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 获取上传文件的url
	 *
	 * @param objectName 对象名称
	 * @param method     方法
	 * @param time       时间
	 * @param timeUnit   时间单位
	 * @return {@link String }
	 * @since 2022-09-23 11:04:01
	 */
	public String getPolicyUrl(String objectName, Method method, int time, TimeUnit timeUnit) {
		try {
			return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
				.method(method)
				.bucket(properties.getBucketName())
				.object(objectName)
				.expiry(time, timeUnit).build());
		} catch (ErrorResponseException | InternalException | InsufficientDataException |
				 InvalidKeyException | InvalidResponseException | IOException |
				 NoSuchAlgorithmException | XmlParserException | ServerException e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 上传文件
	 *
	 * @param file     文件
	 * @param fileName 文件名称
	 * @since 2022-09-23 11:04:01
	 */
	public void upload1(MultipartFile file, String fileName) {
		// 使用putObject上传一个文件到存储桶中。
		try {
			InputStream inputStream = file.getInputStream();
			minioClient.putObject(PutObjectArgs.builder()
				.bucket(properties.getBucketName())
				.object(fileName)
				.stream(inputStream, file.getSize(), -1)
				.contentType(file.getContentType())
				.build());
		} catch (ErrorResponseException | InsufficientDataException | InternalException |
				 InvalidKeyException | InvalidResponseException | IOException |
				 NoSuchAlgorithmException | ServerException | XmlParserException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据filename获取文件访问地址
	 *
	 * @param objectName 对象名称
	 * @param time       时间
	 * @param timeUnit   时间单位
	 * @return {@link String }
	 * @since 2022-09-23 11:04:01
	 */
	public String getUrl(String objectName, int time, TimeUnit timeUnit) {
		String url = null;
		try {
			url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
				.method(Method.GET)
				.bucket(properties.getBucketName())
				.object(objectName)
				.expiry(time, timeUnit).build());
		} catch (ErrorResponseException | InsufficientDataException | InternalException |
				 InvalidKeyException | InvalidResponseException | IOException |
				 NoSuchAlgorithmException | XmlParserException | ServerException e) {
			e.printStackTrace();
		}
		return url;
	}


	/**
	 * 上传文件
	 *
	 * @param file       文件
	 * @param bucketName bucket名称
	 * @param fileName   文件名称
	 * @since 2022-09-23 11:04:01
	 */
	public void uploadFile(MultipartFile file, String bucketName, String fileName) {
		//判断文件是否为空
		if (null == file || 0 == file.getSize()) {
			LogUtils.error("文件不能为空");
		}
		//判断存储桶是否存在
		bucketExists(bucketName);
		//文件名
		if (file != null) {
			String originalFilename = file.getOriginalFilename();
			//新的文件名 = 存储桶文件名_时间戳.后缀名
			assert originalFilename != null;
			//开始上传
			try {
				InputStream inputStream = file.getInputStream();
				minioClient.putObject(
					PutObjectArgs.builder().bucket(bucketName).object(fileName).stream(
							inputStream, file.getSize(), -1)
						.contentType(file.getContentType())
						.build());
			} catch (Exception e) {
				LogUtils.error(e.getMessage());
			}
		}
	}

	/**
	 * 上传文件（可以传空） 数据备份使用
	 *
	 * @param filePath   文件路径
	 * @param bucketName bucket名称
	 * @param fileName   文件名称
	 * @since 2022-09-23 11:04:31
	 */
	public void uploadFiles(String filePath, String bucketName, String fileName)
		throws IOException {
		MultipartFile file = FileUtil.fileToMultipartFile(new File(filePath));
		//开始上传
		try {
			InputStream inputStream = file.getInputStream();
			minioClient.putObject(
				PutObjectArgs.builder().bucket(bucketName).object(fileName).stream(
						inputStream, file.getSize(), -1)
					.contentType(file.getContentType())
					.build());
		} catch (Exception e) {
			LogUtils.error(e.getMessage());
		}
	}

	/**
	 * 下载文件
	 *
	 * @param fileName   文件名
	 * @param bucketName 桶名（文件夹）
	 */
	public void downFile(String fileName, String bucketName) {
		if (fileName.contains("jpg") || fileName.contains("png")) {
			dowloadMinioFile(fileName, bucketName);
			return;
		}

		InputStream inputStream = null;
		try {
			inputStream = minioClient.getObject(
				GetObjectArgs.builder()
					.bucket(bucketName)
					.object(fileName)
					.build());
			//下载文件
			HttpServletResponse response = RequestUtils.getResponse();
			HttpServletRequest request = RequestUtils.getRequest();
			try {
				BufferedInputStream bis = new BufferedInputStream(inputStream);
				assert response != null;
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/plain");
				if (fileName.contains(".svg")) {
					response.setContentType("image/svg+xml");
				}
				//编码的文件名字,关于中文乱码的改造
				String codeFileName = "";
				assert request != null;
				String agent = request.getHeader("USER-AGENT").toLowerCase();
				if (agent.contains("msie") || agent.contains("trident")) {
					//IE
					codeFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
				} else if (agent.contains("mozilla")) {
					//火狐，谷歌
					codeFileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
				} else {
					codeFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
				}
				response.setHeader("Content-Disposition",
					"attachment;filename=" + new String(codeFileName.getBytes(), StandardCharsets.UTF_8));
				OutputStream os = response.getOutputStream();
				int i;
				byte[] buff = new byte[1024 * 8];
				while ((i = bis.read(buff)) != -1) {
					os.write(buff, 0, i);
				}
				os.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			LogUtils.error(e.getMessage());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 返回图片
	 *
	 * @param fileName   文件名
	 * @param bucketName 桶名（文件夹）
	 * @since 2022-09-23 11:04:46
	 */
	public void dowloadMinioFile(String fileName, String bucketName) {
		try {
			InputStream inputStream = minioClient.getObject(
				GetObjectArgs.builder()
					.bucket(bucketName)
					.object(fileName)
					.build());
			ServletOutputStream outputStream1 = Objects.requireNonNull(RequestUtils.getResponse())
				.getOutputStream();

			//读取指定路径下面的文件
			OutputStream outputStream = new BufferedOutputStream(outputStream1);
			//创建存放文件内容的数组
			byte[] buff = new byte[1024];
			//所读取的内容使用n来接收
			int n;
			//当没有读取完时,继续读取,循环
			while ((n = inputStream.read(buff)) != -1) {
				//将字节数组的数据全部写入到输出流中
				outputStream.write(buff, 0, n);
			}
			//强制将缓存区的数据进行输出
			outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 获取資源
	 *
	 * @param fileName   文件名称
	 * @param bucketName bucket名称
	 * @return {@link String }
	 * @since 2022-09-23 11:04:55
	 */
	public String getFile(String fileName, String bucketName) {
		String objectUrl = null;
		try {
			objectUrl = minioClient.getPresignedObjectUrl(
				GetPresignedObjectUrlArgs.builder()
					.method(Method.GET)
					.bucket(bucketName)
					.object(fileName)
					.build());
		} catch (Exception e) {
			LogUtils.error(e.getMessage());
		}
		return objectUrl;
	}

	/**
	 * 下载文件
	 *
	 * @param fileName   文件名称
	 * @param bucketName 存储桶名称
	 * @return {@link InputStream }
	 * @since 2022-09-23 11:04:58
	 */
	public InputStream downloadMinio(String fileName, String bucketName) {
		try {
			return minioClient.getObject(
				GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.info(e.getMessage());
			return null;
		}
	}

	/**
	 * 获取全部bucket
	 *
	 * @return {@link List }<{@link String }>
	 * @since 2022-09-23 11:05:01
	 */
	public List<String> getAllBuckets() throws Exception {
		return minioClient.listBuckets().stream().map(Bucket::name).collect(Collectors.toList());
	}

	/**
	 * 根据bucketName删除信息
	 *
	 * @param bucketName bucket名称
	 * @since 2022-09-23 11:05:03
	 */
	public void removeBucket(String bucketName) throws Exception {
		minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
	}

	/**
	 * 删除一个对象
	 *
	 * @param bucketName bucket名称
	 * @param name       名称
	 * @return boolean
	 * @since 2022-09-23 11:05:05
	 */
	public boolean removeFile(String bucketName, String name) {
		boolean isOK = true;
		try {
			minioClient.removeObject(
				RemoveObjectArgs.builder().bucket(bucketName).object(name).build());
		} catch (Exception e) {
			e.printStackTrace();
			isOK = false;
		}
		return isOK;
	}

	/**
	 * 检查存储桶是否已经存在(不存在不创建)
	 *
	 * @param name 名称
	 * @return boolean
	 * @since 2022-09-23 11:05:07
	 */
	public boolean bucketExists(String name) {
		boolean isExist = false;
		try {
			isExist = minioClient.bucketExists(getBucketExistsArgs(name));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isExist;
	}

	/**
	 * 检查存储桶是否已经存在(不存在则创建)
	 *
	 * @param name 名称
	 * @since 2022-09-23 11:05:11
	 */
	public void bucketExistsCreate(String name) {
		try {
			minioClient.bucketExists(getBucketExistsArgs(name));
			minioClient.makeBucket(getMakeBucketArgs(name));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 代码生成器下载代码
	 *
	 * @param filePath   文件路径
	 * @param bucketName 存储桶
	 * @param objectName 文件夹名称
	 * @return boolean
	 * @since 2022-09-23 11:05:19
	 */
	public boolean putFolder(String filePath, String bucketName, String objectName) {
		boolean flag = false;
		try {
			//判断文件夹是否存在
			if (!FileUtil.fileIsExists(filePath)) {
				return false;
			}
			//压缩文件后上传到minio
			FileUtil.toZip(filePath + ".zip", true, filePath);
			MultipartFile multipartFile = FileUtil.fileToMultipartFile(
				new File(filePath + ".zip"));
			//上传到minio
			uploadFile(multipartFile, bucketName, objectName + ".zip");
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 通过流下载文件
	 *
	 * @param bucketName bucket名称
	 * @param filePath   文件路径
	 * @param objectName 对象名称
	 * @since 2022-09-23 11:05:29
	 */
	public void streamToDown(String bucketName, String filePath, String objectName) {
		try {
			InputStream stream =
				minioClient.getObject(
					GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
			FileUtil.writeFile(stream, filePath, objectName);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.info(e.getMessage());
		}
	}

	/**
	 * 获取存储桶下所有文件
	 *
	 * @param bucketName 存储桶名
	 * @return {@link List }<{@link Item }>
	 * @since 2022-09-23 11:05:32
	 */
	public List<Item> getFileList(String bucketName) {
		List<Item> list = new ArrayList<>();
		try {
			Iterable<Result<Item>> results = minioClient.listObjects(
				ListObjectsArgs.builder().bucket(bucketName).build());
			for (Result<Item> result : results) {
				Item item = result.get();
				list.add(item);
			}
		} catch (Exception e) {
			LogUtils.error(e.getMessage());
		}
		return list;
	}

	/**
	 * 获取存储桶下所有文件
	 *
	 * @param bucketName bucket名称
	 * @param type       类型
	 * @return {@link List }
	 * @since 2022-09-23 11:05:38
	 */
	public List getFileList(String bucketName, String type) {
		List<Item> list = new ArrayList<>();
		try {
			Iterable<Result<Item>> results = minioClient.listObjects(
				ListObjectsArgs.builder().bucket(bucketName).prefix(type).recursive(true).build());
			for (Result<Item> result : results) {
				Item item = result.get();
				list.add(item);
			}
		} catch (Exception e) {
			LogUtils.error(e.getMessage());
		}
		return list;
	}

	/**
	 * 拷贝文件
	 *
	 * @param bucketName       bucket名称
	 * @param objectName       文件名称
	 * @param copyToBucketName 目标bucket名称
	 * @param copyToObjectName 目标文件名称
	 * @since 2022-09-23 11:05:41
	 */
	public void copyObject(String bucketName, String objectName, String copyToBucketName,
						   String copyToObjectName) {
		try {
			minioClient.copyObject(
				CopyObjectArgs.builder()
					.source(CopySource.builder().bucket(bucketName).object(objectName).build())
					.bucket(copyToBucketName)
					.object(copyToObjectName)
					.build());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * String转MakeBucketArgs
	 *
	 * @param name 名称
	 * @return {@link MakeBucketArgs }
	 * @since 2022-09-23 11:05:44
	 */
	public static MakeBucketArgs getMakeBucketArgs(String name) {
		return MakeBucketArgs.builder().bucket(name).build();
	}

	/**
	 * String转BucketExistsArgs
	 *
	 * @param name 名称
	 * @return {@link BucketExistsArgs }
	 * @since 2022-09-23 11:05:45
	 */
	public static BucketExistsArgs getBucketExistsArgs(String name) {
		return BucketExistsArgs.builder().bucket(name).build();
	}

	/**
	 * String转SetBucketPolicyArgs
	 *
	 * @param name 名称
	 * @return {@link SetBucketPolicyArgs }
	 * @since 2022-09-23 11:05:48
	 */
	public static SetBucketPolicyArgs getSetBucketPolicyArgs(String name) {
		return SetBucketPolicyArgs.builder().bucket(name).build();
	}

	/**
	 * 通过流下载文件
	 *
	 * @param bucketName bucket名称
	 * @param filePath   文件路径
	 * @param objectName 对象名称
	 * @since 2022-09-23 11:05:53
	 */
	public void downToLocal(String bucketName, String filePath, String objectName) {
		try {
			InputStream stream =
				minioClient.getObject(
					GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
			FileUtil.write(stream, filePath, objectName);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.info(e.getMessage());
		}
	}

}

