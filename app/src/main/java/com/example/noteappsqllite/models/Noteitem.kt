package com.example.noteappsqllite.models

import java.sql.Timestamp

data class Noteitem( val id:Int? = null ,
                     val title:String? = null,
                     val note: String? = null,
                     val timestamp: String?=null)
