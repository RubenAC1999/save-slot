# Architecture

## Overview
SaveSlot is an API REST build in Java 21 and Spring Boot 4 following a monolithic architecture organized by features.

## Why a Monolith
This will be a personal project with frequently and strong transactions between domains. 

If the application grows and a service requires a specific mantain, the feature-based structure makes more easy to migrate the architecture to a more complex one.

## Architecture Pattern 
The project follows a **layered architecture organized by features** instead of technical layers.

Each feature contains all its related clases:
- Controller -> Handle HTTP requests
- Service -> Store business logic
- Repository -> Access Data
- Model -> Define entities
- DTO -> Data Transfer Objects
- Mapper -> Converts between entities and DTOs.

## Project Structure
```
src/main/java/com/saveslot/
├── user/
│   ├── UserController.java
│   ├── UserService.java
│   ├── UserRepository.java
│   ├── User.java
│   ├── UserDTO.java
│   └── UserMapper.java
├── game/
└── usergame/
```

## Tech Stack
- **Language:** Java 21
- **Framework:** Spring Boot, Spring Security
- **Database:** PostgreSQL
- **ORM:** Spring Data JPA
- **Migrations:** Flyway
- **Containerization:** Docker
- **External API:** RAWG
- **CI/CD:** GitHub Actions
- **Documentation:** Swagger / OpenAPI

## External API Integration
Game data is fetched from the RAWG API. Only the minimum data is 
stored locally (rawg_id, title, cover_image_url) to avoid redundancy 
and keep data up to date.