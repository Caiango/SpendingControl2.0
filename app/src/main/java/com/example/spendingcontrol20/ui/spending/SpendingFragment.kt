package com.example.spendingcontrol20.ui.spending

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
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
import java.text.SimpleDateFormat
import java.util.*


class SpendingFragment : Fragment(), ElementAdapter.onClickListener,
    ElementAdapter.onLongClickListener {

    private lateinit var spendingViewModel: SpendingViewModel
    lateinit var superAnim: LottieAnimationView
    val db = FirebaseFirestore.getInstance()
    private var collection = ""
    var userId = ""
    private val type = "Desp"

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
        val LoadingAnim = root.findViewById<LottieAnimationView>(R.id.animation_spend_loading)
        val floatBtn = root.findViewById<FloatingActionButton>(R.id.floatingSpend)
        val textSaldo = root.findViewById<TextView>(R.id.txtDespTotal)
        val txtData = root.findViewById<TextView>(R.id.txtDate)

        txtData.text = getDate()

        superAnim = LottieAnimationView(context)
        superAnim = anim

        var adapter = ElementAdapter(this, this, "Desp")

        setAdapter(rv, root.context, adapter)

        val acct = GoogleSignIn.getLastSignedInAccount(root.context)
        if (acct != null) {
            userId = acct.id!!
            collection = acct.id!! + type
        }

        FireStoreUtils.getItems(db, LoadingAnim, collection, root.context, type, userId)

        floatBtn.setOnClickListener {
            var UID = getRandomString()

            DialogManager.dialogAdd(
                root.context,
                "Adicionar Despesa",
                "Despesa",
                userId,
                type,
                UID
            ) {
                showEndAnimation(anim)
            }

        }

        //OBSERVERS

        spendingViewModel.saldoText.observe(viewLifecycleOwner, {
            textSaldo.text = "R$ $it"
        })

        spendingViewModel.lista.observe(viewLifecycleOwner, { list ->
            adapter.setAdapterList(list)

        })

        return root
    }

    override fun onItemClick(item: HashMap<String, String>, position: Int) {

        val UID = item[Constants.ITEM_UID]
        val name = item[Constants.ITEM_NAME]
        val valor = item[Constants.ITEM_VALUE]

        if (UID != null && name != null && valor != null) {
            context?.let {
                DialogManager.dialogItem(
                    it,
                    userId,
                    type,
                    UID,
                    name,
                    valor,
                ) { FireStoreUtils.getItems(db, superAnim, collection, it, type, userId) }
            }

        }

    }

    override fun onLongItemClick(item: java.util.HashMap<String, String>, position: Int) {

    }
}

private fun setAdapter(rv: RecyclerView, context: Context, adapter: ElementAdapter) {
    rv.layoutManager = LinearLayoutManager(context)
    rv.setHasFixedSize(true)
    rv.adapter = adapter
}


fun showEndAnimation(anim: LottieAnimationView) {
    anim.visibility = View.VISIBLE
    anim.playAnimation()
    Handler().postDelayed({
        anim.visibility = View.GONE
        anim.pauseAnimation()
    }, 4700)
}

fun FinishLoadingAnimation(anim: LottieAnimationView) {
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

fun getDate(): String {
    var calendar = Calendar.getInstance()
    var format = SimpleDateFormat("dd/MM/yyyy")
    var dataFinal = format.format(calendar.time)
    return dataFinal

}



