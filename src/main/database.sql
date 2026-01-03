DROP TABLE IF EXISTS rating CASCADE;
DROP TABLE IF EXISTS mediaentry_genre CASCADE;
DROP TABLE IF EXISTS mediaentry CASCADE;
DROP TABLE IF EXISTS genre CASCADE;
DROP TABLE IF EXISTS mrp_user CASCADE;
DROP TABLE IF EXISTS favorite CASCADE;
DROP TABLE IF EXISTS profile CASCADE;
DROP TABLE IF EXISTS likes CASCADE;

CREATE TABLE mrp_user (
    userid SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE profile (
    profileid SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    favoritegenre VARCHAR(255) DEFAULT '//no favorite genre yet',
    totalratings INTEGER DEFAULT 0,
    avgscore INTEGER DEFAULT 0,
    userid INT REFERENCES mrp_user(userid) ON DELETE CASCADE
);

CREATE TABLE mediaentry (
    mediaentryid SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    media_type VARCHAR(50),
    release_year INTEGER,
    age_restriction INTEGER,
    creator INT REFERENCES mrp_user(userid) ON DELETE CASCADE
);


CREATE TABLE rating (
    ratingid SERIAL PRIMARY KEY,
    mediaentryid INT REFERENCES mediaentry(mediaentryid) ON DELETE CASCADE,
    creator INT REFERENCES mrp_user(userid),
    stars INTEGER CHECK (stars BETWEEN 1 AND 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    confirmed BOOLEAN DEFAULT false
);


CREATE TABLE genre (
    genreid SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE mediaentry_genre (
    mediaentryid INT REFERENCES mediaentry(mediaentryid) ON DELETE CASCADE,
    genreid INT REFERENCES genre(genreid) ON DELETE CASCADE,
    PRIMARY KEY (mediaentryid, genreid)
);

CREATE TABLE favorite (
    userid INT REFERENCES mrp_user(userid) ON DELETE CASCADE,
    mediaentryid INT REFERENCES mediaentry(mediaentryid) ON DELETE CASCADE,
    PRIMARY KEY (userid, mediaentryid)
);

CREATE TABLE likes (
    userid INT REFERENCES mrp_user(userid) ON DELETE CASCADE,
    ratingid INT REFERENCES rating(ratingid) ON DELETE CASCADE,
    PRIMARY KEY (userid, ratingid)
);

