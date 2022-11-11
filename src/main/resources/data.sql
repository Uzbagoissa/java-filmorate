/*DELETE FROM FILM_GENRE;
DELETE FROM FILM_USER_LIKE;
DELETE FROM FRIEND_STATUS;
DELETE FROM USERS;
DELETE FROM FILMS;

ALTER TABLE USERS ALTER COLUMN USER_ID RESTART WITH 1;
ALTER TABLE FILMS ALTER COLUMN FILM_ID RESTART WITH 1;
ALTER TABLE FRIEND_STATUS ALTER COLUMN FRIEND_STATUS_ID RESTART WITH 1;
ALTER TABLE FILM_USER_LIKE ALTER COLUMN LIKE_ID RESTART WITH 1;
ALTER TABLE FILM_GENRE ALTER COLUMN FILM_GENRE_ID RESTART WITH 1;*/

MERGE INTO public.MPA (MPA_ID, NAME) VALUES (1, 'G');
MERGE INTO public.MPA (MPA_ID, NAME) VALUES (2, 'PG');
MERGE INTO public.MPA (MPA_ID, NAME) VALUES (3, 'PG-13');
MERGE INTO public.MPA (MPA_ID, NAME) VALUES (4, 'R');
MERGE INTO public.MPA (MPA_ID, NAME) VALUES (5, 'NC-17');

MERGE INTO public.GENRE (GENRE_ID, NAME) VALUES (1, 'Комедия');
MERGE INTO public.GENRE (GENRE_ID, NAME) VALUES (2, 'Драма');
MERGE INTO public.GENRE (GENRE_ID, NAME) VALUES (3, 'Мультфильм');
MERGE INTO public.GENRE (GENRE_ID, NAME) VALUES (4, 'Триллер');
MERGE INTO public.GENRE (GENRE_ID, NAME) VALUES (5, 'Документальный');
MERGE INTO public.GENRE (GENRE_ID, NAME) VALUES (6, 'Боевик');



