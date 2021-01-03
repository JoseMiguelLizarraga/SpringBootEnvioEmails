package com.proyecto;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class VistasConfig implements WebMvcConfigurer 
{
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) 
	{
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/contactoPersona").setViewName("contactoPersona/index");
        registry.addViewController("/envioMasivoCorreo").setViewName("envioMasivoCorreo/index");
        registry.addViewController("/maestroEnvioEmail").setViewName("maestroEnvioEmail/index");
        registry.addViewController("/respaldoDatos").setViewName("respaldoDatos/index");
	}
}
