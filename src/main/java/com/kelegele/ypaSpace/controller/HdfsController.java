package com.kelegele.ypaSpace.controller;

import com.alibaba.druid.util.StringUtils;
import com.kelegele.ypaSpace.entity.JsonResult;
import com.kelegele.ypaSpace.service.HdfsService;
import com.kelegele.ypaSpace.utils.code.ResultCode;
import org.apache.hadoop.fs.BlockLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hadoop/hdfs")
public class HdfsController {

    private static Logger LOGGER = LoggerFactory.getLogger(HdfsController.class);

    /**
     * 创建文件夹
     * @param path
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "mkdir", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult mkdir(@RequestParam("path") String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            LOGGER.debug("请求参数为空");
            return JsonResult.failed("请求参数为空");
        }
        // 创建空文件夹
        boolean isOk = HdfsService.mkdir(path);
        if (isOk) {
            LOGGER.debug("文件夹创建成功");
            return JsonResult.success(ResultCode.SUCCESS,"文件夹创建成功");
        } else {
            LOGGER.debug("文件夹创建失败");
            return JsonResult.failed("文件夹创建失败");
        }
    }

    /**
     * 读取HDFS目录信息
     * @param path
     * @return
     * @throws Exception
     */
    @PostMapping("/readPathInfo")
    public JsonResult readPathInfo(@RequestParam("path") String path) throws Exception {
        List<Map<String, Object>> list = HdfsService.readPathInfo(path);
        return JsonResult.success(list,"读取HDFS目录信息成功");
    }

    /**
     * 获取HDFS文件在集群中的位置
     * @param path
     * @return
     * @throws Exception
     */
    @PostMapping("/getFileBlockLocations")
    public JsonResult getFileBlockLocations(@RequestParam("path") String path) throws Exception {
        BlockLocation[] blockLocations = HdfsService.getFileBlockLocations(path);
        return JsonResult.success(blockLocations,"获取HDFS文件在集群中的位置");
    }

    /**
     * 创建文件
     * @param path
     * @return
     * @throws Exception
     */
    @PostMapping("/createFile")
    public JsonResult createFile(@RequestParam("path") String path, @RequestParam("file") MultipartFile file)
            throws Exception {
        if (StringUtils.isEmpty(path) || null == file.getBytes()) {
            return JsonResult.failed("请求参数为空");
        }
        HdfsService.createFile(path, file);
        return JsonResult.success("创建文件成功");
    }

    /**
     * 读取HDFS文件内容
     * @param path
     * @return
     * @throws Exception
     */
    @PostMapping("/readFile")
    public JsonResult readFile(@RequestParam("path") String path) throws Exception {
        String targetPath = HdfsService.readFile(path);
        return JsonResult.success(targetPath,"读取HDFS文件内容");
    }

    /**
     * 读取HDFS文件转换成Byte类型
     * @param path
     * @return
     * @throws Exception
     */
//    @PostMapping("/openFileToBytes")
//    public JsonResult openFileToBytes(@RequestParam("path") String path) throws Exception {
//        byte[] files = HdfsService.openFileToBytes(path);
//        return JsonResult.success(files,"读取HDFS文件转换成Byte类型");
//    }

    /**
     * 读取HDFS文件装换成User对象
     * @param path
     * @return
     * @throws Exception
     */
//    @PostMapping("/openFileToUser")
//    public JsonResult openFileToUser(@RequestParam("path") String path) throws Exception {
//        User user = HdfsService.openFileToObject(path, User.class);
//        return new Result(Result.SUCCESS, "读取HDFS文件装换成User对象", user);
//    }

    /**
     * 读取文件列表
     * @param path
     * @return
     * @throws Exception
     */
    @PostMapping("/listFile")
    public JsonResult listFile(@RequestParam("path") String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return JsonResult.failed("请求参数为空");
        }
        List<Map<String, String>> returnList = HdfsService.listFile(path);
        return JsonResult.success(returnList,"读取文件列表成功");
    }

    /**
     * 重命名文件
     * @param oldName
     * @param newName
     * @return
     * @throws Exception
     */
    @PostMapping("/renameFile")
    public JsonResult renameFile(@RequestParam("oldName") String oldName, @RequestParam("newName") String newName)
            throws Exception {
        if (StringUtils.isEmpty(oldName) || StringUtils.isEmpty(newName)) {
            return JsonResult.failed("请求参数为空");
        }
        boolean isOk = HdfsService.renameFile(oldName, newName);
        if (isOk) {
            return JsonResult.success("文件重命名成功");
        } else {
            return JsonResult.failed("文件重命名失败");
        }
    }

    /**
     * 删除文件
     * @param path
     * @return
     * @throws Exception
     */
    @PostMapping("/deleteFile")
    public JsonResult deleteFile(@RequestParam("path") String path) throws Exception {
        boolean isOk = HdfsService.deleteFile(path);
        if (isOk) {
            return JsonResult.success("delete file success");
        } else {
            return JsonResult.failed("delete file fail");
        }
    }

    /**
     * 上传文件
     * @param path
     * @param uploadPath
     * @return
     * @throws Exception
     */
    @PostMapping("/uploadFile")
    public JsonResult uploadFile(@RequestParam("path") String path, @RequestParam("uploadPath") String uploadPath)
            throws Exception {
        HdfsService.uploadFile(path, uploadPath);
        return JsonResult.success("upload file success");
    }

    /**
     * 下载文件
     * @param path
     * @param downloadPath
     * @return
     * @throws Exception
     */
    @PostMapping("/downloadFile")
    public JsonResult downloadFile(@RequestParam("path") String path, @RequestParam("downloadPath") String downloadPath)
            throws Exception {
        HdfsService.downloadFile(path, downloadPath);
        return JsonResult.success("download file success");
    }

    /**
     * HDFS文件复制
     * @param sourcePath
     * @param targetPath
     * @return
     * @throws Exception
     */
    @PostMapping("/copyFile")
    public JsonResult copyFile(@RequestParam("sourcePath") String sourcePath, @RequestParam("targetPath") String targetPath)
            throws Exception {
        HdfsService.copyFile(sourcePath, targetPath);
        return JsonResult.success("copy file success");
    }

    /**
     * 查看文件是否已存在
     * @param path
     * @return
     * @throws Exception
     */
    @PostMapping("/existFile")
    public JsonResult existFile(@RequestParam("path") String path) throws Exception {
        boolean isExist = HdfsService.existFile(path);
        return JsonResult.success("file isExist: " + isExist);
    }
}
