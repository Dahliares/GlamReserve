package com.glamreserve.glamreserve.entities.interceptor;

import com.glamreserve.glamreserve.entities.user.User;
import com.glamreserve.glamreserve.entities.user.UserRepository;
import com.glamreserve.glamreserve.service.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                }
            }
        }
        String requestURI = request.getRequestURI();


        boolean isProtectedEndpoint = requestURI.startsWith("/admin")
                || requestURI.startsWith("/main")
                || requestURI.startsWith("/edit-user")
                || requestURI.equals("/registerCompany");

        if(!isProtectedEndpoint){
            return true;
        }
        if (token == null) {
            response.sendRedirect("/login");
            return false;
        }

        if (token != null) {
            String username = AuthenticationService.validateToken(token);
            if (username != null) {
                User user = userRepository.findByUsername(username);
                if(requestURI.equals("/main")){
                    response.sendRedirect("/main/" + user.getId());
                    return false;
                }
                boolean isRegisterEndpoint = requestURI.startsWith("/registerCompany");
                boolean isAdminEndpoint = requestURI.startsWith("/admin");
                boolean isOwnProfile = user != null
                        && requestURI.matches("/main/\\d+")
                        && requestURI.equals("/main/" + user.getId());
                boolean isOwnEdit = user != null
                        && requestURI.matches("/edit-user/\\d+")
                        && requestURI.equals("/edit-user/" + user.getId());

                if (user != null && (isAdminEndpoint && user.getRoleId() == 0 || isOwnProfile || isOwnEdit || isRegisterEndpoint)) {
                    return true;
                }
            }else{
                response.sendRedirect("/login");
                return false;
            }
        }

        response.sendRedirect("/error?error=403");
        return false;
    }

}
