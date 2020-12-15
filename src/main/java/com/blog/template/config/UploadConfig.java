package com.blog.template.config;

import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UploadConfig {
	@Value("${qiniu.accessKey}")
	private String accessKey;

	@Value("${qiniu.secretKey}")
	private String secretKey;



	@Bean
	public com.qiniu.storage.Configuration qiniuConfig() {

		return new com.qiniu.storage.Configuration(Zone.zone0());
	}


	@Bean
	public UploadManager uploadManager() {
		return new UploadManager(qiniuConfig());
	}


	@Bean
	public Auth auth() {
		return Auth.create(accessKey, secretKey);
	}

	
	@Bean
	public BucketManager bucketManager() {
		return new BucketManager(auth(), qiniuConfig());
	}
}
