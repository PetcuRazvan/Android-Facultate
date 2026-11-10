package ro.ase.semdam_1085;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    public ListView listView;

    List<Student> listaStud = new ArrayList<>();

    public static final int REQUEST_CODE_ADD = 100;

    public static final int REQUEST_CODE_EDIT = 200;

    public static final String EDIT_STUDENT = "editStudent";

    public int poz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listView = findViewById(R.id.listView);

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                //startActivity(intent);
                startActivityForResult(intent, REQUEST_CODE_ADD);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

                Student student = listaStud.get(position);

                //ArrayAdapter adapter = (ArrayAdapter) listView.getAdapter();
                CustomAdapter adapter = (CustomAdapter) listView.getAdapter();

                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Confirmare stergere")
                        .setMessage("Doriti sa stergeti?")
                        .setNegativeButton("NU", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setPositiveButton("DA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                listaStud.remove(student);
                                adapter.notifyDataSetChanged();
                                dialogInterface.cancel();
                            }
                        })
                        .create();

                dialog.show();

                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                poz = position;
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                intent.putExtra(EDIT_STUDENT, listaStud.get(position));
                startActivityForResult(intent, REQUEST_CODE_EDIT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_ADD && resultCode==RESULT_OK&& data!=null)
        {
            Student student = (Student) data.getSerializableExtra(AddActivity.ADD_STUDENT);
            if(student!=null)
            {
                listaStud.add(student);
                /*Toast.makeText(getApplicationContext(), student.toString(),
                        Toast.LENGTH_LONG).show();*/
               /* ArrayAdapter<Student> adapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, listaStud);*/
                CustomAdapter adapter = new CustomAdapter(getApplicationContext(), R.layout.elem_listview,
                        listaStud, getLayoutInflater()){
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);

                        Student student1 = listaStud.get(position);

                        TextView tvMedie = view.findViewById(R.id.tvMedie);
                        if(student1.getMedie()>=5)
                            tvMedie.setTextColor(Color.GREEN);
                        else
                            tvMedie.setTextColor(Color.RED);

                        return view;
                    }
                };
                listView.setAdapter(adapter);
            }
        }
        else
        if(requestCode==REQUEST_CODE_EDIT && resultCode==RESULT_OK&& data!=null)
        {
            Student student = (Student) data.getSerializableExtra(AddActivity.ADD_STUDENT);
            if(student!=null)
            {
                listaStud.get(poz).setNume(student.getNume());
                listaStud.get(poz).setDataNasterii(student.getDataNasterii());
                listaStud.get(poz).setMedie(student.getMedie());
                listaStud.get(poz).setFacultate(student.getFacultate());
                listaStud.get(poz).setAreBursa(student.isAreBursa());

                //ArrayAdapter<Student> adapter = (ArrayAdapter<Student>) listView.getAdapter();
                CustomAdapter adapter = (CustomAdapter) listView.getAdapter();
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.meniu_principal, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.optiune1)
        {
            Intent intent = new Intent(this, BNRActivity.class);
            startActivity(intent);

            return true;
        }
        else if(item.getItemId()==R.id.optiune2)
        {
            ExtractXML extractXML = new ExtractXML()
            {
                @Override
                protected void onPostExecute(InputStream inputStream) {
                    listaStud.addAll(studentList);

                    CustomAdapter adapter = new CustomAdapter(getApplicationContext(), R.layout.elem_listview,
                            listaStud, getLayoutInflater()){
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);

                            Student student1 = listaStud.get(position);

                            TextView tvMedie = view.findViewById(R.id.tvMedie);
                            if(student1.getMedie()>=5)
                                tvMedie.setTextColor(Color.GREEN);
                            else
                                tvMedie.setTextColor(Color.RED);

                            return view;
                        }
                    };
                    listView.setAdapter(adapter);
                }
            };
            try {
                extractXML.execute(new URL("https://pastebin.com/raw/v0diWZJG"));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        else if(item.getItemId()==R.id.optiune3)
        {
            ExtractJson extractJSON = new ExtractJson() {
              ProgressDialog progressDialog;

                @Override
                protected void onPreExecute() {
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Please wait...");

                    progressDialog.show();
                }

                @Override
                protected void onPostExecute(String s) {
                    progressDialog.cancel();

                    listaStud.addAll(this.studentListJSON);

                    CustomAdapter adapter = new CustomAdapter(getApplicationContext(), R.layout.elem_listview,
                            listaStud, getLayoutInflater()){
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);

                            Student student1 = listaStud.get(position);

                            TextView tvMedie = view.findViewById(R.id.tvMedie);
                            if(student1.getMedie()>=5)
                                tvMedie.setTextColor(Color.GREEN);
                            else
                                tvMedie.setTextColor(Color.RED);

                            return view;
                        }
                    };
                    listView.setAdapter(adapter);
                }
            };

            try {
                extractJSON.execute(new URL("https://pastebin.com/raw/Z8ajeUhk"));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        else if(item.getItemId()==R.id.optiune4)
        {
            ExtractTxt extractTxt = new ExtractTxt() {
                ProgressDialog progressDialog;

                @Override
                protected void onPreExecute() {
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Please wait...");

                    progressDialog.show();
                }

                @Override
                protected void onPostExecute(String s) {
                    progressDialog.cancel();

                    listaStud.addAll(this.studentList);

                    CustomAdapter adapter = new CustomAdapter(getApplicationContext(), R.layout.elem_listview,
                            listaStud, getLayoutInflater()){
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);

                            Student student1 = listaStud.get(position);

                            TextView tvMedie = view.findViewById(R.id.tvMedie);
                            if(student1.getMedie()>=5)
                                tvMedie.setTextColor(Color.GREEN);
                            else
                                tvMedie.setTextColor(Color.RED);

                            return view;
                        }
                    };
                    listView.setAdapter(adapter);
                }
            };

            try {
                extractTxt.execute(new URL("https://pastebin.com/raw/LByWg9sT"));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            return true;
        }

        return false;
    }
}
