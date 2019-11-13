package com.fenixu.gui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.fenixu.R;
import com.fenixu.logica_negocio.AdaptadorAgregarNotas;
import com.fenixu.logica_negocio.AdaptadorMateriasPlan;
import com.fenixu.logica_negocio.AdaptadorNotas;
import com.fenixu.logica_negocio.DialogoAgregarNota;
import com.fenixu.logica_negocio.Materia;
import com.fenixu.logica_negocio.MateriasBase;
import com.fenixu.logica_negocio.MateriasNotas;
import com.fenixu.logica_negocio.Nota;
import com.fenixu.logica_negocio.NotasBase;
import com.fenixu.recursos_datos.AdminSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Notas extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;
    private List<Nota> notas;
    private AdaptadorNotas adaptadorNotas;
    private String extraout = ""; //Auxiliar para identificar Activity que se debe cerrar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_notas_materias);

        toolbar = (Toolbar)findViewById(R.id.toolbarAgregarNotas);
        toolbar.setTitle(getIntent().getStringExtra("titulo"));
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorFondos));
        setSupportActionBar(toolbar);

        //Cierra Activity auxiliar
        String msg = (getIntent().getStringExtra("valor") == null)? "": getIntent().getStringExtra("valor");
        if(!msg.isEmpty()) {extraout = msg; finish();}

        //Pone los valores al listview
        listView = (ListView)findViewById(R.id.listViewAgregarNotas);
        if(notas != null) if(notas.size()>0) notas.clear();
        notas = NotasBase.get(getApplicationContext()).getnotas(Integer.parseInt(getIntent().getStringExtra("idMateria")));
        adaptadorNotas = new AdaptadorNotas(Notas.this, notas);
        listView.setAdapter(adaptadorNotas);

        //Evitamos que el programa se voltee horizontalmente
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    //Items del menu superior(agregar, eliminar)
    public boolean onCreateOptionsMenu(Menu menu){
        Drawable icon = getResources().getDrawable(R.drawable.icono_agregar);
        icon.setColorFilter(getResources().getColor(R.color.colorFondos), PorterDuff.Mode.SRC_IN);

        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    //Metodo que se ejecuta al dar click a los items del menu superior
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id==R.id.agregar){
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(Notas.this);
            final View mView = View.inflate(Notas.this, R.layout.dialogo_agregar_nota, null);

            final EditText titulo =  mView.findViewById(R.id.notaMateria);
            titulo.setHint("Nota");

            final EditText notaFinal = mView.findViewById(R.id.porcentajeNota);
            notaFinal.setHint("Porcentaje");

            Button mCrear = mView.findViewById(R.id.btnCrearNota);
            mBuilder.setView(mView);

            final AlertDialog dialog = mBuilder.create();

            mCrear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!titulo.getText().toString().isEmpty() && !notaFinal.getText().toString().isEmpty()){
                        Nota materia = new Nota();
                        materia.setNota(Double.parseDouble(titulo.getText().toString()));
                        materia.setPorcentaje(Integer.parseInt(notaFinal.getText().toString()));
                        materia.setIdMateria(Integer.parseInt(getIntent().getStringExtra("idMateria")));

                        NotasBase.get(Notas.this).guardarnota(materia);
                        dialog.dismiss();

                        Intent intent = new Intent(getApplicationContext(), Notas.class);
                        intent.putExtra("titulo", getIntent().getStringExtra("titulo"));
                        intent.putExtra("idMateria", getIntent().getStringExtra("idMateria"));
                        startActivity(intent);
                        terminar();
                    }else{
                        Toast.makeText(Notas.this,"Llena los datos",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void terminar(){
        finish();
    }

    //Boton para regresar del celular
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, Materias.class);
        startActivity(intent);
        finish();
    }
}