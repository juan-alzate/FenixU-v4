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
import android.widget.TextView;
import android.widget.Toast;

import com.fenixu.R;
import com.fenixu.logica_negocio.AdaptadorMateriasNotas;
import com.fenixu.logica_negocio.AdaptadorSemestres;
import com.fenixu.logica_negocio.Alarma;
import com.fenixu.logica_negocio.AlarmasBase;
import com.fenixu.logica_negocio.DialogoCrearMateria;
import com.fenixu.logica_negocio.Semestre;
import com.fenixu.logica_negocio.SemestresBase;
import com.fenixu.recursos_datos.AdminSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Semestres extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listview;
    private List<Semestre> semestres;
    private AdaptadorSemestres adaptadorSemestres;
    private String extraout = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semestres);

        //Menu superior
        toolbar = (Toolbar)findViewById(R.id.toolbarNotas);
        toolbar.setTitle("PLAN ACADÉMICO");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorFondos));
        setSupportActionBar(toolbar);

        //Cierra Activity auxiliar
        String msg = (getIntent().getStringExtra("valor") == null)? "": getIntent().getStringExtra("valor");
        if(!msg.isEmpty()){
            extraout = msg; finish();
        }

        //pone los valores al listView
        listview = (ListView)findViewById(R.id.listViewSemestres);
        if(semestres != null) if(semestres.size()>0) semestres.clear();
        semestres = SemestresBase.get(getApplicationContext()).getsemestres();
        adaptadorSemestres = new AdaptadorSemestres(Semestres.this, semestres);
        listview.setAdapter(adaptadorSemestres);

        //Evitamos que el programa se voltee horizontalmente
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getBooleanExtra("recarga",true)){
            Intent intent = new Intent(this, Semestres.class);
            intent.putExtra("recarga", false);
            startActivity(intent);
            terminar();
        }else{
            getIntent().putExtra("recarga", true);
        }
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
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(Semestres.this);
            final View mView = getLayoutInflater().inflate(R.layout.dialogo_agregar_alarma, null);
            final TextView titulo = mView.findViewById(R.id.txdialogo_agregar_alarma);
            titulo.setText("Agregar semestre");
            final EditText mTituloAlarma = mView.findViewById(R.id.etTituloAlarma);
            mTituloAlarma.setHint("Nombre del semestre");
            Button mCrear = mView.findViewById(R.id.btnCrearAlarma);
            mCrear.setText("Crear");
            mBuilder.setView(mView);

            final AlertDialog dialog = mBuilder.create();

            mCrear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!mTituloAlarma.getText().toString().isEmpty()){
                        Semestre semestre = new Semestre();
                        semestre.setTitulo(mTituloAlarma.getText().toString());
                        SemestresBase.get(Semestres.this).guardarsemestre(semestre);
                        dialog.dismiss();

                        Intent intent = new Intent(getApplicationContext(), Semestres.class);
                        startActivity(intent);
                        terminar();
                    }else{
                        Toast.makeText(Semestres.this, "Pon un título", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void terminar(){finish();}

    //Boton del celular para regresar
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}