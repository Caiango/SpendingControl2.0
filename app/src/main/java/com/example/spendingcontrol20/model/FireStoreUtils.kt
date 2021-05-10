package com.example.spendingcontrol20.model

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.firestore.FirebaseFirestore

var success = true

var totalSaldo = 0.0

class FireStoreUtils {


    companion object {
        val itemList: MutableLiveData<ArrayList<HashMap<String, String>>> = MutableLiveData()
        val saldoDesp: MutableLiveData<Double> = MutableLiveData()
        val saldoGain: MutableLiveData<Double> = MutableLiveData()
        val saldoMensalDesp: MutableLiveData<Double> = MutableLiveData()
        val saldoMensalGain: MutableLiveData<Double> = MutableLiveData()
        var saldoMensalDouble: Double = 0.0
        val saldoProgDesp: MutableLiveData<Double> = MutableLiveData()
        val saldoProgGain: MutableLiveData<Double> = MutableLiveData()
        val saldoSubProgDesp: MutableLiveData<Double> = MutableLiveData()
        val saldoSubProgGain: MutableLiveData<Double> = MutableLiveData()
        val saldoSubMensal: MutableLiveData<Double> = MutableLiveData()
        var saldoMesGain = 0.0
        var saldoMesDesp = 0.0
        val saldoSubTotal: MutableLiveData<Double> = MutableLiveData()
        var saldoTotalGain = 0.0
        var saldoTotalDesp = 0.0


        fun insertItem(
            db: FirebaseFirestore,
            data: HashMap<String, String>,
            context: Context,
            userId: String,
            type: String, UID: String,
            onComplete: (() -> Unit)?
        ): Boolean {
            db.collection(userId + type)
                .document(UID)
                .set(data)
                .addOnSuccessListener {
                    success = true
                    getItems(
                        db,
                        null,
                        userId + type,
                        context,
                        type,
                        userId
                    ) { onComplete?.invoke() }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        context,
                        it.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    success = false
                }
            return success
        }

        fun insertProg(
            db: FirebaseFirestore,
            data: HashMap<String, String>,
            context: Context,
            collection: String, type: String
        ): Boolean {
            db.collection(collection)
                .document("Valor Fixo")
                .set(data)
                .addOnSuccessListener {
                    success = true
                    getSaldoFixed(db, collection, context, type)
                }
                .addOnFailureListener {
                    Toast.makeText(
                        context,
                        it.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    success = false
                }
            return success
        }

        fun updateItem(
            db: FirebaseFirestore,
            data: HashMap<String, String>,
            context: Context,
            userId: String,
            type: String, UID: String
        ): Boolean {
            db.collection(userId + type).document(UID).update(data as Map<String, Any>)
                .addOnSuccessListener {
                    success = true

                }
                .addOnFailureListener {
                    Toast.makeText(
                        context,
                        it.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    success = false

                }
            return success
        }

        fun getItems(
            db: FirebaseFirestore,
            anim: LottieAnimationView?,
            collection: String,
            context: Context,
            type: String,
            userId: String,
            onComplete: (() -> Unit)?
        ) {
            var lista = ArrayList<HashMap<String, String>>()
            db.collection(collection).orderBy("item_data").get().addOnSuccessListener { task ->
                for (document in task) {
                    lista.add(document.data as HashMap<String, String>)
                }

                itemList.postValue(lista)

                if (anim == null) {
                    getSaldo(db, userId + type, context, type, null)
                    onComplete?.invoke()

                } else {
                    getSaldo(db, userId + type, context, type, anim)
                    onComplete?.invoke()
                }

            }
        }

        fun getItemsByData(
            db: FirebaseFirestore,
            collection: String,
            data: String
        ) {
            var lista = ArrayList<HashMap<String, String>>()
            db.collection(collection).whereEqualTo("item_data", data).get()
                .addOnSuccessListener { task ->
                    for (document in task) {
                        lista.add(document.data as HashMap<String, String>)
                    }

                    itemList.postValue(lista)

                }
        }

        fun getSaldo(
            db: FirebaseFirestore,
            collection: String,
            context: Context,
            type: String,
            anim: LottieAnimationView?
        ) {
            db.collection(collection).get().addOnSuccessListener { task ->
                var saldoList = ArrayList<Double>()
                for (document in task) {

                    var valor: String = document["item_value"] as String
                    valor.trim().toDouble()

                    saldoList.add(valor.toDouble())
                }

                totalSaldo = 0.0
                saldoList.forEach { item -> totalSaldo += item }

                if (type == "Desp") {
                    saldoDesp.postValue(totalSaldo)
                    saldoTotalDesp = totalSaldo
                    saldoSubTotal.postValue(saldoTotalGain - saldoTotalDesp)
                    getSaldoFixed(db, collection + "Prog", context, type)
                } else if (type == "Gain") {
                    saldoGain.postValue(totalSaldo)
                    saldoTotalGain = totalSaldo
                    saldoSubTotal.postValue(saldoTotalGain - saldoTotalDesp)
                    getSaldoFixed(db, collection + "Prog", context, type)
                }

                success = true

                if (anim != null) {
                    anim.pauseAnimation()
                    anim.visibility = View.GONE
                    success = true
                }

            }.addOnFailureListener {
                Toast.makeText(
                    context,
                    it.toString(),
                    Toast.LENGTH_SHORT
                ).show()
                success = false
            }
        }

        fun getSaldoFixed(
            db: FirebaseFirestore,
            collection: String,
            context: Context,
            type: String
        ) {
            db.collection(collection).document("Valor Fixo").get().addOnSuccessListener { task ->
                try {


                    var saldoFixed = 0.0

                    var valor: String = task["valor_fixo"] as String
                    valor.trim()
                    saldoFixed = valor.toDouble()

                    if (type == "Desp") {
                        saldoProgDesp.postValue(saldoFixed)
                        saldoSubProgDesp.postValue(saldoFixed - saldoMesDesp)
                    } else if (type == "Gain") {
                        saldoProgGain.postValue(saldoFixed)
                        saldoSubProgGain.postValue(saldoFixed - saldoMesGain)
                    }

                    success = true

                } catch (e: Exception) {
                    success = false
                }
            }.addOnFailureListener {
                Toast.makeText(
                    context,
                    it.toString(),
                    Toast.LENGTH_SHORT
                ).show()
                success = false
            }
        }

        fun getSaldoMensal(
            db: FirebaseFirestore,
            collection: String,
            context: Context,
            type: String,
            anim: LottieAnimationView?,
            mes: String
        ) {
            db.collection(collection).get()
                .addOnSuccessListener { task ->
                    var saldoList = ArrayList<Double>()
                    for (document in task) {
                        if (document["item_data"] == mes || document["item_data"] == "MENSAL") {
                            var valor: String = document["item_value"] as String
                            valor.trim().toDouble()

                            saldoList.add(valor.toDouble())
                        }
                    }

                    totalSaldo = 0.0
                    saldoList.forEach { item -> totalSaldo += item }
                    saldoMensalDouble = totalSaldo

                    if (type == "Desp") {
                        saldoMensalDesp.postValue(totalSaldo)
                        saldoMesDesp = totalSaldo
                        saldoSubMensal.postValue(saldoMesGain - saldoMesDesp)
                    } else if (type == "Gain") {
                        saldoMensalGain.postValue(totalSaldo)
                        saldoMesGain = totalSaldo
                        saldoSubMensal.postValue(saldoMesGain - saldoMesDesp)
                    }

                    success = true

                    if (anim != null) {
                        anim.pauseAnimation()
                        anim.visibility = View.GONE
                        success = true
                    }

                }.addOnFailureListener {
                    Toast.makeText(
                        context,
                        it.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    success = false
                }
        }

        fun deleteItem(
            db: FirebaseFirestore,
            context: Context,
            userId: String,
            type: String, UID: String
        ): Boolean {
            db.collection(userId + type).document(UID).delete()
                .addOnSuccessListener {
                    success = true

                }.addOnFailureListener {
                    Toast.makeText(
                        context,
                        it.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    success = false
                }
            return success
        }

    }


}