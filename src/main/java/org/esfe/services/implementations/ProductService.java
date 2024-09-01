package org.esfe.services.implementations;

import org.esfe.models.Product;
import org.esfe.repository.IProductRepository;
import org.esfe.repository.IUserRepository;
import org.esfe.services.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

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

    // Sobrecarga del método createOEditar para manejar la imagen
    public Product createOEditar(Product product, MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = firebaseStorageService.upload(imageFile);
            product.setImageUrl(imageUrl);
        }
        return productRepository.save(product);
    }
}
