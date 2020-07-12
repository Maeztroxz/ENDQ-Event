package com.example.demo.api.controllers;

import com.example.demo.api.dtos.ImageResponse;
import com.example.demo.api.exceptions.NotFoundException;
import com.example.demo.api.models.ImageFile;
import com.example.demo.api.repositories.ImageRepository;
import com.example.demo.api.services.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImageService imageService;



    @PostMapping("/images")
    public ImageResponse uploadFile(@Valid @RequestParam MultipartFile image) {
        ImageFile imageFile = imageService.safeImage(image);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/")
                .path(imageFile.getFileName())
                .toUriString();

        return new ImageResponse(imageFile.getFileName(), fileDownloadUri,
                image.getContentType(), image.getSize());

    }


    @RequestMapping(value = "/images/{imageName}", method = RequestMethod.GET)
    public ResponseEntity<?> downloadFileUrl(@Valid @PathVariable String imageName) {
        // Load file from database
        ImageFile imageFile = imageService.getFile(imageName);
        if(imageFile == null)
            throw  new NotFoundException("Image not found with name " + imageName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imageFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageFile.getFileName() + "\"")
                .body(new ByteArrayResource(imageFile.getData()));
    }



}