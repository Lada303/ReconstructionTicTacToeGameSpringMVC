package Lada303.dao;

import Lada303.models.Gameplay;

import java.util.List;

public interface GameplayDAO {
    void addNewGameplay(Gameplay gameplay);
    int getLastGameplayId();
    List<Gameplay> getAllGameplay();
    String getGameplayMapSize(int id_gameplay);
}
