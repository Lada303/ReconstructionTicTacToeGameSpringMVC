package Lada303.utils.parsers.readers;

/*
Осуществлеят парсинг json-файлов
Использует jackson потоковое API
 */

import Lada303.utils.enums.Dots;
import Lada303.models.players.HumanGamer;
import Lada303.utils.ParserTag;
import Lada303.models.Step;

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
                if (name == null || name.equals(ParserTag.GAME_PLAY)) {
                    continue;
                }
                switch (name){
                    case ParserTag.PLAYER -> {
                        readPlayer();
                        if(drawResult) {
                            drawResult = false;
                        }
                    }
                    case ParserTag.MAP -> {
                        jsonParser.nextToken();
                        element = jsonParser.getValueAsString();
                        list.add(element);
                    }
                    case ParserTag.STEP -> readStep();
                    case ParserTag.RESULT -> drawResult = true;
                }
            }

            if (drawResult) {
                list.add(ParserTag.DRAW);
            }

            jsonParser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void readPlayer() throws IOException {
        int id = 0;
        Dots dot = Dots.X;
        String name = "Some";
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            if (jsonParser.getCurrentName().equals(ParserTag.PLAYER_ID)) {
                jsonParser.nextToken();
                id = jsonParser.getValueAsInt();
                dot = (id == 1 ? Dots.X : Dots.O);
            }
            if (jsonParser.getCurrentName().equals(ParserTag.PLAYER_NAME)) {
                jsonParser.nextToken();
                name = jsonParser.getValueAsString();
            }
        }
        element = new HumanGamer(id, name, dot);
        list.add(element);
    }

    private void readStep() throws IOException {
        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
            element = new Step();
            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                if (jsonParser.getCurrentName() == null) {
                    continue;
                }
                if (jsonParser.getCurrentName().equals(ParserTag.STEP_NUM)) {
                    jsonParser.nextToken();
                    ((Step) element).setNum(jsonParser.getValueAsInt());
                }
                if (jsonParser.getCurrentName().equals(ParserTag.STEP_PLAYER_ID)) {
                    jsonParser.nextToken();
                    ((Step) element).setPlayerId(jsonParser.getValueAsInt());
                }
                if (jsonParser.getCurrentName().equals(ParserTag.STEP_VALUE)) {
                    jsonParser.nextToken();
                    ((Step) element).setCellValue(jsonParser.getValueAsString());
                }
            }
            list.add(element);
        }
    }
}
