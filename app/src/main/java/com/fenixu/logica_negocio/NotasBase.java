package com.fenixu.logica_negocio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fenixu.recursos_datos.AdminSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class NotasBase {

    private SQLiteDatabase notasDataBase;
    private NotasBase(Context context){ notasDataBase = new AdminSQLiteOpenHelper(context).getWritableDatabase(); }

    private static NotasBase notasBase;
    public static NotasBase get(Context context){
        if(notasBase == null)
            notasBase = new NotasBase(context);
        return notasBase;
    }

    //Retorna todas las notas de la base de datos
    public List<Nota> getnotas(){

        Cursor cursor = notasDataBase.query("notas", null,
                null, null, null, null,
                null);

        List<Nota> notas = new ArrayList<>();
        try{
            if(cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {

                    int idnota = Integer.parseInt(cursor.getString(cursor.getColumnIndex("idNota")));

                    double titulonota = Double.parseDouble(cursor.getString(cursor.getColumnIndex("nota")));
                    double promedionota = Double.parseDouble(cursor.getString(cursor.getColumnIndex("porcentaje")));
                    int creditosnota = Integer.parseInt(cursor.getString(cursor.getColumnIndex("idMateria")));

                    Nota nota = new Nota();
                    nota.setIdNota(idnota);
                    nota.setNota(titulonota);
                    nota.setPorcentaje(promedionota);
                    nota.setIdMateria(creditosnota);
                    notas.add(nota);

                    cursor.moveToNext();
                }
            }
        }finally { cursor.close(); }
        return notas;
    }

    //Retorna todas las notas de la base de datos
    public List<Nota> getnotas(int idMateria){

        Cursor cursor = notasDataBase.rawQuery("SELECT * FROM notas WHERE idMateria = " +
                idMateria,  null);

        List<Nota> notas = new ArrayList<>();
        try{
            if(cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {

                    int idnota = Integer.parseInt(cursor.getString(cursor.getColumnIndex("idNota")));
                    double titulonota = Double.parseDouble(cursor.getString(cursor.getColumnIndex("nota")));
                    double promedionota = Double.parseDouble(cursor.getString(cursor.getColumnIndex("porcentaje")));
                    int creditosnota = Integer.parseInt(cursor.getString(cursor.getColumnIndex("idMateria")));

                    Nota nota = new Nota();
                    nota.setIdNota(idnota);
                    nota.setNota(titulonota);
                    nota.setPorcentaje(promedionota);
                    nota.setIdMateria(creditosnota);
                    notas.add(nota);

                    cursor.moveToNext();
                }
            }
        }finally{
            cursor.close();
        }
        return notas;
    }

    //Envia los datos a actualizar en nota con @param id
    public void actualizarnota(Nota nota){
        notasDataBase.update("notas", getContentValues(nota),
                "idNota = "+(nota.getIdNota()) ,null);
    }

    public void eliminarNotas(String idMateria){
        notasDataBase.delete("notas", "idMateria = ?", new String[]{idMateria});
    }

    //Envia nota a la base de datos
    public void guardarnota(Nota nota){
        int idNuevo = getNewId();
        nota.setIdNota(idNuevo);
        Log.d("NotasBase", "Guardando "+nota.toString());
        notasDataBase.insert("notas", null,
                getContentValues(nota));
    }

    public void eliminarnota(Nota nota){
        notasDataBase.delete("notas", "idNota = ?", new String[]{Integer.toString(nota.getIdNota())});
    }

    //Retorna un objeto ContentValues con los valores de un objeto de la clase nota
    private ContentValues getContentValues(Nota nota){
        ContentValues contentValues = new ContentValues();
        contentValues.put("idNota", nota.getIdNota());
        contentValues.put("nota", nota.getNota());
        contentValues.put("porcentaje", nota.getPorcentaje());
        contentValues.put("idMateria", nota.getIdMateria());
        return contentValues;
    }

    //Crea un nuevo id compatible en base al maximo actual en la base de datos
    private int getNewId(){
        Cursor cursor = notasDataBase.rawQuery("SELECT * FROM notas WHERE idNota = " +
                "(SELECT MAX(idNota) FROM notas)",  null);

        try{ return (cursor.moveToFirst())?  Integer.parseInt(cursor.getString(cursor.getColumnIndex("idNota")))+1:0;
        }finally { cursor.close(); }
    }
}