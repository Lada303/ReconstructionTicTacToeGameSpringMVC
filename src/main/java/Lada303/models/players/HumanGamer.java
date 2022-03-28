package Lada303.models.players;

import Lada303.models.gameplay.Competition;
import Lada303.models.gamemap.Dots;
import Lada303.models.gamemap.GameMap;

public class HumanGamer extends Gamer {

    public HumanGamer(int id, String name, Dots dots) {
        super(id, name, dots);
    }

    @Override
    public boolean doStep(Competition competition) {
        int x = this.getCell().getColumnNumber();
        int y = this.getCell().getRowNumber();
        if (isValidEmpty(x, y, competition.getMap())) {
            competition.getMap().getCell(x, y).setDot(this.getDots());
            this.setCell(competition.getMap().getCell(x, y));
            return true;
        }
        return false;
    }

    private boolean isValidEmpty(int x, int y, GameMap map) {
        return map.isCellValid(x, y) && map.getCell(x, y).isEmptyCell();
    }
}
