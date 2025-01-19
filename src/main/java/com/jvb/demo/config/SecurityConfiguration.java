package com.jvb.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.jvb.demo.service.AccountService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	@Autowired
	private AccountService accountService;
	
	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(accountService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }
	
    @Bean()
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
	
		@Override
	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	        auth.authenticationProvider(authenticationProvider());
			auth.inMemoryAuthentication().withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN");
	    }
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests()
                    .antMatchers("/cart","/order","/order/all","/order/{\\d+}","/payment/**",
                            "/account","/account/update/password","/account/update/profile","/order/cancel").hasRole("CUSTOMER")
                    .antMatchers(
                        "/product/create","/product/admin/{\\d+}","/product/admin","/admin/**","/account/all",
                            "/account/disable","/account/enable","/order/all/admin","/order/admin/{\\d+}").hasRole("ADMIN")
                    .antMatchers(
                            "/js/**",
                            "/css/**",
                            "/img/**",
                            "/images/**", "/static/**", "/templates/**", "/src/**",
                            "/scss/**", "/webfonts/**", "/","/sign-up","/product","/product/{\\d+}","/login-expired","/google/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin(login -> login
                            .loginPage("/login").successHandler(new CustomLoginSuccessHandler())
                            .failureUrl("/login-error?error")
                            .permitAll())
                    .logout(logout -> logout
                            .invalidateHttpSession(true)
                            .clearAuthentication(true)
                            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                            .logoutSuccessUrl("/login?logout=success")
                            .permitAll())
                    .sessionManagement(management -> management.maximumSessions(10)
                            .sessionRegistry(sessionRegistry())
                            .expiredUrl("/login-expired"));
		}
	
}
