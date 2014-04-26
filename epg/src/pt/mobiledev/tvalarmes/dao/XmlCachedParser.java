package pt.mobiledev.tvalarmes.dao;

import android.content.Context;
import android.util.Xml;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import static java.util.Arrays.asList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import org.xmlpull.v1.XmlPullParser;
import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;
import org.xmlpull.v1.XmlPullParserException;

public class XmlCachedParser {

    public static XmlPullParser getParser(Context context, String id, String url, int cacheHours) throws IOException, XmlPullParserException {
        InputStream inputStream;
        try {
            inputStream = getFileInputStream(context, id, url, cacheHours);
        } catch (FileNotFoundException e) {
            // se houver erro de servidor, busca o que tem em disco
            inputStream = getFileInputStream(context, id, url, 10000);
        }
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(inputStream, null);
        parser.nextTag();
        return parser;
    }

    public static void skipTag(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    public static Map<String, String> readValuesAsMap(XmlPullParser parser, String... tags) throws IOException, XmlPullParserException {
        Map<String, String> values = new HashMap<String, String>();
        while (parser.next() != END_TAG) {
            if (parser.getEventType() == START_TAG) {
                String tagName = parser.getName();
                if (asList(tags).contains(tagName)) {
                    values.put(tagName, readTextElement(parser).trim());
                } else {
                    skipTag(parser);
                }
            }
        }
        return values;
    }

    public static String readTextElement(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    static InputStream getFileInputStream(Context context, String id, String url, int cacheHours) throws IOException {
        File file = new File(context.getFilesDir(), id);
        boolean isFileOld = !file.exists() || new Date().getTime() - file.lastModified() > MILLISECONDS.convert(cacheHours, TimeUnit.HOURS);
        if (isFileOld) { // vamos buscar o original e guardamos para cache
            InputStream inputStream = new URL(url).openStream();
            FileOutputStream outputStream = context.openFileOutput(id, Context.MODE_PRIVATE);
            int read;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.close();
        }
        return context.openFileInput(id);  // retorna o ficheiro do disco
    }
}
