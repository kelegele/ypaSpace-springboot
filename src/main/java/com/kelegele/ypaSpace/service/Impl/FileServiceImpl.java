package com.kelegele.ypaSpace.service.Impl;

import com.kelegele.ypaSpace.entity.HdfsFile;
import com.kelegele.ypaSpace.entity.JsonResult;
import com.kelegele.ypaSpace.mapper.UserMapper;
import com.kelegele.ypaSpace.service.FileService;
import com.kelegele.ypaSpace.service.HdfsService;
import com.kelegele.ypaSpace.utils.UserUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService {

    @Value("${hdfs.userpath}")
    private String path;

    @Autowired(required = false)
    private UserMapper userMapper;

    private static String userPath;

    @PostConstruct
    public void getPath() {
        userPath = this.path;
    }

    @Override
    public JsonResult listFiles(String path,String token) {

        HashMap<String,Object> data = new HashMap<>();

        String userId = UserUtil.getUserIdByToken(token);
        // TODO TEST
        //String userId = "0b617f05-8152-4d7f-b886-4cafa9de2780";
        ArrayList<HashMap<String, String>> currentPaths = new ArrayList<>();
        ArrayList<HdfsFile> currentDir = new ArrayList<>();

        List<Map<String, Object>> dirInfo = null;
        try {
            dirInfo = HdfsService.readPathInfo(userPath + userId + path );

            String [] paths = StringUtils.splitPreserveAllTokens(String.valueOf(path)  ,"/") ;

            for (int i = 0;i< paths.length;i++) {
                if (0 != paths[i].length() ){
                    HashMap<String, String> map = new HashMap<>();
                    map.put("index",String.valueOf(i));
                    map.put("path",paths[i]);
                    currentPaths.add(map);
                }
            }

            if (null == dirInfo){
                data.put("currentPaths",currentPaths);
                data.put("currentDir",currentDir);
                return JsonResult.success(data);
            }

            for (Map<String, Object> file: dirInfo) {

                Path filePath = (Path) file.get("filePath");

                String [] pathArr = StringUtils.splitPreserveAllTokens(String.valueOf(filePath)  ,"/") ;

                HdfsFile hdfsFile = new HdfsFile();
                hdfsFile.setFileName(pathArr[pathArr.length - 1]);
                hdfsFile.setFileType((String) file.get("fileType"));
                hdfsFile.setUserId(userId);

                String pp = "/";
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(pp);
                for (int i = 5;i< pathArr.length;i++){
                    stringBuilder.append(pathArr[i]);
                    if (i != (pathArr.length - 1)){
                        stringBuilder.append("/");
                    }
                }
                hdfsFile.setFilePath(stringBuilder.toString());

                currentDir.add(hdfsFile);

            }

        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failed(e.getMessage());
        }

        data.put("currentPaths",currentPaths);
        data.put("currentDir",currentDir);
        return JsonResult.success(data);
    }

    @Override
    public JsonResult mkdirByPath(String path, String token) {

        String userId = UserUtil.getUserIdByToken(token);

        try {
            boolean isOk = HdfsService.mkdir(userPath + userId + path);

            if (isOk){
                return JsonResult.success("创建成功！"+ path);
            }else {
                return JsonResult.failed("无效路径或文件已存在！"+ path);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failed(e.getMessage());
        }
    }

    @Override
    public JsonResult existFile(String path, String token) {

        String userId = UserUtil.getUserIdByToken(token);

        try {
            boolean exis = HdfsService.existFile(userPath + userId + path);

            if (exis){
                return JsonResult.success(exis,"已存在路径");
            }else {
                return JsonResult.success(exis,"不存在路径");

            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failed(e.getMessage());
        }
    }

    @Override
    public JsonResult deleteFile(String path, String token) {

        String userId = UserUtil.getUserIdByToken(token);

        try {
            boolean isOk = HdfsService.deleteFile(userPath + userId + path);

            if (isOk){
                return JsonResult.success(isOk,"删除成功");
            }else {
                return JsonResult.success(isOk,"删除失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failed(e.getMessage());
        }
    }

    @Override
    public JsonResult createFile(MultipartFile file, String path, String token) {

        String userId = UserUtil.getUserIdByToken(token);

        try {
            HdfsService.createFile(userPath + userId + path,file);

            return JsonResult.success(path,"上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failed(e.getMessage());
        }

    }

    @Override
    public JsonResult getFile(String path, String token) {

        String userId = UserUtil.getUserIdByToken(token);

        String [] paths = path.split("/");
        String fileName = paths[paths.length - 1];

        File file = new File(fileName);

        try {
            byte [] fileBytes = HdfsService.openFileToBytes(userPath + userId + path);

            OutputStream output = new FileOutputStream(file);
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
            bufferedOutput.write(fileBytes);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }
}
