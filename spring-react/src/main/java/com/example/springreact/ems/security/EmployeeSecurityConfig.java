//package com.example.springreact.ems.security;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.concurrent.TimeUnit;
//
//@EnableWebSecurity
//@Configuration
//public class EmployeeSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService(){
//        return new EmployeesUserDetailsService();
//    }
//
//    public DaoAuthenticationProvider authenticationProvider(){
//        DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(passwordEncoder());
//        provider.setUserDetailsService(userDetailsService());
//        return provider;
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(authenticationProvider());
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers("/users/register/**","/hello/**", "/js/**", "/css/**").permitAll()
//                .antMatchers(HttpMethod.GET,"/**").hasAuthority("ADMIN")
//                .antMatchers(HttpMethod.POST,"/save/**").hasAnyAuthority("ADMIN","USER", "MANAGER")
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                   .loginPage("/login").permitAll()
//                   .usernameParameter("email")
//                   .defaultSuccessUrl("/users", true)
//                   .failureUrl("/login")
//                .and()
//                .logout()
////                   .logoutUrl("/login").permitAll()
//                   .invalidateHttpSession(true)
//                   .deleteCookies("remember-me", "JESSIONID")
//                .and()
//                .rememberMe()
//                   .tokenValiditySeconds(2000)
//                   .key("myremember-me-secret");
//
//    }
//
//}
