package mclass.store.tripant.diary.controller;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.cloudinary.utils.StringUtils;
import com.google.gson.Gson;





@Controller
public class ImageUploadController {
	@Value("${cloudinary.cloud_name}")
	private String cloudName;
	@Value("${cloudinary.api_key}")
	private String apiKey;
	@Value("${cloudinary.api_secret}")
	private String apiSecret;

	@PostMapping("/post/cloudinary")
	@ResponseBody
	public String postCloudinary(MultipartHttpServletRequest multiFile) throws IOException {
		
		System.out.println("들어옴");
		// 내 클라우드 정보로 cloudinary 객체 생성 
		Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
				"cloud_name", cloudName,
				"api_key", apiKey,
				"api_secret", apiSecret,
				"secure", true)
		);
		
		String result =null;
		String savedfolder = "/resources/uploadfile";
//		String uploadPath = request.getServletContext().getRealPath("/resources/uploadfile");
		String uploadPath = cloudinary.uploader().getUploadUrl(ObjectUtils.emptyMap());
		System.out.println("uploadPath : " + uploadPath);
		
		File uploadPathFile = new File(uploadPath);
		if(!uploadPathFile.exists()) {
			uploadPathFile.mkdirs();
		}

		int uploadFileLimit = 50 * 1024 * 1024; // 50MB
		MultipartFile file = multiFile.getFile("upload");
		if(file != null) {
			if(file.getSize() > 0 && StringUtils.isNotBlank(file.getName())) {
				if(file.getContentType().toLowerCase().startsWith("image/")) { //이미지 파일만 검색
					try {
						String originalFileName = file.getOriginalFilename();
						File f = Files.createTempFile("temp",file.getOriginalFilename()).toFile();
						file.transferTo(f);
						
						Map<String, String> config = new HashMap<String, String>();
						config.put("cloud_name", cloudName);
						config.put("api_key", apiKey);
						config.put("api_secret", apiSecret);
//						"http://res.cloudinary.com/dnhmep72p/image/upload/v1719391588/a4rpapbglkqd1g7lxipk.png
						Map uploadResult = cloudinary.uploader().upload(f, ObjectUtils.emptyMap());
						
						System.out.println("==============================================");
						System.out.println(uploadResult.get("url"));
						
						HashMap<String, String> map = new HashMap<String, String>();
						
						map.put("uploaded", "1");
						map.put("fileName", originalFileName);
						map.put("url", uploadResult.get("url").toString());
						return new Gson().toJson(map);
					} catch(Exception e) {
						e.printStackTrace();
					} 
				}
			}
		}
		return "failur";
	}
}
