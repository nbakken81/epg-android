package pt.mobiledev.tvalarmes.dao;

import android.util.Xml;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.xmlpull.v1.XmlPullParser;
import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;
import org.xmlpull.v1.XmlPullParserException;

public class XmlParser {

    public static XmlPullParser getParser(String url, String startTag) throws IOException, XmlPullParserException {
        InputStream in = new URL(url).openStream();
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in, null);
        parser.nextTag();
        parser.require(XmlPullParser.START_TAG, null, startTag);
        return parser;
    }

    public static void skipElement(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case END_TAG:
                    depth--;
                    break;
                case START_TAG:
                    depth++;
                    break;
            }
        }
    }

    public static String readTextElement(XmlPullParser parser, String tagName) throws IOException, XmlPullParserException {
        parser.require(START_TAG, null, tagName);
        String result = "";
        if (parser.next() == TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        parser.require(END_TAG, null, tagName);
        return result;
    }
}
