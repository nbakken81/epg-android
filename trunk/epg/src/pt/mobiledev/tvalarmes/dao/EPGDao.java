package pt.mobiledev.tvalarmes.dao;

import android.util.Log;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import org.xmlpull.v1.XmlPullParserException;
import static pt.mobiledev.tvalarmes.dao.XmlParser.getParser;
import static pt.mobiledev.tvalarmes.dao.XmlParser.readTextElement;
import static pt.mobiledev.tvalarmes.dao.XmlParser.skipTag;
import pt.mobiledev.tvalarmes.domain.Channel;
import pt.mobiledev.tvalarmes.domain.Program;
import static pt.mobiledev.tvalarmes.util.API.formatDate;
import pt.mobiledev.tvalarmes.util.Dates;

public class EPGDao {

    final static String BASE_URL = "http://services.sapo.pt/EPG/";
    final static String GET_CHANNEL_FUNCTION = "GetChannelByDateInterval";
    final static String GET_CHANNELS_FUNCTION = "GetChannelList";
    final static int daysInterval = 1;

    public static ArrayList<Program> getPrograms(String... siglas) {
        try {
            // Create a new instance of the SAX parser
            SAXParserFactory saxPF = SAXParserFactory.newInstance();
            SAXParser saxP = saxPF.newSAXParser();
            XMLReader xmlR = saxP.getXMLReader();
            URL url = new URL(BASE_URL + GET_CHANNEL_FUNCTION
                    + "?channelSigla=" + siglas[0]
                    + "&startDate=" + formatDate(Dates.subtractDays(daysInterval))
                    + "&endDate=" + formatDate(Dates.addDays(daysInterval))); // TODO vários canais
            Log.v(EPGDao.class.getPackage().toString(), url.toString());
            XMLProgramsHandler myXMLHandler = new XMLProgramsHandler();
            xmlR.setContentHandler(myXMLHandler);
            xmlR.parse(new InputSource(url.openStream()));
            return myXMLHandler.getPrograms();

        } catch (MalformedURLException ex) {
            Logger.getLogger(EPGDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(EPGDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(EPGDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EPGDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static List<Channel> getChannels() {
        List<Channel> entries = new ArrayList<Channel>();
        try {
            XmlPullParser parser = getParser(BASE_URL + GET_CHANNELS_FUNCTION);
            while (parser.next() != END_TAG) {
                if (parser.getEventType() != START_TAG) {
                    continue;
                }
                if (parser.getName().equals("Channel")) {
                    entries.add(readChannel(parser));
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(EPGDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XmlPullParserException ex) {
            Logger.getLogger(EPGDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return entries;
    }

    static Channel readChannel(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(START_TAG, null, "Channel");  // isto é só um assert
        String name = null, sigla = null;
        while (parser.next() != END_TAG) {
            if (parser.getEventType() != START_TAG) {
                continue;
            }
            String tagName = parser.getName();
            if (tagName.equals("Name")) {
                name = readTextElement(parser);
            } else if (tagName.equals("Sigla")) {
                sigla = readTextElement(parser);
            } else {
                skipTag(parser);
            }
        }
        return new Channel(name, sigla);
    }
}
