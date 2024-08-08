package com.time.demo.settings;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class ReturnWhoWrite {
    @Bean
    AuditorAware<Long> auditorAware() {
        return new CreatedBy();
    }
}
