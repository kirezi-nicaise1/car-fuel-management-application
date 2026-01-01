# 🚗 Car Fuel Management System

A Java-based application for managing cars and tracking their fuel consumption. Built with **Spring Boot** (backend) and a **standalone CLI client** that communicates via REST API.

---

##  Features

- ✅ Create and manage multiple cars
- ✅ Track fuel refills with odometer readings
- ✅ Calculate fuel statistics (total fuel, total cost, average consumption)
- ✅ RESTful API with Spring Boot
- ✅ Manual Servlet implementation (demonstrates servlet lifecycle)
- ✅ Standalone CLI application for command-line interaction
- ✅ In-memory storage (no database required)

---

##  Technology Stack

**Backend:**
- Java 21
- Spring Boot 3.2.0
- Maven
- Jackson (JSON processing)
- Jakarta Servlet API

**CLI Client:**
- Java 21
- HttpClient 
- Jackson (JSON parsing)
- Maven

---

##  Project Structure

```
car-fuel-management/
├── backend/                    # Spring Boot REST API
│   ├── src/main/java/
│   │   └── com/carfuel/backend/
│   │       ├── BackendApplication.java
│   │       ├── config/         # Servlet configuration
│   │       ├── controller/     # REST controllers
│   │       ├── model/          # Domain models (Car, FuelEntry, FuelStats)
│   │       ├── service/        # Business logic
│   │       └── servlet/        # Manual servlet implementation
│   └── pom.xml
├── cli-client/                 # Command-line interface
│   ├── src/main/java/
│   │   └── com/carfuel/cli/
│   │       └── CliApplication.java
│   └── pom.xml
└── pom.xml                     # Parent POM
```

---

##  Prerequisites

Before you begin, ensure you have the following installed:

- ☑️ **Java JDK 21** 
- ☑️ **Maven 3.6+** 
- ☑️ **IntelliJ IDEA** (or any Java IDE) 
- ☑️ **Postman** (optional, for API testing) 

**Verify installations:**
```bash
java -version    # Should show Java 21
mvn -version     # Should show Maven 3.6+
```

---

##  Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/kirezi-nicaise1/car-fuel-management-application.git
cd car-fuel-management
```

### 2. Import Project in IntelliJ

1. Open **IntelliJ IDEA**
2. Click **File → Open**
3. Navigate to the `car-fuel-management` folder
4. Click **OK**
5. Wait for Maven to download dependencies (check bottom-right corner)

---

##  Running the Application

### Step 1: Start the Backend Server

**Option A: Run from IntelliJ (Recommended)**

1. Navigate to `backend/src/main/java/com/carfuel/backend/BackendApplication.java`
2. Right-click on the file
3. Select **Run 'BackendApplication.main()'**
4. Wait for the server to start

**Option B: Run from Terminal**

```bash
cd backend
mvn spring-boot:run
```

**✅ Success Indicator:**

You should see:
```
🚗 Car Fuel Management Backend is running on http://localhost:8080
```

**Verify it's working:**
- Open browser: http://localhost:8080/api/cars
- You should see: `[]` (empty array)

---

### Step 2: Use the CLI Client

The CLI is a **separate application** that communicates with the backend via HTTP.

**Prerequisites:** Backend must be running first!

#### Running CLI from IntelliJ

1. Open `cli-client/src/main/java/com/carfuel/cli/CliApplication.java`
2. Click the **green arrow** next to `main()` method
3. Select **Modify Run Configuration...**
4. In **Program arguments**, enter one of these commands:

**Create a car:**
```
create-car --brand Toyota --model Corolla --year 2018
```

**Add fuel entry:**
```
add-fuel --carId 1 --liters 40 --price 52.5 --odometer 45000
```

**View statistics:**
```
fuel-stats --carId 1
```

5. Click **OK** then **Run**
6. Check the console for output

**To run different commands:** Just change the **Program arguments** and run again.

---

##  API Documentation

### Base URL
```
http://localhost:8080
```

### Endpoints

#### 1. Create a Car
**POST** `/api/cars`

**Request Body:**
```Example
{
  "brand": "Toyota",
  "model": "Corolla",
  "year": 2018
}
```

** Expected response:** `201 Created`
```json
{
  "id": 1,
  "brand": "Toyota",
  "model": "Corolla",
  "year": 2018,
  "fuelEntries": []
}
```

---

#### 2. List All Cars
**GET** `/api/cars`

**Expected response:** `200 OK`
```json
[
  {
    "id": 1,
    "brand": "Toyota",
    "model": "Corolla",
    "year": 2018,
    "fuelEntries": [...]
  }
]
```

---

#### 3. Add Fuel Entry
**POST** `/api/cars/{id}/fuel`

**Example Request Body:**
```json
{
  "liters": 40,
  "price": 52.5,
  "odometer": 45000
}
```

**Expected Response:** `200 OK`
```json
"Fuel entry added successfully"
```

**Error Response:** `404 Not Found`
```json
"Car not found"
```

---

#### 4. Get Fuel Statistics (REST)
**GET** `/api/cars/{id}/fuel/stats`

**Expected Response:** `200 OK`
```json
{
  "totalFuel": 85.0,
  "totalCost": 112.5,
  "avgConsumption": 17.0
}
```

**Calculation Logic:**
- `totalFuel` = Sum of all liters
- `totalCost` = Sum of all prices
- `avgConsumption` = (totalFuel / distance) × 100
  - where `distance` = max odometer - min odometer

---

#### 5. Get Fuel Statistics (Servlet)
**GET** `/servlet/fuel-stats?carId={id}`

**Expected Response:** `200 OK`
```json
{
  "totalFuel": 85.0,
  "totalCost": 112.5,
  "avgConsumption": 17.0
}
```

**Note:** This endpoint demonstrates manual servlet implementation with the same functionality as the REST endpoint.

---

##  Testing with Postman

### Initial Setup

1. Open **Postman**
2. Create a new **Collection** called "Car Fuel Management"
3. Set **Base URL** variable: `http://localhost:8080`

