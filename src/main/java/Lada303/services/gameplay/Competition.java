package Lada303.services.gameplay;

/*
"Соревнование" - игру идут подряд, пока пользователь не прервет
При создании "соревнования" определеям вид: с ИИ или другим игроком
(инициализируются игроки и судья)
В начале каждой игре:
- выбираем размер поля
- выбираем количество фишек для выгрыша
*/

import Lada303.models.gamemap.Cell;
import Lada303.models.gamemap.GameMap;
import Lada303.models.players.Gamer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Competition {

    private final CompetitionJudge judge;
    private String mode;
    private String typeFile;
    private Gamer gamer1;
    private Gamer gamer2;
    private GameMap map;
    private int dots_to_win;

    @Autowired
    public Competition(CompetitionJudge judge) {
        this.judge = judge;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setTypeFile(String typeFile) {
        this.typeFile = typeFile;
    }

    public void setGamer1(Gamer gamer1) {
        this.gamer1 = gamer1;
    }

    public void setGamer2(Gamer gamer2) {
        this.gamer2 = gamer2;
    }

    public void setMap(int x, int y) {
        this.map = new GameMap(x, y);
    }

    public void setDots_to_win(int dots_to_win) {
        this.dots_to_win = dots_to_win;
    }

    public String getMode() {
        return mode;
    }

    public String getTypeFile() {
        return typeFile;
    }

    public Gamer getGamer1() {
        return gamer1;
    }

    public Gamer getGamer2() {
        return gamer2;
    }

    public CompetitionJudge getJudge() {
        return judge;
    }

    public GameMap getMap() {
        return map;
    }

    public int getDots_to_win() {
        return dots_to_win;
    }

    public void startNewRound() {
        judge.resetCountStep();
        judge.clearListStep();
        judge.resetWhoseMove();
    }

    public boolean isPlayerDidStep(int stepX, int stepY) {
        if (judge.getWhoseMove() == 1) {
            gamer1.setCell(stepX, stepY);
            if (!gamer1.doStep(this)){
                return false;
            }
        } else {
            if (mode.equals("player")) {
                gamer2.setCell(stepX, stepY);
            }
            if (!gamer2.doStep(this)){
                return false;
            }
        }

        Cell lastCell = judge.getWhoseMove() == 1 ? gamer1.getCell() : gamer2.getCell();
        judge.addToListStep(lastCell);
        judge.incrementCountStep();
        judge.changeWhoseMove();
        return true;
    }

    public boolean isTheEndOfRound(){
        //т.к. поменяла игрока в isPlayerDidStep на нового, а надо проверить именно ход старого игрока
        Cell lastCell = judge.getWhoseMove() == -1 ? gamer1.getCell() : gamer2.getCell();
        if (judge.isWin(lastCell) || judge.isDraw()) {
            judge.printScore();
            judge.printScoreToFile();
            return true;
        }
        return false;
    }

}
