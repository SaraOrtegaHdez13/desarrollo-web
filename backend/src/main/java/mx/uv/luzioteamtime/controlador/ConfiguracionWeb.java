package mx.uv.luzioteamtime.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConfiguracionWeb implements WebMvcConfigurer {

    @Autowired
    private SesionInterceptor sesionInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registro) {
        registro.addMapping("/**")
            .allowedOrigins("http://localhost:5500")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registro) {
        registro.addInterceptor(sesionInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/crearUsuario", "/iniciarSesion");
    }
}