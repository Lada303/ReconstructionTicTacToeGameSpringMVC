package Lada303.models.players;

/*
Абстрактный класс, описывающий общие свойства игроков
 */

import Lada303.services.game.Game;
import Lada303.models.gamemap.Cell;
import Lada303.utils.enums.Dots;

public abstract class Gamer implements GamerDoing {

    private final int id;
    private final String name;
    private final Dots dots;
    private Cell cell;
    private int score;

    public Gamer(int id, String name, Dots dots) {
        this.id = id;
        this.name = name;
        this.dots = dots;
        this.score = 0;
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

    public int getScore() {
        return score;
    }

    public void incrementScore() {
        this.score++;
    }

    public abstract boolean doStep(Game game);

}