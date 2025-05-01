package eduPanel;


import eduPanel.converter.LecturerTypeConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebMvc
@ComponentScan
public class WebAppConfig implements WebMvcConfigurer  {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new LecturerTypeConverter());
    }

    @Bean
    public StandardServletMultipartResolver multipartResolver(){
         return new StandardServletMultipartResolver();
     }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("/home/thaaruni-dissanayake/Documents/DEP13/my_projects/last-project-EduPanel/edupanel-jave-api/src/main/java/eduPanel/uploads");
    }
}
