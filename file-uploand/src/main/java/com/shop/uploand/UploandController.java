package com.shop.uploand;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.Contended;

import java.io.*;

@Controller
public class UploandController {

    @RequestMapping("image")
    @ResponseBody
    public String upload(@RequestParam(value = "file",required = false)MultipartFile file){

        File file1 = new File(file.getOriginalFilename());
        File file2 = new File("E:/img/" + file1.getName());
        try {
            InputStream inputStream = file.getInputStream();
            FileOutputStream out=new FileOutputStream(file2);
            FileCopyUtils.copy(inputStream,out);
            return "http://image.leyou.com/images/"+file2.getName();
        }catch (IOException e){
            e.printStackTrace();
            return  null;
        }

    }
}
