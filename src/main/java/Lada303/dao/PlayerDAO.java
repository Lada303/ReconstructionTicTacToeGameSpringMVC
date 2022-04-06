package Lada303.dao;

import Lada303.models.players.Gamer;

public interface PlayerDAO {
    void addNewGameplay(int id_gameplay, Gamer player);
    Gamer getPlayerByItId(int id_gameplay, int player_id);
}
