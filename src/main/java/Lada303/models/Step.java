package Lada303.models;

public class Step{
    private int number;
    private int playerId;
    private String cellValue;

    public Step() {}

    public Step(int num, int playerId, String cellValue) {
        this.number = num;
        this.playerId = playerId;
        this.cellValue = cellValue;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getCellValue() {
        return cellValue;
    }

    public void setCellValue(String cellValue) {
        this.cellValue = cellValue;
    }

    @Override
    public String toString() {
        return "Step{" +
                "num='" + number + '\'' +
                ", playerId='" + playerId + '\'' +
                ", cellValue='" + cellValue + '\'' +
                '}';
    }
}
