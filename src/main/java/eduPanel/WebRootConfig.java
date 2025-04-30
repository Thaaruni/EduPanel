package eduPanel;

import eduPanel.converter.LecturerTypeConverter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebRootConfig  {
    @Bean(destroyMethod = "close")
    public EntityManagerFactory entityManagerFactory(){
        return Persistence.createEntityManagerFactory("default");
    };

    @Bean(destroyMethod = "close")
    @RequestScope
    public EntityManager entityManager(){
        return entityManagerFactory().createEntityManager();
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
