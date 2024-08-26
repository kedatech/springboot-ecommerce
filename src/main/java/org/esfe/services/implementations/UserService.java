package org.esfe.services.implementations;

import org.esfe.models.User;
import org.esfe.repository.IUserRepository;
import org.esfe.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class UserService  implements IUserService {

    @Autowired
    private IUserRepository userRepository;
    
    public Optional<User> buscarPorGoogleId(String googleId) {
        return userRepository.findByGoogleId(googleId);
    }
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

    @Override
    public User processOAuthPostLogin(OAuth2User oAuth2User) {
        String googleId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        Optional<User> existingUser = userRepository.findByGoogleId(googleId);

        if (existingUser.isPresent()) {
            return existingUser.get();
        } else {
            Optional<User> userByEmail = userRepository.findByEmail(email);

            if (userByEmail.isPresent()) {
                User user = userByEmail.get();
                if ((user.getGoogleId() == null || user.getGoogleId().isEmpty()) && user.isAdmin()) {
                    user.setGoogleId(googleId);
                    user.setName(oAuth2User.getAttribute("name"));
                    return userRepository.save(user);
                }
            }
            User newUser = new User();
            newUser.setName(oAuth2User.getAttribute("name"));
            newUser.setEmail(email);
            newUser.setGoogleId(googleId);
            return userRepository.save(newUser);
        }
    }
}
