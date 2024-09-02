package org.esfe.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    // Método que maneja el error con parámetros personalizados recibidos por la ruta (path)
    @RequestMapping("/error/{errorCode}/{errorMessage}")
    public String handleCustomError(@PathVariable(value = "errorCode") String errorCode,
                                    @PathVariable(value = "errorMessage") String errorMessage,
                                    Model model) {
        model.addAttribute("errorCode", errorCode);
        model.addAttribute("errorMessage", errorMessage);
        return "404"; // Retorna la vista 404.html
    }

    // Este método está obsoleto en las nuevas versiones de Spring Boot, pero es necesario para compatibilidad.
    public String getErrorPath() {
        return "/error";
    }
}
