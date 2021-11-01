package com.example.spendingcontrol20.utils

import android.content.Context
import android.content.DialogInterface
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.spendingcontrol20.R
import com.example.spendingcontrol20.model.FireStoreUtils
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

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
            onComplete: () -> Unit,
            onComplete2: (() -> Unit)?
        ) {
            val dialog = AlertDialog.Builder(context)
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_add, null)
            dialog.setTitle(title)
            dialog.setView(view)
            val item = view.findViewById<EditText>(R.id.edt_element)
            val value = view.findViewById<EditText>(R.id.edt_valor)
            val calendar = view.findViewById<CalendarView>(R.id.calendarViewAdd)
            val check = view.findViewById<CheckBox>(R.id.cbMensal)
            val txtData = view.findViewById<TextView>(R.id.txtData)
            val txtData1 = view.findViewById<TextView>(R.id.textView7)
            val txtData2 = view.findViewById<TextView>(R.id.textView8)
            txtData.text = "Mês Selecionado: ${getDate()}"
            var mes: Int
            var ano: Int
            var finalData = getDate()
            item.hint = hint

            check.setOnCheckedChangeListener { _, b ->
                if (b) {
                    calendar.visibility = View.GONE
                    txtData.text = "Mês Selecionado: MENSAL"
                    txtData1.visibility = View.GONE
                    txtData2.visibility = View.GONE
                    finalData = "MENSAL"
                } else {
                    calendar.visibility = View.VISIBLE
                    txtData.text = "Mês Selecionado: ${getDate()}"
                    txtData1.visibility = View.VISIBLE
                    txtData2.visibility = View.VISIBLE
                    finalData = getDate()
                }
            }

            calendar.setOnDateChangeListener { calendarView, i, i2, i3 ->
                mes = i2 + 1
                ano = i
                finalData = "$mes/$ano"
                txtData.text = "Mês Selecionado: $finalData"
            }

            dialog.setPositiveButton("Adicionar") { _: DialogInterface, _: Int ->
                if (item.text.toString() != "" && value.text.toString() != "") {
                    mountData(item.text.toString(), value.text.toString(), UID, finalData, "false")

                    if (FireStoreUtils.insertItem(
                            db,
                            sendingData,
                            context,
                            userId,
                            type,
                            UID
                        ) {
                            onComplete2?.invoke()
                        }
                    ) onComplete()
                } else {
                    Toast.makeText(context, "Insira valores Válidos", Toast.LENGTH_LONG).show()
                }

            }
            dialog.setNegativeButton("Cancelar") { dialogInterface: DialogInterface, i: Int ->
                Toast.makeText(context, "Cancelado", Toast.LENGTH_SHORT).show()
            }
            dialog.show()
        }


        fun dialogAddFixed(
            context: Context,
            title: String,
            hint: String,
            collection: String,
            fixed: Double,
            onComplete: () -> Unit
        ) {
            val dialog = AlertDialog.Builder(context)
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_fixed, null)
            dialog.setTitle(title)
            dialog.setView(view)
            val valor = view.findViewById<EditText>(R.id.editFixed)
            val txFixed = view.findViewById<TextView>(R.id.txFixedDialog)
            txFixed.text = "Valor Fixo R$ $fixed"
            valor.hint = hint

            dialog.setPositiveButton("Adicionar") { _: DialogInterface, _: Int ->
                if (valor.text.toString() != "") {
                    val hashFixo = HashMap<String, String>()
                    val value = valor.text.toString().trim()
                    hashFixo.put("valor_fixo", value)

                    if (FireStoreUtils.insertProg(
                            db,
                            hashFixo,
                            context,
                            collection, "Desp"
                        )
                    ) onComplete()
                } else {
                    Toast.makeText(context, "Insira valores Válidos", Toast.LENGTH_LONG).show()
                }

            }
            dialog.setNegativeButton("Cancelar") { dialogInterface: DialogInterface, i: Int ->
                Toast.makeText(context, "Cancelado", Toast.LENGTH_SHORT).show()
            }
            dialog.show()
        }

        fun dialogItem(
            context: Context,
            userId: String,
            type: String,
            UID: String,
            name: String,
            values: String,
            onComplete: () -> Unit
        ) {
            val dialog = AlertDialog.Builder(context)
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_add, null)
            dialog.setTitle("O que deseja realizar?")
            dialog.setView(view)
            val item = view.findViewById<EditText>(R.id.edt_element)
            val value = view.findViewById<EditText>(R.id.edt_valor)
            val calendar = view.findViewById<CalendarView>(R.id.calendarViewAdd)
            val check = view.findViewById<CheckBox>(R.id.cbMensal)
            val txtData = view.findViewById<TextView>(R.id.txtData)
            val txtData1 = view.findViewById<TextView>(R.id.textView7)
            val txtData2 = view.findViewById<TextView>(R.id.textView8)
            txtData.text = "Mês Selecionado: ${getDate()}"

            item.text = Editable.Factory.getInstance().newEditable(name)
            value.text = Editable.Factory.getInstance().newEditable(values)
            var mes: Int
            var ano: Int
            var finalData = getDate()

            check.setOnCheckedChangeListener { _, b ->
                if (b) {
                    calendar.visibility = View.GONE
                    txtData.text = "Mês Selecionado: MENSAL"
                    txtData1.visibility = View.GONE
                    txtData2.visibility = View.GONE
                    finalData = "MENSAL"
                } else {
                    calendar.visibility = View.VISIBLE
                    txtData.text = "Mês Selecionado: ${getDate()}"
                    txtData1.visibility = View.VISIBLE
                    txtData2.visibility = View.VISIBLE
                    finalData = getDate()
                }
            }

            calendar.setOnDateChangeListener { calendarView, i, i2, i3 ->
                mes = i2 + 1
                ano = i
                finalData = "$mes/$ano"
                txtData.text = "Mês Selecionado: $finalData"
            }

            dialog.setPositiveButton("Atualizar") { _: DialogInterface, _: Int ->
                if (item.text.toString() != "" && value.text.toString() != "") {
                    mountData(item.text.toString(), value.text.toString(), UID, finalData, "null")

                    if (FireStoreUtils.updateItem(
                            db,
                            sendingData,
                            context,
                            userId,
                            type,
                            UID
                        )
                    ) onComplete()
                } else {
                    Toast.makeText(context, "Insira valores Válidos", Toast.LENGTH_LONG).show()
                }


            }
            dialog.setNeutralButton("Excluir") { _: DialogInterface, _: Int ->
                if (FireStoreUtils.deleteItem(db, context, userId, type, UID)) onComplete()
            }
            dialog.setNegativeButton("Cancelar") { dialogInterface: DialogInterface, i: Int ->
                Toast.makeText(context, "Cancelado", Toast.LENGTH_SHORT).show()
            }
            dialog.show()
        }

        fun dialogCalendar(
            context: Context,
            userId: String,
            type: String
        ) {
            val dialog = AlertDialog.Builder(context)
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_calendar, null)
            dialog.setTitle("Selecione o Mês desejado")
            dialog.setView(view)
            val calendar = view.findViewById<CalendarView>(R.id.calendarViewDialog)
            val txtData = view.findViewById<TextView>(R.id.txtCalData)
            val check = view.findViewById<CheckBox>(R.id.cbMes)
            txtData.text = "Mês Selecionado: ${getDate()}"
            var mes: Int
            var ano: Int
            var finalData = getDate()

            check.setOnCheckedChangeListener { _, b ->
                if (b) {
                    calendar.visibility = View.GONE
                    txtData.text = "Mês Selecionado: MENSAL"
                    txtData.visibility = View.GONE
                    finalData = "MENSAL"
                } else {
                    calendar.visibility = View.VISIBLE
                    txtData.text = "Mês Selecionado: ${getDate()}"
                    txtData.visibility = View.VISIBLE
                    finalData = getDate()
                }
            }
            calendar.setOnDateChangeListener { calendarView, i, i2, i3 ->

                mes = i2 + 1
                ano = i
                finalData = "$mes/$ano"
                txtData.text = "Mês Selecionado: $finalData"
            }
            dialog.setPositiveButton("Filtrar") { _: DialogInterface, _: Int ->
                FireStoreUtils.getItemsByData(
                    db,
                    userId + type,
                    finalData
                )

            }
            dialog.setNegativeButton("Cancelar") { dialogInterface: DialogInterface, i: Int ->
                Toast.makeText(context, "Cancelado", Toast.LENGTH_SHORT).show()
            }
            dialog.show()
        }


    }

}

fun getDate(): String {
    var calendar = Calendar.getInstance()
    var format = SimpleDateFormat("MM/yyyy")
    var dataFinal: String = format.format(calendar.time)
    var first: String = (dataFinal.first()).toString()

    return if (first == "0") {
        dataFinal.removePrefix(dataFinal.first().toString())
    } else {
        dataFinal
    }

}

fun mountData(item: String, value: String, UID: String, data: String, payed: String) {
    sendingData.put(Constants.ITEM_NAME, item)
    sendingData.put(Constants.ITEM_VALUE, value)
    sendingData.put(Constants.ITEM_UID, UID)
    sendingData.put(Constants.ITEM_DATA, data)
    sendingData.put(Constants.ITEM_PAYED, payed)
}