package ro.ase.semdam_1085;

import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ExtractTxt extends AsyncTask<URL, Void, String> {

    public List<Student> studentList = new ArrayList<>();

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


            parsareTxt(rezultat);

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

    public static String getAttributeValue(Node node, String attrName) {
        try {
            return ((Element)node).getAttribute(attrName);
        }
        catch (Exception e) {
            return "";
        }
    }

    public void parsareTxt(String txtStr) {
        String[] linii = txtStr.split("\\r?\\n");

        for (int i = 0; i < linii.length; i++) {
            String linie = linii[i];

            String[] campuri = linie.split(";");

            String nume = campuri[0];
            Date dataNasterii = new Date(campuri[1]);
            float medie = Float.parseFloat(campuri[2]);
            String facultate = campuri[3];
            boolean areBursa = Boolean.parseBoolean(campuri[4]);

            Student student = new Student(nume, dataNasterii, medie, facultate, areBursa);

            studentList.add(student);
        }
    }
}
