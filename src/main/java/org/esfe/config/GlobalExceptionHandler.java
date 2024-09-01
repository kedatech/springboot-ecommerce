package org.esfe.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleNotFound(NoHandlerFoundException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("404");  // Nombre del archivo 404.html en tu carpeta de plantillas
        return modelAndView;
    }
}
