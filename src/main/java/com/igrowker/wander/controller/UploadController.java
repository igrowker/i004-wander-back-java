package com.igrowker.wander.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.igrowker.wander.service.UploadAvatarService;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
public class UploadController {

    @Autowired
    private UploadAvatarService uploadAvatarService;

    @PostMapping(value = "/upload-avatar", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadAvatar(@RequestParam("bucketName") String bucketName, @RequestParam("filePath") String filePath, @RequestParam("image") MultipartFile imageFile) {
        try {
            if (imageFile.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El archivo está vacío.");
            }

            String contentType = imageFile.getContentType();

            if (!"image/png".equals(contentType) && !"image/jpeg".equals(contentType))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Solo se permiten archivos PNG o JPG.");

            System.out.println("El usuario pudo cargar la imagen: " + uploadAvatarService.setAvatar(bucketName, filePath, imageFile));

            return ResponseEntity.status(HttpStatus.CREATED).body("El archivo se ha subido correctamente.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al subir el archivo: " + e.getMessage());
        }
    }
}
