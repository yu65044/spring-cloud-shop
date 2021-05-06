package com.gupaoedu.vip.mall.file.controller;

import com.gupaoedu.mall.util.RespResult;
import com.gupaoedu.vip.mall.file.ceph.FileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*****
 * @Author: 波波
 * @Description: 云商城
 ****/
@RestController
@RequestMapping(value = "/file")
public class UploadController {

    @Autowired
    private FileHandler fileHandler;

    /****
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping(value = "/upload")
    public RespResult upload(MultipartFile file) throws IOException {
        //调用
        fileHandler.upload(file.getOriginalFilename(),file.getBytes());
        return RespResult.ok();
    }

    /****
     * 文件下载
     * @return
     */
    @GetMapping(value = "/download/{filename}")
    public void download(@PathVariable String filename, HttpServletResponse response) throws IOException {
        //调用
        byte[] buffer = fileHandler.download(filename);
        ServletOutputStream os = response.getOutputStream();
        os.write(buffer);
        os.flush();
        os.close();
    }
}
