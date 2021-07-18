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

-- Song Youtube Link ---------------------------------------------------
INSERT INTO `song_youtube_link` (`song_id`, `song_type`, `youtube_link`, `time_sync_offset`) VALUES
    (1, 0, '0IYv2nrqOoU', 0),
    (2, 0, 'HRQEs4vOIrY', 12), (2, 1, 'KzCPmGrpduw', 0),
    (3, 0, 'YXGVpjqIopE', 12), (3, 1, 'GAcK-Vdnmr4', 0),
    (4, 0, '4_LYCZjRwa8', 0), (4, 1, 'xjM9COqImUo', 0),
    (5, 0, 'vLYtZsONlJ0', 0), (5, 1, '9WkfYsdBfmk', 0),
    (6, 0, 'e4OUt56aCiA', 6), (6, 1, 'ujljcUApLTc', 0),
    (7, 0, 'k4MbozP6Pns', 0),
    (8, 0, 'uw1ZSSS4hjE', 12),
    (9, 0, 'K1xwONwwBA0', 0), (9, 1, 'h01IRF940YQ', 0),
    (10, 0, 'zbnRfBGjeaw', 14), (10, 1, 'oOC3TsJqCTE', 0),
    (11, 0, '-oBzcJ3QXHk', 0), (11, 1, '9f70uXLUwe8', 0),
    (12, 0, 'pd7n1WRGPJI', 0),
    (13, 0, 'v7qisJ_KuYI', 3), (13, 1, 'oS_LFhle5s0', 0),
    (14, 0, '5uX_wPvkgu8', 0), (14, 1, 'yYWYEajSfqs', 0),
    (15, 0, 'gVZP5E9eGtU', 0), (15, 1, '5Q5jd2BzTSQ', 0),
    (16, 0, 'iD99hILPHCc', 0),
    (17, 0, '9ITALVXxjQI', 0),
    (18, 0, 'mwOYx4McUGY', 0),
    (19, 0, 'hyKBa3RNo4M', 11), (19, 1, '21wlcIvJByU', 0),
    (20, 0, 'Evy6eka1L1U', 0), (20, 1, 'IWxmyKdKyUY', 0),
    (21, 0, 'i9Gch8D-7As', 0),
    (22, 0, 'Ix0DCb84sUA', 0),
    (23, 0, 'S_IBk0RCsOo', 4), (23, 1, 'mibdyggVxHg', 0),
    (24, 0, 'a_rLFp-0bcM', 0), (24, 1, 'dcAzwgJ73iQ', 0),
    (25, 0, '1nwMMlxOT-c', 0), (25, 1, '0za_wxj_4yk', 0),
    (26, 0, 'eJFBHyX1GPk', 0), (26, 1, '2C4hGRI5mG0', 0),
    (27, 0, 'zyijd2ieQsw', 0),
    (28, 0, 'ILee9Ctb-rc', 0), (28, 1, 'GinWazECcuQ', 0),
    (29, 0, '1qmn-t17mlo', 0),
    (30, 0, 'a1ENnG-s630', 5), (30, 1, 'dcAzwgJ73iQ', 0),
    (31, 0, 'Yxj0QTk4gko', 0), (31, 1, 's2cTQ1L3eDU', 0),
    (32, 0, 'HxAOGaAjn34', 0), (32, 1, 'pZqB1I5a5Sc', 0),
    (33, 0, 'QnK_MYevy44', 0), (33, 1, 'vqfZRIOmE9c', 0),
    (34, 0, 'OZEwv_DM5-8', 0), (34, 1, '8czcwCQ2NDI', 0),
    (35, 0, 'njeOw1_F_LM', 0),
    (36, 0, '0arobqPg6rg', 0),
    (37, 0, 'BZOaR6M7KIs', 0), (37, 1, 'G4EDfCO89Fw', 0),
    (38, 0, 'CFo5GM6TNT4', 0), (38, 1, '0iO6DGYv3fY', 0),
    (39, 0, 'YtjroEaclss', 0),
    (40, 0, 'wMCoQaE0LvQ', 3), (40, 1, 'DmuDQGK4FuU', 0),
    (41, 0, 'Lw7PJRYXxQE', 0), (41, 1, 'I5ELQy3n_jk', 0),
    (42, 0, 'AMJ3fw_6Kxg', 0),
    (43, 0, 'vDxD4HwEFdY', 16), (43, 1, 'mYfSJWeeuOU', 0),
    (44, 0, 'lDC6hc0CO9I', 0), (44, 1, 'AJw5C6Ro53s', 0),
    (45, 0, '24lzLJpnrE8', 0), (45, 1, 'AUjLqtVsry8', 0),
    (46, 0, '_2_JfSpzDAM', 0), (46, 1, '2zWRFxmdPPg', 0),
    (47, 0, 'aM6NqDx2gIY', 0), (47, 1, 'EvHXakS8JJs', 0),
    (48, 0, 'QGh1EafzXV0', 0), (48, 1, 'mhCn1gOTBb0', 0),
    (49, 0, 'Lahh_UUfcPw', 0),
    (50, 0, 'WEWtyTm2foM', 0),
    (51, 0, 'cHBdzDJUl6U', 0),
    (52, 0, 'Ph0RBVbWLO8', 0),
    (53, 0, 'GUMgbZzO-oI', 0),
    (54, 0, 'ksUp-gW7gac', 0),
    (55, 0, 'uer7k-jkk78', 4),
    (56, 0, 'uUMXKouqzLY', 0),
    (57, 0, 'obX621oa9RM', 4),
    (58, 0, '04mTRdkJGcE', 0),
    (59, 0, '3shm_VBWi3s', 0),
    (60, 0, '7059Y9CLI6Q', 0),
    (61, 0, 'WTjciY2HyXo', 0),
    (62, 0, 't1EBBG27tp8', 0),
    (63, 0, 'YeOyMKbLEqU', 0),
    (64, 0, 'isls26FGUaA', 3),
    (65, 0, '9OVZ8cdX5cc', 0),
    (66, 0, 'oUfv67J6buI', 0),
    (67, 0, 'zVoJ1BSBDC4', 0),
    (68, 0, 'BnmJkdAslAI', 0),
    (69, 0, 'gX1Q_Wc8H50', 0),
    (70, 0, 'RxB7SnRLIlU', 9),
    (71, 0, 'dbVPeHTNv4s', 0),
    (72, 0, 'IPiIAyy89j4', 0),
    (73, 0, '5UNuBy2s30o', 0),
    (74, 0, 'kh7Sjlsk7eo', 0),
    (75, 0, 'Qv8GGLZ_AWo', 0),
    (76, 0, 'aE6curPGQRY', 4),
    (77, 0, 'OdRmH3ljKlM', 0),
    (78, 0, 'HArmaWk_hvo', 0),
    (79, 0, 'fLsUuw0KiUc', 0),
    (80, 0, 'RaFn32xZ-NI', 4),
    (81, 0, 'KrgFe6RmV3s', 0),
    (82, 0, 'odXvp_rP62s', 0),
    (83, 0, '9GUqqRzIZgw', 12),
    (84, 0, '_t31K3lXg2s', 0),
    (85, 0, 'xdVCxKWsMJw', 0),
    (86, 0, '8T-vlqvxJOM', 0),
    (87, 0, 'IHioL6G7GLw', 0);

