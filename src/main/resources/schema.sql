CREATE TABLE "film" (
  "film_id" int PRIMARY KEY,
  "name" varchar,
  "description" varchar,
  "release_date" date,
  "duration" int,
  "rating_id" int
);

CREATE TABLE "genre" (
  "genre_id" int PRIMARY KEY,
  "name" varchar
);

CREATE TABLE "rating" (
  "rating_id" int PRIMARY KEY,
  "name" varchar
);

CREATE TABLE "film_user_like" (
  "like_id" int PRIMARY KEY,
  "film_id" int,
  "user_id" int
);

CREATE TABLE "film_genre" (
  "film_genre_id" int PRIMARY KEY,
  "film_id" int,
  "genre_id" int
);

CREATE TABLE "user" (
  "user_id" int PRIMARY KEY,
  "email" varchar,
  "name" varchar,
  "login" varchar,
  "birthday" date
);

CREATE TABLE "friend_status" (
  "friend_status_id" int PRIMARY KEY,
  "user_id" int,
  "friend_id" int,
  "status" boolean
);

ALTER TABLE "film" ADD FOREIGN KEY ("rating_id") REFERENCES "rating" ("rating_id");

ALTER TABLE "film_genre" ADD FOREIGN KEY ("genre_id") REFERENCES "genre" ("genre_id");

ALTER TABLE "film_user_like" ADD FOREIGN KEY ("film_id") REFERENCES "film" ("film_id");

ALTER TABLE "film_genre" ADD FOREIGN KEY ("film_id") REFERENCES "film" ("film_id");

ALTER TABLE "film_user_like" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("user_id");

ALTER TABLE "friend_status" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("user_id");

ALTER TABLE "friend_status" ADD FOREIGN KEY ("friend_id") REFERENCES "user" ("user_id");
