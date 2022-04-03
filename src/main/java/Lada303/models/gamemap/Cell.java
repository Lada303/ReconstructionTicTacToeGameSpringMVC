package Lada303.models.gamemap;

import Lada303.utils.enums.Dots;

public class Cell {

    private final int columnNumber;
    private final int rowNumber;
    private Dots dot;

    public Cell(int x, int y) {
        columnNumber = x;
        rowNumber = y;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setDot(Dots dot) {
        this.dot = dot;
    }

    public Dots getDot() {
        return dot;
    }

    public boolean isEmptyCell() {
        return dot == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cell that = (Cell) obj;
        if (this.columnNumber != that.columnNumber) return false;
        if (this.rowNumber != that.rowNumber) return false;
        return this.dot == that.dot;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "columnNumber=" + columnNumber +
                ", rowNumber=" + rowNumber +
                ", dot=" + dot +
                '}';
    }
}
