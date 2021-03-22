package com.example.spendingcontrol20.ui.spending

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.spendingcontrol20.R
import com.example.spendingcontrol20.adapters.ElementAdapter
import com.example.spendingcontrol20.firestore.FireStoreUtils
import com.example.spendingcontrol20.utils.Constants
import com.example.spendingcontrol20.utils.DialogManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

private var collection = ""
private var saldo: Double = 0.0
private lateinit var saldoListDesp: ArrayList<Double>
private lateinit var textSaldo: TextView

class SpendingFragment : Fragment(), ElementAdapter.onClickListener,
    ElementAdapter.onLongClickListener {

    private lateinit var spendingViewModel: SpendingViewModel
    var userId = ""
    lateinit var superAnim: LottieAnimationView
    val db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        spendingViewModel =
            ViewModelProvider(this).get(SpendingViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_spending, container, false)
        val rv = root.findViewById<RecyclerView>(R.id.rv_spend)
        val anim = root.findViewById<LottieAnimationView>(R.id.animation_spend)
        val floatBtn = root.findViewById<FloatingActionButton>(R.id.floatingSpend)
        textSaldo = root.findViewById(R.id.txtDespTotal)

        superAnim = LottieAnimationView(context)
        superAnim = anim

        saldoListDesp = ArrayList()

        var adapter = ElementAdapter(this, this)
        rv.layoutManager = LinearLayoutManager(root.context)
        rv.setHasFixedSize(true)
        rv.adapter = adapter

        val acct = GoogleSignIn.getLastSignedInAccount(root.context)
        if (acct != null) {
            userId = acct.id!!
            collection = acct.id!! + "Desp"
        }

        floatBtn.setOnClickListener {
            var UID = getRandomString()

            DialogManager.dialogAdd(
                root.context,
                "Adicionar Despesa",
                "Despesa",
                userId,
                "Desp",
                UID
            ) {
                showAnimation(anim)
            }


        }
        spendingViewModel.text.observe(viewLifecycleOwner, Observer {

        })

        FireStoreUtils.getItems(db, adapter, anim, collection)

        saldoListDesp = FireStoreUtils.getSaldo(db, collection, root.context) {
            setDespSaldoText(
                textSaldo
            )
        }




        return root

    }

    override fun onItemClick(item: java.util.HashMap<String, String>, position: Int) {
        Toast.makeText(context, item["item_name"] + item["item_value"], Toast.LENGTH_SHORT).show()

    }

    override fun onLongItemClick(item: java.util.HashMap<String, String>, position: Int) {

        val UID = item[Constants.ITEM_UID]
        val name = item[Constants.ITEM_NAME]
        val valor = item[Constants.ITEM_VALUE]

        if (UID != null && name != null && valor != null) {
            context?.let {
                DialogManager.dialogItem(
                    it,
                    "Atualizar Item",
                    userId,
                    "Desp",
                    UID, name, valor,
                ) { showAnimation(superAnim) }

            }
        }

    }
}

private fun setDespSaldoText(tv: TextView) {
    saldo = 0.0
    if (saldoListDesp.isNotEmpty()) saldoListDesp.forEach { valor -> saldo += valor }

    tv.text = "R$ $saldo"

}


fun showAnimation(anim: LottieAnimationView) {
    anim.visibility = View.VISIBLE
    anim.playAnimation()
    Handler().postDelayed({
        anim.visibility = View.GONE
        anim.pauseAnimation()
    }, 4700)
}

fun getRandomString(): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..20)
        .map { allowedChars.random() }
        .joinToString("")
}

