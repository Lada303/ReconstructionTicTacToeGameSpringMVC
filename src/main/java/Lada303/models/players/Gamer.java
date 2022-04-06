package Lada303.models.players;

/*
Абстрактный класс, описывающий общие свойства игроков
 */

import Lada303.models.Gameplay;
import Lada303.services.gameplay.gameplaymap.Cell;
import Lada303.utils.enums.Dots;

public abstract class Gamer implements GamerDoing {

    private final int id;
    private final String name;
    private final Dots dots;
    private Cell cell;

    public Gamer(int id, String name, Dots dots) {
        this.id = id;
        this.name = name;
        this.dots = dots;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Dots getDots() {
        return dots;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public void setCell(int x, int y) {
        this.cell = new Cell(x, y);
    }

    public abstract boolean doStep(Gameplay game);

}