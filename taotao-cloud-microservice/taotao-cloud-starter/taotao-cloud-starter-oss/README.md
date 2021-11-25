# 文件上传集成说明
## 介绍
此模块集成，七牛文件服务；如果有需要，可以扩展阿里云等等云服务

## 依赖引用

```java 
<dependency>
	<artifactId>csx-bsf-file</artifactId>
	<groupId>com.yh.csx.bsf</groupId>
	<version>1.7.1-SNAPSHOT</version>
</dependency>
```

## 配置说明

```shell
## bsf file  集成
##file服务的启用开关，非必须，默认true
bsf.file.enabled=true 			
##file服务默认为七牛云，可以扩展其他的文件服务
bsf.file.provider=qiniu 	
##七牛云的accessKey	
bsf.file.qiniu.accessKey = 
##七牛云的securityKey	
bsf.file.qiniu.securityKey = 	
##七牛云的bucketName
bsf.file.qiniu.bucketName = 	
##七牛云的bucketUrl
bsf.file.qiniu.bucketUrl = 	 
bsf.file.qiniu.tempDir=
```
关于七牛云的使用，请参阅[七牛云](https://developer.qiniu.com/)

## 使用示例

```
	@Autowired(required = false)
	private FileProvider fileProvider;

	/**
	*  文件上传
	*/
    @PostMapping("/upload")
    public CommonResponse<String> uploadFile(MultipartFile file) throws Exception {    	
    	return CommonResponse.success(fileProvider.upload(file.getInputStream(), file.getOriginalFilename()));
    }
	/**
	* 上传服务器本地文件
	*/    
    public CommonResponse<String> uploadLocalFile(String file) throws Exception {    	
    	return CommonResponse.success(fileProvider.upload(file, FileUtils.getFileName(file)));
    }
```

