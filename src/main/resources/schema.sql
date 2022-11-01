CREATE TABLE IF NOT EXISTS film (
  film_id int PRIMARY KEY,
  name varchar,
  description varchar,
  release_date date,
  duration int,
  rating_id int
);

CREATE TABLE IF NOT EXISTS genre (
  genre_id int PRIMARY KEY,
  name varchar
);

CREATE TABLE IF NOT EXISTS rating (
  rating_id int PRIMARY KEY,
  name varchar
);

CREATE TABLE IF NOT EXISTS film_user_like (
  like_id int PRIMARY KEY,
  film_id int,
  user_id int
);

CREATE TABLE IF NOT EXISTS film_genre (
  film_genre_id int PRIMARY KEY,
  film_id int,
  genre_id int
);

CREATE TABLE IF NOT EXISTS userr (
  user_id int PRIMARY KEY,
  email varchar,
  name varchar,
  login varchar,
  birthday date
);

CREATE TABLE IF NOT EXISTS friend_status (
  friend_status_id int PRIMARY KEY,
  user_id int,
  friend_id int,
  status boolean
);

ALTER TABLE film ADD CONSTRAINT IF NOT EXISTS rating_id FOREIGN KEY (rating_id) REFERENCES rating (rating_id);

ALTER TABLE film_genre ADD CONSTRAINT IF NOT EXISTS genre_id FOREIGN KEY (genre_id) REFERENCES genre (genre_id);

ALTER TABLE film_user_like ADD CONSTRAINT IF NOT EXISTS film_id FOREIGN KEY (film_id) REFERENCES film (film_id);

ALTER TABLE film_genre ADD CONSTRAINT IF NOT EXISTS film_id1 FOREIGN KEY (film_id) REFERENCES film (film_id);

ALTER TABLE film_user_like ADD CONSTRAINT IF NOT EXISTS user_id FOREIGN KEY (user_id) REFERENCES userr (user_id);

ALTER TABLE friend_status ADD CONSTRAINT IF NOT EXISTS user_id1 FOREIGN KEY (user_id) REFERENCES userr (user_id);

ALTER TABLE friend_status ADD CONSTRAINT IF NOT EXISTS friend_id FOREIGN KEY (friend_id) REFERENCES userr (user_id);
