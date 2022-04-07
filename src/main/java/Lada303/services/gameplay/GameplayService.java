package Lada303.services.gameplay;

import Lada303.models.Gameplay;

public interface GameplayService {
    void setStartData(Gameplay gameplay);
    void startNewRound();
    boolean isPlayerDidStep(int stepX, int stepY);
    boolean isTheEndOfRound();
    int getLastWinner_id();
}
