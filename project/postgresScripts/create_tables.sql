DROP TABLE IF EXISTS track_artist;
DROP TABLE IF EXISTS artist_album;
DROP TABLE IF EXISTS album_track;
DROP TABLE IF EXISTS track_genre;
DROP TABLE IF EXISTS album_format;
DROP TABLE IF EXISTS album_genre;
DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS artist;
DROP TABLE IF EXISTS track;
DROP TABLE IF EXISTS album;

CREATE TABLE artist(
    id VARCHAR(32) PRIMARY KEY NOT NULL,
    name VARCHAR(100) NOT NULL,
    starting_year INTEGER NOT NULL
);

CREATE TABLE track(
    id VARCHAR(32) PRIMARY KEY NOT NULL,
    length INTEGER NOT NULL,
    name VARCHAR(100) NOT NULL,
    popularity INTEGER NOT NULL,
    rating_score INTEGER,
    rating_voters INTEGER
);

CREATE TABLE track_genre(
    track_fk VARCHAR(32) NOT NULL,
    genre VARCHAR(100) NOT NULL,
    FOREIGN KEY(track_fk) REFERENCES track(id) ON DELETE CASCADE
);

CREATE TABLE track_artist(
    track_fk VARCHAR(32) NOT NULL,
    artist_fk VARCHAR(32) NOT NULL,
    PRIMARY KEY(track_fk, artist_fk),
    FOREIGN KEY(track_fk) REFERENCES track(id) ON DELETE NO ACTION,
    FOREIGN KEY(artist_fk) REFERENCES artist(id) ON DELETE NO ACTION
);

CREATE TABLE album(
    id VARCHAR(32) PRIMARY KEY NOT NULL,
    name VARCHAR(100) NOT NULL,
    popularity INTEGER NOT NULL,
    release_year INTEGER NOT NULL
);

CREATE TABLE artist_album(
    artist_fk VARCHAR(32) NOT NULL,
    album_fk VARCHAR(32) NOT NULL,
    PRIMARY KEY(artist_fk, album_fk),
    FOREIGN KEY(artist_fk) REFERENCES artist(id) ON DELETE NO ACTION,
    FOREIGN KEY(album_fk) REFERENCES album(id) ON DELETE NO ACTION
);

CREATE TABLE album_track(
    album_fk VARCHAR(32) NOT NULL,
    track_fk VARCHAR(32) NOT NULL,
    track_number INTEGER,
    PRIMARY KEY(album_fk, track_fk),
    FOREIGN KEY(album_fk) REFERENCES album(id) ON DELETE NO ACTION,
    FOREIGN KEY(track_fk) REFERENCES track(id) ON DELETE NO ACTION
);

CREATE TABLE album_format(
    album_fk VARCHAR(32) NOT NULL,
    format VARCHAR(100) NOT NULL,
    FOREIGN KEY(album_fk) REFERENCES album(id) ON DELETE CASCADE
);

CREATE TABLE album_genre(
    album_fk VARCHAR(32) NOT NULL,
    genre VARCHAR(100),
    FOREIGN KEY(album_fk) REFERENCES album(id) ON DELETE CASCADE
);

CREATE TABLE review(
    album_fk VARCHAR(32) NOT NULL,
    journalist VARCHAR(100) NOT NULL,
    rank VARCHAR(100) NOT NULL,
    stars INTEGER NOT NULL,
    medium_name VARCHAR(100),
    medium_type VARCHAR(100),
    medium_url VARCHAR(100),
    FOREIGN KEY(album_fk) REFERENCES album(id) ON DELETE CASCADE
);
