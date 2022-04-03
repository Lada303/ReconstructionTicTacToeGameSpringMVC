package Lada303.services.game;
/*
"Судья"
- следит за игрой (кто ходит, какой сечас шаг и т.п.)
- определяет результаты игры (кто выграл, ничья)
- считает очки по игроку1, игроку2 и ничьи
- печатает очки в консоль и в фаил
- записывает процесс игры в фаил
*/

import Lada303.models.gamemap.Cell;
import Lada303.utils.enums.Dots;
import Lada303.services.parsers.writers.JacksonWriter;
import Lada303.services.parsers.writers.StaxWriter;
import Lada303.services.parsers.writers.WriteGameToFile;
import Lada303.utils.ServerPath;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GameManager {
    private static final File dir_write = new File(ServerPath.SERVER_DIR_WRITE);
    private static final File dir = new File(ServerPath.SERVER_DIR);

    private Game game;
    private int drawScore;
    private int whoseMove;
    private int countStep;
    private int countFiles;
    private List<String> listStep;
    private WriteGameToFile writer;
    private String lastWinner;
    private int lastWinner_id;
    private File parserFile;
    private String typeWriter;

    public void setGame(Game game) {
        this.game = game;
        this.drawScore = 0;
        this.listStep = new ArrayList<>();
        if (game.getTypeFile().contains("xml")) {
            this.typeWriter = ".xml";
            this.writer = new StaxWriter(game.getGamer1(), game.getGamer2(), listStep);
        } else {
            this.typeWriter = ".json";
            this.writer = new JacksonWriter(game.getGamer1(), game.getGamer2(), listStep);
        }
    }

    public int getWhoseMove() {
        return whoseMove;
    }

    public void resetWhoseMove() {
        this.whoseMove = 1;
    }

    public void changeWhoseMove() {
        this.whoseMove = whoseMove * (-1);
    }

    public int getCountStep() {
        return countStep;
    }

    public String getLastWinner() {
        return lastWinner;
    }

    public void incrementCountStep() {
        this.countStep++;
    }

    public void resetCountStep() {
        this.countStep = 0;
    }

    public void clearListStep() {
        listStep.clear();
    }

    public void addToListStep(Cell lastCell) {
        listStep.add((lastCell.getColumnNumber() + 1) + " " + (lastCell.getRowNumber() + 1));
    }

    public void printScoreToFile() {
        File file = new File(dir.getAbsolutePath() + File.separator + "Score.txt");
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file, true))) {
            if (game.getGamer1().getScore() + game.getGamer2().getScore() + drawScore == 1) {
                out.write("\n");
                out.write("Новое соревнование:\n");
            }
            out.write(String.format("Счет:\t%s : %d\t\t%s : %d\t\tНичья : %d\n",
                    game.getGamer1().getName(), game.getGamer1().getScore(),
                    game.getGamer2().getName(), game.getGamer2().getScore(),
                    drawScore));
        } catch (IOException e) {
            System.out.println("Exp: " + e.getMessage());
        }
    }

    public boolean isWin(Cell lastCell) {
        if (countStep >= game.getDots_to_win() * 2 - 1) {
            if (lastCell.getDot() == Dots.X && checkWin(lastCell)) {
                game.getGamer1().incrementScore();
                lastWinner = game.getGamer1().getName();
                lastWinner_id = 1;
                return true;
            }
            if (lastCell.getDot() == Dots.O && checkWin(lastCell)) {
                game.getGamer2().incrementScore();
                lastWinner = game.getGamer2().getName();
                lastWinner_id = 2;
                return true;
            }
        }
        return false;
    }

    private boolean checkWin(Cell lastCell) {
        if (countNonInterruptDotsToWin(lastCell.getDot(),
                game.getMap().getRow(lastCell)) == game.getDots_to_win()) {
            return true;
        }
        if (countNonInterruptDotsToWin(lastCell.getDot(),
                game.getMap().getColumn(lastCell)) == game.getDots_to_win()) {
            return true;
        }
        if (game.getMap().isD1(lastCell, game.getDots_to_win()) &&
                countNonInterruptDotsToWin(lastCell.getDot(), game.getMap().getD1(lastCell))
                        == game.getDots_to_win()) {
            return true;
        }
        return (game.getMap().isD2(lastCell, game.getDots_to_win()) &&
                countNonInterruptDotsToWin(lastCell.getDot(), game.getMap().getD2(lastCell))
                        == game.getDots_to_win());
    }

    // неприрывающаяся последовательность симоволов - для определения победителя
    private int countNonInterruptDotsToWin(Dots dot, Cell[] arrCells) {
        int counter = 0;
        for (Cell cell : arrCells) {
            counter = (cell != null && cell.getDot() == dot ? counter + 1 : 0);
            if (counter == game.getDots_to_win()) return counter;
        }
        return counter;
    }

    public boolean isDraw() {
        if (countStep >= game.getMap().getCountColumn() * game.getMap().getCountRow()) {
            lastWinner = "Draw!!!";
            lastWinner_id = 0;
            drawScore++;
            return true;
        }
        return false;
    }

    private void lookCounterFile() {
       do {
            countFiles++;
            String name = game.getGamer1().getName()+"Vs" + game.getGamer2().getName()
                    + "_" + countFiles + typeWriter;
            parserFile = new File(dir_write.getAbsolutePath() + File.separator + name);
         } while (parserFile.exists());
    }

    public void writeGameplayFile() {
        lookCounterFile();
        writer.writeGameToFile(parserFile, game.getMap().getSize(), lastWinner_id);
    }

}
