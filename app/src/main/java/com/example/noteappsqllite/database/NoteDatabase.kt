package com.example.noteappsqllite.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

import com.example.noteappsqllite.models.Noteitem

class NoteDatabase(context: Context):SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "NotesDatabase"
        private const val TABLE_NAME = "NotesTable"

        private const val KEY_ID = "id"
        private const val KEY_TITLE = "tittle"
        private const val KEY_NOTE ="note"
        private const val KEY_TIMESTAMP ="timeStamp"

    }

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL("CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY ," + KEY_TITLE + " TEXT ," + KEY_NOTE +" TEXT ,"+ KEY_TIMESTAMP + " TEXT " + ")" )
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME ")
    }
    fun addNote(noteItem: Noteitem) : Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, noteItem.title)
        contentValues.put(KEY_TIMESTAMP, noteItem.timestamp)
        contentValues.put(KEY_NOTE, noteItem.note)
        contentValues.put(KEY_ID, noteItem.id)

        val result = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return result

    }



    fun editNote(note: String, title:String , id:Int) : Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, title)
        contentValues.put(KEY_NOTE, note)


        val result = db.update(TABLE_NAME,contentValues,"id=$id",null)
        db.close()
        return result

    }


    @SuppressLint("Range")
    fun getNotes() : ArrayList<Noteitem>{
        val noteList : ArrayList<Noteitem> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_NAME "
        val db = this.writableDatabase

        try{
            val cursor = db.rawQuery(selectQuery, null)
            if(cursor.moveToFirst()){
                do{
                    val note = Noteitem(
                        id = cursor.getInt((cursor.getColumnIndex(KEY_ID))),
                        title = cursor.getString((cursor.getColumnIndex(KEY_TITLE))),
                        note = cursor.getString((cursor.getColumnIndex(KEY_NOTE))),
                        timestamp = cursor.getString((cursor.getColumnIndex(KEY_TIMESTAMP)))
                    )
                        noteList.add(note)

                }while (cursor.moveToNext())
            }
            cursor.close()
        }catch (e: SQLiteException){
                e.printStackTrace()
            return ArrayList()
        }
        return  noteList
    }

fun deleteNote(id:Int ): Int{
    val db = this.writableDatabase

    val result = db.delete(TABLE_NAME,"$KEY_ID = $id", null)
    db.close()
    return result
}



}