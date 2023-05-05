package com.mafv.academy.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.mafv.academy.services.UsuariosService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    
    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    UsuariosService myUserService(){
        return new UsuariosService();
    }

    @Bean
    public UserDetailsService user(){

        UserDetails user = User.builder()
            .username("user")
            .password("{noop}user*1234")
            .authorities("USER")
            .build();

        UserDetails admin = User.builder()
            .username("admin")
            .password("{noop}admin*1234")
            .authorities("USER","ADMIN")
            .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider autProvider = new DaoAuthenticationProvider();

        autProvider.setUserDetailsService(myUserService());
        autProvider.setPasswordEncoder(passwordEncoder());

        return autProvider;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        String[] staticResources  =  {
            "/css/**",
            "/img/**",
            "/fonts/**",
            "/js/**",
        };

        http.authorizeRequests()
                .antMatchers(staticResources).permitAll()
                .antMatchers("/login").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/welcome")
                .permitAll()
                .and()
                .logout()
                    .logoutSuccessUrl("/login?logout=true")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                .permitAll();
    }

    // Evita que te redirija a cualquier css o js
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/js/**");
    }
}
