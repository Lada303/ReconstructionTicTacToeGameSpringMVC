package Lada303.models.players;

import Lada303.services.gameplay.Competition;
import Lada303.models.gamemap.Cell;
import Lada303.utils.enums.Dots;
import Lada303.models.gamemap.GameMap;

import java.util.Random;

public class AIGamer extends Gamer {

    private static final Random rand = new Random();

    public AIGamer(int id, String name, Dots dots) {
        super(id, name, dots);
    }

    @Override
    public boolean doStep(Competition competition) {
        GameMap map = competition.getMap();
        for (int decrement = 1; decrement <  competition.getDots_to_win(); decrement++) {
            //шаг для победы
            if (decrement == 1 && isAIStep(this.getCell(), decrement, competition)) {
                break;
            }
            //шаг для помехи
            if (isAIStep(competition.getGamer1().getCell(), decrement, competition)) {
                break;
            }
        }
        if (this.getCell() == null || !map.getCell(this.getCell()).isEmptyCell()) {
            int x, y;
            do {
                x = rand.nextInt(map.getCountColumn());
                y = rand.nextInt(map.getCountRow());
            } while (map.getCell(x, y).getDot() != null);
            this.setCell(map.getCell(x, y));
        }
        map.getCell(this.getCell()).setDot(Dots.O);
        return true;
    }

    //выбирает шаг
    private boolean isAIStep(Cell lastCell, int decrement, Competition competition) {
        GameMap map = competition.getMap();
        if (lastCell == null ||
                competition.getJudge().getCountStep() < competition.getDots_to_win() * 2 - 1 && lastCell.getDot() == Dots.O) {
            return false;
        }
        if (map.isD1(lastCell, competition.getDots_to_win())
                && countDotsInLine(lastCell.getDot(), map.getD1(lastCell)) == competition.getDots_to_win() - decrement
                && isEmptyValidCellInLine(map.getD1(lastCell), lastCell)) {
            return true;
        }
        if (map.isD2(lastCell,competition.getDots_to_win())
                && countDotsInLine(lastCell.getDot(), map.getD2(lastCell)) == competition.getDots_to_win() - decrement
                && isEmptyValidCellInLine(map.getD2(lastCell), lastCell)) {
            return true;
        }
        if (countDotsInLine(lastCell.getDot(), map.getRow(lastCell)) == competition.getDots_to_win() - decrement
                && isEmptyValidCellInLine(map.getRow(lastCell), lastCell)) {
            return true;
        }
        return countDotsInLine(lastCell.getDot(), map.getColumn(lastCell)) == competition.getDots_to_win() - decrement
                && isEmptyValidCellInLine(map.getColumn(lastCell), lastCell);
    }

    //Всего элементов  - для определения пути противодействия игроку
    private int countDotsInLine(Dots dot, Cell[] arrCells) {
        int counter = 0;
        for (Cell cell : arrCells) {
            if (cell != null && cell.getDot() == dot) counter++;
        }
        return counter;
    }

    // находит пустую ячиеку и устанавливает ее координаты
    private boolean isEmptyValidCellInLine(Cell[] arrCells, Cell lastCell) {
        int position = 0;
        for (int i = 0; i < arrCells.length; i++) {
            if (arrCells[i] != null && arrCells[i] == lastCell) {
                position = i;
                break;
            }
        }
        if (position != 0 && arrCells[position - 1] != null && arrCells[position - 1].isEmptyCell()) {
            this.setCell(arrCells[position - 1]);
            return true;
        }
        if (position != arrCells.length - 1 && arrCells[position + 1] != null && arrCells[position + 1].isEmptyCell()) {
            this.setCell(arrCells[position + 1]);
            return true;
        }
        for (Cell cell : arrCells) {
            if (cell != null && cell.isEmptyCell()) {
                this.setCell(cell);
                return true;
            }
        }
        return false;
    }
}
