package Lada303.dao;

public interface WinnerDAO {
    void addNewWinner(int id_gameplay, int id_winner);
    int getWinner(int id_gameplay);
}
