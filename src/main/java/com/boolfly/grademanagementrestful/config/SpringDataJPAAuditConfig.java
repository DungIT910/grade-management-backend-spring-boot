package com.boolfly.grademanagementrestful.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration class for enabling JPA auditing in Spring Data JPA.
 * <p>
 * This class is annotated with {@link Configuration} to indicate that it is a source of bean definitions,
 * and with {@link EnableJpaAuditing} to enable JPA auditing features.
 *
 * @see Configuration
 * @see EnableJpaAuditing
 *
 * @author Bao Le
 */
@Configuration
@EnableJpaAuditing
public class SpringDataJPAAuditConfig {
}
