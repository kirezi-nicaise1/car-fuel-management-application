package com.carfuel.backend.servlet;

import com.carfuel.backend.model.FuelStats;
import com.carfuel.backend.service.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public class FuelStatsServlet extends HttpServlet {

    private CarService carService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void setCarService(CarService carService) {
        this.carService = carService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // STEP 1: Manually parse query parameter
        String carIdParam = request.getParameter("carId");

        if (carIdParam == null || carIdParam.isEmpty()) {
            // STEP 2: Manually set error status
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"carId parameter is required\"}");
            return;
        }

        try {
            Long carId = Long.parseLong(carIdParam);

            // STEP 3: Use same service layer as REST API
            Optional<FuelStats> stats = carService.getFuelStats(carId);

            if (stats.isPresent()) {
                // STEP 4: Manually set success response
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");

                // STEP 5: Convert Java object to JSON and write to response
                String jsonResponse = objectMapper.writeValueAsString(stats.get());
                response.getWriter().write(jsonResponse);

            } else {
                // Car not found or no fuel entries
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Car not found or no fuel data\"}");
            }

        } catch (NumberFormatException e) {
            // Invalid carId format
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid carId format\"}");
        }
    }
}