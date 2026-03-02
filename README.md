# SaveSlot
A backend application for gamers who love to complete and collect games, not just play them.

## Table of contents
- General info
- Setup
- Features
- Technologies
- Status

## General info
SaveSlot is a personal project inspired by Letterboxd, but focused on videogames with a twist: 
it's built for **completionists and collectors**.

Unlike other tracking apps, Gameboxd focuses on the **completion experience**. Users can track 
not just whether they played a game, but *how* they completed it: main story, 100%, or full 
platinum. Completed games are displayed with special visual indicators (platinum frame, gold 
border, etc.) turning your game list into a real collection showcase.

## Setup
TODO - Instructions will be added soon.

## Key features
- User authentication (sign up / log in)
- Search and explore games (powered by RAWG API)
- Personal play log with status (Playing, Completed, Dropped, Wishlist)
- Completion levels: Story, 100%, Platinum
- Rating and reviews
- Customizable lists
- Follow other users and see their activity
- Achievements and badges for milestones (first platinum, completed a full saga, etc.)

## Mobile MVP (Prototype)
Before starting this Java/Spring backend project, I built a **mobile MVP using Kotlin and 
Jetpack Compose**.
The application includes the core features mentioned above.

This project is an evolution of that prototype, focused on what I enjoy the most: 
**backend development**.

## Screenshots from the mobile prototype

### Login
![Login](./screenshots/login.png)

### Home Screen
![Home Screen](./screenshots/HomeScreen.png)

### Game Information
![Game Information](./screenshots/GameInformation.png)

### User list example
![User List](./screenshots/ListExample.png)

## Technologies
- **Language:** Java 17
- **Framework:** Spring Boot, Spring Security
- **Database:** PostgreSQL
- **ORM:** Spring Data JPA
- **Migrations:** Flyway
- **Containerization:** Docker
- **External API:** RAWG
- **CI/CD:** GitHub Actions
- **Documentation:** Swagger / OpenAPI

## Status
This project is in early development.

- [x] Define project scope and README structure
- [x] Define MVP features
- [x] Choose architecture and core technologies
- [x] Design domain entities
- [ ] Set up Spring Boot project base
- [ ] Implement user authentication (JWT)
- [ ] Implement game search via RAWG API
- [ ] Implement user game list with statuses
- [ ] Implement completion levels
- [ ] Deploy to Railway/Render
