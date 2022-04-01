package Lada303.services.parsers.writers;

/*
Записывает xml-фаил используя StAx, потоковую форму записи XMLOutputFactory.
Записывает ход игры согласно установленной структуре
*/

import Lada303.models.players.Gamer;
import Lada303.utils.ParserTag;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class StaxWriter2 implements WriteGameToFile{

    private final Gamer gamer1;
    private final Gamer gamer2;
    private final List<String> listStep;
    private XMLStreamWriter writer;

    public StaxWriter2(Gamer gamer1, Gamer gamer2, List<String> listStep) {
        this.gamer1 = gamer1;
        this.gamer2 = gamer2;
        this.listStep = listStep;
    }

    @Override
    public void writeGameToFile(File parserFile, String mapSize, int winner) {
        try {
            writeXMLDocument(parserFile, mapSize, winner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeXMLDocument(File parserFile, String mapSize,  int winner)
            throws Exception {
        XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
        writer = outputFactory.createXMLStreamWriter(new FileOutputStream(parserFile));
        writer.writeStartDocument("1.0");
        writer.writeDTD("\n");
        writer.writeStartElement(ParserTag.GAME_PLAY);
        writer.writeDTD("\n");

        gamerWriter(gamer1);
        gamerWriter(gamer2);
        gameMapWriter(mapSize);
        gameStepsWriter();
        resultWriter(winner);

        writer.writeEndDocument();
        writer.close();
    }

    private void gamerWriter(Gamer gamer) throws XMLStreamException {
        writer.writeDTD("\t");
        writer.writeEmptyElement(ParserTag.PLAYER);
        writer.writeAttribute(ParserTag.PLAYER_ID,  String.valueOf(gamer.getId()));
        writer.writeAttribute(ParserTag.PLAYER_NAME,  gamer.getName());
        writer.writeAttribute(ParserTag.PLAYER_SYMBOL,  gamer.getDots().toString());
        writer.writeDTD("\n");
    }

    private void gameMapWriter(String mapSize) throws XMLStreamException {
        writer.writeDTD("\t");
        writer.writeStartElement(ParserTag.MAP);
        writer.writeCharacters(mapSize);
        writer.writeEndElement();
        writer.writeDTD("\n");
    }

    private void gameStepsWriter() throws XMLStreamException {
        writer.writeDTD("\t");
        writer.writeStartElement(ParserTag.GAME);
        writer.writeDTD("\n");
        for (int i = 0; i < listStep.size(); i++) {
            stepWriter(String.valueOf(i + 1), i % 2 == 0 ? "1" : "2", listStep.get(i));
        }
        writer.writeDTD("\t");
        writer.writeEndElement();
        writer.writeDTD("\n");
    }

    private void stepWriter(String stepCounter, String numGamer, String cellStep)
            throws XMLStreamException {
        writer.writeDTD("\t\t");
        writer.writeStartElement(ParserTag.STEP);
        writer.writeAttribute(ParserTag.STEP_NUM, stepCounter);
        writer.writeAttribute(ParserTag.STEP_PLAYER_ID, numGamer);
        writer.writeCharacters(cellStep);
        writer.writeEndElement();
        writer.writeDTD("\n");
    }

    private void resultWriter(int winner) throws XMLStreamException {
        writer.writeDTD("\t");
        writer.writeStartElement(ParserTag.RESULT);
        if (winner == 0) {
            writer.writeCharacters(ParserTag.DRAW);
        } else {
            writer.writeDTD("\n");
            writer.writeDTD("\t");
            gamerWriter(winner == 1 ? gamer1 : gamer2);
            writer.writeDTD("\t");
        }
        writer.writeEndElement();
        writer.writeDTD("\n");
    }
}
