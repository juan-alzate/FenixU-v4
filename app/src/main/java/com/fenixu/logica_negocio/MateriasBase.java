package com.fenixu.logica_negocio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fenixu.recursos_datos.AdminSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MateriasBase {

    private SQLiteDatabase materiasDataBase;
    private MateriasBase(Context context){ materiasDataBase = new AdminSQLiteOpenHelper(context).getWritableDatabase(); }

    private static MateriasBase materiasBase;
    public static MateriasBase get(Context context){
        if(materiasBase == null)
            materiasBase = new MateriasBase(context);
        return materiasBase;
    }

    //Retorna todas las materias de la base de datos
    public List<Materia> getMaterias(String idSemestre){

        Cursor cursor = materiasDataBase.rawQuery("SELECT * FROM materiasPlan WHERE idSemestre = " +
                idSemestre,  null);

        List<Materia> materias = new ArrayList<>();
        try{
            if(cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {

                    int idMateria = Integer.parseInt(cursor.getString(cursor.getColumnIndex("idMateria")));

                    String tituloMateria = cursor.getString(cursor.getColumnIndex("titulo"));
                    String promedioMateria = cursor.getString(cursor.getColumnIndex("porcentaje"));
                    String creditosMateria = cursor.getString(cursor.getColumnIndex("creditos"));
                    String idSemetre = cursor.getString(cursor.getColumnIndex("idSemestre"));

                    Materia materia = new Materia();
                    materia.setId(idMateria);
                    materia.setTitulo(tituloMateria);
                    materia.setPromedio(Double.parseDouble(promedioMateria));
                    materia.setCreditos(Integer.parseInt(creditosMateria));
                    materia.setIdSemestre(Integer.parseInt(idSemetre));
                    materias.add(materia);

                    cursor.moveToNext();
                }
            }
        }finally { cursor.close(); }
        return materias;
    }

    //Retorna todas las materias de la base de datos
    public Materia getMateria(String idSemestre){

        Cursor cursor = materiasDataBase.rawQuery("SELECT * FROM materiasPlan WHERE idMateria = " +
                idSemestre,  null);


        Materia materia = new Materia();
        try{
            if(cursor.moveToFirst()) {


            int idMateria = Integer.parseInt(cursor.getString(cursor.getColumnIndex("idMateria")));

            String tituloMateria = cursor.getString(cursor.getColumnIndex("titulo"));
            String promedioMateria = cursor.getString(cursor.getColumnIndex("porcentaje"));
            String creditosMateria = cursor.getString(cursor.getColumnIndex("creditos"));
            String idSemetre = cursor.getString(cursor.getColumnIndex("idSemestre"));

            materia.setId(idMateria);
            materia.setTitulo(tituloMateria);
            materia.setPromedio(Double.parseDouble(promedioMateria));
            materia.setCreditos(Integer.parseInt(creditosMateria));
            materia.setIdSemestre(Integer.parseInt(idSemetre));

            }
        }finally { cursor.close(); }
        return materia;
    }

    //Retorna todas las materias de la base de datos
    public List<Materia> getMaterias(){

        Cursor cursor = materiasDataBase.query("materiasPlan", null,
                null, null, null, null,
                null);

        List<Materia> materias = new ArrayList<>();
        try{
            if(cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {

                    int idMateria = Integer.parseInt(cursor.getString(cursor.getColumnIndex("idMateria")));

                    String tituloMateria = cursor.getString(cursor.getColumnIndex("titulo"));
                    String promedioMateria = cursor.getString(cursor.getColumnIndex("porcentaje"));
                    String creditosMateria = cursor.getString(cursor.getColumnIndex("creditos"));
                    String idSemetre = cursor.getString(cursor.getColumnIndex("idSemestre"));

                    Materia materia = new Materia();
                    materia.setId(idMateria);
                    materia.setTitulo(tituloMateria);
                    materia.setPromedio(Double.parseDouble(promedioMateria));
                    materia.setCreditos(Integer.parseInt(creditosMateria));
                    materia.setIdSemestre(Integer.parseInt(idSemetre));
                    materias.add(materia);

                    cursor.moveToNext();
                }
            }
        }finally { cursor.close(); }
        return materias;
    }

    //Envia los datos a actualizar en materia con @param id
    public void actualizarMateria(Materia materia){
        materiasDataBase.update("materiasPlan", getContentValues(materia),
                "idMateria = "+(materia.getId()) ,null);
    }

    //Envia materia a la base de datos
    public void guardarMateria(Materia materia){
        int idNuevo = getNewId();
        materia.setId(idNuevo);
        Log.d("Materias Base", "Guardando "+ materia.toString());
        materiasDataBase.insert("materiasPlan", null,
                getContentValues(materia));
    }

    public void eliminarMaterias(String idSemestre){
        materiasDataBase.delete("materiasPlan", "idSemestre = ?", new String[]{idSemestre});
    }

    public void eliminarMateria(Materia materia){
        materiasDataBase.delete("materiasPlan", "idMateria = ?", new String[]{Integer.toString(materia.getId())});
    }

    //Retorna un objeto ContentValues con los valores de un objeto de la clase materia
    private ContentValues getContentValues(Materia materia){
        ContentValues contentValues = new ContentValues();
        contentValues.put("idMateria", materia.getId());
        contentValues.put("titulo", materia.getTitulo());
        contentValues.put("porcentaje", materia.getPromedio());
        contentValues.put("creditos", materia.getCreditos());
        contentValues.put("idSemestre", materia.getIdSemestre());
        return contentValues;
    }

    //Crea un nuevo id compatible en base al maximo actual en la base de datos
    private int getNewId(){
        Cursor cursor = materiasDataBase.rawQuery("SELECT * FROM materiasPlan WHERE idMateria = " +
                "(SELECT MAX(idMateria) FROM materiasPlan)",  null);

        try{ return (cursor.moveToFirst())?  Integer.parseInt(cursor.getString(cursor.getColumnIndex("idMateria")))+1:0;
        }finally { cursor.close(); }
    }
}
