package com.inventario.backend_inventario.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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
}
