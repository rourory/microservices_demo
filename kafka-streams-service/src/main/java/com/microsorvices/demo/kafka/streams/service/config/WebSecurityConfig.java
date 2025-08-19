package com.microsorvices.demo.kafka.streams.service.config;

import com.microsorvices.demo.kafka.streams.service.security.KafkaStreamsUserDetailsService;
import com.microsorvices.demo.kafka.streams.service.security.KafkaStreamsUserJwtConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    private final KafkaStreamsUserDetailsService userDetailsService;
    private final OAuth2ResourceServerProperties resourceServerProperties;

    public WebSecurityConfig(KafkaStreamsUserDetailsService userDetailsService,
                             OAuth2ResourceServerProperties resourceServerProperties) {
        this.userDetailsService = userDetailsService;
        this.resourceServerProperties = resourceServerProperties;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(configurer ->
                        configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry ->
                        registry.anyRequest().fullyAuthenticated())
                .oauth2ResourceServer(configurer ->
                        configurer.jwt(jwtConfigurer ->
                                jwtConfigurer.jwtAuthenticationConverter(kafkaStreamsUserJwtConverter())));

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder(@Qualifier("kafka-streams-service-audience-validator") OAuth2TokenValidator<Jwt> audienceValidator) {
        NimbusJwtDecoder decoder = JwtDecoders.fromIssuerLocation(
                resourceServerProperties.getJwt().getIssuerUri()
        );
        OAuth2TokenValidator<Jwt> withIssuer =
                JwtValidators.createDefaultWithIssuer(
                        resourceServerProperties.getJwt().getIssuerUri()
                );
        OAuth2TokenValidator<Jwt> withAudience =
                new DelegatingOAuth2TokenValidator<Jwt>(withIssuer, audienceValidator);
        decoder.setJwtValidator(withAudience);
        return decoder;
    }

    @Bean
    public Converter<Jwt, ? extends AbstractAuthenticationToken> kafkaStreamsUserJwtConverter() {
        return new KafkaStreamsUserJwtConverter(userDetailsService);
    }

}
