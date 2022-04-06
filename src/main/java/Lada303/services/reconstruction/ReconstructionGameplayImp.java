package Lada303.services.reconstruction;
/*
Производит непосредственно отрисовку процесса игры
В этом классе можно заменить на нужный конвектор координат
 */
import Lada303.models.Step;
import Lada303.utils.enums.Dots;
import Lada303.services.gameplay.gameplaymap.GameplayMap;
import Lada303.models.players.Gamer;
import Lada303.services.reconstruction.convectors.CoordinateConvector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReconstructionGameplayImp implements ReconstructionGameplay{

    private final CoordinateConvector coordinateConvector;

    @Autowired
    public ReconstructionGameplayImp(@Qualifier("myCoordinateConvector") CoordinateConvector coordinateConvector) {
        this.coordinateConvector = coordinateConvector;
    }

    public List<String> reconstruction(List<Object> listReadFile) {
        //первые две строки списка - это два ирока
        listReadFile.remove(0);
        listReadFile.remove(0);
        GameplayMap map;
        if (listReadFile.get(0) instanceof String) {
            String str = (String)(listReadFile.remove(0));
            map = new GameplayMap(Integer.parseInt(str.substring(2)), Integer.parseInt(str.substring(0, 1)));
        } else {
            map = new GameplayMap(3,3);
        }

        List<String> gameText = new ArrayList<>();
        for (Object item : listReadFile) {
            if (item instanceof Step) { // игровове поле после каждого шага
                int[] xy = coordinateConvector.mapCoordinateConvector(((Step) item).getCellValue());
                map.getCell(xy[0], xy[1]).setDot(((Step) item).getPlayerId() == 1 ? Dots.X : Dots.O);
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
