package Lada303.models.players;

/*
интерфейс, описывающий что может делать игрок
 */

import Lada303.services.game.Game;

public interface GamerDoing {
    boolean doStep(Game game);
}
