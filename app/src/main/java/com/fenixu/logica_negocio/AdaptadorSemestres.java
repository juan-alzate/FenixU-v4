package com.fenixu.logica_negocio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.fenixu.R;
import com.fenixu.gui.MateriasPlanAcademico;
import com.fenixu.gui.Semestres;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorSemestres extends BaseAdapter {

    private Context context;
    private List<Semestre> semestres;

    public AdaptadorSemestres(Context context, List<Semestre> semestres) {
        this.context = context;
        this.semestres = semestres;
    }

    @Override
    public int getCount() {
        return semestres.size();
    }

    @Override
    public Object getItem(int i) {
        return semestres.get(i);
    }

    @Override
    public long getItemId(int i) {
        return (long) semestres.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null) view = View.inflate(context, R.layout.elemento_lista_semestre, null);

        //Seran usados en los onclick
        final View viewin = view;
        final int iin = i;
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        final Semestre semestre = semestres.get(i);

        //Pone titulo al semestre y onclick para actualizar
        TextView tituloSemestre = view.findViewById(R.id.nombreSemestre);
        tituloSemestre.setText(semestre.getTitulo());
        tituloSemestre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final View mView = View.inflate(context, R.layout.dialogo_agregar_alarma, null);

                final TextView titulo =  mView.findViewById(R.id.txdialogo_agregar_alarma);
                titulo.setText("Nombre Semestre");

                final EditText mTituloAlarma = mView.findViewById(R.id.etTituloAlarma);
                mTituloAlarma.setHint("Nombre");

                Button mCrear = mView.findViewById(R.id.btnCrearAlarma);
                mCrear.setText("Cambiar");
                mBuilder.setView(mView);

                final AlertDialog dialog = mBuilder.create();

                mCrear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!mTituloAlarma.getText().toString().isEmpty()){

                            semestre.setTitulo(mTituloAlarma.getText().toString());

                            SemestresBase.get(context).actualizarsemestre(semestre);

                            dialog.dismiss();

                            refresca();
                        } else {
                            Toast.makeText(context,"Pon un nombre",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });

        List<Materia> materias = new ArrayList<>();
        double promedio = 0.0;
        int creditos = 0;
        for(Materia m : MateriasBase.get(context).getMaterias()){
            if(m.getIdSemestre() == semestre.getId()) {
                materias.add(m);
                promedio += (m.getPromedio() * m.getCreditos());
                creditos += m.getCreditos();
            }
        }
        promedio /= creditos;
        promedio = Math.round(promedio *100)/ 100d;

        //Pone promedio al semestre
        TextView promedioSemestre = view.findViewById(R.id.itemPorcentajeSemestre);
        promedioSemestre.setText(Double.toString(promedio));

        //Pone creditos al semestre
        TextView creditosSemestre = view.findViewById(R.id.itemNumeroCreditos);
        creditosSemestre.setText(Integer.toString(creditos));


        ImageButton agregar = view.findViewById(R.id.btnAgregarSemestre);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putString("semestre", Integer.toString(semestre.getId()));
                b.putString("titulo", semestre.getTitulo());
                Intent i = new Intent(context, MateriasPlanAcademico.class);
                i.putExtra("semestre", Integer.toString(semestre.getId()));
                i.putExtra("titulo", semestre.getTitulo());
                context.startActivity( i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), b);
            }
        });

        ImageButton eliminar = view.findViewById(R.id.btnEliminarSemestre);
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SemestresBase.get(context).eliminarsemestre(semestre);
                MateriasBase.get(context).eliminarMaterias(Integer.toString(semestre.getId()));
                semestres.clear();
                semestres = SemestresBase.get(context).getsemestres();
                //Refresca los datos
                notifyDataSetChanged();
                refresca();
            }
        });

        return view;
    }



    //"Refresca" Cronograma activity
    private void refresca(){
        Bundle b = new Bundle();
        b.putString("valor", "prueba");
        Intent i = new Intent(context, Semestres.class);
        i.putExtra("valor", "prueba");
        context.startActivity( i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), b);
    }

}
