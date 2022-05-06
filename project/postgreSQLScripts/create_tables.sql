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
    genre VARCHAR(100) NOT NULL,
    FOREIGN KEY(track_fk) REFERENCES track(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS track_artist;
CREATE TABLE track_artist(
    track_fk UUID NOT NULL,
    artist_fk UUID NOT NULL,
    PRIMARY KEY(track_fk, artist_fk),
    FOREIGN KEY(track_fk) REFERENCES track(id) ON DELETE NO ACTION,
    FOREIGN KEY(artist_fk) REFERENCES artist(id) ON DELETE NO ACTION
);

DROP TABLE IF EXISTS album;
CREATE TABLE album(
    id UUID PRIMARY KEY NOT NULL,
    name VARCHAR(100) NOT NULL,
    popularity INTEGER NOT NULL,
    release_year INTEGER NOT NULL
);

DROP TABLE IF EXISTS artist_album;
CREATE TABLE artist_album(
    artist_fk UUID NOT NULL,
    album_fk UUID NOT NULL,
    PRIMARY KEY(artist_fk, album_fk),
    FOREIGN KEY(artist_fk) REFERENCES artist(id) ON DELETE NO ACTION,
    FOREIGN KEY(album_fk) REFERENCES album(id) ON DELETE NO ACTION
);

DROP TABLE IF EXISTS album_track;
CREATE TABLE album_track(
    album_fk UUID NOT NULL,
    track_fk UUID NOT NULL,
    track_number INTEGER,
    PRIMARY KEY(album_fk, track_fk),
    FOREIGN KEY(album_fk) REFERENCES album(id) ON DELETE NO ACTION,
    FOREIGN KEY(track_fk) REFERENCES track(id) ON DELETE NO ACTION
);

DROP TABLE IF EXISTS album_format;
CREATE TABLE album_format(
    album_fk UUID NOT NULL,
    format VARCHAR(100) NOT NULL,
    FOREIGN KEY(album_fk) REFERENCES album(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS album_genre;
CREATE TABLE album_genre(
    album_fk UUID NOT NULL,
    genre VARCHAR(100),
    FOREIGN KEY(album_fk) REFERENCES album(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS review;
CREATE TABLE review(
    album_fk UUID NOT NULL,
    journalist VARCHAR(100) NOT NULL,
    rank VARCHAR(100) NOT NULL,
    stars INTEGER NOT NULL,
    medium_name VARCHAR(100),
    medium_type VARCHAR(100),
    medium_url VARCHAR(100),
    FOREIGN KEY(album_fk) REFERENCES album(id) ON DELETE CASCADE
);
