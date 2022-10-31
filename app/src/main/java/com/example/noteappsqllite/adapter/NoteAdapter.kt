package com.example.noteappsqllite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteappsqllite.R
import com.example.noteappsqllite.models.Noteitem
import android.content.ClipData
import android.content.ClipDescription
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi

class NoteAdapter (  private val list: MutableList<Noteitem>,
                     private val clickListener : NoteClickListener): RecyclerView.Adapter<listViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): listViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        return  listViewHolder(inflater, parent)
    }
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: listViewHolder, position: Int) {
      val noteItem = list[position]
        val itemView = holder.itemView
        val titleText = itemView.findViewById<TextView>(R.id.note_list_item_title)
        val noteText = itemView.findViewById<TextView>(R.id.note_list_item_note)
       titleText.text = noteItem.title
        noteText.text = noteItem.note

      itemView.setOnClickListener {
          val clipData = noteItem.id.toString()
          val item = ClipData.Item(clipData)
          val mimeType = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
          val data = ClipData(clipData, mimeType,item)

          val dragShadowBuilder = View.DragShadowBuilder(it)
          it.startDragAndDrop(data,dragShadowBuilder,it,0)


      }
        itemView.setOnClickListener {
            clickListener.onClick(noteItem)
        }

    }

    override fun getItemCount(): Int {
      return  list.size
    }

}




class listViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder(inflater.inflate(
    R.layout.note_list_item, parent,false))

interface NoteClickListener{
    fun onClick(noteItem: Noteitem)
}