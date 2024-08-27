package org.esfe.controllers;

import org.esfe.services.implementations.FirebaseStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = firebaseStorageService.uploadFile(file);
            return "redirect:/?success=" + fileName;
        } catch (Exception e) {
            return "redirect:/?error";
        }
    }
}
