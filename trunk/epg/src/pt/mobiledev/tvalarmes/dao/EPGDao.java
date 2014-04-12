package pt.mobiledev.tvalarmes.dao;

import android.content.Context;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xmlpull.v1.XmlPullParser;
import static org.xmlpull.v1.XmlPullParser.END_DOCUMENT;
import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import org.xmlpull.v1.XmlPullParserException;
import static pt.mobiledev.tvalarmes.dao.XmlParser.getParser;
import static pt.mobiledev.tvalarmes.dao.XmlParser.readValuesAsMap;
import pt.mobiledev.tvalarmes.domain.Channel;
import pt.mobiledev.tvalarmes.domain.Program;
import pt.mobiledev.tvalarmes.util.Util;

public class EPGDao {

    final static String BASE_URL = "http://services.sapo.pt/EPG/";
    final static String GET_CHANNEL_FUNCTION = "GetChannelByDateInterval";
    final static String GET_CHANNELS_FUNCTION = "GetChannelList";
    final static int daysInterval = 1;

    public static List<Channel> getChannels(Context context) {
        List<Channel> entries = new ArrayList<Channel>();
        try {
            XmlPullParser parser = getParser(context, BASE_URL + GET_CHANNELS_FUNCTION, 30);
            while (parser.next() != END_TAG) {
                if (parser.getEventType() == START_TAG
                        && parser.getName().equals("Channel")) {
                    // OK encontrou tag; controi canal!
                    Map<String, String> channelAsMap = readValuesAsMap(parser, "Name", "Sigla");
                    entries.add(new Channel(channelAsMap.get("Name"), channelAsMap.get("Sigla")));
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(EPGDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XmlPullParserException ex) {
            Logger.getLogger(EPGDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return entries;
    }

    public static List<Program> getPrograms(String... siglas) {
        Set<Program> entries = new HashSet<Program>();
        try {
            URL url = new URL(BASE_URL + GET_CHANNEL_FUNCTION
                    + "?channelSigla=" + siglas[0]
                    + "&startDate=" + formatDate(Util.subtractDays(daysInterval))
                    + "&endDate=" + formatDate(Util.addDays(daysInterval)));
            // TODO suporte a vários canais de uma só vez: útil para o scheduler
            XmlPullParser parser = getParser(url.toString());
            while (parser.next() != END_DOCUMENT) {
                if (parser.getEventType() == START_TAG && parser.getName().equals("Program")) {
                    // Constrói programa
                    Map<String, String> channelAsMap = readValuesAsMap(parser, "Id", "Title", "ChannelSigla");
                    Program program = new Program();
                    program.setId(channelAsMap.get("Id")); // TODO: porque não é um int?
                    program.setTitle(channelAsMap.get("Title"));
                    program.setChannelSigla(channelAsMap.get("ChannelSigla"));  // TODO buscar objeto canal?
                    // TODO  parse do resto?
                    entries.add(program);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(EPGDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XmlPullParserException ex) {
            Logger.getLogger(EPGDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        // ordenar programas alfabeticamente
        List listEntries = new ArrayList<Program>(entries);
        Collections.sort(listEntries, new Comparator<Program>() {

            public int compare(Program p1, Program p2) {
                return p1.getTitle().compareTo(p2.getTitle());
            }
        });
        return listEntries;
    }

    public static String formatDate(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
        String encondedDate = dateFormatter.format(date);
        return encondedDate.replace(" ", "+").replace(":", "%3A");
    }
}
