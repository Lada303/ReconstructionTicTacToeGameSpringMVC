package Lada303.services.gameplay;
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
import Lada303.services.parsers.writers.JacksonWriter;
import Lada303.services.parsers.writers.StaxWriter2;
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
public class CompetitionJudge {
    private static final File dir_write = new File(ServerPath.SERVER_DIR_WRITE);
    private static final File dir = new File(ServerPath.SERVER_DIR);

    private Competition competition;
    private int drawScore;
    private int whoseMove;
    private int countStep;
    private int countFiles;
    private List<String> listStep;
    private WriteGameToFile writer;
    private String lastWinner;
    private File parserFile;
    private String typeWriter;

    public void setCompetition(Competition competition) {
        this.competition = competition;
        this.drawScore = 0;
        this.listStep = new ArrayList<>();
        if (competition.getTypeFile().contains("xml")) {
            this.typeWriter = ".xml";
            this.writer = new StaxWriter2(competition.getGamer1(), competition.getGamer2(), listStep);
        } else {
            this.typeWriter = ".json";
            this.writer = new JacksonWriter(competition.getGamer1(), competition.getGamer2(), listStep);
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

    public void printScore() {
        System.out.printf("Счет:\t%s : %d\t\t%s : %d\t\tНичья : %d\n%n",
                competition.getGamer1().getName(), competition.getGamer1().getScore(),
                competition.getGamer2().getName(), competition.getGamer2().getScore(),
                drawScore);
        System.out.println();
    }

    public void printScoreToFile() {
        File file = new File(dir.getAbsolutePath() + File.separator + "Score.txt");
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file, true))) {
            if (competition.getGamer1().getScore() + competition.getGamer2().getScore() + drawScore == 1) {
                out.write("\n");
                out.write("Новое соревнование:\n");
            }
            out.write(String.format("Счет:\t%s : %d\t\t%s : %d\t\tНичья : %d\n",
                    competition.getGamer1().getName(), competition.getGamer1().getScore(),
                    competition.getGamer2().getName(), competition.getGamer2().getScore(),
                    drawScore));
        } catch (IOException e) {
            System.out.println("Exp: " + e.getMessage());
        }
    }

    public boolean isWin(Cell lastCell) {
        if (countStep >= competition.getDots_to_win() * 2 - 1) {
            if (lastCell.getDot() == Dots.X && checkWin(lastCell)) {
                competition.getGamer1().incrementScore();
                lastWinner = competition.getGamer1().getName();
                lookCounterFile();
                writer.writeGameToFile(parserFile, competition.getMap().getSize(), 1);
                return true;
            }
            if (lastCell.getDot() == Dots.O && checkWin(lastCell)) {
                competition.getGamer2().incrementScore();
                lastWinner = competition.getGamer2().getName();
                lookCounterFile();
                writer.writeGameToFile(parserFile, competition.getMap().getSize(), 2);
                return true;
            }
        }
        return false;
    }

    private boolean checkWin(Cell lastCell) {
        if (countNonInterruptDotsToWin(lastCell.getDot(),
                competition.getMap().getRow(lastCell)) == competition.getDots_to_win()) {
            return true;
        }
        if (countNonInterruptDotsToWin(lastCell.getDot(),
                competition.getMap().getColumn(lastCell)) == competition.getDots_to_win()) {
            return true;
        }
        if (competition.getMap().isD1(lastCell, competition.getDots_to_win()) &&
                countNonInterruptDotsToWin(lastCell.getDot(), competition.getMap().getD1(lastCell))
                        == competition.getDots_to_win()) {
            return true;
        }
        return (competition.getMap().isD2(lastCell, competition.getDots_to_win()) &&
                countNonInterruptDotsToWin(lastCell.getDot(), competition.getMap().getD2(lastCell))
                        == competition.getDots_to_win());
    }

    // неприрывающаяся последовательность симоволов - для определения победителя
    private int countNonInterruptDotsToWin(Dots dot, Cell[] arrCells) {
        int counter = 0;
        for (Cell cell : arrCells) {
            counter = (cell != null && cell.getDot() == dot ? counter + 1 : 0);
            if (counter == competition.getDots_to_win()) return counter;
        }
        return counter;
    }

    public boolean isDraw() {
        if (countStep >= competition.getMap().getCountColumn() * competition.getMap().getCountRow()) {
            lastWinner = "Draw!!!";
            drawScore++;
            lookCounterFile();
            writer.writeGameToFile(parserFile, competition.getMap().getSize(), 0);
            return true;
        }
        return false;
    }

    private void lookCounterFile() {
       do {
            countFiles++;
            String name = competition.getGamer1().getName()+"Vs" + competition.getGamer2().getName()
                    + "_" + countFiles + typeWriter;
            parserFile = new File(dir_write.getAbsolutePath() + File.separator + name);
         } while (parserFile.exists());
     }
}
