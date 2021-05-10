package com.example.spendingcontrol20.ui.gains

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.spendingcontrol20.R
import com.example.spendingcontrol20.adapters.ElementAdapter
import com.example.spendingcontrol20.model.FireStoreUtils
import com.example.spendingcontrol20.utils.Constants
import com.example.spendingcontrol20.utils.DialogManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class GainsFragment : Fragment(), ElementAdapter.onClickListener,
    ElementAdapter.onLongClickListener {

    lateinit var superAnim: LottieAnimationView
    val db = FirebaseFirestore.getInstance()
    private var collection = ""
    private var collectionProg = ""
    private val type = "Gain"
    private var gainFixo = 0.0

    private lateinit var gainsViewModel: GainsViewModel
    var userId = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gainsViewModel =
            ViewModelProvider(this).get(GainsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gains, container, false)
        val rv = root.findViewById<RecyclerView>(R.id.rv_gain)
        val anim = root.findViewById<LottieAnimationView>(R.id.animation_gain)
        val LoadingAnim = root.findViewById<LottieAnimationView>(R.id.animation_gain_loading)
        val floatBtn = root.findViewById<FloatingActionButton>(R.id.floatingGain)
        val floatBtnFixedGain = root.findViewById<FloatingActionButton>(R.id.floatAddFixed)
        val floatBtnFilter = root.findViewById<FloatingActionButton>(R.id.floatingFilterGain)
        val textSaldo = root.findViewById<TextView>(R.id.txtGainTotal)
        val textMensal = root.findViewById<TextView>(R.id.txtMesTotal)
        val textRemove = root.findViewById<TextView>(R.id.txtRemove)
        val textFixed = root.findViewById<TextView>(R.id.txtFixed)
        val txtData = root.findViewById<TextView>(R.id.txtDate)

        txtData.text = getDate("A")

        superAnim = LottieAnimationView(context)
        superAnim = anim

        var adapter = ElementAdapter(this, this, "Gain")

        setAdapter(rv, root.context, adapter)

        val acct = GoogleSignIn.getLastSignedInAccount(root.context)
        if (acct != null) {
            userId = acct.id!!
            collection = acct.id!! + type
            collectionProg = acct.id!! + type + "Prog"
        }

        FireStoreUtils.getItems(
            db,
            LoadingAnim,
            collection,
            root.context,
            type,
            userId
        ) {
            FireStoreUtils.getSaldoMensal(
                db,
                userId + type,
                root.context,
                type,
                null,
                getDate("M")
            )
        }
        FireStoreUtils.getSaldoMensal(db, userId + type, root.context, type, null, getDate("M"))
        FireStoreUtils.getSaldoFixed(db, collectionProg, root.context, "Gain")

        floatBtn.setOnClickListener {
            var UID = getRandomString()

            DialogManager.dialogAdd(
                root.context,
                "Adicionar Ganho",
                "Ganho",
                userId,
                type,
                UID,
                {
                    FireStoreUtils.getSaldoMensal(
                        db,
                        userId + type,
                        root.context,
                        type,
                        null,
                        getDate("M")
                    )
                }
            ) {
                showEndAnimation(anim)
            }

        }

        floatBtnFixedGain.setOnClickListener {
            DialogManager.dialogAddFixed(
                root.context,
                "Valor Fixo",
                "200.00",
                collectionProg, gainFixo
            ) { FireStoreUtils.getSaldoFixed(db, collectionProg, root.context, "Gain") }
        }

        floatBtnFilter.setOnClickListener {
            DialogManager.dialogCalendar(root.context, userId, type)
        }

        textRemove.setOnClickListener {
            LoadingAnim.playAnimation()
            LoadingAnim.visibility = View.VISIBLE
            FireStoreUtils.getItems(
                db,
                LoadingAnim,
                collection,
                root.context,
                type,
                userId
            ) {
                FireStoreUtils.getSaldoMensal(
                    db,
                    userId + type,
                    root.context,
                    type,
                    null,
                    getDate("M")
                )
            }
        }

        //OBSERVERS

        gainsViewModel.saldoText.observe(viewLifecycleOwner, {
            textSaldo.text = "Total R$ $it"
        })

        gainsViewModel.lista.observe(viewLifecycleOwner, { list ->
            adapter.setAdapterList(list)

        })

        gainsViewModel.saldoMensal.observe(viewLifecycleOwner, { mensal ->
            textMensal.text = "Mês Atual R$ $mensal"
        })

        gainsViewModel.saldoProg.observe(viewLifecycleOwner, { prog ->
            textFixed.text = "R$ $prog"
            gainFixo = prog
        })
        return root
    }

    fun getRandomString(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..20)
            .map { allowedChars.random() }
            .joinToString("")
    }


    override fun onItemClick(item: HashMap<String, String>, position: Int) {

        val UID = item[Constants.ITEM_UID]
        val name = item[Constants.ITEM_NAME]
        val valor = item[Constants.ITEM_VALUE]
        val data = item[Constants.ITEM_DATA]

        if (UID != null && name != null && valor != null && data != null) {
            context?.let {
                DialogManager.dialogItem(
                    it,
                    userId,
                    type,
                    UID,
                    name,
                    valor,
                ) {
                    FireStoreUtils.getItems(
                        db,
                        superAnim,
                        collection,
                        it,
                        type,
                        userId
                    ) {
                        FireStoreUtils.getSaldoMensal(
                            db,
                            userId + type,
                            it,
                            type,
                            null,
                            getDate("M")
                        )
                    }
                }
            }

        }

    }

    override fun onLongItemClick(item: HashMap<String, String>, position: Int) {

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


fun getDate(condicao: String): String {
    var data = ""
    if (condicao == "A") {
        var calendar = Calendar.getInstance()
        var format = SimpleDateFormat("dd/MM/yyyy")
        var dataFinal = format.format(calendar.time)
        data = dataFinal
    } else if (condicao == "M") {
        var calendar = Calendar.getInstance()
        var format = SimpleDateFormat("MM/yyyy")
        var dataFinal = format.format(calendar.time)
        var first: String = (dataFinal.first()).toString()
        if (first == "0") {
            data = dataFinal.removePrefix(dataFinal.first().toString())
        } else {
            data = dataFinal
        }
    }
    return data
}