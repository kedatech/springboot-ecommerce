package org.esfe.repository;

import org.esfe.models.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IWishilistRepository extends JpaRepository<Wishlist,Integer> {
}
