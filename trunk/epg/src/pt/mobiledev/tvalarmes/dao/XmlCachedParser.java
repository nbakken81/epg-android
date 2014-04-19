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

    public static XmlPullParser getParser(Context context, String url, int cacheDays) throws IOException, XmlPullParserException {
        InputStream inputStream = getFileInputStream(context, url, cacheDays);
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(inputStream, null);
        parser.nextTag();
        return parser;
    }

    public static XmlPullParser getParser(Context context, String url) throws IOException, XmlPullParserException {
        return getParser(context, url, 0);
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

    static InputStream getFileInputStream(Context context, String url, int cacheDays) throws IOException {
        String filename = url.substring(url.lastIndexOf("/") + 1);
        if (cacheDays <= 0) {
            return tryServerFile(context, url, filename);
        }
        File file = new File(context.getFilesDir(), filename);
        boolean isFileOld = !file.exists() || new Date().getTime() - file.lastModified() > MILLISECONDS.convert(cacheDays, TimeUnit.DAYS);
        if (isFileOld) { // vamos buscar o original e guardamos para cache
            InputStream inputStream = new URL(url).openStream();
            FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            int read;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.close();
        }
        return context.openFileInput(filename);  // retorna o ficheiro do disco
    }
    
    static InputStream tryServerFile(Context context, String url, String filename) throws IOException {
        try {
            return new URL(url).openStream(); // se não há cache.... devolve já sem escrever nada
        } catch (FileNotFoundException notFound) {
            return context.openFileInput(filename);  // vamos prosseguir com a versão em disco
        }
    }
}
