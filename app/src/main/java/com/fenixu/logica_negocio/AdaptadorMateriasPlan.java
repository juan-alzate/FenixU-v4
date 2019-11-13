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

import java.util.List;

public class AdaptadorMateriasPlan extends BaseAdapter {

    private Context context;
    private List<Materia> materias;

    public AdaptadorMateriasPlan(Context context, List<Materia> materias) {
        this.context = context;
        this.materias = materias;
    }

    @Override
    public int getCount() {
        return materias.size();
    }

    @Override
    public Object getItem(int i) {
        return materias.get(i);
    }

    @Override
    public long getItemId(int i) {
        return (long) materias.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null) view = View.inflate(context, R.layout.elemento_lista_materia_plan, null);

        //Seran usados en los onclick
        final View viewin = view;
        final int iin = i;
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        final Materia materia = materias.get(i);

        //Pone titulo a la materia y onclick para actualizar
        TextView tituloMateria = view.findViewById(R.id.nombreMateriaPlan);
        tituloMateria.setText(materia.getTitulo());
        tituloMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final View mView = View.inflate(context, R.layout.dialogo_agregar_alarma, null);
                final TextView titulo =  mView.findViewById(R.id.txdialogo_agregar_alarma);
                titulo.setText("Nombre Materia");
                final EditText mTituloAlarma = mView.findViewById(R.id.etTituloAlarma);
                mTituloAlarma.setHint("Nombre");
                Button mCrear = mView.findViewById(R.id.btnCrearAlarma);
                mCrear.setText("Cambiar");
                mBuilder.setView(mView);

                final AlertDialog dialog = mBuilder.create();

                mCrear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!mTituloAlarma.getText().toString().isEmpty()){
                            materia.setTitulo(mTituloAlarma.getText().toString());
                            MateriasBase.get(context).actualizarMateria(materia);
                            dialog.dismiss();
                            notifyDataSetChanged();
                            refresca();
                        }else{
                            Toast.makeText(context,"Pon un nombre",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });

        //Pone promedio al materia
        TextView promedioMateria = view.findViewById(R.id.itemPorcentajeMateriaPlan);
        promedioMateria.setText(Double.toString(materia.getPromedio()));
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
                            materia.setPromedio(Double.parseDouble(mTituloAlarma.getText().toString()));
                            MateriasBase.get(context).actualizarMateria(materia);
                            dialog.dismiss();
                            notifyDataSetChanged();
                            refresca();
                        }else{
                            Toast.makeText(context,"¿Qué nota sacaste?",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });

        //Pone creditos al materia
        TextView creditosMateria = view.findViewById(R.id.itemCreditosMateriaPlan);
        creditosMateria.setText(Integer.toString(materia.getCreditos()));
        creditosMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View mView = View.inflate(context, R.layout.dialogo_agregar_alarma, null);
                final TextView titulo =  mView.findViewById(R.id.txdialogo_agregar_alarma);
                titulo.setText("Créditos Materia");
                final EditText mTituloAlarma = mView.findViewById(R.id.etTituloAlarma);
                mTituloAlarma.setHint("Créditos");
                Button mCrear = mView.findViewById(R.id.btnCrearAlarma);
                mCrear.setText("Cambiar");
                mBuilder.setView(mView);

                final AlertDialog dialog = mBuilder.create();

                mCrear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!mTituloAlarma.getText().toString().isEmpty()){
                            materia.setCreditos(Integer.parseInt(mTituloAlarma.getText().toString()));
                            MateriasBase.get(context).actualizarMateria(materia);
                            dialog.dismiss();
                            notifyDataSetChanged();
                            refresca();
                        }else{
                            Toast.makeText(context,"¿Cuántos créditos?",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });

        ImageButton eliminar = view.findViewById(R.id.btnEliminarMateriaPlan);
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MateriasBase.get(context).eliminarMateria(materia);
                String idSemestre = Integer.toString(materias.get(0).getIdSemestre());
                materias.clear();
                materias = MateriasBase.get(context).getMaterias(idSemestre);
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
        Intent i = new Intent(context, MateriasPlanAcademico.class);
        i.putExtra("valor", "prueba");
        context.startActivity( i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), b);
    }
}