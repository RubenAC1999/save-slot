-- games TABLE --
CREATE TABLE games(
    id UUID PRIMARY KEY,
    rawg_id INT UNIQUE NOT NULL,
    title VARCHAR(100) NOT NULL,
    cover_image_url VARCHAR(255) NOT NULL UNIQUE,
    synopsis TEXT NOT NULL,
    company VARCHAR(50) NOT NULL
);

-- user_games TABLE --
CREATE TABLE user_games (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    game_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('PLAYING', 'COMPLETED', 'WISHLIST', 'DROPPED', 'MASTERED')),
    rating DECIMAL(3, 2) CHECK (rating >= 0.00 AND rating <= 5.00),
    review_text TEXT,
    completion_percentage INT CHECK(completion_percentage >= 0 and completion_percentage <= 100),
    completion_date DATE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_game_id FOREIGN KEY (game_id) REFERENCES games(id)
);