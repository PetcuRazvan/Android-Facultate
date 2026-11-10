package ro.ase.semdam_1085;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ExtractJson extends AsyncTask<URL, Void, String> {

    public List<Student> studentListJSON=new ArrayList<>();

    @Override
    protected String doInBackground(URL... urls) {

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) urls[0].openConnection();
            connection.setRequestMethod("GET");
            InputStream ist = connection.getInputStream();



            //var2 - conversie String
            InputStreamReader isr = new InputStreamReader(ist);
            BufferedReader br = new BufferedReader(isr);
            String linie = null;
            String rezultat="";
            while((linie = br.readLine())!=null)
                rezultat+=linie;


            //parsare JSON
            parsareJSON(rezultat);


            return rezultat;


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static Node getNodeByName(String nodeName, Node parentNode) throws Exception {

        if (parentNode.getNodeName().equals(nodeName)) {
            return parentNode;
        }

        NodeList list = parentNode.getChildNodes();
        for (int i = 0; i < list.getLength(); i++)
        {
            Node node = getNodeByName(nodeName, list.item(i));
            if (node != null) {
                return node;
            }
        }
        return null;
    }

    public void parsareJSON(String jsonStr){
        try {
            JSONObject jsonObject=new JSONObject(jsonStr);
            JSONArray studenti=jsonObject.getJSONArray("studenti");
            for(int i=0;i<studenti.length();i++){
                JSONObject obj=studenti.getJSONObject(i);
                String nume=obj.getString("Nume");
                Date dataNasterii=new Date(obj.getString("DataNasterii"));
                float medie=Float.parseFloat(obj.getString("Medie"));
                String facultate=obj.getString("Facultate");

                boolean areBursa=Boolean.parseBoolean(obj.getString("AreBursa"));

                Student student=new Student(nume, dataNasterii,medie,facultate,areBursa);
                studentListJSON.add(student);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


}