package org.esfe.controllers;

import org.esfe.services.implementations.FirebaseStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/upload")
public class FileUploadController {

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    private static final String UPLOAD_PAGE = "product/subir";

    @GetMapping("/one")
    public String showUploadForm(Model model) {
        return UPLOAD_PAGE;
    }

    @PostMapping("/one")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        try {
            String fileUrl = firebaseStorageService.upload(file);
            model.addAttribute("message", "File uploaded successfully: " + fileUrl);
            model.addAttribute("fileUrl", fileUrl); // Agregar la URL del archivo al modelo para mostrarla en la vista si es necesario
            return UPLOAD_PAGE;
        } catch (Exception e) {
            // Imprime el stack trace del error para entender qué salió mal
            e.printStackTrace();
            model.addAttribute("message", "File upload failed: " + e.getMessage());
            return UPLOAD_PAGE;
        }
    }

}
