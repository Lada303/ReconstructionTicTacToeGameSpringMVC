package Lada303.services.gameplay;
/*
- следит за игрой (кто ходит, какой сечас шаг и т.п.)
- определяет результаты игры (кто выграл, ничья)
- считает очки по игроку1, игроку2 и ничьи
- печатает очки в консоль и в фаил
- записывает процесс игры в фаил
*/

import Lada303.models.Gameplay;
import Lada303.models.Step;
import Lada303.services.gameplay.gameplaymap.Cell;
import Lada303.utils.enums.Dots;
import Lada303.utils.parsers.writers.JacksonWriter;
import Lada303.utils.parsers.writers.StaxWriter;
import Lada303.utils.parsers.writers.WriteGameToFile;
import Lada303.utils.ServerPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GameplayService {
    private static final File dir_write = new File(ServerPath.SERVER_DIR_WRITE);
    private static final File dir = new File(ServerPath.SERVER_DIR);

    private final Gameplay gameplay;
    private String typeWriter;
    private WriteGameToFile writer;
    private String mode;

    private int drawScore;
    private int gamer1Score;
    private int gamer2Score;
    private int whoseMove;
    private int countStep;
    private int countFiles;
    private final List<Step> listStep;
    private String lastWinner;
    private int lastWinner_id;
    private File parserFile;

    @Autowired
    public GameplayService(Gameplay gameplay) {
        this.gameplay = gameplay;
        this.listStep = new ArrayList<>();
    }

    public void setStartData() {
        this.drawScore = 0;
        this.gamer1Score = 0;
        this.gamer2Score = 0;
    }

    public Gameplay getGameplay() {
        return gameplay;
    }

    public void setTypeWriter(String typeWriter) {
        this.typeWriter = typeWriter;
    }

    public void setWriter() {
        if (typeWriter.equals("xml")) {
            this.writer = new StaxWriter(gameplay.getGamer1(), gameplay.getGamer2(), listStep);
        } else {
            this.writer = new JacksonWriter(gameplay.getGamer1(), gameplay.getGamer2(), listStep);
        }
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
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

    public void incrementCountStep() {
        this.countStep++;
    }

    public void resetCountStep() {
        this.countStep = 0;
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

    public String getLastWinner() {
        return lastWinner;
    }

    public int getLastWinner_id() {
        return lastWinner_id;
    }


    public boolean isPlayerDidStep(int stepX, int stepY) {
        if (whoseMove == 1) {
            gameplay.getGamer1().setCell(stepX, stepY);
            if (!gameplay.getGamer1().doStep(gameplay)){
                return false;
            }
        } else {
            if (mode.equals("player")) {
                gameplay.getGamer2().setCell(stepX, stepY);
            }
            if (!gameplay.getGamer2().doStep(gameplay)){
                return false;
            }
        }

        Cell lastCell = whoseMove == 1 ? gameplay.getGamer1().getCell() : gameplay.getGamer2().getCell();
        incrementCountStep();
        addToListStep(new Step(getCountStep(),
                whoseMove == 1 ? 1 : 2,
                (lastCell.getColumnNumber()) + " " + (lastCell.getRowNumber())));
        return true;
    }

    public boolean isTheEndOfRound(){
        Cell lastCell = whoseMove == 1 ? gameplay.getGamer1().getCell() : gameplay.getGamer2().getCell();
        changeWhoseMove();
        if (isWin(lastCell) || isDraw()) {
            printScoreToFile();
            writeGameplayFile();
            return true;
        }
        return false;
    }

    public void printScoreToFile() {
        File file = new File(dir.getAbsolutePath() + File.separator + "Score.txt");
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file, true))) {
            if (gamer1Score + gamer2Score + drawScore == 1) {
                out.write("\n");
                out.write("Новое соревнование:\n");
            }
            out.write(String.format("Счет:\t%s : %d\t\t%s : %d\t\tНичья : %d\n",
                    gameplay.getGamer1().getName(), gamer1Score,
                    gameplay.getGamer2().getName(), gamer2Score,
                    drawScore));
        } catch (IOException e) {
            System.out.println("Exp: " + e.getMessage());
        }
    }

    public boolean isWin(Cell lastCell) {
        if (countStep >= gameplay.getDots_to_win() * 2 - 1) {
            if (lastCell.getDot() == Dots.X && checkWin(lastCell)) {
                gamer1Score++;
                lastWinner = gameplay.getGamer1().getName();
                lastWinner_id = 1;
                return true;
            }
            if (lastCell.getDot() == Dots.O && checkWin(lastCell)) {
                gamer2Score++;
                lastWinner = gameplay.getGamer2().getName();
                lastWinner_id = 2;
                return true;
            }
        }
        return false;
    }

    private boolean checkWin(Cell lastCell) {
        if (countNonInterruptDotsToWin(lastCell.getDot(),
                gameplay.getMap().getRow(lastCell)) == gameplay.getDots_to_win()) {
            return true;
        }
        if (countNonInterruptDotsToWin(lastCell.getDot(),
                gameplay.getMap().getColumn(lastCell)) == gameplay.getDots_to_win()) {
            return true;
        }
        if (gameplay.getMap().isD1(lastCell, gameplay.getDots_to_win()) &&
                countNonInterruptDotsToWin(lastCell.getDot(), gameplay.getMap().getD1(lastCell))
                        == gameplay.getDots_to_win()) {
            return true;
        }
        return (gameplay.getMap().isD2(lastCell, gameplay.getDots_to_win()) &&
                countNonInterruptDotsToWin(lastCell.getDot(), gameplay.getMap().getD2(lastCell))
                        == gameplay.getDots_to_win());
    }

    private int countNonInterruptDotsToWin(Dots dot, Cell[] arrCells) {
        int counter = 0;
        for (Cell cell : arrCells) {
            counter = (cell != null && cell.getDot() == dot ? counter + 1 : 0);
            if (counter == gameplay.getDots_to_win()) return counter;
        }
        return counter;
    }

    public boolean isDraw() {
        if (countStep >= gameplay.getMap().getCountColumn() * gameplay.getMap().getCountRow()) {
            lastWinner = "Draw!!!";
            lastWinner_id = 0;
            drawScore++;
            return true;
        }
        return false;
    }

    public void writeGameplayFile() {
        lookCounterFile();
        writer.writeGameToFile(parserFile, gameplay.getMap().getSize(), lastWinner_id);
    }

    private void lookCounterFile() {
        do {
            countFiles++;
            String name = gameplay.getName() + "_" + countFiles + "." + typeWriter;
            parserFile = new File(dir_write.getAbsolutePath() + File.separator + name);
        } while (parserFile.exists());
    }

}
