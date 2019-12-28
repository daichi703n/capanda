// package com.daichi703n.capanda;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// @Configuration
// @EnableWebSecurity
// public class OAuth2LoginSecurityConfig extends WebSecurityConfigurerAdapter {
//     @Override
//     protected void configure(HttpSecurity http) throws Exception {
//         http
//             .authorizeRequests()
//                 .antMatchers("/","/css/**","/js/**","/webjars/**").permitAll()
//                 .anyRequest().authenticated()
//                 .and()
//             // .formLogin()
//             //     .loginPage("/login")
//             //     .permitAll()
//             //     .and()
//             .oauth2Login()
//             //     .loginPage("/login/oauth2")
//             //     .permitAll()
//                 .and()
//             .logout()
//                 .logoutSuccessUrl("/")
//                 .permitAll();
//     }
// }
