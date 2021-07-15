-- User -------------------------------------------------------------
-- password : verysimplepassword
INSERT INTO `user` (`id`, `name`, `password`, `enabled`) VALUES
    (NEXT VALUE FOR user_seq, 'name1', '$2a$10$KAgt9R2EsBJsOuoGPkx7TeYg6wTqlIYJA8.gEtr1vt1.tzVLQ6nSG', 1);
INSERT INTO `user_roles` (`user_id`, `user_roles`) VALUES
    (1, 'ADMIN');

-- Album ------------------------------------------------------------
INSERT INTO `album` (`album_name`, `released_date`, `album_cover`) VALUES
/*  1 */ ('Lovelyz 1st Album [Girls'' Invasion]', '2014-11-17', '/img/album-cover/girls_invasion.png'),
/*  2 */ ('Lovelyz 1st Album Repackage [Hi~]', '2015-03-03', '/img/album-cover/hi.png'),
/*  3 */ ('Lovelyz 1st Mini Album [Lovelyz8]', '2015-10-01', '/img/album-cover/lovelyz8.png'),
/*  4 */ ('Lovelyz 1st Single Album [Lovelinus]', '2015-12-07', '/img/album-cover/lovelinus.png'),
/*  5 */ ('Lovelyz 2nd Mini Album [A New Trilogy]', '2016-04-25', '/img/album-cover/ANT.png'),
/*  6 */ ('Lovelyz 2nd Album [R U Ready?]', '2017-02-26', '/img/album-cover/RUR.png'),
/*  7 */ ('Lovelyz 2nd Album Repackage [지금, 우리]', '2017-05-02', '/img/album-cover/now-we.png'),
/*  8 */ ('Lovelyz 3rd Mini ALbum [Fall in Lovelyz]', '2017-11-14', '/img/album-cover/Fall-in-L.png'),
/*  9 */ ('Lovelyz 4th Mini Album [治癒(치유)]', '2018-04-23', '/img/album-cover/Heal.png'),
/* 10 */ ('Lovelyz Digital Single [여름 한 조각]', '2018-07-01', '/img/album-cover/Wagzak.png'),
/* 11 */ ('Lovelyz 5th Mini Album [SANCTUARY]', '2018-11-26', '/img/album-cover/Sanctuary.png'),
/* 12 */ ('Lovelyz 6th Mini Album [Once upon a time]', '2019-05-20', '/img/album-cover/Once_upon_a_time.png'),
/* 13 */ ('Kim Ji Yeon 1st Mini Album [Over and Over]', '2019-10-08', '/img/album-cover/over_and_over.png'),
/* 14 */ ('Ryu Su Jeong 1st Mini Album [Tiger Eyes]', '2020-05-20', '/img/album-cover/Tiger_eyes.png'),
/* 15 */ ('Lovelyz 7th Mini Album [Unforgettable]', '2020-09-01', '/img/album-cover/Unforgettable.png');

-- Song ------------------------------------------------------------
INSERT INTO `song` (`song_name`, `album_id`) VALUES
    -- 1
    ('Introducing the Candy', 1), ('Candy Jelly Love', 1), ('어제처럼 굿나잇', 1), ('이별 Chapter 1', 1),
    ('비밀여행', 1), ('남보다 못한 사이 (feat. 휘성) (Babysoul Solo)', 1),
    ('그녀는 바람둥이야 (feat. 동우 Of 인피니트) (Babysoul & Kei)', 1),
    ('Delight (Yoo Ji-Ae Solo)', 1), ('너만 없다 (JIN Solo)', 1),

    -- 10
    ('안녕(Hi~)', 2), ('놀이공원', 2),

    -- 12
    ('Welcome to the Lovelyz8', 3), ('Ah-Choo', 3), ('작별하나', 3), ('Hug Me', 3),
    ('예쁜 여자가 되는 법', 3), ('새콤달콤', 3), ('라푼젤', 3),

    -- 19
    ('그대에게', 4), ('Circle', 4), ('Bebe', 4),

    -- 22
    ('Moonrise', 5), ('Destiny (나의 지구)', 5), ('퐁당', 5), ('책갈피', 5),
    ('1cm', 5), ('마음 (*취급주의)', 5), ('인형', 5),

    -- 29
    ('R U Ready?', 6), ('WoW!', 6), ('Cameo', 6), ('Emotion', 6),
    ('새벽별 (Babysoul & Kei & JIN)', 6), ('첫눈', 6), ('똑똑', 6),
    ('The (Lee Mi-Joo & Ryu Su-Jeong & Jeong Ye-In)', 6),
    ('Night and Day', 6), ('숨바꼭질', 6),
    ('나의 연인 (Yoo Ji-Ae & Seo Ji-Soo)', 6),

    -- 40
    ('지금, 우리', 7), ('Aya', 7),

    -- 42
    ('Spotlight', 8), ('종소리', 8), ('삼각형', 8), ('그냥', 8),
    ('FALLIN''', 8), ('비밀정원', 8), ('졸린 꿈', 8),

    -- 49
    ('治癒(치유)', 9), ('그날의 너', 9), ('미묘미묘해', 9),
    ('Temptation', 9), ('수채화', 9), ('SHINING★STAR', 9),

    -- 55
    ('여름 한 조각', 10),

    -- 56
    ('Never Ending', 11), ('찾아가세요', 11), ('Like U', 11), ('Rewind', 11),
    ('Rain', 11), ('백일몽', 11), ('꽃점', 11),

    -- 63
    ('Once upon a time', 12), ('그 시절 우리가 사랑했던 우리 (Beautiful Days)', 12),
    ('Close To You', 12), ('Sweet Luv', 12), ('Secret Story', 12), ('LOVE GAME', 12),

    -- 69
    ('Back in the Day', 13), ('I Go', 13), ('Dreaming', 13),
    ('종이달', 13), ('Cry', 13), ('이 비(雨)', 13),

    -- 75
    ('Be Cautious', 14), ('Tiger Eyes', 14), ('CALL BACK', 14), ('너의 이름 (Your Name)', 14),
    ('42=', 14), ('나, 니 (NA, NI)', 14), ('자장가 (zz)', 14),

    -- 82
    ('Unforgettable', 15), ('Obliviate', 15), ('자각몽', 15),
    ('절대, 비밀', 15), ('이야기꽃', 15), ('걱정 인형', 15);

    -- 88

-- Question ------------------------------------------------------------
INSERT INTO `question` (`id`, `song_id`, `answer_location_in_second`, `difficulty`, `question_info`) VALUES
    (NEXT VALUE FOR question_seq, 1,  0, 'MEDIUM', 'Intro of Introducing the Candy'),
    (NEXT VALUE FOR question_seq, 1, 21, 'HARD', '도시도솔, 따단, Introducing the Candy'),
    (NEXT VALUE FOR question_seq, 1, 26, 'MEDIUM', '시 레 도시도 솔파미도 Introducing the Candy'),
    (NEXT VALUE FOR question_seq, 1, 48, 'MEDIUM', '마지막 부분, 시도레 도레 미 시 Introducing the Candy'),

    (NEXT VALUE FOR question_seq, 2, 24, 'MEDIUM',  '굴리다 Candy Jelly Love'),
    (NEXT VALUE FOR question_seq, 2, 60, 'HARD', '캔디 젤리 럽~ 배경 인스트 of Candy Jelly Love'),
    (NEXT VALUE FOR question_seq, 2, 95, 'MEDIUM',   '행복이 번져~ 이후 인스트 of Candy Jelly Love');

