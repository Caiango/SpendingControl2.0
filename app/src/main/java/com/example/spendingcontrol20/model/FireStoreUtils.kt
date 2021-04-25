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
        val saldoMensal: MutableLiveData<Double> = MutableLiveData()
        var saldoMensalDouble: Double = 0.0
        val saldoProg: MutableLiveData<Double> = MutableLiveData()
        val saldoSubProg: MutableLiveData<Double> = MutableLiveData()


        fun insertItem(
            db: FirebaseFirestore,
            data: HashMap<String, String>,
            context: Context,
            userId: String,
            type: String, UID: String
        ): Boolean {
            db.collection(userId + type)
                .document(UID)
                .set(data)
                .addOnSuccessListener {
                    success = true
                    getItems(db, null, userId + type, context, type, userId)
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
            userId: String
        ) {
            var lista = ArrayList<HashMap<String, String>>()
            db.collection(collection).orderBy("item_data").get().addOnSuccessListener { task ->
                for (document in task) {
                    lista.add(document.data as HashMap<String, String>)
                }

                itemList.postValue(lista)

                if (anim == null) {
                    getSaldo(db, userId + type, context, type, null)

                } else {
                    getSaldo(db, userId + type, context, type, anim)
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
                } else if (type == "Gain") {
                    //TODO set txtganho
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
                        //saldoProg.postValue(saldoFixed - saldoMensalDouble)
                        saldoProg.postValue(saldoFixed)
                        saldoSubProg.postValue(saldoFixed)
                    } else if (type == "Gain") {
                        //TODO set txtganho
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
            db.collection(collection).whereEqualTo("item_data", mes).get()
                .addOnSuccessListener { task ->
                    var saldoList = ArrayList<Double>()
                    for (document in task) {

                        var valor: String = document["item_value"] as String
                        valor.trim().toDouble()

                        saldoList.add(valor.toDouble())
                    }

                    totalSaldo = 0.0
                    saldoList.forEach { item -> totalSaldo += item }
                    saldoMensalDouble = totalSaldo

                    if (type == "Desp") {
                        saldoMensal.postValue(totalSaldo)
                    } else if (type == "Gain") {
                        //TODO set txtganho
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