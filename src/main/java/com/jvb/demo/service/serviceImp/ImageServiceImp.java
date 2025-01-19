package com.jvb.demo.service.serviceImp;

import java.io.File;

import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.jvb.demo.service.ImageService;

@Service
public class ImageServiceImp implements ImageService {
    private static final String imageFolder = "/images/product/";
    private static final String saveImagePath = System.getProperty("user.dir") + "/src/main/resources/static"
            + imageFolder;

    @Override
    public String saveImage(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            File temp = new File(saveImagePath + fileName);
            temp.createNewFile();
            FileCopyUtils.copy(file.getBytes(), temp);
            return imageFolder + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
