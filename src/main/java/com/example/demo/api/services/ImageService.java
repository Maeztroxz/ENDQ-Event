package com.example.demo.api.services;

import com.example.demo.api.exceptions.BusinessException;
import com.example.demo.api.models.ImageFile;
import com.example.demo.api.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public ImageFile safeImage(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) throw new BusinessException("Sorry! Filename contains invalid path sequence " + fileName);


            ImageFile imageFile = new ImageFile(fileName, file.getContentType(), file.getBytes());
            String type = imageFile.getFileType();
            if(!(fileName.contains(".png") || fileName.contains(".jpg") || fileName.contains(".jpeg"))) throw new BusinessException("Invalid image type");

            return imageRepository.save(imageFile);
        } catch (IOException ex) {
            throw new BusinessException("Could not store file " + fileName + ". Please try again!");
        }
    }

    public ImageFile getFile(String imageName) {
        return imageRepository.findImageByNameFile(imageName);
    }
}