### Test Scenario - Complete Workflow

#### Test 1: Create First Car

**Request:**
- Method: `POST`
- URL: `{{baseUrl}}/api/cars`
- Headers: `Content-Type: application/json`
- Body (raw JSON):
```json
{
  "brand": "Toyota",
  "model": "Corolla",
  "year": 2018
}
```

**Expected Response:** Status `201 Created`
```json
{
  "id": 1,
  "brand": "Toyota",
  "model": "Corolla",
  "year": 2018,
  "fuelEntries": []
}
```

** Note the `id` in the response - you'll need it for the next steps!**

---

#### Test 2: Create Second Car

**Request:**
- Method: `POST`
- URL: `{{baseUrl}}/api/cars`
- Body:
```json
{
  "brand": "Honda",
  "model": "Civic",
  "year": 2020
}
```

---

#### Test 3: List All Cars

**Request:**
- Method: `GET`
- URL: `{{baseUrl}}/api/cars`

**Expected Response:** Array with 2 cars

---

#### Test 4: Add First Fuel Entry

**Request:**
- Method: `POST`
- URL: `{{baseUrl}}/api/cars/1/fuel`
- Body:
```json
{
  "liters": 40,
  "price": 52.5,
  "odometer": 45000
}
```

**Expected Response:** Status `200 OK`

---

#### Test 5: Add Second Fuel Entry

**Request:**
- Method: `POST`
- URL: `{{baseUrl}}/api/cars/1/fuel`
- Body:
```json
{
  "liters": 45,
  "price": 60.0,
  "odometer": 45500
}
```

---

#### Test 6: Add Third Fuel Entry

**Request:**
- Method: `POST`
- URL: `{{baseUrl}}/api/cars/1/fuel`
- Body:
```json
{
  "liters": 38,
  "price": 48.0,
  "odometer": 46000
}
```

---

#### Test 7: Get Statistics (REST API)

**Request:**
- Method: `GET`
- URL: `{{baseUrl}}/api/cars/1/fuel/stats`

**Expected Response:**
```json
{
  "totalFuel": 123.0,
  "totalCost": 160.5,
  "avgConsumption": 12.3
}
```

** Calculation Breakdown:**
- Total Fuel: 40 + 45 + 38 = 123 liters
- Total Cost: 52.5 + 60.0 + 48.0 = 160.50
- Distance: 46000 - 45000 = 1000 km
- Avg Consumption: (123 / 1000) × 100 = 12.3 L/100km

---

#### Test 8: Get Statistics (Servlet)

**Request:**
- Method: `GET`
- URL: `{{baseUrl}}/servlet/fuel-stats?carId=1`

**Expected Response:** Same as Test 7 (demonstrates both endpoints use the same service layer)

---

#### Test 9: Error Handling - Invalid Car ID

**Request:**
- Method: `GET`
- URL: `{{baseUrl}}/api/cars/999/fuel/stats`

**Expected Response:** Status `404 Not Found`

---

#### Test 10: Error Handling - Missing Parameter

**Request:**
- Method: `GET`
- URL: `{{baseUrl}}/servlet/fuel-stats`

**Expected Response:** Status `400 Bad Request`
```json
{
  "error": "carId parameter is required"
}
```

---

##  CLI Commands

All CLI commands require the backend to be running first.

Open cli-client/src/main/java/com/carfuel/cli/CliApplication.java
Click the green arrow next to main() method
Select Modify Run Configuration...
In Program arguments, enter one of these commands:

### 1. Create a Car

```bash
create-car --brand <brand> --model <model> --year <year>
```

**Example:**
```bash
create-car --brand Toyota --model Corolla --year 2018
```

**Output:**
```
✅ Car created successfully!
   ID: 1
   Brand: Toyota
   Model: Corolla
   Year: 2018
```

---

### 2. Add Fuel Entry

```bash
add-fuel --carId <id> --liters <liters> --price <price> --odometer <odometer>
```

**Example:**
```bash
add-fuel --carId 1 --liters 40 --price 52.5 --odometer 45000
```

**Output:**
```
✅ Fuel entry added successfully!
```

---

### 3. View Fuel Statistics

```bash
fuel-stats --carId <id>
```

**Example:**
```bash
fuel-stats --carId 1
```

**Output:**
```
 Fuel Statistics for Car ID: 1
   Total fuel: 123.0 L
   Total cost: 160.50
   Average consumption: 12.3 L/100km
```

##  Notes

-  **Data is not persisted** - All data is stored in-memory and will be lost when the server stops
-  **No authentication** - This is a demo project, no security implemented
-  **Single instance only** - In-memory storage means data is not shared across multiple server instances

---
