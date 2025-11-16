# 📁 Project Structure Overview – GeoLocateAPI

This document provides a clear overview of the folder and file responsibilities within the GeoLocateAPI project.  
The architecture follows **Clean Architecture** + **Hexagonal Architecture**, ensuring each layer has a single, well‑defined purpose.

---

## 1. domain/ — Core Business Logic

Pure Java classes containing the domain models.  
No Spring, no annotations, no external dependencies.

### Files:
- **GeoLocationData.java**  
  Domain model representing geolocation information (lat/lon, country, region, city, timezone, ISP).  
  Independent from provider DTOs or frameworks.

---

## 2. application/ — Use Cases & Ports

Orchestrates logic and defines the boundary between the domain and the outside world.

### Files:
- **GeoService.java**  
  Main application service. Handles requests, delegates to provider ports, transforms results into domain objects.

- **GeoProviderPort.java**  
  Hexagonal port defining the contract for external geolocation providers.  
  Implemented by adapters in the `infrastructure` layer.

---

## 3. infrastructure/ — External Integrations (Adapters)

Concrete implementations of provider ports.  
Handles HTTP calls, provider DTOs, and mapping.

### Key folders & files:

#### /client
- **IpApiClient.java** – Calls ip-api.com (IP → geolocation)
- **OpenMeteoClient.java** – Calls Open-Meteo (city search)
- **NominatimClient.java** – Calls OSM/Nominatim (reverse geocoding)

All clients use RestTemplate and are isolated from business logic.

#### /dto
Provider-specific data transfer objects representing external JSON responses.

#### /mapper
- Converts provider DTOs into domain `GeoLocationData`.
- Ensures infrastructure concerns never leak outside this layer.

---

## 4. presentation/ — HTTP Layer (Controllers & API Models)

Exposes the REST endpoints.  
Maps incoming HTTP requests to application services.

### Files:
- **GeoController.java**  
  REST controller for IP lookup, coordinate search, and city lookup.  
  Contains no business logic.

#### /response  
API response DTOs returned to clients.  
Separate from domain models and provider DTOs.

#### /exception  
Global exception handlers and standardized error formats.

---

## 5. test/ — Complete Test Suite

Combination of unit, integration, and controller tests.

### Files & folders:
- **GeoControllerTest.java** – Tests controller layer via MockMvc.
- **WireMock stubs** – Simulate provider APIs for integration tests.
- **Provider client tests** – Validate mapping and HTTP behavior.

---

## 6. Application Entry

- **GeoLocateApiApplication.java**  
  The Spring Boot main entry point.

---

## 7. config/

Configuration beans such as RestTemplate configuration, timeouts, and message converters.

---

## Summary

- **domain** → core model  
- **application** → use cases + ports  
- **infrastructure** → provider clients, DTOs, mappings  
- **presentation** → controllers + API response  
- **test** → full coverage using MockMvc + WireMock  

This structure enforces separation of concerns and keeps the system maintainable, testable, and extensible.
