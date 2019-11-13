package com.fenixu.logica_negocio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fenixu.recursos_datos.AdminSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SemestresBase {

    private SQLiteDatabase semestresDataBase;
    private SemestresBase(Context context){ semestresDataBase = new AdminSQLiteOpenHelper(context).getWritableDatabase(); }

    private static SemestresBase semestresBase;
    public static SemestresBase get(Context context){
        if(semestresBase == null)
            semestresBase = new SemestresBase(context);
        return semestresBase;
    }

    //Retorna todas las semestres de la base de datos
    public List<Semestre> getsemestres(){

        Cursor cursor = semestresDataBase.query("semestres", null,
                null, null, null, null,
                null);

        List<Semestre> semestres = new ArrayList<>();
        try{
            if(cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {

                    int idsemestre = Integer.parseInt(cursor.getString(cursor.getColumnIndex("idSemestre")));
                    if(idsemestre == 0){ cursor.moveToNext(); continue;} //Salta Semestre especial
                    String titulosemestre = cursor.getString(cursor.getColumnIndex("titulo"));
                    double promediosemestre = Double.parseDouble(cursor.getString(cursor.getColumnIndex("promedio")));
                    int creditossemestre = Integer.parseInt(cursor.getString(cursor.getColumnIndex("creditos")));

                    Semestre semestre = new Semestre();
                    semestre.setId(idsemestre);
                    semestre.setTitulo(titulosemestre);
                    semestre.setPromedio(promediosemestre);
                    semestre.setCreditos(creditossemestre);
                    semestres.add(semestre);

                    cursor.moveToNext();
                }
            }
        }finally { cursor.close(); }
        return semestres;
    }

    //Retorna el semestre Actual para el modulo notas
    public Semestre getsemestreActual(){

        Cursor cursor = semestresDataBase.rawQuery("SELECT * FROM semestres WHERE idSemestre = 0", null);

        Semestre semestre = new Semestre();
        try{
            if(cursor.moveToFirst()) {
                int idsemestre = Integer.parseInt(cursor.getString(cursor.getColumnIndex("idSemestre")));

                String titulosemestre = cursor.getString(cursor.getColumnIndex("titulo"));
                double promediosemestre = Double.parseDouble(cursor.getString(cursor.getColumnIndex("promedio")));
                int creditossemestre = Integer.parseInt(cursor.getString(cursor.getColumnIndex("creditos")));

                semestre.setId(idsemestre);
                semestre.setTitulo(titulosemestre);
                semestre.setPromedio(promediosemestre);
                semestre.setCreditos(creditossemestre);

            }
            else{
                semestre = new Semestre(0, "SemestreActual", 0, 0);
                guardarsemestre(semestre);
            }
        }finally { cursor.close(); }
        return semestre;
    }

    //Envia los datos a actualizar en semestre con @param id
    public void actualizarsemestre(Semestre semestre){
        semestresDataBase.update("semestres", getContentValues(semestre),
                "idSemestre = "+(semestre.getId()) ,null);
    }

    //Envia semestre a la base de datos
    public void guardarsemestre(Semestre semestre){
        int idNuevo = getNewId();
        semestre.setId(idNuevo);
        semestresDataBase.insert("semestres", null,
                getContentValues(semestre));
    }

    public void eliminarsemestre(Semestre semestre){
        semestresDataBase.delete("semestres", "idSemestre = ?", new String[]{Integer.toString(semestre.getId())});
    }

    //Retorna un objeto ContentValues con los valores de un objeto de la clase semestre
    private ContentValues getContentValues(Semestre semestre){
        ContentValues contentValues = new ContentValues();
        contentValues.put("idSemestre", semestre.getId());
        contentValues.put("titulo", semestre.getTitulo());
        contentValues.put("promedio", semestre.getPromedio());
        contentValues.put("creditos", semestre.getCreditos());
        return contentValues;
    }

    //Crea un nuevo id compatible en base al maximo actual en la base de datos
    private int getNewId(){
        Cursor cursor = semestresDataBase.rawQuery("SELECT * FROM semestres WHERE idSemestre = " +
                "(SELECT MAX(idSemestre) FROM semestres)",  null);

        try{ return (cursor.moveToFirst())?  Integer.parseInt(cursor.getString(cursor.getColumnIndex("idSemestre")))+1:0;
        }finally { cursor.close(); }
    }
}
