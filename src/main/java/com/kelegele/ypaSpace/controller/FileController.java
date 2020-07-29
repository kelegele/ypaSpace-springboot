package com.kelegele.ypaSpace.controller;

import com.kelegele.ypaSpace.entity.JsonResult;
import com.kelegele.ypaSpace.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
