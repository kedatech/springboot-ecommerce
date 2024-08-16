package org.esfe.services.implementations;

import org.esfe.models.User;
import org.esfe.repository.IUserRepository;
import org.esfe.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class UserService  implements IUserService {

    @Autowired
    private IUserRepository userRepository;
    @Override
    public Page<User> buscarTodosPaginados(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public List<User> obtenerTodos() {
        return userRepository.findAll();

    }

    @Override
    public Optional<User> buscarPorId(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public User createOEditar(User user) {
        return userRepository.save(user);
    }

    @Override
    public void eliminarPorId(Integer id) {
        userRepository.deleteById(id);
    }
}
