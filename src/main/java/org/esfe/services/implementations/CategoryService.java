package org.esfe.services.implementations;

import org.esfe.models.Category;
import org.esfe.repository.ICategoryRepository;
import org.esfe.services.interfaces.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll(); // Obtiene todas las categorías
    }

    @Override
    public Optional<Category> getCategoryById(Integer id) {
        return categoryRepository.findById(id); // Obtiene una categoría por ID
    }

    @Override
    public Page<Category> buscarTodosPaginados(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public List<Category> obtenerTodos() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> buscarPorId(Integer id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category createOEditar(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void eliminarPorId(Integer id) {
        categoryRepository.deleteById(id);
    }

}
