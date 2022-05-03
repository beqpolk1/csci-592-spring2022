DROP TABLE IF EXISTS artist;
CREATE TABLE artist(
    id UUID PRIMARY KEY NOT NULL,
    name VARCHAR(100) NOT NULL,
    starting_year INTEGER NOT NULL
);

DROP TABLE IF EXISTS track;
CREATE TABLE track(
    id UUID PRIMARY KEY NOT NULL,
    length INTEGER NOT NULL,
    name VARCHAR(100) NOT NULL,
    popularity INTEGER NOT NULL,
    rating_score INTEGER,
    rating_voters INTEGER
);

DROP TABLE IF EXISTS track_genre;
CREATE TABLE track_genre(
    track_fk UUID NOT NULL,
    genre VARCHAR(100) NOT NULL
);

DROP TABLE IF EXISTS track_artist;
CREATE TABLE track_artist(
    track_fk UUID NOT NULL,
    artist_fk UUID NOT NULL
);
