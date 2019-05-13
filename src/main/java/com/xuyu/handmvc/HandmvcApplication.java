package com.xuyu.handmvc;

import com.xuyu.handmvc.servlet.DispatherServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ServletComponentScan
public class HandmvcApplication {

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(new DispatherServlet(), "/");
    }

    public static void main(String[] args) {
        SpringApplication.run(HandmvcApplication.class, args);
    }

}
