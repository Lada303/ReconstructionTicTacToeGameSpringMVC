package Lada303.reconstructionApp.parsers;

/*
Осуществлеят парсинг json-файлов
Использует jackson потоковое API
 */

import Lada303.reconstructionApp.models.Player;
import Lada303.reconstructionApp.models.Step;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JacksonParser implements Parser{

    private final List<Object> list;
    private Object element;
    private boolean drawResult;
    private JsonParser jsonParser;

    {
        list = new ArrayList<>();
        element = null;
        drawResult = false;
    }

    @Override
    public List<Object> readConfig(String configFile) {

        try {
            jsonParser = new JsonFactory().createParser(new File(configFile));

            while(jsonParser.nextToken() != JsonToken.END_OBJECT) {
                String name = jsonParser.getCurrentName();
                if (name == null || name.equals(GameTag.GAME_PLAY)) {
                    continue;
                }
                switch (name){
                    case GameTag.PLAYER -> {
                        readPlayer();
                        if(drawResult) {
                            drawResult = false;
                        }
                    }
                    case GameTag.MAP -> {
                        jsonParser.nextToken();
                        element = jsonParser.getValueAsString();
                        list.add(element);
                    }
                    case GameTag.STEP -> readStep();
                    case GameTag.RESULT -> drawResult = true;
                }
            }

            if (drawResult) {
                list.add(GameTag.DRAW);
            }

            jsonParser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void readPlayer() throws IOException {
        element = new Player();
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            if (jsonParser.getCurrentName().equals(GameTag.PLAYER_ID)) {
                jsonParser.nextToken();
                ((Player) element).setId(jsonParser.getValueAsString());
            }
            if (jsonParser.getCurrentName().equals(GameTag.PLAYER_NAME)) {
                jsonParser.nextToken();
                ((Player) element).setName(jsonParser.getValueAsString());
            }
            if (jsonParser.getCurrentName().equals(GameTag.PLAYER_SYMBOL)) {
                jsonParser.nextToken();
                ((Player) element).setSymbol(jsonParser.getValueAsString());
            }
        }
        list.add(element);
    }

    private void readStep() throws IOException {
        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
            element = new Step();
            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                if (jsonParser.getCurrentName() == null) {
                    continue;
                }
                if (jsonParser.getCurrentName().equals(GameTag.STEP_NUM)) {
                    jsonParser.nextToken();
                    ((Step) element).setNum(jsonParser.getValueAsString());
                }
                if (jsonParser.getCurrentName().equals(GameTag.STEP_PLAYER_ID)) {
                    jsonParser.nextToken();
                    ((Step) element).setPlayerId(jsonParser.getValueAsString());
                }
                if (jsonParser.getCurrentName().equals(GameTag.STEP_VALUE)) {
                    jsonParser.nextToken();
                    ((Step) element).setCellValue(jsonParser.getValueAsString());
                }
            }
            list.add(element);
        }
    }
}
