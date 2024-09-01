package org.esfe.controllers;

import org.esfe.models.Category;
import org.esfe.services.interfaces.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @GetMapping
    public String index(Model model){
        List<Category> categories = categoryService.obtenerTodos();
        model.addAttribute("categories", categories);
        return "category/index";
    }

    @GetMapping("/create")
    public String create(Category category){
        return "category/create";
    }

    @PostMapping("/save")
    public String save(Category category, BindingResult result, Model model, RedirectAttributes attributes){
        if (result.hasErrors()){
            model.addAttribute(category);
            attributes.addFlashAttribute("error", "No se puede guardar por un error.");
            return "category/create";
        }
        categoryService.createOEditar(category);
        attributes.addFlashAttribute("msg", "Categoría creada correctamente");
        return "redirect:/categories";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model){
        Category category = categoryService.buscarPorId(id).get();
        model.addAttribute("category", category);
        return "category/details";
    }

    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable Integer id, Model model) {
        Optional<Category> category = categoryService.buscarPorId(id);
        if (category.isPresent()) {
            model.addAttribute("category", category.get());
            return "category/edit";
        } else {
            return "redirect:/categories";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateCategory(@PathVariable Integer id, @ModelAttribute("category") Category category) {
        categoryService.save(category);
        return "redirect:/categories"; // Redirigir a la lista de categorías después de guardar
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model){
        Category category = categoryService.buscarPorId(id).orElse(null);
        model.addAttribute("category", category);
        return "category/delete";
    }

    @PostMapping("/delete")
    public String delete(Category category, RedirectAttributes attributes){
        categoryService.eliminarPorId(category.getId());
        attributes.addFlashAttribute("msg", "Categoría eliminada correctamente");
        return "redirect:/categories";
    }
}
