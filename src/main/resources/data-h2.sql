-- User -------------------------------------------------------------
-- password : verysimplepassword
INSERT INTO `user` (`id`, `name`, `password`, `enabled`) VALUES
    (NEXT VALUE FOR user_seq, 'name1', '$2a$10$KAgt9R2EsBJsOuoGPkx7TeYg6wTqlIYJA8.gEtr1vt1.tzVLQ6nSG', 1);
INSERT INTO `user_roles` (`user_id`, `user_roles`) VALUES
    (1, 'ADMIN');

-- Album ------------------------------------------------------------
INSERT INTO `album` (`album_name`, `released_date`, `album_cover`) VALUES
    ('Girls'' Invasion', '2014-11-17', '/img/album-cover/girls_invasion.png'),
    ('Hi~', '2015-03-03', '/img/album-cover/hi.png'),
    ('Lovelyz8', '2015-10-01', '/img/album-cover/lovelyz8.png'),
    ('Lovelinus', '2015-12-07', '/img/album-cover/lovelinus.png'),
    ('A New Trilogy', '2016-04-25', '/img/album-cover/ANT.png'),
    ('R U Ready?', '2017-02-26', '/img/album-cover/RUR.png'),
    ('Now, We', '2017-05-02', '/img/album-cover/now-we.png'),
    ('Fall in Lovelyz', '2017-11-14', '/img/album-cover/Fall-in-L.png'),
    ('治癒 (치유)', '2018-04-23', '/img/album-cover/Heal.png');

-- Song ------------------------------------------------------------
INSERT INTO `song` (`song_name`, `album_id`) VALUES
    ('Introducing the Candy', 1),
    ('Candy Jelly Love', 1),
    ('어제처럼 굿나잇', 1),
    ('이별 Chapter 1', 1),
    ('비밀여행', 1),
    ('놀이공원', 2),
    ('Welcome to the Lovelyz8', 3),
    ('治癒 (치유)', 9);

-- Question ------------------------------------------------------------
INSERT INTO `question` (`id`, `song_id`, `answer_location_in_second`, `difficulty`, `question_info`) VALUES
    (NEXT VALUE FOR question_seq, 1, 3, 'EASY',   'Intro #1 of Introducing the Candy'),
    (NEXT VALUE FOR question_seq, 1, 5, 'MEDIUM', 'Outro #2 of Introducing the Candy'),
    (NEXT VALUE FOR question_seq, 2, 1, 'EASY',   'Intro #1 of Candy Jelly Love'),
    (NEXT VALUE FOR question_seq, 2, 4, 'MEDIUM', 'Intro #2 of Candy Jelly Love'),
    (NEXT VALUE FOR question_seq, 2, 7, 'HARD',   'Outro #1 of Candy Jelly Love');