-- Question ------------------------------------------------------------
INSERT INTO `question` (`id`, `song_id`, `song_type`, `answer_location_in_second`, `difficulty`, `question_info`) VALUES
    (NEXT VALUE FOR question_seq, 1, 'AR',  0, 'MEDIUM', 'Intro of Introducing the Candy'),
    (NEXT VALUE FOR question_seq, 1, 'AR', 21, 'HARD', '도시도솔, 따단, Introducing the Candy'),
    (NEXT VALUE FOR question_seq, 1, 'AR', 26, 'MEDIUM', '시 레 도시도 솔파미도 Introducing the Candy'),
    (NEXT VALUE FOR question_seq, 1, 'AR', 48, 'MEDIUM', '마지막 부분, 시도레 도레 미 시 Introducing the Candy'),

    (NEXT VALUE FOR question_seq, 2, 'AR', 24, 'MEDIUM',  '굴리다 Candy Jelly Love'),
    (NEXT VALUE FOR question_seq, 2, 'MR', 60, 'HARD', '캔디 젤리 럽~ 배경 인스트 of Candy Jelly Love'),
    (NEXT VALUE FOR question_seq, 2, 'MR', 95, 'MEDIUM',   '행복이 번져~ 이후 인스트 of Candy Jelly Love');

-- QuizSet -------------------------------------------------------------
INSERT INTO `quiz_set` (`id`, `user_id`, `title`, `description`, `ready_made`, `average_difficulty`) VALUES
    (NEXT VALUE FOR quiz_set_seq, 1, '캔디, 젤리, 러브', '첫 번째 앨범 [Girls'' Invasion]의 수록곡으로 구성된 퀴즈 셋', 1, null);

-- QuizSet QuestionPool ------------------------------------------------
INSERT INTO `quiz_set_question_pool` (`quiz_set_id`, `question_pool_id`) VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (1, 4),
    (1, 5),
    (1, 6),
    (1, 7);
