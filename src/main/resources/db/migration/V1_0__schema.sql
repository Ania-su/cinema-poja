
CREATE EXTENSION IF NOT EXISTS "pgcrypto";


CREATE TYPE genre AS ENUM (
    'THRILLER',
    'ROMANCE',
    'COMEDY',
    'DRAMA',
    'ACTION',
    'SCI_FI',
    'FANTASY',
    'ANIMATION'
);

CREATE TYPE reservation_status AS ENUM (
    'PENDING',
    'SUCCESS',
    'CANCELED'
);

CREATE TYPE user_role AS ENUM (
    'CLIENT',
    'EMPLOYEE',
    'MANAGER'
);


CREATE TABLE room (
    id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    number   VARCHAR(50) NOT NULL,
    capacity INT NOT NULL CHECK (capacity > 0)
);


CREATE TABLE seat (
    id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    number  VARCHAR(50) NOT NULL,
    room_id UUID NOT NULL REFERENCES room(id) ON DELETE CASCADE,

    CONSTRAINT uq_seat_room_number UNIQUE (room_id, number)
);

CREATE INDEX idx_seat_room_id ON seat(room_id);


CREATE TABLE movie (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    duration    INTERVAL NOT NULL
);


CREATE TABLE movie_genre (
    movie_id UUID NOT NULL REFERENCES movie(id) ON DELETE CASCADE,
    genre    genre NOT NULL,

    PRIMARY KEY (movie_id, genre)
);


CREATE TABLE projection (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    datetime   TIMESTAMPTZ NOT NULL,
    seat_price NUMERIC(10,2) NOT NULL CHECK (seat_price >= 0),

    movie_id   UUID NOT NULL REFERENCES movie(id) ON DELETE CASCADE,
    room_id    UUID NOT NULL REFERENCES room(id) ON DELETE RESTRICT
);

CREATE INDEX idx_projection_movie_id ON projection(movie_id);
CREATE INDEX idx_projection_room_id ON projection(room_id);


CREATE TABLE app_user (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    birthdate  DATE NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    phone      VARCHAR(30),
    role       user_role NOT NULL
);


CREATE TABLE reservation (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    status        reservation_status NOT NULL DEFAULT 'PENDING',

    projection_id UUID NOT NULL REFERENCES projection(id) ON DELETE CASCADE,
    client_id     UUID NOT NULL REFERENCES app_user(id) ON DELETE RESTRICT,
    employee_id   UUID REFERENCES app_user(id) ON DELETE SET NULL
);



CREATE TABLE reservation_seat (
    reservation_id UUID NOT NULL REFERENCES reservation(id) ON DELETE CASCADE,
    seat_id        UUID NOT NULL REFERENCES seat(id) ON DELETE RESTRICT,

    PRIMARY KEY (reservation_id, seat_id)
);
