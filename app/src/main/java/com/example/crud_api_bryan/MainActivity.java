package com.example.crud_api_bryan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Adapter adapter;
    public static ArrayList<Usuarios> usuariosArrayList = new ArrayList<>();


    Usuarios usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView =findViewById(R.id.list);
        adapter=new Adapter(this,usuariosArrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
               // ProgressDialog progessDialog =new ProgressDialog(view.getContext());

                CharSequence[]dialogo={"VER DATOS","EDITAR DATOS","ELIMINAR DATOS"};

                builder.setTitle(usuariosArrayList.get(position).getNombre());

                builder.setItems(dialogo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i){

                            case 0:
                                startActivity(new Intent(getApplicationContext(),Detalles.class).putExtra("position",position));
                                break;
                            case 1:
                                startActivity(new Intent(getApplicationContext(), Editar.class).putExtra("position",position));
                                break;
                            case 2:
                              //  startActivity(usuariosArrayList.get(position).getId());
                                break;
                        }
                    }
                });
                builder.show();
            }
        });



        ListarDatos();
    }

    public void agregar(View view) {
        Intent intent = new Intent(MainActivity.this, Agregar.class);
        startActivity(intent);
    }
    public void ListarDatos(){
        StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.1.24/crud_android/mostrar_.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        usuariosArrayList.clear();
                        try {
                            JSONObject jsonObject =new JSONObject(response);
                            String exito=jsonObject.getString("exito");
                            JSONArray jsonArray =jsonObject.getJSONArray("datos");

                            if (exito.equals("1")){
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject object=jsonArray.getJSONObject(i);
                                    String id=object.getString("id");
                                    String nombre=object.getString("nombre");
                                    String correo=object.getString("correo");
                                    String direccion=object.getString("direccion");

                                    usuarios =new Usuarios(id,nombre,correo,direccion);
                                    usuariosArrayList.add(usuarios);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){

            }
        }
        ){

        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);

    }


}
