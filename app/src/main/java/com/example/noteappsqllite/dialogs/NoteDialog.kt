package com.example.noteappsqllite.dialogs

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi


import androidx.fragment.app.DialogFragment
import com.example.noteappsqllite.R
import com.example.noteappsqllite.models.Noteitem
import java.time.Instant
import java.time.format.DateTimeFormatter

class NoteDialog(private val noteListener :AddNoteListener,private val noteItem: Noteitem?):DialogFragment() {
private var editMode = false;

   @RequiresApi(Build.VERSION_CODES.O)
   override  fun onCreateView(inflater: LayoutInflater, container:ViewGroup?, savedInstanceState: Bundle?): View?{

       val rootView = layoutInflater.inflate(R.layout.notes_dialog,container,false)
       val button = rootView.findViewById<Button>(R.id.notes_dialog_submitButton)
       val noteTitle = rootView.findViewById<EditText>(R.id.notes_dialog_title)
       val noteText = rootView.findViewById<EditText>(R.id.notes_dialog_body)
       noteItem?.let{
           editMode = true
           noteTitle.setText(it.title)
           noteText.setText(it.note)
           button.text = getString(R.string.save)
       }
       
       button.setOnClickListener { 
           if(noteText.text.toString().isEmpty() || noteTitle.text.toString().isEmpty()){
               Toast.makeText(requireContext(), "Please provide note title and a note body", Toast.LENGTH_SHORT).show()
               return@setOnClickListener
           }
           if(editMode && noteItem != null){
               noteListener.editNote(noteText.text.toString(),noteTitle.text.toString(),noteItem.id!!)
               activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
               return@setOnClickListener
           }
           val note = Noteitem(
               title = noteTitle.text.toString(),
                note = noteText.text.toString(),
               timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
           )
           noteListener.addNote(note)
       }
       return rootView
   }
}

interface AddNoteListener {
    fun addNote(noteItem:Noteitem)
    fun editNote(note:String, title:String,id:Int)

}