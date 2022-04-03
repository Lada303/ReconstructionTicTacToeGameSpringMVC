package Lada303.models.players;

import Lada303.services.game.Game;
import Lada303.utils.enums.Dots;
import Lada303.models.gamemap.GameMap;

public class HumanGamer extends Gamer {

    public HumanGamer(int id, String name, Dots dots) {
        super(id, name, dots);
    }

    @Override
    public boolean doStep(Game game) {
        int x = this.getCell().getColumnNumber();
        int y = this.getCell().getRowNumber();
        if (isValidEmpty(x, y, game.getMap())) {
            game.getMap().getCell(x, y).setDot(this.getDots());
            this.setCell(game.getMap().getCell(x, y));
            return true;
        }
        return false;
    }

    private boolean isValidEmpty(int x, int y, GameMap map) {
        return map.isCellValid(x, y) && map.getCell(x, y).isEmptyCell();
    }
}
