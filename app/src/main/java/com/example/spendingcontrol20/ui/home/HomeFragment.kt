package com.example.spendingcontrol20.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.spendingcontrol20.R
import com.example.spendingcontrol20.adapters.HomeAdapter
import com.example.spendingcontrol20.model.FireStoreUtils
import com.example.spendingcontrol20.ui.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment(), HomeAdapter.onClickListener,
    HomeAdapter.onLongClickListener {

    private lateinit var homeViewModel: HomeViewModel
    lateinit var googleSignInClient: GoogleSignInClient
    val db = FirebaseFirestore.getInstance()
    private var gainCollection = ""
    private var despCollection = ""
    private var despProgCollection = ""
    private var gainProgCollection = ""
    lateinit var anim_warn1: LottieAnimationView
    lateinit var anim_warn2: LottieAnimationView
    lateinit var anim_ok1: LottieAnimationView
    lateinit var anim_ok2: LottieAnimationView
    lateinit var txtGainProg: TextView
    lateinit var txtDespProg: TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        //getting views
        val rv = root.findViewById<RecyclerView>(R.id.rv_home)
        val txProfile = root.findViewById<TextView>(R.id.txProfile)
        val btn = root.findViewById<Button>(R.id.buttonSignOut)
        txtDespProg = root.findViewById<TextView>(R.id.textViewDesp)
        txtGainProg = root.findViewById(R.id.textViewGain)
        val txtMensal = root.findViewById<TextView>(R.id.textViewMensal)
        val txtTotal = root.findViewById<TextView>(R.id.textViewTotal)
        anim_warn1 = root.findViewById(R.id.anim_warning)
        anim_warn2 = root.findViewById(R.id.anim_warning2)
        anim_ok1 = root.findViewById(R.id.anim_ok)
        anim_ok2 = root.findViewById(R.id.anim_ok2)

        var adapter = HomeAdapter(this, this)
        rv.layoutManager = LinearLayoutManager(root.context)
        rv.setHasFixedSize(true)
        rv.adapter = adapter

        //LOGIN GOOGLE
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .requestProfile()
            .build()

        googleSignInClient = GoogleSignIn.getClient(root.context, gso)

        val acct = GoogleSignIn.getLastSignedInAccount(root.context)
        if (acct != null) {
            var personPhoto = acct.photoUrl
            gainCollection = acct.id + "Gain"
            despCollection = acct.id + "Desp"
            despProgCollection = acct.id + "Desp" + "Prog"
            gainProgCollection = acct.id + "Gain" + "Prog"

            txProfile.text = acct.displayName?.toUpperCase()
            Glide.with(root.context).load(personPhoto).into(root.findViewById(R.id.profile_image))

            btn.setOnClickListener {
                googleSignInClient.signOut()
                FireStoreUtils.clearData()
                val intent = Intent(root.context, LoginActivity::class.java)
                startActivity(intent)
            }


            FireStoreUtils.getSaldo(db, gainCollection, root.context, "Gain", null)
            FireStoreUtils.getSaldo(db, despCollection, root.context, "Desp", null)
            FireStoreUtils.getSaldoMensal(
                db,
                gainCollection,
                root.context,
                "Gain",
                null,
                getDate("M")
            )
            FireStoreUtils.getSaldoMensal(
                db,
                despCollection,
                root.context,
                "Desp",
                null,
                getDate("M")
            )


        }

        //OBSERVERS

        homeViewModel.saldoMensal.observe(viewLifecycleOwner, { mensal ->
            setAnimVisibility(mensal, "Mensal")
            txtMensal.text = mensal.toString()
        })

        homeViewModel.saldoProgDesp.observe(viewLifecycleOwner, { prog ->
            setProgText(prog, "Desp")
        })

        homeViewModel.saldoProgGain.observe(viewLifecycleOwner, { prog ->
            setProgText(prog, "Gain")
        })

        homeViewModel.saldoTotal.observe(viewLifecycleOwner, { total ->
            setAnimVisibility(total, "Total")
            txtTotal.text = total.toString()
        })



        return root
    }

    private fun setProgText(value: Double, type: String) {
        if (type == "Gain") {
            when {
                value > 0.0 -> {
                    txtGainProg.text = "-$value"
                }
                value < 0.0 -> {
                    txtGainProg.text = "+" + (-value).toString()
                }
                else -> {
                    txtGainProg.text = value.toString()
                }
            }
        } else {
            when {
                value > 0.0 -> {
                    txtDespProg.text = "+$value"


                }
                value < 0.0 -> {
                    txtDespProg.text = "$value"

                }
                else -> {
                    txtDespProg.text = value.toString()
                }
            }
        }
    }

    private fun setAnimVisibility(valor: Double, type: String) {
        if (type == "Mensal") {
            if (valor < 0.0) {
                anim_warn1.visibility = View.VISIBLE
                anim_ok1.visibility = View.INVISIBLE
            } else if (valor > 0.0) {
                anim_ok1.visibility = View.VISIBLE
                anim_warn1.visibility = View.INVISIBLE
            }
        } else if (type == "Total") {
            if (valor < 0.0) {
                anim_warn2.visibility = View.VISIBLE
                anim_ok2.visibility = View.INVISIBLE
            } else if (valor > 0.0) {
                anim_ok2.visibility = View.VISIBLE
                anim_warn2.visibility = View.INVISIBLE
            }
        }
    }

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