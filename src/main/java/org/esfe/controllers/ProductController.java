package org.esfe.controllers;


import org.esfe.models.Category;
import org.esfe.models.Product;
import org.esfe.services.implementations.FirebaseStorageService;
import org.esfe.services.interfaces.ICategoryService;
import org.esfe.services.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private FirebaseStorageService firebaseStorageService; // Inyectar el servicio de Firebase
    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService; // Inyectar el servicio de categorías

    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1) - 1; // Si no está seteado, se asigna 0
        int pageSize = size.orElse(5); // Tamaño de la página, se asigna 5
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<Product> products = productService.buscarTodosPaginados(pageable);
        model.addAttribute("products", products);

        int totalPages = products.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "product/index";
    }

    @GetMapping("/create")
    public String create(Product product, Model model) {
        List<Category> categories = categoryService.getAllCategories(); // Obtener la lista de categorías
        model.addAttribute("categories", categories); // Agregar la lista al modelo para que esté disponible en la vista
        return "product/create";
    }

    @PostMapping("/save")
    public String save (Product product, BindingResult result, Model model,  @RequestParam("image") MultipartFile image, // Recibir el archivo de imagen
                       RedirectAttributes attributes) {
        if (result.hasErrors()) {

            // Cargar la lista de categorías para la vista
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories); // Agregar la lista al modelo para que esté disponible en la vista
            model.addAttribute(product);
            attributes.addFlashAttribute("error", "No se puede guardar por un error.");
            return "product/create";
        }

        // Si el producto tiene una categoría asignada
        if (product.getCategory() != null) {
            // Aquí se puede hacer una validación adicional si es necesario
            Category category = categoryService.getCategoryById(product.getCategory().getId()).orElse(null);
            if (category != null) {
                product.setCategory(category);
            } else {
                attributes.addFlashAttribute("error", "Categoría no encontrada.");
                return "product/create";
            }
        }

        // Subir la imagen a Firebase y guardar la URL en el producto
        if (!image.isEmpty()) {
            String imageUrl = firebaseStorageService.upload(image); // Subir la imagen y obtener la URL
            product.setImageUrl(imageUrl); // Guardar la URL en el objeto Product
        }

        productService.createOEditar(product);
        attributes.addFlashAttribute("msg", "Producto creado correctamente");
        return "redirect:/products";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model) {
        Product product = productService.buscarPorId(id).get();
        model.addAttribute("product", product);
        return "product/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Product product = productService.buscarPorId(id).orElse(null);
        List<Category> categories = categoryService.getAllCategories(); // Obtener la lista de categorías para la vista de edición
        model.addAttribute("categories", categories); // Agregar la lista al modelo
        model.addAttribute("product", product);
        return "product/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model) {
        Product product = productService.buscarPorId(id).orElse(null);
        model.addAttribute("product", product);
        return "product/delete";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes attributes) {
        productService.eliminarPorId(id);
        attributes.addFlashAttribute("msg", "Producto eliminado correctamente");
        return "redirect:/products";
    }
}

