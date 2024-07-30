package com.chat.app.service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface ICloudinaryService {
	
	public Map upload(MultipartFile multipartFile) throws IOException;

	public Map uploadFile(File file) throws IOException;
	
	public Map delete(String cloudinaryImageId) throws IOException;
}
