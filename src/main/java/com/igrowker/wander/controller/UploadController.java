package com.igrowker.wander.controller;

import com.igrowker.wander.service.AwsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UploadController {

    @Autowired
    private AwsService awsService;

    @PostMapping(value = "/upload-avatar", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadAvatar(@RequestParam("image") MultipartFile imageFile) {
        try {
            String contentType = imageFile.getContentType();
            if (!"image/png".equals(contentType) && !"image/jpeg".equals(contentType))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Solo se permiten archivos PNG o JPG.");
            return ResponseEntity.status(HttpStatus.CREATED).body("El archivo se ha subido correctamente: " + awsService.uploadImageToAmazon(imageFile).toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al subir el archivo: " + e.getMessage());
        }
    }

    @PostMapping("/experiences")
    public ResponseEntity<String> uploadExperiencesPhotos(@RequestParam("images") List<MultipartFile> images){
        return ResponseEntity.status(HttpStatus.CREATED).body("Las imagenes se han subido correctamente: " + awsService.insertImages(images));
    }
}
