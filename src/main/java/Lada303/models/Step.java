package Lada303.models;

public class Step{
    private int num;
    private int playerId;
    private String cellValue;

    public Step() {}

    public Step(int num, int playerId, String cellValue) {
        this.num = num;
        this.playerId = playerId;
        this.cellValue = cellValue;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
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
                "num='" + num + '\'' +
                ", playerId='" + playerId + '\'' +
                ", cellValue='" + cellValue + '\'' +
                '}';
    }
}
