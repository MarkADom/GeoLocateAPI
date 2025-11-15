# 🌍 GeoLocateAPI
Unified, free geolocation service built with Java 17 + Spring Boot + Clean Architecture.

![Java](https://img.shields.io/badge/Java-17-007396?style=for-the-badge)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=for-the-badge)
![Gradle](https://img.shields.io/badge/Gradle-8.x-02303A?style=for-the-badge)
![Tests](https://img.shields.io/badge/Tests-JUnit5-blue?style=for-the-badge)

---

# Purpose

GeoLocateAPI provides a **simple, reliable and fully free** way to obtain geolocation data without relying on commercial APIs or paid subscriptions.

It exposes a clean REST interface for three essential operations:

- **IP → Location**
- **Coordinates → City/Country**
- **City Name → Coordinates**

The service acts as a unified layer over public, open-data providers (ip-api, OpenStreetMap/Nominatim, Open-Meteo), avoiding the friction of API keys, quotas and billing constraints.

The project is intentionally **stateless, lightweight and easy to integrate**, suitable for backend systems, mobile apps, internal tooling, analytics or prototyping.

---

# Features

- IP → Location lookup  
- Coordinates → Reverse geocoding  
- City → Coordinates search  
- Unified `GeoLocationData` domain model  
- Clean Architecture  
- Hexagonal provider integration  
- Global exception handling  
- WireMock integration tests  
- MockMvc controller tests  
- Auto-generated Swagger/OpenAPI documentation  

---

# Endpoints

## ▶ Search by IP  
```
GET /api/v1/geo/ip/{ip}
```

Example:
```
curl http://localhost:8080/api/v1/geo/ip/8.8.8.8
```

## ▶ Reverse Geocoding  
```
GET /api/v1/geo/coordinates?lat=40.0&lon=-8.0
```

Example:
```
curl http://localhost:8080/api/v1/geo/coordinates?lat=40.0&lon=-8.0
```

## ▶ Search by City Name
```
GET /api/v1/geo/city/{name}
```

Example:
```
curl http://localhost:8080/api/v1/geo/city/Lisbon
```

---

# Architecture

### Clean + Hexagonal Layout
```
src/
 ├─ domain/
 ├─ application/
 ├─ infrastructure/
 └─ presentation/
```

---

### Architecture Diagram

Rendered SVG: `architecture.svg`

Source (PlantUML): `architecture.puml`

---

# Setup

## 1. Clone the repository
```
git clone https://github.com/your-user/GeoLocateAPI.git
cd GeoLocateAPI
```

## 2. Run the application
```
./gradlew bootRun
```

## 3. Swagger / OpenAPI

Once the app is running:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `/v3/api-docs`

---

# Tests
```
./gradlew test
```

---

# Future Improvements

- Provider fallback  
- Rate limiting  
- API key authentication  
- CI pipeline + coverage badge  
- Dockerfile (optional)  

---

# About This Project

This service was originally built as part of my **backend engineering portfolio**, focusing on clean architecture, external API integration, structured testing and maintainability.

---

# Author

Developed by **Marco Domingues**.
Focused on clean, testable backend services that are easy to understand, extend and deploy.
