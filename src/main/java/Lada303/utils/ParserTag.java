package Lada303.utils;
/*
Содержит в себе ключевые слова - теги для записи и чтения хода игры
Используется *Writer из GameApp для записи файла
Используется *Parser из ReconstructionApp для парсинга файла
 */
public class ParserTag {
    public static final String GAME_PLAY = "Gameplay";
    public static final String GAME = "Game";
    public static final String PLAYER = "Player";
    public static final String PLAYER_ID = "id";
    public static final String PLAYER_NAME = "name";
    public static final String PLAYER_SYMBOL = "symbol";
    public static final String MAP = "GameMap";
    public static final String STEP = "Step";
    public static final String STEP_NUM = "num";
    public static final String STEP_PLAYER_ID = "playerId";
    public static final String STEP_VALUE = "value";
    public static final String RESULT = "GameResult";
    public static final String DRAW = "Draw!";
}
