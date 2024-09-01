package org.esfe.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        // Aquí puedes personalizar la lógica para diferentes códigos de estado
        return "404"; // Retorna la vista 404.html
    }

    // Este método está obsoleto en las nuevas versiones de Spring Boot, pero es necesario para compatibilidad.
    public String getErrorPath() {
        return "/error";
    }
}
