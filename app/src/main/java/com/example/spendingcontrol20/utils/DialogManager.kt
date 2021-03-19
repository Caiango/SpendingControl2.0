package com.example.spendingcontrol20.utils

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.spendingcontrol20.R

class DialogManager {
    companion object {
        fun dialogAdd(context: Context, title: String, hint: String ,onComplete: () -> Unit) {
            val dialog = AlertDialog.Builder(context)
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_add, null)
            dialog.setTitle(title)
            dialog.setView(view)
            val element = view.findViewById<EditText>(R.id.edt_element)
            element.hint = hint
            dialog.setPositiveButton("Adicionar") { _: DialogInterface, _: Int ->
                onComplete()
            }
            dialog.setNegativeButton("Cancelar") { dialogInterface: DialogInterface, i: Int ->
                Toast.makeText(context, "Cancelado", Toast.LENGTH_SHORT).show()
            }
            dialog.show()
        }
    }
}