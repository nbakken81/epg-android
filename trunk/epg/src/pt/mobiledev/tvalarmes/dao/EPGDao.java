package pt.mobiledev.tvalarmes.dao;

import android.content.Context;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import static java.lang.Integer.parseInt;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.protocol.HTTP;
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
    final static String GET_PROGRAMS_FUNCTION = "GetChannelListByDateInterval";
    final static String GET_CHANNELS_FUNCTION = "GetChannelList";
    final static SimpleDateFormat MEO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    final static Pattern PROG_EP_PATTERN = Pattern.compile("^(.*) - Ep. ([0-9]{1,3})$");
    final static Pattern PROG_EP_SE_PATTERN = Pattern.compile("^(.*) T([0-9]{1,2}) - Ep. ([0-9]{1,3})$");

    public static List<Channel> getChannels(Context context) {
        List<Channel> entries = new ArrayList<Channel>();
        try {
            XmlPullParser parser = getParser(context, "listaCanais", BASE_URL + GET_CHANNELS_FUNCTION, 30 * 24);
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

    public static List<Program> getAllPrograms(Context context, List<Channel> channels) {
        List<Program> entries = new ArrayList<Program>();
        try {
            URL url = new URL(BASE_URL + GET_PROGRAMS_FUNCTION
                    + "?channelSiglas=" + formatSiglas(channels)
                    + "&startDate=" + formatDate(Util.subtractDays(0))
                    + "&endDate=" + formatDate(Util.addDays(1)));
            // TODO suporte a vários canais de uma só vez: útil para o scheduler
            XmlPullParser parser = getParser(context, formatSiglas(channels), url.toString(), 1);
            while (parser.next() != END_DOCUMENT) {
                if (parser.getEventType() == START_TAG && parser.getName().equals("Program")) {
                    // Constrói programa
                    Map<String, String> channelAsMap = readValuesAsMap(parser, "Id", "Title", "ChannelSigla", "StartTime");
                    Program program = new Program();
                    program.setId(Integer.parseInt(channelAsMap.get("Id")));
                    program.setTitle(channelAsMap.get("Title"));
                    Matcher matcher = PROG_EP_SE_PATTERN.matcher(channelAsMap.get("Title"));
                    // "nome programa Txy - Ep. kzy"
                    if (matcher.matches()) {
                        program.setTitle(matcher.group(1).trim());
                        program.setEpisode(matcher.group(2) == null ? 0 : parseInt(matcher.group(2)));
                        program.setEpisode(matcher.group(3) == null ? 0 : parseInt(matcher.group(3)));
                    } else {
                        // "nome programa Ep. kzy"
                        matcher = PROG_EP_PATTERN.matcher(channelAsMap.get("Title"));
                        if (matcher.matches()) {
                            program.setTitle(matcher.group(1).trim());
                            program.setSeason(matcher.group(2) == null ? 0 : parseInt(matcher.group(2)));
                        }
                    }
                    program.setChannelId(channelAsMap.get("ChannelSigla"));  // TODO buscar objeto canal?
                    program.setStartDate(MEO_DATE_FORMAT.parse(channelAsMap.get("StartTime")));
                    entries.add(program);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(EPGDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XmlPullParserException ex) {
            Logger.getLogger(EPGDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(EPGDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return entries;
    }

    public static List<Program> getAvailablePrograms(Context context, Channel channel) {
        List<Program> progs = getAllPrograms(context, Arrays.asList(channel));
        removeRepeated(progs);
        sort(progs);
        return progs;
    }

    /**
     * Ordena programas por canal e depois alfabeticamente.
     */
    static void sort(List<Program> listEntries) {

        Collections.sort(listEntries, new Comparator<Program>() {

            public int compare(Program p1, Program p2) {
                if (!p1.getChannelId().equals(p2.getChannelId())) {
                    return p1.getChannelId().compareTo(p2.getChannelId());
                }
                return p1.getTitle().compareTo(p2.getTitle());
            }
        });
    }

    /**
     * Ordena programas por canal e depois alfabeticamente.
     */
    static void removeRepeated(List<Program> listEntries) {
        Set<Program> programs = new HashSet<Program>(listEntries);
        listEntries.clear();
        listEntries.addAll(programs);
    }

    public static String formatDate(Date date) {
        try {
            return URLEncoder.encode(MEO_DATE_FORMAT.format(date), HTTP.UTF_8);
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
    }

    public static String formatSiglas(List<Channel> channels) {
        StringBuilder siglasBuilder = new StringBuilder();
        for (Channel ch : channels) {
            siglasBuilder.append(ch.getId()).append(",");
        }
        return siglasBuilder.toString();
    }
}
