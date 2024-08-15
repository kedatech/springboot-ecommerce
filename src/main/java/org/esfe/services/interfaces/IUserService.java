package org.esfe.services.interfaces;

import org.esfe.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {

    Page<User> buscarTodosPaginados(Pageable pageable);


}
