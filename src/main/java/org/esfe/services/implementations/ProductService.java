package org.esfe.services.implementations;

import org.esfe.models.Product;
import org.esfe.repository.IProductRepository;
import org.esfe.repository.IUserRepository;
import org.esfe.services.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {

    @Autowired
    private IProductRepository productRepository;

    @Override
    public Page<Product> buscarTodosPaginados(Pageable pageable) {

        return productRepository.findAll(pageable);
    }

    @Override
    public List<Product> obtenerTodos() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> buscarPorId(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    public Product createOEditar(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void eliminarPorId(Integer id) {
        productRepository.deleteById(id);
    }
}
