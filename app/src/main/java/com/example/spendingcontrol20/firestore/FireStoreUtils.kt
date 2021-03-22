package com.example.spendingcontrol20.firestore

import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

var successInsert = true
var successUpdate = true

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

                }
                .addOnFailureListener {
                    Toast.makeText(
                        context,
                        it.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    successInsert = false
                }
            return successInsert
        }

        fun update(
            db: FirebaseFirestore,
            data: HashMap<String, String>,
            context: Context,
            userId: String,
            type: String, UID: String
        ): Boolean {
            db.collection(userId + type).document(UID).update(data as Map<String, Any>)
                .addOnSuccessListener { }.addOnFailureListener {
                    Toast.makeText(
                        context,
                        it.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    successUpdate = false
                }
            return successUpdate
        }
    }

}