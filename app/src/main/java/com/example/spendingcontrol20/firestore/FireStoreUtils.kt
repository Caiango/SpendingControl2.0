package com.example.spendingcontrol20.firestore

import android.content.Context
import android.view.View
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.example.spendingcontrol20.adapters.ElementAdapter
import com.google.firebase.firestore.FirebaseFirestore

var success = true


private var saldoList = ArrayList<Double>()

class FireStoreUtils {

    companion object {
        fun insert(
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

        fun update(
            db: FirebaseFirestore,
            data: HashMap<String, String>,
            context: Context,
            userId: String,
            type: String, UID: String
        ): Boolean {
            db.collection(userId + type).document(UID).update(data as Map<String, Any>)
                .addOnSuccessListener { success = true }.addOnFailureListener {
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
            adapter: ElementAdapter,
            anim: LottieAnimationView,
            collection: String
        ) {
            var lista = ArrayList<HashMap<String, String>>()
            db.collection(collection).get().addOnSuccessListener { task ->
                for (document in task) {

                    lista.add(document.data as HashMap<String, String>)
                }
                adapter.setAdapterList(lista)
                anim.pauseAnimation()
                anim.visibility = View.GONE
            }
        }

        fun getSaldo(
            db: FirebaseFirestore,
            collection: String,
            context: Context,
            onComplete: () -> Unit
        ): ArrayList<Double> {
            db.collection(collection).get().addOnSuccessListener { task ->
                saldoList.clear()
                for (document in task) {

                    var valor: String = document["item_value"] as String
                    valor.trim().toDouble()

                    saldoList.add(valor.toDouble())
                }
                success = true
                onComplete()
            }.addOnFailureListener {
                Toast.makeText(
                    context,
                    it.toString(),
                    Toast.LENGTH_SHORT
                ).show()
                success = false
            }
            return saldoList
        }

        fun deleteItem(
            db: FirebaseFirestore,
            context: Context,
            userId: String,
            type: String, UID: String
        ): Boolean {
            db.collection(userId + type).document(UID).delete()
                .addOnSuccessListener { success = true }.addOnFailureListener {
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