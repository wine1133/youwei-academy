package com.example.controller;
import cn.hutool.core.io.FileUtil;
import com.example.common.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
@RestController @RequestMapping("/files")
public class FileController {
    private static final String filePath = System.getProperty("user.dir") + "/files/";
    @PostMapping("/upload")
    public Result upload(MultipartFile file){
        String original = file.getOriginalFilename()==null?"file":file.getOriginalFilename();
        String fileName=System.currentTimeMillis()+"-"+original.replaceAll("[\\\\/:*?\"<>|]","_");
        try { if(!FileUtil.isDirectory(filePath)) FileUtil.mkdir(filePath); FileUtil.writeBytes(file.getBytes(),filePath+fileName); }
        catch(IOException e){ return Result.error("文件上传失败"); }
        String url=ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/download/").path(fileName).toUriString();
        return Result.success(url);
    }
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName,HttpServletResponse response){
        String realFilePath=filePath+fileName;
        try {
            String contentType=Files.probeContentType(Paths.get(realFilePath));
            response.setContentType(contentType==null?"application/octet-stream":contentType);
            response.setHeader("Content-Disposition","inline;filename="+URLEncoder.encode(fileName,StandardCharsets.UTF_8));
            byte[] bytes=FileUtil.readBytes(realFilePath);
            ServletOutputStream os=response.getOutputStream(); os.write(bytes); os.flush(); os.close();
        } catch(IOException e){ response.setStatus(404); }
    }
}
