package pt.mobiledev.tvalarmes.dao;

import android.util.Log;
import android.util.Xml;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
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
                    + "&startDate=" + formatDate(Dates.addDays(daysInterval))
                    + "&endDate=" + formatDate(Dates.subtractDays(daysInterval))); // TODO vários canais
            Log.v(EPGDao.class.getPackage().toString(), url.toString());

            // Create the Handler to handle each of the XML tags
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

    public static ArrayList<Channel> getChannels() throws IOException, XmlPullParserException {
        InputStream in = new URL(BASE_URL + GET_CHANNELS_FUNCTION).openStream();
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in, null);
        parser.nextTag();
        in.close();
        return null;
    }

}
