package org.esfe.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.esfe.models.User;
import org.esfe.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private IUserService userService;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String uri = request.getRequestURI();
        boolean isAdmin = false;
        boolean isAuthenticated = false;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            String googleId = oauthToken.getPrincipal().getAttribute("sub");
            Optional<User> user = userService.buscarPorGoogleId(googleId);
            isAdmin = user.isPresent() && user.get().isAdmin();
            isAuthenticated = true;
            System.out.println(user.get().isAdmin());
        }

        System.out.println(isAdmin);
        if (uri.contains("/manage")) {
            if(isAdmin){
                response.sendRedirect("user/manage");
                return;
            }
            if(!isAuthenticated)
            {
                response.sendRedirect("/oauth2/authorization/google");
            }
            else{
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            }
        }
        else{
            if(!isAuthenticated)
            {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            }
        }


    }
}
