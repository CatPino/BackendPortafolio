package com.inventario.backend_inventario.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    String rutaImgs = Paths
        .get(System.getProperty("user.dir"), "img")
        .toAbsolutePath()
        .toString()
        .replace("\\", "/");

    registry.addResourceHandler("/img/**")
            .addResourceLocations("file:" + rutaImgs + "/");
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
            .allowedOrigins(
                "http://localhost:5173",
                "https://frontend-portafolio-lumiskin-yebo.vercel.app"
            )
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(false);
  }
}
