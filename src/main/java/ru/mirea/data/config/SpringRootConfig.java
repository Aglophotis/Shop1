package ru.mirea.data.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "ru.mirea.data.service" })
public class SpringRootConfig {
}