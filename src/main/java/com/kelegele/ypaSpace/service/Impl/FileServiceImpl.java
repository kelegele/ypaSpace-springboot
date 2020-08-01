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

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
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
    public JsonResult listFiles(String path, String token) {

        HashMap<String, Object> data = new HashMap<>();

        String userId = UserUtil.getUserIdByToken(token);
        // TODO TEST
        //String userId = "0b617f05-8152-4d7f-b886-4cafa9de2780";
        ArrayList<HashMap<String, String>> currentPaths = new ArrayList<>();
        ArrayList<HdfsFile> currentDir = new ArrayList<>();

        List<Map<String, Object>> dirInfo = null;
        try {
            dirInfo = HdfsService.readPathInfo(userPath + userId + path);

            String[] paths = StringUtils.splitPreserveAllTokens(String.valueOf(path), "/");

            for (int i = 0; i < paths.length; i++) {
                if (0 != paths[i].length()) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("index", String.valueOf(i));
                    map.put("path", paths[i]);
                    currentPaths.add(map);
                }
            }

            if (null == dirInfo) {
                data.put("currentPaths", currentPaths);
                data.put("currentDir", currentDir);
                return JsonResult.success(data);
            }

            for (Map<String, Object> file : dirInfo) {

                Path filePath = (Path) file.get("filePath");

                String[] pathArr = StringUtils.splitPreserveAllTokens(String.valueOf(filePath), "/");

                HdfsFile hdfsFile = new HdfsFile();
                hdfsFile.setFileName(pathArr[pathArr.length - 1]);
                hdfsFile.setFileType((String) file.get("fileType"));
                hdfsFile.setUserId(userId);

                String pp = "/";
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(pp);
                for (int i = 5; i < pathArr.length; i++) {
                    stringBuilder.append(pathArr[i]);
                    if (i != (pathArr.length - 1)) {
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

        data.put("currentPaths", currentPaths);
        data.put("currentDir", currentDir);
        return JsonResult.success(data);
    }

    @Override
    public JsonResult mkdirByPath(String path, String token) {

        String userId = UserUtil.getUserIdByToken(token);

        try {
            boolean isOk = HdfsService.mkdir(userPath + userId + path);

            if (isOk) {
                return JsonResult.success("创建成功！" + path);
            } else {
                return JsonResult.failed("无效路径或文件已存在！" + path);
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

            if (exis) {
                return JsonResult.success(exis, "已存在路径");
            } else {
                return JsonResult.success(exis, "不存在路径");

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

            if (isOk) {
                return JsonResult.success(isOk, "删除成功");
            } else {
                return JsonResult.success(isOk, "删除失败");
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
            HdfsService.createFile(userPath + userId + path, file);

            return JsonResult.success(path, "上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failed(e.getMessage());
        }

    }

    @Override
    public JsonResult getFile(String path, String token, HttpServletResponse response) {

        byte[] fileBytes = null;

        String userId = UserUtil.getUserIdByToken(token);

        //储存到本地 弃用
//        FileOutputStream fos = null;
//        BufferedOutputStream bos = null;
//        File file = null;
//        String fileName = System.getProperty("user.dir") + "/temp/" + userId + path;
//        file = new File(fileName);
//
//        try {
//
//            if (!file.getParentFile().exists()) {
//                //上级目录不存在，创建上级目录
//                file.getParentFile().mkdirs();
//            }
//
//            if (file.exists()) {
//                //存在，删除
//                file.delete();
//            }
//            file.createNewFile();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            fileBytes = HdfsService.openFileToBytes(userPath + userId + path);
//
//            fos = new FileOutputStream(file);
//            bos = new BufferedOutputStream(fos);
//            bos.write(fileBytes);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally{
//            if (bos != null){
//                try{
//                    bos.close();
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
//            }
//            if (fos != null){
//                try{
//                    fos.close();
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
//            }
//        }

        String[] paths = path.split("/");
        String filename = paths[paths.length - 1];

        // 设置信息给客户端不解析
        String type = new MimetypesFileTypeMap().getContentType(filename);
        // 设置contenttype，即告诉客户端所发送的数据属于什么类型
        response.setHeader("file-type", type);
        // 设置编码
        String decode = null;
        decode = new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        response.setHeader("filename",decode);

        //首先设置响应的内容格式是force-download，那么你一旦点击下载按钮就会自动下载文件了
        response.setContentType("application/x-download");
        // 设置扩展头，当Content-Type 的类型为要下载的类型时 , 这个信息头会告诉浏览器这个文件的名字和类型。
        response.addHeader("Content-Disposition", "attachment");

        try {
            // 发送给客户端的数据
            fileBytes = HdfsService.openFileToBytes(userPath + userId + path);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(fileBytes, 0, fileBytes.length);
            outputStream.flush();
            return JsonResult.success("下载成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failed(e.getMessage());
        }
    }
}
