package Lada303.reconstructionApp.parsers;

/*
Общий интерфейс для парсеров файлов содержащих процесс игры
На вход получет фаил
Возвращает список объектов:
Player1 - Player2 - GameMap (если есть) - Step1 - Step2-... - GameResult (Player or "Draw!")
 */

import java.util.List;

@FunctionalInterface
public interface Parser {
    List<Object> readConfig(String configFile);
}
