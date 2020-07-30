package com.kelegele.ypaSpace.service;

import com.kelegele.ypaSpace.entity.JsonResult;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    JsonResult listFiles(String path,String token);

    JsonResult mkdirByPath(String path,String token);

    JsonResult existFile(String path,String token);

    JsonResult deleteFile(String path,String token);

    JsonResult getFile(String path,String token);

    JsonResult createFile(MultipartFile file,String path,String token);
}
