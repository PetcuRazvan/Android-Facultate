package ro.ase.semdam_1085;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BNRActivity extends AppCompatActivity {

    InputStream ist = null;
    static CursValutar cv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bnr);

        Log.e("lifecycle", "Apel metoda onCreate");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tvDate = findViewById(R.id.tvDate);
        EditText etEUR = findViewById(R.id.editTextEUR);
        EditText etUSD = findViewById(R.id.editTextUSD);
        EditText etGBP = findViewById(R.id.editTextGBP);
        EditText etXAU = findViewById(R.id.editTextXAU);

        Button btnShow = findViewById(R.id.btnShow);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Network network = new Network(){
                    @Override
                    protected void onPostExecute(InputStream inputStream) {
                        *//*Toast.makeText(getApplicationContext(),
                                Network.rezultat, Toast.LENGTH_LONG).show();*//*
                        tvDate.setText(cv.getDataCurs());
                        etEUR.setText(cv.getCursEUR());
                        etUSD.setText(cv.getCursUSD());
                        etGBP.setText(cv.getCursGBP());
                        etXAU.setText(cv.getCursXAU());
                    }
                };
                try {
                    network.execute(new URL("https://curs.bnr.ro/nbrfxrates.xml"));
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }*/

                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("https://curs.bnr.ro/nbrfxrates.xml");
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            ist = connection.getInputStream();
                            cv = parsareXML(ist);
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                        catch (Exception e)
                        {
                            throw new RuntimeException(e);
                        }
                        //new Handler(Looper.getMainLooper()).post(new Runnable() {
                        BNRActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                    tvDate.setText(cv.getDataCurs());
                                    etEUR.setText(cv.getCursEUR());
                                    etUSD.setText(cv.getCursUSD());
                                    etGBP.setText(cv.getCursGBP());
                                    etXAU.setText(cv.getCursXAU());
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("lifecycle", "Apel metoda onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("lifecycle", "Apel metoda onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("lifecycle", "Apel metoda onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("lifecycle", "Apel metoda onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("lifecycle", "Apel metoda onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("lifecycle", "Apel metoda onDestroy");
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
    public CursValutar parsareXML(InputStream ist)
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document domDoc = db.parse(ist);
            domDoc.getDocumentElement().normalize();

            cv = new CursValutar();

            Node cube = getNodeByName("Cube", domDoc.getDocumentElement());
            if(cube!=null)
            {
                String dataCurs = getAttributeValue(cube, "date");
                cv.setDataCurs(dataCurs);

                NodeList listaCopii = cube.getChildNodes();
                for(int i=0;i<listaCopii.getLength();i++)
                {
                    Node rate = listaCopii.item(i);
                    String atribut = getAttributeValue(rate, "currency");
                    if(atribut.equals("EUR"))
                        cv.setCursEUR(rate.getTextContent());
                    if(atribut.equals("USD"))
                        cv.setCursUSD(rate.getTextContent());
                    if(atribut.equals("GBP"))
                        cv.setCursGBP(rate.getTextContent());
                    if(atribut.equals("XAU"))
                        cv.setCursXAU(rate.getTextContent());
                }
            }

        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return cv;
    }
}