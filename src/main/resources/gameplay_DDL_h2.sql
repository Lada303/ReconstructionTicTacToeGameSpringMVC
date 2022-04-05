CREATE TABLE `gameplay` (
  `id_gameplay` INT NOT NULL AUTO_INCREMENT,
  `name_gameplay` VARCHAR(25) NOT NULL,
  PRIMARY KEY (`id_gameplay`));

CREATE TABLE `directory_game_player_id` (
  `player_id` INT NOT NULL,
  PRIMARY KEY (`player_id`));
  
Insert into directory_game_player_id values (1), (2);

CREATE TABLE `players` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_gameplay` INT NOT NULL REFERENCES `gameplay` (`id_gameplay`),
  `player_id` INT NOT NULL REFERENCES `directory_game_player_id` (`player_id`),
  `player_name` VARCHAR(11) NOT NULL,
  `player_symbol` VARCHAR(1) NOT NULL,
  `player_score` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`));
 
  CREATE TABLE `maps` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_gameplay` INT NOT NULL REFERENCES `gameplay` (`id_gameplay`),
  `map_size` VARCHAR(3) NOT NULL,
  PRIMARY KEY (`id`));
      
CREATE TABLE `winners` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_gameplay` INT NOT NULL REFERENCES `gameplay` (`id_gameplay`),
  `winner_player_id` INT REFERENCES `directory_game_player_id` (`player_id`),
  PRIMARY KEY (`id`));
 
CREATE TABLE `steps` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_gameplay` INT NOT NULL REFERENCES `gameplay` (`id_gameplay`),
  `step_number` INT NOT NULL,
  `step_player_id` INT NOT NULL REFERENCES `directory_game_player_id` (`player_id`),
  `step_value` VARCHAR(3) NOT NULL,
  PRIMARY KEY (`id`));
