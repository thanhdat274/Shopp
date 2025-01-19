package com.jvb.demo.config;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler{

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for(GrantedAuthority grantedAuthority : authorities){
            String roleName = grantedAuthority.getAuthority();
            if(roleName.equals("ROLE_ADMIN")){
                new SimpleUrlAuthenticationSuccessHandler("/admin")
                    .onAuthenticationSuccess(request, response, authentication);
                return;
            }
        }
        new SimpleUrlAuthenticationSuccessHandler("/")
            .onAuthenticationSuccess(request, response, authentication);
    }

}