package com.example.spendingcontrol20.utils

import android.content.Context
import android.content.DialogInterface
import android.text.Editable
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.spendingcontrol20.R
import com.example.spendingcontrol20.firestore.FireStoreUtils
import com.google.firebase.firestore.FirebaseFirestore

val db = FirebaseFirestore.getInstance()
private var sendingData: HashMap<String, String> = HashMap()

class DialogManager {

    companion object {

        fun dialogAdd(
            context: Context,
            title: String,
            hint: String,
            userId: String,
            type: String,
            UID: String,
            onComplete: () -> Unit
        ) {
            val dialog = AlertDialog.Builder(context)
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_add, null)
            dialog.setTitle(title)
            dialog.setView(view)
            val item = view.findViewById<EditText>(R.id.edt_element)
            val value = view.findViewById<EditText>(R.id.edt_valor)
            item.hint = hint

            dialog.setPositiveButton("Adicionar") { _: DialogInterface, _: Int ->
                mountData(item.text.toString(), value.text.toString(), UID)

                if (FireStoreUtils.insert(db, sendingData, context, userId, type, UID)) onComplete()

            }
            dialog.setNegativeButton("Cancelar") { dialogInterface: DialogInterface, i: Int ->
                Toast.makeText(context, "Cancelado", Toast.LENGTH_SHORT).show()
            }
            dialog.show()
        }

        fun dialogUpdate(
            context: Context,
            title: String,
            userId: String,
            type: String,
            UID: String,
            name: String,
            values: String,
            onComplete: () -> Unit
        ) {
            val dialog = AlertDialog.Builder(context)
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_add, null)
            dialog.setTitle(title)
            dialog.setView(view)
            val item = view.findViewById<EditText>(R.id.edt_element)
            val value = view.findViewById<EditText>(R.id.edt_valor)
            item.text = Editable.Factory.getInstance().newEditable(name)
            value.text = Editable.Factory.getInstance().newEditable(values)

            dialog.setPositiveButton("Adicionar") { _: DialogInterface, _: Int ->
                mountData(item.text.toString(), value.text.toString(), UID)

                if (FireStoreUtils.update(db, sendingData, context, userId, type, UID)) onComplete()

            }
            dialog.setNegativeButton("Cancelar") { dialogInterface: DialogInterface, i: Int ->
                Toast.makeText(context, "Cancelado", Toast.LENGTH_SHORT).show()
            }
            dialog.show()
        }


    }

}

fun mountData(item: String, value: String, UID: String) {
    sendingData.put(Constants.ITEM_NAME, item)
    sendingData.put(Constants.ITEM_VALUE, value)
    sendingData.put(Constants.ITEM_UID, UID)
}