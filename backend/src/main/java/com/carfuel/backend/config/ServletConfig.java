package com.carfuel.backend.config;

import com.carfuel.backend.service.CarService;
import com.carfuel.backend.servlet.FuelStatsServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletConfig {
    @Bean
    public FuelStatsServlet fuelStatsServlet(CarService carService) {
        FuelStatsServlet servlet = new FuelStatsServlet();
        servlet.setCarService(carService);  // Manual injection
        return servlet;
    }

    @Bean
    public ServletRegistrationBean<FuelStatsServlet> fuelStatsServletRegistration(FuelStatsServlet servlet) {
        return new ServletRegistrationBean<>(servlet, "/servlet/fuel-stats");
    }
}