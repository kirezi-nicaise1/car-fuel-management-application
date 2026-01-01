package com.carfuel.cli;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class CliApplication {

    private static final String BASE_URL = "http://localhost:8080";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        String command = args[0];

        try {
            switch (command) {
                case "create-car":
                    handleCreateCar(args);
                    break;
                case "add-fuel":
                    handleAddFuel(args);
                    break;
                case "fuel-stats":
                    handleFuelStats(args);
                    break;
                default:
                    System.out.println("❌ Unknown command: " + command);
                    printUsage();
            }
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void handleCreateCar(String[] args) throws Exception {
        Map<String, String> params = parseArguments(args);

        String brand = params.get("brand");
        String model = params.get("model");
        String year = params.get("year");

        if (brand == null || model == null || year == null) {
            System.out.println("❌ Missing required parameters: --brand, --model, --year");
            return;
        }

        // Build JSON request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("brand", brand);
        requestBody.put("model", model);
        requestBody.put("year", Integer.parseInt(year));

        String jsonBody = objectMapper.writeValueAsString(requestBody);

        // Make HTTP POST request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/cars"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            JsonNode carNode = objectMapper.readTree(response.body());
            System.out.println("✅ Car created successfully!");
            System.out.println("   ID: " + carNode.get("id").asLong());
            System.out.println("   Brand: " + carNode.get("brand").asText());
            System.out.println("   Model: " + carNode.get("model").asText());
            System.out.println("   Year: " + carNode.get("year").asInt());
        } else {
            System.out.println("❌ Failed to create car. Status: " + response.statusCode());
        }
    }

    private static void handleAddFuel(String[] args) throws Exception {
        Map<String, String> params = parseArguments(args);

        String carId = params.get("carId");
        String liters = params.get("liters");
        String price = params.get("price");
        String odometer = params.get("odometer");

        if (carId == null || liters == null || price == null || odometer == null) {
            System.out.println("❌ Missing required parameters: --carId, --liters, --price, --odometer");
            return;
        }

        // Build JSON request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("liters", Double.parseDouble(liters));
        requestBody.put("price", Double.parseDouble(price));
        requestBody.put("odometer", Integer.parseInt(odometer));

        String jsonBody = objectMapper.writeValueAsString(requestBody);

        // Make HTTP POST request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/cars/" + carId + "/fuel"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("✅ Fuel entry added successfully!");
        } else if (response.statusCode() == 404) {
            System.out.println("❌ Car not found with ID: " + carId);
        } else {
            System.out.println("❌ Failed to add fuel entry. Status: " + response.statusCode());
        }
    }

    private static void handleFuelStats(String[] args) throws Exception {
        Map<String, String> params = parseArguments(args);

        String carId = params.get("carId");

        if (carId == null) {
            System.out.println("❌ Missing required parameter: --carId");
            return;
        }

        // Make HTTP GET request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/cars/" + carId + "/fuel/stats"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonNode statsNode = objectMapper.readTree(response.body());

            double totalFuel = statsNode.get("totalFuel").asDouble();
            double totalCost = statsNode.get("totalCost").asDouble();
            double avgConsumption = statsNode.get("avgConsumption").asDouble();

            System.out.println("📊 Fuel Statistics for Car ID: " + carId);
            System.out.println("   Total fuel: " + String.format("%.1f", totalFuel) + " L");
            System.out.println("   Total cost: " + String.format("%.2f", totalCost));
            System.out.println("   Average consumption: " + String.format("%.1f", avgConsumption) + " L/100km");

        } else if (response.statusCode() == 404) {
            System.out.println("❌ Car not found or no fuel data available for ID: " + carId);
        } else {
            System.out.println("❌ Failed to retrieve stats. Status: " + response.statusCode());
        }
    }

    private static Map<String, String> parseArguments(String[] args) {
        Map<String, String> params = new HashMap<>();

        for (int i = 1; i < args.length; i += 2) {
            if (args[i].startsWith("--") && i + 1 < args.length) {
                String key = args[i].substring(2); // Remove "--"
                String value = args[i + 1];
                params.put(key, value);
            }
        }

        return params;
    }


    private static void printUsage() {
        System.out.println("🚗 Car Fuel Management CLI");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  create-car --brand <brand> --model <model> --year <year>");
        System.out.println("  add-fuel --carId <id> --liters <liters> --price <price> --odometer <odometer>");
        System.out.println("  fuel-stats --carId <id>");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  create-car --brand Toyota --model Corolla --year 2018");
        System.out.println("  add-fuel --carId 1 --liters 40 --price 52.5 --odometer 45000");
        System.out.println("  fuel-stats --carId 1");
    }
}