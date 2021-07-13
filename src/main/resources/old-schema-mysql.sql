# create table album (
#                        id bigint not null auto_increment,
#                        album_cover varchar(255) not null,
#                        album_name varchar(60) not null,
#                        released_date date not null,
#                        primary key (id)
# ) engine=InnoDB;
#
# create table audio_file (
#                             uuid BINARY(16) not null,
#                             content_id varchar(255),
#                             content_length bigint,
#                             difficulty bigint not null,
#                             mime_type varchar(20) not null,
#                             question_id bigint,
#                             audio_list_key bigint,
#                             primary key (uuid)
# ) engine=InnoDB;
#
# create table question (
#                           id bigint not null auto_increment,
#                           difficulty varchar(10) default 'EASY' not null,
#                           play_location_in_second bigint not null,
#                           question_info varchar(255),
#                           song_id bigint,
#                           primary key (id)
# ) engine=InnoDB;
#
# create table quiz (
#                       id bigint not null auto_increment,
#                       created_date datetime,
#                       last_modified_date datetime,
#                       closed bit,
#                       hint_token_used tinyblob,
#                       question_num bigint,
#                       user_id bigint,
#                       quiz_set_id bigint,
#                       primary key (id)
# ) engine=InnoDB;
#
# create table quiz_hint_map (
#                                quiz_id bigint not null,
#                                hint_map bigint,
#                                hint_map_key bigint not null,
#                                primary key (quiz_id, hint_map_key)
# ) engine=InnoDB;
#
# create table quiz_question_list (
#                                     quiz_id bigint not null,
#                                     question_list_id bigint not null
# ) engine=InnoDB;
#
# create table quiz_response_map (
#                                    quiz_id bigint not null,
#                                    response_map bigint,
#                                    response_map_key bigint not null,
#                                    primary key (quiz_id, response_map_key)
# ) engine=InnoDB;
#
# create table quiz_score_list (
#                                  quiz_id bigint not null,
#                                  score_list bit,
#                                  score_list_key bigint not null,
#                                  primary key (quiz_id, score_list_key)
# ) engine=InnoDB;
#
# create table quiz_set (
#                           id bigint not null auto_increment,
#                           average_difficulty double precision,
#                           description varchar(255),
#                           ready_made bit,
#                           title varchar(255),
#                           user_id bigint,
#                           primary key (id)
# ) engine=InnoDB;
#
# create table quiz_set_question_pool (
#                                         quiz_set_id bigint not null,
#                                         question_pool_id bigint not null
# ) engine=InnoDB;
#
# create table song (
#                       id bigint not null auto_increment,
#                       song_name varchar(255) not null,
#                       album_id bigint,
#                       primary key (id)
# ) engine=InnoDB;
#
# create table user (
#                       id bigint not null auto_increment,
#                       enabled bit not null,
#                       name varchar(40) not null,
#                       password varchar(255) not null,
#                       primary key (id)
# ) engine=InnoDB;
#
# create table user_roles (
#                             user_id bigint not null,
#                             user_roles varchar(255)
# ) engine=InnoDB;