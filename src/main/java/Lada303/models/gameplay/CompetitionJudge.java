package Lada303.models.gameplay;
/*
"Судья"
- следит за игрой (кто ходит, какой сечас шаг и т.п.)
- определяет результаты игры (кто выграл, ничья)
- считает очки по игроку1, игроку2 и ничьи
- печатает очки в консоль и в фаил
- записывает процесс игры в фаил
*/

import Lada303.models.gamemap.Cell;
import Lada303.models.gamemap.Dots;
import Lada303.models.gamemap.GameMap;
import Lada303.models.players.Gamer;
import Lada303.models.parsers.writers.JacksonWriter;
import Lada303.models.parsers.writers.StaxWriter2;
import Lada303.models.parsers.writers.WriteGameToFile;
import Lada303.services.ServerPath;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CompetitionJudge {
    private static final File dir_write = new File(ServerPath.SERVER_DIR_WRITE);
    private static final File dir = new File(ServerPath.SERVER_DIR);

    private final Gamer gamer1;
    private final Gamer gamer2;
    private int drawScore;
    private int whoseMove;
    private int countStep;
    private int countFiles;
    private GameMap map;
    private int dots_to_win;
    private final List<String> listStep;
    private final WriteGameToFile writer;
    private String lastWinner;
    private File parserFile;
    private final String typeWriter;

    public CompetitionJudge(Competition competition) {
        this.gamer1 = competition.getGamer1();
        this.gamer2 = competition.getGamer2();
        this.drawScore = 0;
        this.listStep = new ArrayList<>();
        if (competition.getTypeFile().contains("xml")) {
            this.typeWriter = ".xml";
            this.writer = new StaxWriter2(gamer1, gamer2, listStep);
        } else {
            this.typeWriter = ".json";
            this.writer = new JacksonWriter(gamer1, gamer2, listStep);
        }
        this.countFiles = 0;
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

    public void setMap(GameMap map) {
        this.map = map;
    }

    public void setDots_to_win(int dots_to_win) {
        this.dots_to_win = dots_to_win;
    }

    public void clearListStep() {
        listStep.clear();
    }

    public void addToListStep(Cell lastCell) {
        listStep.add((lastCell.getColumnNumber() + 1) + " " + (lastCell.getRowNumber() + 1));
    }

    public void printScore() {
        System.out.printf("Счет:\t%s : %d\t\t%s : %d\t\tНичья : %d\n%n",
                gamer1.getName(), gamer1.getScore(), gamer2.getName(), gamer2.getScore(), drawScore);
        System.out.println();
    }

    public void printScoreToFile() {
        File file = new File(dir.getAbsolutePath() + File.separator + "Score.txt");
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file, true))) {
            if (gamer1.getScore() + gamer2.getScore() + drawScore == 1) {
                out.write("\n");
                out.write("Новое соревнование:\n");
            }
            out.write(String.format("Счет:\t%s : %d\t\t%s : %d\t\tНичья : %d\n",
                    gamer1.getName(), gamer1.getScore(), gamer2.getName(), gamer2.getScore(), drawScore));
        } catch (IOException e) {
            System.out.println("Exp: " + e.getMessage());
        }
    }

    public boolean isWin(Cell lastCell) {
        if (countStep >= dots_to_win * 2 - 1) {
            if (lastCell.getDot() == Dots.X && checkWin(lastCell)) {
                gamer1.incrementScore();
                lastWinner = gamer1.getName();
                lookCounterFile();
                writer.writeGameToFile(parserFile, map.getSize(), 1);
                return true;
            }
            if (lastCell.getDot() == Dots.O && checkWin(lastCell)) {
                gamer2.incrementScore();
                lastWinner = gamer2.getName();
                lookCounterFile();
                writer.writeGameToFile(parserFile, map.getSize(), 2);
                return true;
            }
        }
        return false;
    }

    private boolean checkWin(Cell lastCell) {
        if (countNonInterruptDotsToWin(lastCell.getDot(), map.getRow(lastCell)) == dots_to_win) {
            return true;
        }
        if (countNonInterruptDotsToWin(lastCell.getDot(), map.getColumn(lastCell)) == dots_to_win) {
            return true;
        }
        if (map.isD1(lastCell, dots_to_win) &&
                countNonInterruptDotsToWin(lastCell.getDot(), map.getD1(lastCell)) == dots_to_win) {
            return true;
        }
        return (map.isD2(lastCell, dots_to_win) &&
                countNonInterruptDotsToWin(lastCell.getDot(), map.getD2(lastCell)) == dots_to_win);
    }

    // неприрывающаяся последовательность симоволов - для определения победителя
    private int countNonInterruptDotsToWin(Dots dot, Cell[] arrCells) {
        int counter = 0;
        for (Cell cell : arrCells) {
            counter = (cell != null && cell.getDot() == dot ? counter + 1 : 0);
            if (counter == dots_to_win) return counter;
        }
        return counter;
    }

    public boolean isDraw() {
        if (countStep >= map.getCountColumn() * map.getCountRow()) {
            lastWinner = "Draw!!!";
            drawScore++;
            lookCounterFile();
            writer.writeGameToFile(parserFile, map.getSize(), 0);
            return true;
        }
        return false;
    }

    private void lookCounterFile() {
       do {
            countFiles++;
            String name = gamer1.getName()+"Vs" + gamer2.getName() + "_" + countFiles + typeWriter;
            parserFile = new File(dir_write.getAbsolutePath() + File.separator + name);
         } while (parserFile.exists());
     }
}
