package pt.mobiledev.tvalarmes.dao;

import pt.mobiledev.tvalarmes.model.Program;
import pt.mobiledev.tvalarmes.util.Dates;
import android.util.Log;
import java.io.IOException;
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
import static pt.mobiledev.tvalarmes.util.API.formatDate;

public class EPGDao {

    final static String TAG = "TVAlarmes";
    final static String BASE_URL = "http://services.sapo.pt/EPG/";
    final static String GET_CHANNEL_FUNCTION = "GetChannelByDateInterval";
    final static int daysInterval = 1;

    /* URL Methods */
    public static String getProgramsURL(String sigla) {
        return BASE_URL + GET_CHANNEL_FUNCTION
                + "?channelSigla=" + sigla
                + "&startDate=" + formatDate(Dates.addDays(daysInterval))
                + "&endDate=" + formatDate(Dates.subtractDays(daysInterval));
    }

    public static ArrayList<Program> getPrograms(String... siglas) {
        try {
            // Create a new instance of the SAX parser
            SAXParserFactory saxPF = SAXParserFactory.newInstance();
            SAXParser saxP = saxPF.newSAXParser();
            XMLReader xmlR = saxP.getXMLReader();
            URL url = new URL(getProgramsURL(siglas[0])); // TODO várias
            Log.v(TAG, url.toString());

            // Create the Handler to handle each of the XML tags
            XMLHandler myXMLHandler = new XMLHandler();
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

}
