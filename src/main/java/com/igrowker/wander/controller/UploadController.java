package com.igrowker.wander.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.igrowker.wander.service.UploadService;

@RestController
@RequestMapping("/api/users")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload-avatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam("imageUrl") String imageUrl, String userId) {
        try {
            uploadService.setAvatar(imageUrl, userId);
            return ResponseEntity.ok("La imagen ha sido cargada correctamente");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al cargar la imagen");
        }
    }
}
