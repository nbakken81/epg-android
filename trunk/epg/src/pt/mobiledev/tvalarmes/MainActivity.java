package pt.mobiledev.tvalarmes;

import java.net.URL;
import java.util.ArrayList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ListView;

public class MainActivity extends Activity {

	ListView lvPrograms;
	Context context = MainActivity.this;
	ArrayList<Program> programs;
	
	private static String TAG = "TVAlarmes";
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = 
			        new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			}
		        	try {   
		    		    /**
		    		    * Create a new instance of the SAX parser
		    		    **/
		    			
		    		    SAXParserFactory saxPF = SAXParserFactory.newInstance();
		    		    SAXParser saxP = saxPF.newSAXParser();
		    		    XMLReader xmlR = saxP.getXMLReader();

		    		    URL url = new URL(Utils.getProgramsURL("RTP2"));
	    
	    		    	Log.v(TAG, url.toString());
		    		    
		    		    /** 
		    		    * Create the Handler to handle each of the XML tags. 
		    		    **/
		    		    XMLHandler myXMLHandler = new XMLHandler();
		    		    xmlR.setContentHandler(myXMLHandler);

		    		    xmlR.parse(new InputSource(url.openStream()));  
		    		         
		    		    programs = myXMLHandler.getPrograms();
		    		    
		    		    lvPrograms = (ListView) findViewById(R.id.lvPrograms);
		    		    lvPrograms.setAdapter(new ProgramsBaseAdapter(context, programs));
		    		    
		    		    for (Program program : programs) {
		    		    	Log.v(TAG, (program.getTitle()));
		    		    }
		    		    
		    		} catch (Exception e) {
		    		    System.out.println("ERRO:" + e);
		    		}
	}


}
