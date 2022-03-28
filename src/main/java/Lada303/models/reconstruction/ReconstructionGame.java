package Lada303.models.reconstruction;
/*
Производит непосредственно отрисовку процесса игры
В этом классе можно заменить на нужный конвектор координат
 */
import Lada303.models.gamemap.Dots;
import Lada303.models.gamemap.GameMap;
import Lada303.models.players.Gamer;
import Lada303.models.reconstruction.convectors.CoordinateConvector;
import Lada303.models.reconstruction.convectors.MyCoordinateConvector;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReconstructionGame {

    public List<String> reconstruction(List<Object> listReadFile) {
        // Введите нужный конвектор коорлинат
        CoordinateConvector coordinateConvector = new MyCoordinateConvector();
        //первые две строки списка - это два ирока
        listReadFile.remove(0);
        listReadFile.remove(0);
        GameMap map;
        if (listReadFile.get(0) instanceof String) {
            String str = (String)(listReadFile.remove(0));
            map = new GameMap(Integer.parseInt(str.substring(2)), Integer.parseInt(str.substring(0, 1)));
        } else {
            map = new GameMap(3,3);
        }

        List<String> gameText = new ArrayList<>();
        for (Object item : listReadFile) {
            if (item instanceof Step) { // игровове поле после каждого шага
                int[] xy = coordinateConvector.mapCoordinateConvector(((Step) item).getCellValue());
                map.getCell(xy[1], xy[0]).setDot(((Step) item).getPlayerId().equals("1") ? Dots.X : Dots.O);
                gameText.addAll(map.mapAsString());
            } else { // GameResult
                if (item instanceof String) {
                    gameText.add((String) item);
                } else {
                     gameText.add("Player " + ((Gamer)item).getId() + " -> " + ((Gamer)item).getName() +
                            " is winner as '" + ((Gamer)item).getDots() + "'");
                }
                break;
            }
        }
        return gameText;
    }

}
