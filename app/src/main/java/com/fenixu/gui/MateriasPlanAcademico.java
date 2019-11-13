package com.fenixu.gui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.fenixu.R;
import com.fenixu.logica_negocio.AdaptadorAlarmas;
import com.fenixu.logica_negocio.AdaptadorMateriasPlan;
import com.fenixu.logica_negocio.Alarma;
import com.fenixu.logica_negocio.AlarmasBase;
import com.fenixu.logica_negocio.Materia;
import com.fenixu.logica_negocio.MateriasBase;
import com.fenixu.logica_negocio.Semestre;
import com.fenixu.logica_negocio.SemestresBase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.shape.MaterialShapeDrawable;

import java.util.ArrayList;
import java.util.List;

public class MateriasPlanAcademico extends AppCompatActivity {


    private Toolbar toolbar;
    private ListView listView;
    private List<Materia> materias;
    private AdaptadorMateriasPlan adaptadorMaterias;
    private String extraout = ""; //Auxiliar para identificar Activity que se debe cerrar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Busca vista de la actividad
        setContentView(R.layout.activity_materias_plan);

        //Cierra Activity auxiliar
        String msg = (getIntent().getStringExtra("valor") == null)? "": getIntent().getStringExtra("valor");
        if(!msg.isEmpty()) {extraout = msg; finish();}

        //Menu superior
        toolbar = (Toolbar)findViewById(R.id.toolbarMateriasPlan);
        toolbar.setTitle(getIntent().getStringExtra("titulo"));
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorFondos));
        setSupportActionBar(toolbar);


        //Pone valores al listview
        listView = findViewById(R.id.listViewMateriasPlan);
        if(materias != null) if(materias.size()>0) materias.clear();
        materias = MateriasBase.get(getApplicationContext()).getMaterias(getIntent().getStringExtra("semestre"));
        adaptadorMaterias = new AdaptadorMateriasPlan(MateriasPlanAcademico.this, materias);
        listView.setAdapter(adaptadorMaterias);

        //Fija orientaciond de pantalla
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
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MateriasPlanAcademico.this);
            final View mView = View.inflate(MateriasPlanAcademico.this, R.layout.dialogo_agregar_materia_semestre, null);

            final EditText titulo =  mView.findViewById(R.id.nombreAgregarMateriaSemestre);
            titulo.setHint("Nombre de la materia");

            final EditText notaFinal = mView.findViewById(R.id.nombrePromedioMateriaSemestre);
            notaFinal.setHint("Nota final");

            final EditText creditos = mView.findViewById(R.id.numeroAgregarMateriaCreditos);
            creditos.setHint("Número de créditos");

            Button mCrear = mView.findViewById(R.id.btnAgregarMateriaSemestre);

            mBuilder.setView(mView);

            final AlertDialog dialog = mBuilder.create();

            mCrear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!titulo.getText().toString().isEmpty()
                     && !notaFinal.getText().toString().isEmpty()
                     && !creditos.getText().toString().isEmpty()){

                        Materia materia = new Materia();
                        materia.setTitulo(titulo.getText().toString());
                        materia.setPromedio(Double.parseDouble(notaFinal.getText().toString()));
                        materia.setCreditos(Integer.parseInt(creditos.getText().toString()));
                        materia.setIdSemestre(Integer.parseInt(getIntent().getStringExtra("semestre")));

                        MateriasBase.get(MateriasPlanAcademico.this).guardarMateria(materia);

                        dialog.dismiss();

                        Intent intent = new Intent(getApplicationContext(), MateriasPlanAcademico.class);
                        intent.putExtra("titulo", getIntent().getStringExtra("titulo"));
                        intent.putExtra("semestre", getIntent().getStringExtra("semestre"));
                        startActivity(intent);
                        terminar();
                    } else {
                        Toast.makeText(MateriasPlanAcademico.this,"Llena los datos",Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("MateriasPlanAcademico", "Llendo atrás");
    }
}
