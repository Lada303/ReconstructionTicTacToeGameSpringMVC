package Lada303.models.gamemap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameMap {

    private final Cell[][] map;

    public GameMap(int x, int y) {
        map = new Cell[y][x];
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                map[i][j] = new Cell(j, i);
            }
        }
    }

    public boolean isCellValid(int x, int y) {
        return (0 <= x && x < map[0].length) && (0 <= y && y < map.length);
    }

    public int getCountRow() {
        return map.length;
    }

    public int getCountColumn() {
        return map[1].length;
    }

    public Cell getCell(int x, int y) {
        return map[y][x];
    }

    public Cell getCell(Cell cell) {
        return map[cell.getRowNumber()][cell.getColumnNumber()];
    }

    public Cell[] getRow(Cell cell) {
        Cell[] row = new Cell[map[0].length];
        System.arraycopy(map[cell.getRowNumber()], 0, row, 0, row.length);
        return row;
    }

    public Cell[] getColumn(Cell cell) {
        Cell[] column = new Cell[map.length];
        for (int i = 0; i < column.length; i++) {
            column[i] = map[i][cell.getColumnNumber()];
        }
        return column;
    }

    public Cell[] getD1(Cell cell) {
        int b = cell.getColumnNumber() - cell.getRowNumber();
        Cell[] d1 = new Cell[map.length];
        for (int i = 0; i < d1.length; i++) {
            if (i + b >= 0 && i + b < map[0].length) {
                d1[i] = map[i][i + b];
            }
        }
        return d1;
    }

    public Cell[] getD2(Cell cell) {
        int b = cell.getColumnNumber() + cell.getRowNumber();
        Cell[] d2 = new Cell[map.length];
        for (int i = 0; i < map.length ; i++) {
            if (b - i >= 0 && b - i < map[0].length) {
                d2[i] = map[i][b - i];
            }
        }
        System.out.println(Arrays.toString(d2));
        return d2;
    }

    public boolean isD1(Cell cell, int lengthD) {
        return (cell.getRowNumber() - (map.length - lengthD) <= cell.getColumnNumber()
                && cell.getColumnNumber() <= map[0].length - lengthD + cell.getRowNumber());
    }

    public boolean isD2(Cell cell, int lengthD) {
        return (lengthD - 1 - cell.getRowNumber() <= cell.getColumnNumber()
                && cell.getColumnNumber() <= map[0].length - 1 - cell.getRowNumber() + (map.length - lengthD));
    }

    public String getSize() {
        return map[0].length + "*" + map.length;
    }

    public List<String > mapAsString() {
        List<String> mapAsString = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("_|");
        for (int i = 1; i <= map[1].length; i++) {
            sb.append(i).append("|");
        }
        mapAsString.add(String.valueOf(sb));

        for (int i = 0; i < map.length; i++) {
            sb = new StringBuilder();
            sb.append(i + 1).append("|");
            for (int j = 0; j < map[1].length; j++) {
                sb.append((map[i][j].getDot() == null ? "_" : map[i][j].getDot()).toString().toLowerCase()).append("|");
            }
            mapAsString.add(String.valueOf(sb));

        }
        mapAsString.add("---------");
        return mapAsString;
    }

}
