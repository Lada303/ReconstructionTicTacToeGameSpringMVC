package Lada303.models.players;

/*
интерфейс, описывающий что может делать игрок
 */

import Lada303.models.Gameplay;

public interface GamerDoing {
    boolean doStep(Gameplay game);
}
