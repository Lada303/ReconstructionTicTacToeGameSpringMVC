package Lada303.models;

public class Step {
    private String num;
    private String playerId;
    private String cellValue;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
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
