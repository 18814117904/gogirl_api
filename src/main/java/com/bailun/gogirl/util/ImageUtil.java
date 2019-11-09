package com.bailun.gogirl.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.bailun.gogirl.bean.ImageManage;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

public class ImageUtil {
	public static List<ImageManage> saveImage(String picturePath,MultipartFile [] files) throws IOException {
	    List<ImageManage> list = new ArrayList<ImageManage>();
	    for(MultipartFile file: files){
	   //     System.setProperty("sun.jnu.encoding","utf-8");
	        String fileName;
			fileName = new String(file.getOriginalFilename().replaceAll(",", ""));
//			fileName = new String(file.getOriginalFilename().getBytes("utf8"), System.getProperty("sun.jnu.encoding"));
	        File filePicture = new File(picturePath + fileName);
	        if (!filePicture.getParentFile().exists()) {
	            filePicture.getParentFile().mkdirs();
	        }
	        if(!filePicture.exists()){
	            filePicture.createNewFile();
	        }
	        file.transferTo(filePicture);
	        
//	        list.add(new ImageManage().setName(file.getOriginalFilename().replaceAll(",", "")).setUrl(file.getOriginalFilename().replaceAll(",", "")));
	        String qiniuName = qiniuUpload(filePicture);
	        list.add(new ImageManage().setName(qiniuName).setUrl(qiniuName));
	    }
	    return list;
	}
	public static String imageManageListToString(List<ImageManage> list) {
		StringBuffer sb = new StringBuffer("");
		for(int i = 0;i<list.size();i++){
			sb.append(list.get(i).getUrl());
			sb.append(",");
		}
		if(sb.length()>0){
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	public  static String qiniuUpload(File localFilePath) {
	      //构造一个带指定Zone对象的配置类
      Configuration cfg = new Configuration(Zone.zone2());
      //...其他参数参考类注释
      UploadManager uploadManager = new UploadManager(cfg);
      //...生成上传凭证，然后准备上传
//    String accessKey = "MHRIqINwea8MnhFHXhARWofsKzF6WTqPzOp92VHy";
//    String secretKey = "TjZPPnshhkysa40VOjyzdCUJBn4E-QUD0kBoEQwk";
    String accessKey = "RWQXlbVA7oe3BxnPuFtqkAJocQZkWTwrwYyldklr";
    String secretKey = "tS2gxsQO26mGoFZJI-x8WSH9X5aPgYMJcyoJdak5";
    String bucket = "begogirls";
      //如果是Windows情况下，格式是 D:\\qiniu\\test.png
//      String localFilePath = "E://test.jpg";
      //默认不指定key的情况下，以文件内容的hash值作为文件名
      String key = null;
      Auth auth = Auth.create(accessKey, secretKey);
      String upToken = auth.uploadToken(bucket);
      try {
          Response response = uploadManager.put(localFilePath, key, upToken);
          //解析上传成功的结果
          DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
          System.out.println(putRet.key);
          System.out.println(putRet.hash);
          return putRet.key;
      } catch (QiniuException ex) {
          Response r = ex.response;
          System.err.println(r.toString());
          try {
              System.err.println(r.bodyString());
          } catch (QiniuException ex2) {
              //ignore
          }
          return null;
      }
				
	}
}
