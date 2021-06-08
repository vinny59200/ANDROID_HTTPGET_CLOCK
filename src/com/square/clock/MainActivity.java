 package com.square.clock;
 
	import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import android.os.AsyncTask;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

    public class MainActivity extends Activity  {
    	TextView et;
    	private static ProgressBar progressBar;
    	   
/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    et = (TextView) findViewById(R.id.AutoCompleteTextView01);
    progressBar = (ProgressBar) findViewById(R.id.progressBar1);
    new Task().execute();	
    // et.setText(new Task().doInBackground((Void[]) null));
}

@Override
protected void onPause() {

    super.onPause();

    System.exit(0);

}

private class Task extends AsyncTask<Void, Void, String> {

	
    @Override
    protected String doInBackground(Void... params) {
   
    	System.out.println("vv in doinbackgrouund");    	         progressBar.setProgress(20);
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpGet httpGet = new HttpGet(
                "http://www.earthtools.org/timezone/51.5085300/-0.1257400");
    	System.out.println("vv in doinbackgrouund 0");    	         progressBar.setProgress(40);
        String text = null;
        try {
            HttpResponse response = httpClient.execute(httpGet,
                    localContext);
        	System.out.println("vv in doinbackgrouund 1");    	         progressBar.setProgress(60);
            HttpEntity entity = response.getEntity();
            try{
            	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(entity.getContent());
                
            	//optional, but recommended
            	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            	doc.getDocumentElement().normalize();
             
            	System.out.println("vv Root element :" + doc.getDocumentElement().getNodeName());    	         progressBar.setProgress(80);
             
            	NodeList nList = doc.getElementsByTagName("timezone");
             
            	System.out.println("----------------------------"+nList.getLength());
             
            	for (int temp = 0; temp < nList.getLength(); temp++) {
             
            		Node nNode = nList.item(temp);
             
            		System.out.println("\nCurrent Element :" + nNode.getNodeName());
             
            		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
             
            			Element eElement = (Element) nNode;
             
            			
            			System.out.println("Time : " + eElement.getElementsByTagName("localtime").item(0).getTextContent());
            			text = eElement.getElementsByTagName("localtime").item(0).getTextContent();
            		}
            	}
                } catch (Exception e) {
            	e.printStackTrace();
                }

            
            
        } catch (Exception e) {
            return e.getLocalizedMessage();
        }
        return text;
    }

   @Override
protected void onPostExecute(String result) {
	// TODO Auto-generated method stub
	super.onPostExecute(result);

    progressBar.setProgress(100);
   
	et.setText(result);
}
}


}