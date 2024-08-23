package org.esfe.services.interfaces;

import org.esfe.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    Page<User> buscarTodosPaginados(Pageable pageable);

    List<User> obtenerTodos();

    Optional<User> buscarPorId(Integer id);

    User createOEditar(User user);

    void eliminarPorId(Integer id);

    User processOAuthPostLogin(OAuth2User oAuth2User);
}
