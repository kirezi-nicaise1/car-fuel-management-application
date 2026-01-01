package com.carfuel.backend.controller;

import com.carfuel.backend.model.Car;
import com.carfuel.backend.model.FuelEntry;
import com.carfuel.backend.model.FuelStats;
import com.carfuel.backend.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping
    public ResponseEntity<Car> createCar(@RequestBody Map<String, Object> request) {
        try {
            String brand = (String) request.get("brand");
            String model = (String) request.get("model");
            Integer year = (Integer) request.get("year");

            if (brand == null || model == null || year == null) {
                return ResponseEntity.badRequest().build();
            }

            Car car = carService.createCar(brand, model, year);
            return ResponseEntity.status(HttpStatus.CREATED).body(car);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        List<Car> cars = carService.getAllCars();
        return ResponseEntity.ok(cars);
    }

    @PostMapping("/{id}/fuel")
    public ResponseEntity<String> addFuelEntry(
            @PathVariable("id") Long id,
            @RequestBody Map<String, Object> request) {

        try {
            Double liters = ((Number) request.get("liters")).doubleValue();
            Double price = ((Number) request.get("price")).doubleValue();
            Integer odometer = (Integer) request.get("odometer");

            if (liters == null || price == null || odometer == null) {
                return ResponseEntity.badRequest().body("Missing required fields");
            }

            boolean success = carService.addFuelEntry(id, liters, price, odometer);

            if (success) {
                return ResponseEntity.ok("Fuel entry added successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Car not found");
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid input data");
        }
    }

    @GetMapping("/{id}/fuel/stats")
    public ResponseEntity<FuelStats> getFuelStats(@PathVariable("id") Long id) {
        Optional<FuelStats> stats = carService.getFuelStats(id);

        if (stats.isPresent()) {
            return ResponseEntity.ok(stats.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}