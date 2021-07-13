-- User -------------------------------------------------------------
-- password : verysimplepassword
INSERT INTO user (id, name, password, enabled) VALUES (USER_SEQ.next_val, 'myName1', '$2a$10$KAgt9R2EsBJsOuoGPkx7TeYg6wTqlIYJA8.gEtr1vt1.tzVLQ6nSG', 1);
INSERT INTO user_roles (user_id, user_roles) VALUES (1, 'ADMIN');

-- Album ------------------------------------------------------------
INSERT INTO album (album_name, released_date, album_cover) VALUES ('Girls'' Invasion', '2014-11-17', '/img/album-cover/girls_invasion.png');
INSERT INTO album (album_name, released_date, album_cover) VALUES ('Hi~', '2015-03-03', '/img/album-cover/hi.png');
INSERT INTO album (album_name, released_date, album_cover) VALUES ('Lovelyz8', '2015-10-01', '/img/album-cover/lovelyz8.png');
INSERT INTO album (album_name, released_date, album_cover) VALUES ('Lovelinus', '2015-12-07', '/img/album-cover/lovelinus.png');
INSERT INTO album (album_name, released_date, album_cover) VALUES ('A New Trilogy', '2016-04-25', '/img/album-cover/ANT.png');
INSERT INTO album (album_name, released_date, album_cover) VALUES ('R U Ready?', '2017-02-26', '/img/album-cover/RUR.png');
INSERT INTO album (album_name, released_date, album_cover) VALUES ('Now, We', '2017-05-02', '/img/album-cover/now-we.png');
INSERT INTO album (album_name, released_date, album_cover) VALUES ('Fall in Lovelyz', '2017-11-14', '/img/album-cover/Fall-in-L.png');
INSERT INTO album (album_name, released_date, album_cover) VALUES ('治癒 (치유)', '2018-04-23', '/img/album-cover/Heal.png');

-- Song ------------------------------------------------------------
INSERT INTO song (song_name, album_id) VALUES ('Introducing the Candy', 1);
INSERT INTO song (song_name, album_id) VALUES ('Candy Jelly Love', 1);
INSERT INTO song (song_name, album_id) VALUES ('어제처럼 굿나잇', 1);
INSERT INTO song (song_name, album_id) VALUES ('이별 Chapter 1', 1);
INSERT INTO song (song_name, album_id) VALUES ('비밀여행', 1);
INSERT INTO song (song_name, album_id) VALUES ('놀이공원', 2);
INSERT INTO song (song_name, album_id) VALUES ('Welcome to the Lovelyz8', 3);
INSERT INTO song (song_name, album_id) VALUES ('治癒 (치유)', 9);

-- Question ------------------------------------------------------------
INSERT INTO question (id, song_id, answer_location_in_second, difficulty, question_info) VALUES (question_seq.nextval, 1, 3, 'EASY',   'Intro #1 of Introducing the Candy');
INSERT INTO question (id, song_id, answer_location_in_second, difficulty, question_info) VALUES (question_seq.nextval, 1, 5, 'MEDIUM', 'Outro #2 of Introducing the Candy');
INSERT INTO question (id, song_id, answer_location_in_second, difficulty, question_info) VALUES (question_seq.nextval, 2, 1, 'EASY',   'Intro #1 of Candy Jelly Love');
INSERT INTO question (id, song_id, answer_location_in_second, difficulty, question_info) VALUES (question_seq.nextval, 2, 4, 'MEDIUM', 'Intro #2 of Candy Jelly Love');
INSERT INTO question (id, song_id, answer_location_in_second, difficulty, question_info) VALUES (question_seq.nextval, 2, 7, 'HARD',   'Outro #1 of Candy Jelly Love');

