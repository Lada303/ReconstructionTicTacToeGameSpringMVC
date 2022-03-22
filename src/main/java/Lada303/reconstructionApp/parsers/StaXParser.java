package Lada303.reconstructionApp.parsers;

/*
Осуществлеят парсинг xml-файлов
Использует StAX
 */

import Lada303.reconstructionApp.models.Player;
import Lada303.reconstructionApp.models.Step;
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
        if (startElement.getName().getLocalPart().equals(GameTag.PLAYER)) {
            element = new Player();
            Iterator<Attribute> attributes = startElement.getAttributes();
            while (attributes.hasNext()) {
                Attribute attribute = attributes.next();
                if (attribute.getName().toString().equals(GameTag.PLAYER_ID)) {
                    ((Player) element).setId(attribute.getValue());
                }
                if (attribute.getName().toString().equals(GameTag.PLAYER_NAME)) {
                    ((Player) element).setName(attribute.getValue());
                }
                if (attribute.getName().toString().equals(GameTag.PLAYER_SYMBOL)) {
                    ((Player) element).setSymbol(attribute.getValue());
                }
            }
        }
    }

    private void readMap(StartElement startElement) throws XMLStreamException {
        if (startElement.getName().getLocalPart().equals(GameTag.MAP)) {
            XMLEvent event = eventReader.nextEvent();
            element = event.asCharacters().getData();
        }
    }

    private void readStep(StartElement startElement) throws XMLStreamException {
        if (startElement.getName().getLocalPart().equals(GameTag.STEP)) {
            element = new Step();
            Iterator<Attribute> attributes = startElement.getAttributes();
            while (attributes.hasNext()) {
                Attribute attribute = attributes.next();
                if (attribute.getName().toString().equals(GameTag.STEP_NUM)) {
                    ((Step) element).setNum(attribute.getValue());
                }
                if (attribute.getName().toString().equals(GameTag.STEP_PLAYER_ID)) {
                    ((Step) element).setPlayerId(attribute.getValue());
                }
            }
            XMLEvent event = eventReader.nextEvent();
            ((Step) element).setCellValue(event.asCharacters().getData());
        }
    }

    private void readResult(StartElement startElement) throws XMLStreamException {
        if (startElement.getName().getLocalPart().equals(GameTag.RESULT)) {
            XMLEvent event = eventReader.nextEvent();
            if (event.isCharacters()) {
                element = event.asCharacters().getData();
            }
        }
    }
}
