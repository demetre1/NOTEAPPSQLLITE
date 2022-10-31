package com.example.noteappsqllite

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteappsqllite.adapter.NoteAdapter
import com.example.noteappsqllite.adapter.NoteClickListener
import com.example.noteappsqllite.database.NoteDatabase
import com.example.noteappsqllite.dialogs.AddNoteListener
import com.example.noteappsqllite.dialogs.NoteDialog
import com.example.noteappsqllite.models.Noteitem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Objects

class MainActivity : AppCompatActivity(), AddNoteListener, NoteClickListener {
    private lateinit var noteDatabase: NoteDatabase
    private lateinit var notesBin : ImageView
    private  var isNoteBinDrawableSet = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        noteDatabase = NoteDatabase(this)
        val fabButton = findViewById<FloatingActionButton>(R.id.notes_fab)

        fabButton.setOnClickListener{
            val dialog = NoteDialog(this,null)
            dialog.show(supportFragmentManager,"Add note")
        }
        getNOtesFromdb()

        notesBin = findViewById(R.id.notes_bin)
    }
    private val dragListener = View.OnDragListener{ view, dragEvent ->
        when(dragEvent.action){
            DragEvent.ACTION_DRAG_STARTED ->{
                view.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> true
            DragEvent.ACTION_DRAG_ENDED -> true
            DragEvent.ACTION_DRAG_EXITED -> {
                view.invalidate()
                true
            }
            DragEvent.ACTION_DROP ->{
                val item = dragEvent.clipData.getItemAt(0)
                showDeleteDialog(item.text.toString())
                true
            }

         else -> false
        }
    }

    private fun showDeleteDialog(noteId:String){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete this note")
            .setCancelable(true)
            .setPositiveButton("Yes",object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    val result = noteDatabase.deleteNote(noteId.toInt())
                    if(result > 0 ){
                        getNOtesFromdb()
                        setBinDrawble()
                        Toast.makeText(this@MainActivity, "note has been deleted",Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this@MainActivity, "error Deleting Note",Toast.LENGTH_LONG).show()
                        p0?.dismiss()
                    }
                }



            })
            .setNegativeButton("No",object :DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, p1: Int) {
                    if(dialog != null){
                        dialog.dismiss()
                    }

                }
            }).create().show()
    }

    private fun setupRecyclerView(list: ArrayList<Noteitem>){
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = NoteAdapter(list,this)
    }
private fun setBinDrawble(){
    if(isNoteBinDrawableSet){
        isNoteBinDrawableSet = true
        notesBin.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.delete))
    }
}
    private fun getNOtesFromdb(){
        val notes = noteDatabase.getNotes()
        setupRecyclerView(notes)
    }


    override fun onClick(noteitem: Noteitem) {
        NoteDialog(this,noteitem).show(supportFragmentManager, "Edit note")

    }

    override fun addNote(noteItem: Noteitem) {
       noteDatabase.addNote(noteItem)
        getNOtesFromdb()
    }

    override fun editNote(note: String, title: String, id: Int) {
        noteDatabase.editNote(note, title, id)
        getNOtesFromdb()

    }
}