package Lada303.dao;

import Lada303.models.Gameplay;

import java.util.List;

public interface GameplayDAO {
    void addNewGameplay(String name_gameplay);
    int getLastGameplayId();
    List<Gameplay> getAllGameplay();
}
