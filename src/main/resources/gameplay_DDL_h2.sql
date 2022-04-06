CREATE TABLE `gameplay` (
                            id_gameplay INT NOT NULL AUTO_INCREMENT,
                            name_gameplay VARCHAR(25) NOT NULL,
                            map_size VARCHAR(3) NOT NULL,
                            dots_to_win INT NOT NULL,
                            PRIMARY KEY (id_gameplay));

CREATE TABLE `dir_game_player_id` (
                                      player_id INT NOT NULL,
                                      player_default_name VARCHAR(9),
                                      PRIMARY KEY (`player_id`));

Insert into dir_game_player_id (player_id, player_default_name) values (0, 'Draw!'), (1, 'Player1'), (2, 'Player2');

CREATE TABLE `players` (
                           `id` INT NOT NULL AUTO_INCREMENT,
                           `id_gameplay` INT NOT NULL REFERENCES `gameplay` (`id_gameplay`),
                           `player_id` INT NOT NULL REFERENCES `dir_game_player_id` (`player_id`),
                           `player_name` VARCHAR(11) NOT NULL,
                           `player_score` INT NOT NULL DEFAULT 0,
                           PRIMARY KEY (`id`));

CREATE TABLE `winners` (
                           `id` INT NOT NULL AUTO_INCREMENT,
                           `id_gameplay` INT NOT NULL REFERENCES `gameplay` (`id_gameplay`),
                           `winner_player_id` INT REFERENCES `dir_game_player_id` (`player_id`),
                           PRIMARY KEY (`id`));

CREATE TABLE `steps` (
                         `id` INT NOT NULL AUTO_INCREMENT,
                         `id_gameplay` INT NOT NULL REFERENCES `gameplay` (`id_gameplay`),
                         `step_number` INT NOT NULL,
                         `step_player_id` INT NOT NULL REFERENCES `dir_game_player_id` (`player_id`),
                         `step_value` VARCHAR(3) NOT NULL,
                         PRIMARY KEY (`id`));

