package org.esfe.services.implementations;

import org.esfe.models.Wishlist;
import org.esfe.repository.IWishlistRepository;
import org.esfe.services.interfaces.IWishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class WishlistService implements IWishlistService {

    @Autowired
    private IWishlistRepository wishlistRepository;

    @Override
    public Page<Wishlist> buscarTodosPaginados(Pageable pageable) {
        return wishlistRepository.findAll(pageable);
    }

    @Override
    public List<Wishlist> obtenerTodos() {
        return wishlistRepository.findAll();
    }

    @Override
    public Optional<Wishlist> buscarPorId(Integer id) {
        return wishlistRepository.findById(id);
    }

    @Override
    public Wishlist createOEditar(Wishlist wishlist) {
        return wishlistRepository.save(wishlist);
    }

    @Override
    public void eliminarPorId(Integer id) {
        wishlistRepository.deleteById(id);
    }
}
