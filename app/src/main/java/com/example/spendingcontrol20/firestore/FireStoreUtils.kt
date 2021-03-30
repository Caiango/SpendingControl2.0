package com.example.spendingcontrol20.firestore

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
            db.collection(collection).orderBy("item_value").get().addOnSuccessListener { task ->
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