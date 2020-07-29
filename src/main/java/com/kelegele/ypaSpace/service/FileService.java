package com.kelegele.ypaSpace.service;

import com.kelegele.ypaSpace.entity.JsonResult;

public interface FileService {

    JsonResult listFiles(String path,String token);
}
