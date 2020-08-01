package com.kelegele.ypaSpace.controller;

import com.kelegele.ypaSpace.entity.JsonResult;
import com.kelegele.ypaSpace.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/file")
@Component
public class FileController {

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult ListFile(@RequestParam("path") String path,@RequestParam("token") String token){
        return fileService.listFiles(path,token);
    }

    @RequestMapping(value = "/mkdir", method = RequestMethod.GET)
    public JsonResult MkdirByPath(@RequestParam("path") String path,@RequestParam("token") String token){
        return fileService.mkdirByPath(path,token);
    }

    @RequestMapping(value = "/exist", method = RequestMethod.GET)
    public JsonResult ExistFile(@RequestParam("path") String path,@RequestParam("token") String token){
        return fileService.existFile(path,token);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public JsonResult DeleteFile(@RequestParam("path") String path, @RequestParam("token") String token){
        return fileService.deleteFile(path,token);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public JsonResult UploadFile(@RequestParam("file") MultipartFile file ,@RequestParam("path") String path, @RequestParam("token") String token){
        return fileService.createFile(file,path,token);
    }

    @RequestMapping(value = "/download")
    public  JsonResult DownloadFile(@RequestParam("path") String path ,@RequestParam("token") String token){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();
        return fileService.getFile(path,token,response);
    }
}
