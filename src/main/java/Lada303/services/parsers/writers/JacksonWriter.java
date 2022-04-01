package Lada303.services.parsers.writers;

/*
Записывает json-фаил используя Jackson, потоковую форму записи.
Записывает ход игры согласно установленной структуре
*/

import Lada303.models.players.Gamer;
import Lada303.utils.ParserTag;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class JacksonWriter implements WriteGameToFile{

    private final Gamer gamer1;
    private final Gamer gamer2;
    private final List<String> listStep;
    private JsonGenerator jsonGenerator;

    public JacksonWriter(Gamer gamer1, Gamer gamer2, List<String> listStep) {
        this.gamer1 = gamer1;
        this.gamer2 = gamer2;
        this.listStep = listStep;
    }

    @Override
    public void writeGameToFile(File parserFile, String mapSize, int winner) {
        try {
            writeJacksonDocument(parserFile, mapSize, winner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeJacksonDocument(File parserFile, String mapSize,  int winner)
            throws Exception {
        jsonGenerator = new JsonFactory().createGenerator(new FileOutputStream(parserFile));
        jsonGenerator.setPrettyPrinter(new DefaultPrettyPrinter());

        jsonGenerator.writeStartObject();
        writePlayer(gamer1);
        writePlayer(gamer2);
        jsonGenerator.writeObjectField(ParserTag.MAP, mapSize);
        writeSteps();
        jsonGenerator.writeObjectFieldStart(ParserTag.RESULT);
        switch (winner){
            case 1 -> writePlayer(gamer1);
            case 2 -> writePlayer(gamer2);
            default -> jsonGenerator.writeStringField("result", ParserTag.DRAW);
        }
        jsonGenerator.writeEndObject();

        jsonGenerator.writeEndObject();
        jsonGenerator.flush();
        jsonGenerator.close();
    }

    private void writePlayer(Gamer gamer) throws IOException {
        jsonGenerator.writeObjectFieldStart(ParserTag.PLAYER);
        jsonGenerator.writeNumberField(ParserTag.PLAYER_ID, gamer.getId());
        jsonGenerator.writeStringField(ParserTag.PLAYER_NAME, gamer.getName());
        jsonGenerator.writeStringField(ParserTag.PLAYER_SYMBOL, gamer.getDots().name());
        jsonGenerator.writeEndObject();
    }

    private void writeSteps() throws IOException {
        jsonGenerator.writeArrayFieldStart(ParserTag.STEP);
        for (int i = 0; i < listStep.size(); i++) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField(ParserTag.STEP_NUM,i + 1);
            jsonGenerator.writeStringField(ParserTag.STEP_PLAYER_ID, i % 2 == 0 ? "1" : "2");
            jsonGenerator.writeStringField(ParserTag.STEP_VALUE, listStep.get(i));
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
    }
}
