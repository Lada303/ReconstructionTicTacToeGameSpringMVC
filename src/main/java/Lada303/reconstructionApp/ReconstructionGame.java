package Lada303.reconstructionApp;
/*
Производит непосредственно отрисовку процесса игры
В этом классе можно заменить на нужный конвектор координат
 */
import Lada303.reconstructionApp.convectors.CoordinateConvector;
import Lada303.reconstructionApp.convectors.MyCoordinateConvector;
import Lada303.reconstructionApp.models.Player;
import Lada303.reconstructionApp.models.Step;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReconstructionGame {

    private String[][] map;
    private CoordinateConvector coordinateConvector;
    private Player player1;
    private Player player2;
    private List<String> gameText;

    public List<String> reconstruction(List<Object> listReadFile) {
        // Введите нужный конвектор коорлинат
        coordinateConvector = new MyCoordinateConvector();
        player1 = (Player) (listReadFile.remove(0));
        player2 = (Player) (listReadFile.remove(0));
        if (listReadFile.get(0) instanceof String) {
            String str = (String)(listReadFile.remove(0));
            map = new String[Integer.parseInt(str.substring(2))][Integer.parseInt(str.substring(0, 1))];
        } else {
            map = new String[3][3];
        }
        gameText = new ArrayList<>();

        for (Object item : listReadFile) {
            if (item instanceof Step) { // print GameMap with step
                int[] xy = coordinateConvector.mapCoordinateConvector(((Step) item).getCellValue());
                map[xy[1]][xy[0]] = ((Step) item).getPlayerId().equals("1") ? player1.getSymbol() : player2.getSymbol();
                printMap();
                System.out.println();
            } else { // print GameResult
                if (item instanceof String) {
                    System.out.println(item);
                    gameText.add((String) item);
                } else {
                    System.out.println("Player " + ((Player)item).getId() + " -> " + ((Player)item).getName() +
                            " is winner as '" + ((Player)item).getSymbol() + "'");
                    gameText.add("Player " + ((Player)item).getId() + " -> " + ((Player)item).getName() +
                            " is winner as '" + ((Player)item).getSymbol() + "'");
                }
                break;
            }
        }
        return gameText;
    }

    private void printMap() {
        StringBuilder sb = new StringBuilder();
        System.out.print("  | ");
        sb.append("_|");
        for (int i = 1; i <= map[1].length; i++) {
            System.out.print(i + " | ");
            sb.append(i).append("|");
        }
        System.out.println();
        gameText.add(String.valueOf(sb));

        printHorizontalLine();
        for (int i = 0; i < map.length; i++) {
            sb = new StringBuilder();
            System.out.print((i + 1) + " | ");
            sb.append(i + 1).append("|");
            for (int j = 0; j < map[1].length; j++) {
                System.out.print((map[i][j] == null  ? " " : map[i][j]) + " | ");
                sb.append((map[i][j] == null ? "_" : map[i][j]).toLowerCase()).append("|");
            }
            System.out.println();
            gameText.add(String.valueOf(sb));
            printHorizontalLine();
        }
        gameText.add("---------");
    }

    private void printHorizontalLine() {
        for (int i = 0; i <= map[1].length; i++) {
            System.out.print("----");
        }
        System.out.println();
    }
}
