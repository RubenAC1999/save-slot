# Endpoints

## Authorization (JWT)
Authorization endpoints to register or login an user using Json Web Token.
- POST /auth/register
- POST /auth/login

## User/Profile
Endpoints to interact with user (see collection, add games to the library...)

- GET /me/library -> See the complete library
- GET /{userId}/library -> See other's complete library
- PUT /me/library/{gameId} -> Add a game status to the list.
- DELETE /me/library/{gameId} -> Delete a game from the list.
- GET /me/library/{gameId} -> See a specific game status in the list.
- GET /me/library/search?name={name} -> Search a game by name
- GET /me/library?status=completed -> See all completed games
- GET /me/library?status=playing -> See all playing games
- GET /me/library?status=dropped -> See all dropped games
- GET /me/wishlist -> See all games from wishlist
- GET /{userId}/wishlist -> See other's wishlist.


## Games
Endpoints to see games information

- GET /games/{gameId} -> See game information
- GET /games/search?name={name} -> Search a game by its name.



