package pt.mobiledev.tvalarmes.dao;

import pt.mobiledev.tvalarmes.domain.Program;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandler extends DefaultHandler {

    ArrayList<Program> programs = new ArrayList<Program>();
    Program program;
    public boolean current = false;
    public String currentValue = null;

    public ArrayList<Program> getPrograms() {
        return programs;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        current = true;
        if (localName.equals("Program")) {
            program = new Program();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        current = false;

        if (localName.equals("Id")) {
            program.setId(currentValue);
        } else if (localName.equals("Title")) {
            program.setTitle(currentValue);
        } else if (localName.equals("Description")) {
            program.setDescription(currentValue);
        } else if (localName.equals("StartTime")) {
            /*program.setStartDate(currentValue);*/
        } else if (localName.equals("EndTime")) {
            /*program.setEndDate(currentValue);*/
        } else if (localName.equals("ChannelSigla")) {
            program.setChannel(currentValue);
        } else if (localName.equals("Program")) {
            programs.add(program);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        if (current) {
            currentValue = new String(ch, start, length);
            current = false;
        }
    }
}
