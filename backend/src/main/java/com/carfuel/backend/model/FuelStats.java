package com.carfuel.backend.model;

public class FuelStats {
    private Double totalFuel;      // Total liters consumed
    private Double totalCost;      // Total money spent
    private Double avgConsumption; // Average liters per 100km

    // Constructors
    public FuelStats() {
    }

    public FuelStats(Double totalFuel, Double totalCost, Double avgConsumption) {
        this.totalFuel = totalFuel;
        this.totalCost = totalCost;
        this.avgConsumption = avgConsumption;
    }

    // Getters and Setters
    public Double getTotalFuel() {
        return totalFuel;
    }

    public void setTotalFuel(Double totalFuel) {
        this.totalFuel = totalFuel;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Double getAvgConsumption() {
        return avgConsumption;
    }

    public void setAvgConsumption(Double avgConsumption) {
        this.avgConsumption = avgConsumption;
    }
}