package Lada303.models.players;

/*
интерфейс, описывающий что может делать игрок
 */

import Lada303.models.gameplay.Competition;

public interface GamerDoing {
    boolean doStep(Competition competition);
}
