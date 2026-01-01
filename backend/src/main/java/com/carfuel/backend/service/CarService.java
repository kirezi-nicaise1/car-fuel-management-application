package com.carfuel.backend.service;

import com.carfuel.backend.model.Car;
import com.carfuel.backend.model.FuelEntry;
import com.carfuel.backend.model.FuelStats;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CarService {

    // In-memory storage: Map of carId -> Car object
    private final Map<Long, Car> cars = new HashMap<>();

    // Thread-safe ID generator
    private final AtomicLong idCounter = new AtomicLong(1);

    public Car createCar(String brand, String model, Integer year) {
        Long id = idCounter.getAndIncrement();
        Car car = new Car(id, brand, model, year);
        cars.put(id, car);
        return car;
    }

    public List<Car> getAllCars() {
        return new ArrayList<>(cars.values());
    }

    public boolean addFuelEntry(Long carId, Double liters, Double price, Integer odometer) {
        Car car = cars.get(carId);
        if (car == null) {
            return false; // Car not found
        }

        FuelEntry entry = new FuelEntry(liters, price, odometer);
        car.addFuelEntry(entry);
        return true;
    }

    public Optional<FuelStats> getFuelStats(Long carId) {
        Car car = cars.get(carId);
        if (car == null || car.getFuelEntries().isEmpty()) {
            return Optional.empty();
        }

        List<FuelEntry> entries = car.getFuelEntries();

        double totalFuel = 0;
        double totalCost = 0;
        int minOdometer = Integer.MAX_VALUE;
        int maxOdometer = Integer.MIN_VALUE;

        for (FuelEntry entry : entries) {
            totalFuel += entry.getLiters();
            totalCost += entry.getPrice();

            if (entry.getOdometer() < minOdometer) {
                minOdometer = entry.getOdometer();
            }
            if (entry.getOdometer() > maxOdometer) {
                maxOdometer = entry.getOdometer();
            }
        }

        // Calculate average consumption (liters per 100km)
        double avgConsumption = 0;
        int distance = maxOdometer - minOdometer;

        if (distance > 0) {
            avgConsumption = (totalFuel / distance) * 100;
        }

        return Optional.of(new FuelStats(totalFuel, totalCost, avgConsumption));
    }
}