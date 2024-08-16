package org.esfe.repository;

import org.esfe.models.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IWishlistRepository extends JpaRepository<Wishlist,Integer> {
}
