package Lada303.models;

/*
*/

import Lada303.services.gameplay.gameplaymap.GameplayMap;
import Lada303.models.players.Gamer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Gameplay {

    private int id;
    private String name;
    private String mapSize;
    private int dots_to_win;
    private Gamer gamer1;
    private Gamer gamer2;
    private GameplayMap map;
    private String mode;
    private String typeWriter;
    private final List<Step> listStep;

    public Gameplay() {
        this.listStep = new ArrayList<>();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGamer1(Gamer gamer1) {
        this.gamer1 = gamer1;
    }

    public void setGamer2(Gamer gamer2) {
        this.gamer2 = gamer2;
    }

    public void setMap(int x, int y) {
        this.map = new GameplayMap(x, y);
        this.mapSize = this.map.getSize();
    }

    public void setMapSize(String mapSize) {
        this.mapSize = mapSize;
    }

    public void setDots_to_win(int dots_to_win) {
        this.dots_to_win = dots_to_win;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setTypeWriter(String typeWriter) {
        this.typeWriter = typeWriter;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Gamer getGamer1() {
        return gamer1;
    }

    public Gamer getGamer2() {
        return gamer2;
    }

    public GameplayMap getMap() {
        return map;
    }

    public String getMapSize() {
        return mapSize;
    }

    public int getDots_to_win() {
        return dots_to_win;
    }

    public String getMode() {
        return mode;
    }

    public String getTypeWriter() {
        return typeWriter;
    }

    public List<Step> getListStep() {
        return listStep;
    }

    public void clearListStep() {
        listStep.clear();
    }

    public void addToListStep(Step step) {
        listStep.add(step);
    }

    public Step getLastStep() {
        return listStep.get(listStep.size() - 1);
    }
}
