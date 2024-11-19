package com.igrowker.wander.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.igrowker.wander.service.UploadAvatarService;

@RestController
@RequestMapping("/users")
public class UploadController {

    @Autowired
    private UploadAvatarService uploadAvatarService;

    @PostMapping("/upload-avatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam("file") String imageUrl, String email) {
        if (uploadAvatarService.setAvatar(email, imageUrl))
            return ResponseEntity.status(HttpStatus.CREATED).body("La imagen ha sido cargada correctamente");
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al cargar la imagen");
        }
}
