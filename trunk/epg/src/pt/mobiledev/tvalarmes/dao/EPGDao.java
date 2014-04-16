package pt.mobiledev.tvalarmes.dao;

import android.content.Context;
import java.io.IOException;
import static java.lang.Integer.parseInt;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;
import static org.xmlpull.v1.XmlPullParser.END_DOCUMENT;
import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import org.xmlpull.v1.XmlPullParserException;
import static pt.mobiledev.tvalarmes.dao.XmlCachedParser.getParser;
import static pt.mobiledev.tvalarmes.dao.XmlCachedParser.readValuesAsMap;
import pt.mobiledev.tvalarmes.domain.Channel;
import pt.mobiledev.tvalarmes.domain.Program;
import pt.mobiledev.tvalarmes.util.Util;

public class EPGDao {

    final static String BASE_URL = "http://services.sapo.pt/EPG/";
    final static String GET_CHANNEL_FUNCTION = "GetChannelListByDateInterval";
    final static String GET_CHANNELS_FUNCTION = "GetChannelList";
    final static Pattern PROG_EP_PATTERN = Pattern.compile("^(.*) - Ep. ([0-9]{1,3})$");
    final static Pattern PROG_EP_SE_PATTERN = Pattern.compile("^(.*) T([0-9]{1,2}) - Ep. ([0-9]{1,3})$");
    final static int DAYS_INTERVAL = 1;

    public static List<Channel> getChannels(Context context) {
        List<Channel> entries = new ArrayList<Channel>();
        try {
            XmlPullParser parser = getParser(context, BASE_URL + GET_CHANNELS_FUNCTION, 30);
            while (parser.next() != END_TAG) {
                if (parser.getEventType() == START_TAG && parser.getName().equals("Channel")) {
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

    public static List<Program> getPrograms(Context context, Channel... channels) {
        Set<Program> entries = new HashSet<Program>();
        try {
            URL url = new URL(BASE_URL + GET_CHANNEL_FUNCTION
                    + "?channelSiglas=" + formatSiglas(channels)
                    + "&startDate=" + formatDate(Util.subtractDays(DAYS_INTERVAL))
                    + "&endDate=" + formatDate(Util.addDays(DAYS_INTERVAL)));
            // TODO suporte a vários canais de uma só vez: útil para o scheduler
            XmlPullParser parser = getParser(url.toString());
            while (parser.next() != END_DOCUMENT) {
                if (parser.getEventType() == START_TAG && parser.getName().equals("Program")) {
                    // Constrói programa
                    Map<String, String> channelAsMap = readValuesAsMap(parser, "Id", "Title", "ChannelSigla");
                    Program program = new Program();
                    program.setId(Integer.parseInt(channelAsMap.get("Id")));
                    program.setTitle(channelAsMap.get("Title"));
                    Matcher matcher = PROG_EP_SE_PATTERN.matcher(channelAsMap.get("Title"));
                    // "nome programa Txy - Ep. kzy"
                    if (matcher.matches()) {
                        program.setTitle(matcher.group(1));
                        program.setEpisode(matcher.group(2) == null ? 0 : parseInt(matcher.group(2)));
                        program.setEpisode(matcher.group(3) == null ? 0 : parseInt(matcher.group(3)));
                    } else {
                        // "nome programa Ep. kzy"
                        matcher = PROG_EP_PATTERN.matcher(channelAsMap.get("Title"));
                        if (matcher.matches()) {
                            program.setTitle(matcher.group(1));
                            program.setSeason(matcher.group(2) == null ? 0 : parseInt(matcher.group(2)));
                        }
                    }
                    program.setChannelId(channelAsMap.get("ChannelSigla"));  // TODO buscar objeto canal?
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
                if (!p1.getChannelId().equals(p2.getChannelId())) {
                    return p1.getChannelId().compareTo(p2.getChannelId());
                }
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

    public static String formatSiglas(Channel[] channels) {
        StringBuilder siglasBuilder = new StringBuilder();
        for (Channel ch : channels) {
            siglasBuilder.append(ch.getId()).append(",");
        }
        return siglasBuilder.toString();
    }
}
