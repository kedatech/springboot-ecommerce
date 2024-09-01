package org.esfe.services.interfaces;


import org.esfe.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {

    Page<Category> buscarTodosPaginados(Pageable pageable);

    List<Category> obtenerTodos();

    void save(Category category);

    Optional<Category> buscarPorId(Integer id);

    Category createOEditar(Category category);

    void eliminarPorId(Integer id);
}
