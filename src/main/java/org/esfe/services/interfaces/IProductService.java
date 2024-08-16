package org.esfe.services.interfaces;

import org.esfe.models.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IProductService {

    Page<Product> buscarTodosPaginados(Pageable pageable);

    List<Product> obtenerTodos();

    Optional<Product> buscarPorId(Integer id);

    Product createOEditar(Product product);

    void eliminarPorId(Integer id);
}
