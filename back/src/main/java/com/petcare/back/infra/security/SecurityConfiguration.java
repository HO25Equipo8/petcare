package com.petcare.back.infra.security;

import com.petcare.back.infra.gsignin.OAuth2AuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@EnableScheduling
public class SecurityConfiguration {

    @Autowired
    private SecurityFilter securityFilter;

    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception
    {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.GET, "/hello").permitAll()
                        .requestMatchers(HttpMethod.POST, "/login", "/register").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/upload").permitAll()
                        .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll() // Allow OAuth2 endpoints

                        //.requestMatchers(HttpMethod.GET, "/", "/api/sitter/{id}", "/api/owner/{id}","/api/pet/{id}").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**")
                        // Role-based endpoints

                        //.requestMatchers(HttpMethod.POST, "/api/cursos").hasRole("ADMIN")
                        //.requestMatchers(HttpMethod.POST, "/api/mentorias", "/api/certificaciones").hasAnyRole("ADMIN", "MENTOR")

                        //.requestMatchers(HttpMethod.PUT, "/api/cursos/").hasRole("ADMIN")
                        //.requestMatchers(HttpMethod.PUT, "/api/mentorias/", "/api/certificaciones/").hasAnyRole("ADMIN", "MENTOR")

                        //.requestMatchers(HttpMethod.DELETE, "/api/cursos/", "/api/mentorias/", "/api/certificaciones/").hasRole("ADMIN")

                        //.requestMatchers(HttpMethod.GET,"/users/**").hasAnyRole("USER", "ADMIN", "OWNER", "SITTER")
                        //.requestMatchers(HttpMethod.PUT,"/users/**").hasAnyRole("USER", "ADMIN", "OWNER", "SITTER")
                        //.requestMatchers(HttpMethod.DELETE,"/users/**").hasRole("ADMIN")
                        .permitAll()
                        .anyRequest().authenticated())
                // .oauth2Login(oauth2 -> oauth2
                //        .successHandler(oAuth2AuthenticationSuccessHandler)
                //)
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(authz -> authz
//                        .requestMatchers(HttpMethod.POST, "/login", "/register").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/hello").permitAll()
//                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                        .requestMatchers("/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
//                .build();
//    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // For production, use specific origins:
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080", "http://localhost:8080/swagger-ui/index.html", "http://v3/api-docs", "https://ourdomain.com"));
        //configuration.setAllowedOrigins(Arrays.asList("https://ourdomain.com"));
        configuration.setAllowedOriginPatterns(Arrays.asList("*")); // For development


        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Puse esto porque me daba error de cors

//    @Value("${url.front.deploy}")
//    private String urlFront;
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList(urlFront));
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(Arrays.asList("*"));
//        configuration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception
    {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
