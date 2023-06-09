package mx.edu.utxj.ti.idgs.dwi_frontlol_200070;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText etNumcampeon;
    private EditText etNombre;
    private EditText etDescripcion;
    private EditText etNumAspectos;
    private EditText etlvlMaestria;
    private EditText etPasiva;
    private EditText etRol;
    private EditText etDificultad;
    private EditText etQ;
    private EditText etW;
    private EditText etE;
    private EditText etR;
    private Button btnGuardar;
    private Button btnBuscar;
    private Button btnActualizar;
    private Button btnEliminar;
    private ListView lvCampeones;
    private RequestQueue requestQueue;
    private JsonArrayRequest jsonArrayRequest;
    private ArrayList<String> origenDatos = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private String url = "http://192.168.3.5:3300";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etNumcampeon = findViewById(R.id.etNumcampeon);
        etNombre = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.etDescripcion);
        etNumAspectos = findViewById(R.id.etNumAspectos);
        etlvlMaestria = findViewById(R.id.etlvlMaestria);
        etPasiva = findViewById(R.id.etPasiva);
        etRol = findViewById(R.id.etRol);
        etDificultad = findViewById(R.id.etDificultad);
        etQ = findViewById(R.id.etQ);
        etW = findViewById(R.id.etW);
        etE = findViewById(R.id.etE);
        etR = findViewById(R.id.etR);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnEliminar = findViewById(R.id.btnEliminar);
        lvCampeones = findViewById(R.id.lvCampeones);
        requestQueue = Volley.newRequestQueue(this);
        listarCampeones();

        //Button Save
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etNumcampeon.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Llena los campos", Toast.LENGTH_SHORT).show();
                }else{
                JSONObject campeon = new JSONObject();
                try {
                    campeon.put("numcampeon", etNumcampeon.getText().toString());
                    campeon.put("nombre", etNombre.getText().toString());
                    campeon.put("descripcion",etDescripcion.getText().toString());
                    campeon.put("rol", etRol.getText().toString());
                    campeon.put("numaspectos", etNumAspectos.getText().toString());
                    campeon.put("nivelmaestria", etlvlMaestria.getText().toString());
                    campeon.put("dificultad", etDificultad.getText().toString());
                    campeon.put("pasiva", etPasiva.getText().toString());
                    JSONArray habilidades = new JSONArray();
                    habilidades.put(etQ.getText().toString());
                    habilidades.put(etW.getText().toString());
                    habilidades.put(etE.getText().toString());
                    habilidades.put(etR.getText().toString());
                    campeon.put("habilidales", habilidades);
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        url +"/insertar",
                        campeon,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.getString("status").equals("Campeon insertado")) {
                                        Toast.makeText(MainActivity.this, "Campeon insertado con EXITO!", Toast.LENGTH_SHORT).show();
                                        etNumcampeon.setText("");
                                        etNombre.setText("");
                                        etDescripcion.setText("");
                                        etRol.setText("");
                                        etNumAspectos.setText("");
                                        etlvlMaestria.setText("");
                                        etDificultad.setText("");
                                        etPasiva.setText("");
                                        etQ.setText("");
                                        etW.setText("");
                                        etE.setText("");
                                        etR.setText("");
                                        adapter.clear();
                                        lvCampeones.setAdapter(adapter);
                                        listarCampeones();
                                    }
                                }catch (JSONException e) {
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                );
                requestQueue.add(jsonObjectRequest);
            }
            }
        });

        //Button Search
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonObjectRequest peticion = new JsonObjectRequest(
                        Request.Method.GET,
                        url + "/" + etNumcampeon.getText().toString(),
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (response.has("status"))
                                    Toast.makeText(MainActivity.this, "Campeon no encontrado", Toast.LENGTH_SHORT).show();
                                else {
                                    try {
                                        etNumcampeon.setText(response.getString("numcampeon"));
                                        etNombre.setText(response.getString("nombre"));
                                        etDescripcion.setText(response.getString("descripcion"));
                                        etRol.setText(response.getString("rol"));
                                        etNumAspectos.setText(String.valueOf(response.getInt("numaspectos")));
                                        etlvlMaestria.setText(response.getString("nivelmaestria"));
                                        etDificultad.setText(response.getString("dificultad"));
                                        etPasiva.setText(response.getString("pasiva"));
                                        JSONArray habilidadesArray = response.getJSONArray("habilidales");
                                        String ability1 = habilidadesArray.getString(0);
                                        String ability2 = habilidadesArray.getString(1);
                                        String ability3 = habilidadesArray.getString(2);
                                        String ability4 = habilidadesArray.getString(3);
                                        etQ.setText(ability1);
                                        etW.setText(ability2);
                                        etE.setText(ability3);
                                        etR.setText(ability4);
                                        adapter.clear();
                                        lvCampeones.setAdapter(adapter);
                                        listarCampeones();

                                    } catch (JSONException e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                requestQueue.add(peticion);
            }
        });

        //Button Update
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etNumcampeon.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Primero use el BOTÓN BUSCAR!", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject campeones = new JSONObject();
                    try {
                        campeones.put("numcampeon", etNumcampeon.getText().toString());

                        if (!etNombre.getText().toString().isEmpty()) {
                            campeones.put("nombre", etNombre.getText().toString());
                        }

                        if (!etRol.getText().toString().isEmpty()) {
                            campeones.put("rol", etRol.getText().toString());
                        }

                        if (!etlvlMaestria.getText().toString().isEmpty()) {
                            campeones.put("nivelmaestria", etlvlMaestria.getText().toString());
                        }

                        if (!etNumAspectos.getText().toString().isEmpty()) {
                            campeones.put("numaspectos", Float.parseFloat(etNumAspectos.getText().toString()));
                        }

                        if (!etDificultad.getText().toString().isEmpty()) {
                            campeones.put("dificultad", etDificultad.getText().toString());
                        }

                        if (!etPasiva.getText().toString().isEmpty()) {
                            campeones.put("pasiva", etPasiva.getText().toString());
                        }

                        JSONArray habilidadesArray = new JSONArray();

                        if (!etQ.getText().toString().isEmpty()) {
                            habilidadesArray.put(etQ.getText().toString());
                        }

                        if (!etW.getText().toString().isEmpty()) {
                            habilidadesArray.put(etW.getText().toString());
                        }

                        if (!etE.getText().toString().isEmpty()) {
                            habilidadesArray.put(etE.getText().toString());
                        }

                        if (!etR.getText().toString().isEmpty()) {
                            habilidadesArray.put(etR.getText().toString());
                        }

                        campeones.put("habilidales", habilidadesArray);

                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    JsonObjectRequest actualizar = new JsonObjectRequest(
                            Request.Method.PUT,
                            url + "/actualizar/" + etNumcampeon.getText().toString(),
                            campeones,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getString("status").equals("Campeón Actualizado")) {
                                            Toast.makeText(MainActivity.this, "Campeon actualizado con EXITO!", Toast.LENGTH_SHORT).show();
                                            etNumcampeon.setText("");
                                            etNombre.setText("");
                                            etDescripcion.setText("");
                                            etRol.setText("");
                                            etNumAspectos.setText("");
                                            etlvlMaestria.setText("");
                                            etDificultad.setText("");
                                            etPasiva.setText("");
                                            etQ.setText("");
                                            etW.setText("");
                                            etE.setText("");
                                            etR.setText("");
                                            adapter.clear();
                                            lvCampeones.setAdapter(adapter);
                                            listarCampeones();
                                        } else if (response.getString("status").equals("Not Found")) {
                                            Toast.makeText(MainActivity.this, "Campeón no encontrado", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                    requestQueue.add(actualizar);
                }
            }
        });

        //Button Delete
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etNumcampeon.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Ingrese el Número de Campeón", Toast.LENGTH_SHORT).show();
                } else {
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.DELETE,
                            url + "/delete/" + etNumcampeon.getText().toString(),
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getString("status").equals("Campeón eliminado")) {
                                            Toast.makeText(MainActivity.this, "Campeón Eliminado con EXITO!", Toast.LENGTH_SHORT).show();
                                            etNumcampeon.setText("");
                                            etNombre.setText("");
                                            etDescripcion.setText("");
                                            etRol.setText("");
                                            etNumAspectos.setText("");
                                            etlvlMaestria.setText("");
                                            etDificultad.setText("");
                                            etPasiva.setText("");
                                            etQ.setText("");
                                            etW.setText("");
                                            etE.setText("");
                                            etR.setText("");
                                            adapter.clear();
                                            lvCampeones.setAdapter(adapter);
                                            listarCampeones();
                                        } else if (response.getString("status").equals("Not Found")) {
                                            Toast.makeText(MainActivity.this, "Campeón no encontrado", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                    requestQueue.add(jsonObjectRequest);
                }
            }
        });

    }


    //list champs
    protected void listarCampeones(){

        jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i = 0 ; i<response.length();i++){
                            try {
                                String Nombre = response.getJSONObject(i).getString("nombre");
                                String Rol = response.getJSONObject(i).getString("rol");
                                String Numero = response.getJSONObject(i).getString("numcampeon");
                                origenDatos.add(Numero+" -> "+Nombre+" -> "+Rol);
                            } catch (JSONException e) {

                            }
                        }
                        adapter = new ArrayAdapter<>(MainActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, origenDatos);
                        lvCampeones.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }
}