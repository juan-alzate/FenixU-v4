package com.fenixu.logica_negocio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
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
import com.fenixu.gui.Notas;
import com.fenixu.gui.Materias;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorMateriasNotas extends BaseAdapter {

    private static LayoutInflater inflater = null;

    Context context;
    List<Materia> materias;

    public AdaptadorMateriasNotas(Context contexto, List<Materia> itemMateria){
        this.context = contexto;
        this.materias = itemMateria;
    }

    @Override
    public int getCount(){
        return materias.size();
    }

    @Override
    public Object getItem(int position){
        return materias.get(position);
    }

    @Override
    public long getItemId(int position){
        return materias.get(position).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup parent){

        if(view == null) view = View.inflate(context, R.layout.elemento_lista, null);

        //Seran usados en los onclick
        final View viewin = view;
        final int iin = i;
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        final Materia materia = materias.get(i);

        TextView titulo = (TextView) view.findViewById(R.id.nombreMateria);
        titulo.setText(materia.getTitulo());
        titulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View mView = View.inflate(context, R.layout.dialogo_agregar_alarma, null);

                final TextView titulo =  mView.findViewById(R.id.txdialogo_agregar_alarma);
                titulo.setText("Nombre materia");

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

                            materia.setTitulo(mTituloAlarma.getText().toString());

                            MateriasBase.get(context).actualizarMateria(materia);

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

        List<Nota> notas = new ArrayList<>();
        double porcentaje = 0.0;
        double notaActual = 0.0;
        double notaNecesaria = 0.0;
        for (Nota n : NotasBase.get(context).getnotas(materia.getId())){
            if(n.getIdMateria() == materia.getId()){
                notas.add(n);
                porcentaje += n.getPorcentaje();
                notaActual += (n.getNota() * n.getPorcentaje());
            }
        }
        notaActual = Math.round((notaActual / porcentaje) * 100) / 100d;
        notaNecesaria = Math.round((((3-(notaActual*(porcentaje/100)))/(1-(porcentaje/100))) * 100))/ 100d;

        TextView porcentajeTV = (TextView) view.findViewById(R.id.itemPorcentajeEvaluado);
        porcentajeTV.setText(Integer.toString((int)porcentaje));

        TextView notaActualTV = (TextView) view.findViewById(R.id.itemNotaActual);
        notaActualTV.setText(Double.toString(notaActual));

        TextView notaNecesariaTV = (TextView) view.findViewById(R.id.itemNotaNecesaria);
        notaNecesariaTV.setText((notaNecesaria>= 0)?Double.toString(notaNecesaria): Integer.toString(0));

        TextView creditos = (TextView) view.findViewById(R.id.itemNumeroCreditos);
        creditos.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        creditos.setText(Integer.toString(materia.getCreditos()));
        creditos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View mView = View.inflate(context, R.layout.dialogo_agregar_alarma, null);

                final TextView titulo =  mView.findViewById(R.id.txdialogo_agregar_alarma);
                titulo.setHint("Créditos materia");

                final EditText mTituloAlarma = mView.findViewById(R.id.etTituloAlarma);
                mTituloAlarma.setHint("Créditos");

                Button mCrear = mView.findViewById(R.id.btnCrearAlarma);
                mCrear.setText("Cambiar");
                mBuilder.setView(mView);

                final AlertDialog dialog = mBuilder.create();

                mCrear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!mTituloAlarma.getText().toString().isEmpty()){

                            materia.setCreditos(Integer.parseInt(mTituloAlarma.getText().toString()));

                            MateriasBase.get(context).actualizarMateria(materia);

                            dialog.dismiss();

                            refresca();
                        } else {
                            Toast.makeText(context,"¿Cuántos créditos?",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });

        ImageButton btnElminarMateria = (ImageButton) view.findViewById(R.id.btnEliminarMaterias);
        btnElminarMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MateriasBase.get(context).eliminarMateria(materia);
                NotasBase.get(context).eliminarNotas(Integer.toString(materia.getId()));
                int idSemestre = materia.getId();
                materias.clear();
                materias = MateriasBase.get(context).getMaterias(Integer.toString(idSemestre));
                //Refresca los datos
                notifyDataSetChanged();
                refresca();
            }
        });

        ImageButton btnAgregarNota = (ImageButton) view.findViewById(R.id.btnAgregarNotas);
        btnAgregarNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putString("idMateria", Integer.toString(materia.getId()));
                b.putString("titulo", materia.getTitulo());
                Intent i = new Intent(context, Notas.class);
                i.putExtra("idMateria", Integer.toString(materia.getId()));
                i.putExtra("titulo", materia.getTitulo());
                context.startActivity( i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), b);
            }
        });



        return view;
    }

    //"Refresca" Cronograma activity
    private void refresca(){
        Bundle b = new Bundle();
        b.putString("valor", "prueba");
        Intent i = new Intent(context, Materias.class);
        i.putExtra("valor", "prueba");
        context.startActivity( i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), b);
    }



}
