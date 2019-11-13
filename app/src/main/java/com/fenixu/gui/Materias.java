package com.fenixu.gui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
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
import com.fenixu.logica_negocio.DialogoCrearMateria;
import com.fenixu.logica_negocio.Materia;
import com.fenixu.logica_negocio.MateriasBase;
import com.fenixu.logica_negocio.Nota;
import com.fenixu.logica_negocio.Semestre;
import com.fenixu.logica_negocio.SemestresBase;
import com.fenixu.recursos_datos.AdminSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Materias extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView lista;
    private List<Materia> materias;
    private AdaptadorMateriasNotas adaptadorMateriasNotas;
    private String extraout = "";
    private Semestre semestreActual = new Semestre();

    //nombre, porcentaje, nota actual, nota necesaria, creditos
    List<List<String>> itemMateria = new ArrayList<List<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);

        //Menu superior
        toolbar = findViewById(R.id.toolbarNotas);
        toolbar.setTitle("NOTAS");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorFondos));
        setSupportActionBar(toolbar);

        //Pone los valores en el listView
        lista = findViewById(R.id.listViewNotas);
        if(materias != null) if(materias.size()>0) materias.clear();
        semestreActual = SemestresBase.get(getApplicationContext()).getsemestreActual();
        if(!semestreActual.isDeafault()){
            materias = MateriasBase.get(getApplicationContext()).getMaterias(Integer.toString(semestreActual.getId()));
            adaptadorMateriasNotas = new AdaptadorMateriasNotas(Materias.this, materias);
            lista.setAdapter(adaptadorMateriasNotas);
        }else{Log.d("Materias", "Aun no hay semestres");}

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
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(Materias.this);

            final View mView = getLayoutInflater().inflate(R.layout.dialogo_agregar_materia2, null);
            final TextView titulo = mView.findViewById(R.id.txdialogo_agregar_alarma);
            titulo.setText("Agregar materia");
            final EditText mTituloAlarma = mView.findViewById(R.id.etTituloAlarma);
            mTituloAlarma.setHint("Nombre de la materia");
            final TextView creditostx = mView.findViewById(R.id.etMateriaSemestre);
            creditostx.setHint("Número de créditos");
            Button mCrear = mView.findViewById(R.id.btnCrearAlarma);
            mCrear.setText("Crear");
            mBuilder.setView(mView);

            final AlertDialog dialog = mBuilder.create();

            mCrear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!mTituloAlarma.getText().toString().isEmpty() && !creditostx.getText().toString().isEmpty()){
                        Materia semestre = new Materia();
                        semestre.setTitulo(mTituloAlarma.getText().toString());
                        semestre.setCreditos(Integer.parseInt(creditostx.getText().toString()));
                        semestre.setIdSemestre(semestreActual.getId());

                        MateriasBase.get(Materias.this).guardarMateria(semestre);
                        dialog.dismiss();

                        Intent intent = new Intent(getApplicationContext(), Materias.class);
                        startActivity(intent);
                        terminar();
                    }else{
                        Toast.makeText(Materias.this, "Pon un título", Toast.LENGTH_SHORT).show();
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