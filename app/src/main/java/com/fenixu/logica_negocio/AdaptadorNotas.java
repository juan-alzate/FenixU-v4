package com.fenixu.logica_negocio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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
import com.fenixu.gui.Materias;
import com.fenixu.gui.MateriasPlanAcademico;
import com.fenixu.gui.Notas;

import java.util.List;

public class AdaptadorNotas extends BaseAdapter {

    private Context context;
    private List<Nota> notas;
    private String titulo;
    private String idMateria;

    public AdaptadorNotas(Context context, List<Nota> notas) {
        this.context = context;
        this.notas = notas;
    }

    @Override
    public int getCount() {
        return notas.size();
    }

    @Override
    public Object getItem(int i) {
        return notas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return notas.get(i).getIdNota();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) view = View.inflate(context, R.layout.elemento_lista_nota, null);

        //Seran usados en los onclick
        final View viewin = view;
        final int iin = i;
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        final Nota materia = notas.get(i);

        idMateria = Integer.toString(notas.get(0).getIdMateria());
        titulo = MateriasBase.get(context).getMateria(idMateria).getTitulo();

        //Pone titulo a la materia y onclick para actualizar
        TextView tituloMateria = view.findViewById(R.id.listaNota);
        tituloMateria.setText(Double.toString(materia.getNota()));
        tituloMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final View mView = View.inflate(context, R.layout.dialogo_agregar_alarma, null);

                final TextView titulo =  mView.findViewById(R.id.txdialogo_agregar_alarma);
                titulo.setText("Valor de la nota");

                final EditText mTituloAlarma = mView.findViewById(R.id.etTituloAlarma);
                mTituloAlarma.setHint("Nota");

                Button mCrear = mView.findViewById(R.id.btnCrearAlarma);
                mCrear.setText("Cambiar");
                mBuilder.setView(mView);

                final AlertDialog dialog = mBuilder.create();

                mCrear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!mTituloAlarma.getText().toString().isEmpty()){

                            materia.setNota(Double.parseDouble(mTituloAlarma.getText().toString()));

                            NotasBase.get(context).actualizarnota(materia);

                            dialog.dismiss();

                            notifyDataSetChanged();
                            refresca();
                        } else {
                            Toast.makeText(context,"¿Cuánto sacaste?",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });

        //Pone promedio al materia
        TextView promedioMateria = view.findViewById(R.id.listaPorcentaje);
        promedioMateria.setText(Integer.toString((int) materia.getPorcentaje()));
        promedioMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View mView = View.inflate(context, R.layout.dialogo_agregar_alarma, null);

                final TextView titulo =  mView.findViewById(R.id.txdialogo_agregar_alarma);
                titulo.setText("Nota Final Materia");

                final EditText mTituloAlarma = mView.findViewById(R.id.etTituloAlarma);
                mTituloAlarma.setHint("Nota Final");

                Button mCrear = mView.findViewById(R.id.btnCrearAlarma);
                mCrear.setText("Cambiar");
                mBuilder.setView(mView);

                final AlertDialog dialog = mBuilder.create();

                mCrear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!mTituloAlarma.getText().toString().isEmpty()){

                            materia.setPorcentaje(Double.parseDouble(mTituloAlarma.getText().toString()));

                            NotasBase.get(context).actualizarnota(materia);

                            dialog.dismiss();

                            notifyDataSetChanged();
                            refresca();
                        } else {
                            Toast.makeText(context,"¿Cuánto porcentaje vale para la materia?",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });


        ImageButton eliminar = view.findViewById(R.id.btnEliminarNotas);
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotasBase.get(context).eliminarnota(materia);
                String idSemestre = Integer.toString(notas.get(0).getIdMateria());
                notas.clear();
                notas = NotasBase.get(context).getnotas(Integer.parseInt(idSemestre));
                //Refresca los datos
                notifyDataSetChanged();
                refresca();
            }
        });

        return view;
    }

    //"Refresca" Materias plan activity
    private void refresca(){
        Bundle b = new Bundle();
        b.putString("valor", "prueba");
        Intent i = new Intent(context, Notas.class);
        i.putExtra("valor", "prueba");
        i.putExtra("idMateria", idMateria);
        i.putExtra("titulo", titulo);
        context.startActivity( i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), b);
    }
}
