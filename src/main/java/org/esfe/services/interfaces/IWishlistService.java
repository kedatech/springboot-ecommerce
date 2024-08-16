package org.esfe.services.interfaces;

import org.esfe.models.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IWishlistService {

    Page<Wishlist> buscarTodosPaginados(Pageable pageable);

    List<Wishlist> obtenerTodos();

    Optional<Wishlist> buscarPorId(Integer id);

    Wishlist createOEditar(Wishlist wishlist);

    void eliminarPorId(Integer id);
}
