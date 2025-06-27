package com.evooq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Supplier;

@Configuration
public class RandomSupplierConfig {

    // springify it so that it can be reliably used in tests
    @Bean
    public Supplier<Double> randomSupplier() {
        return Math::random;
    }
}