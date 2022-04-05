package Lada303.utils.parsers.readers;

/*
Осуществлеят парсинг xml-файлов
Использует StAX
 */

import Lada303.utils.enums.Dots;
import Lada303.models.players.HumanGamer;
import Lada303.utils.ParserTag;
import Lada303.models.Step;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class StaXParser implements Parser{

    private final List<Object> list;
    private Object element;
    private XMLEventReader eventReader;

    {
        list = new ArrayList<>();
        element = null;
    }

    @Override
    public List<Object> readConfig(String configFile) {
        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            eventReader = inputFactory.createXMLEventReader(new FileInputStream(configFile));

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    readPlayer(startElement);
                    readMap(startElement);
                    readStep(startElement);
                    readResult(startElement);
                }

                if (event.isEndElement()) {
                    if (element == null) {
                        continue;
                    }
                    list.add(element);
                    element = null;
                }
            }
        } catch (FileNotFoundException | XMLStreamException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void readPlayer(StartElement startElement) {
        if (startElement.getName().getLocalPart().equals(ParserTag.PLAYER)) {
            int id = 0;
            Dots dot = Dots.X;
            String name = "Some";
            Iterator<Attribute> attributes = startElement.getAttributes();
            while (attributes.hasNext()) {
                Attribute attribute = attributes.next();
                if (attribute.getName().toString().equals(ParserTag.PLAYER_ID)) {
                    id = Integer.parseInt(attribute.getValue());
                    dot = (id == 1 ? Dots.X : Dots.O);
                }
                if (attribute.getName().toString().equals(ParserTag.PLAYER_NAME)) {
                    name = attribute.getValue();
                }
            }
            element = new HumanGamer(id, name, dot);
        }
    }

    private void readMap(StartElement startElement) throws XMLStreamException {
        if (startElement.getName().getLocalPart().equals(ParserTag.MAP)) {
            XMLEvent event = eventReader.nextEvent();
            element = event.asCharacters().getData();
        }
    }

    private void readStep(StartElement startElement) throws XMLStreamException {
        if (startElement.getName().getLocalPart().equals(ParserTag.STEP)) {
            element = new Step();
            Iterator<Attribute> attributes = startElement.getAttributes();
            while (attributes.hasNext()) {
                Attribute attribute = attributes.next();
                if (attribute.getName().toString().equals(ParserTag.STEP_NUM)) {
                    ((Step) element).setNum(Integer.parseInt(attribute.getValue()));
                }
                if (attribute.getName().toString().equals(ParserTag.STEP_PLAYER_ID)) {
                    ((Step) element).setPlayerId(Integer.parseInt(attribute.getValue()));
                }
            }
            XMLEvent event = eventReader.nextEvent();
            ((Step) element).setCellValue(event.asCharacters().getData());
        }
    }

    private void readResult(StartElement startElement) throws XMLStreamException {
        if (startElement.getName().getLocalPart().equals(ParserTag.RESULT)) {
            XMLEvent event = eventReader.nextEvent();
            if (event.isCharacters()) {
                element = event.asCharacters().getData();
            }
        }
    }
}
