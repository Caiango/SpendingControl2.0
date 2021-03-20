package com.example.spendingcontrol20.ui.home

import android.content.Context
import android.os.Bundle
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
import com.bumptech.glide.Glide
import com.example.spendingcontrol20.R
import com.example.spendingcontrol20.UserData
import com.example.spendingcontrol20.adapters.ElementAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment(), ElementAdapter.onClickListener,
    ElementAdapter.onLongClickListener {

    private lateinit var homeViewModel: HomeViewModel

    val db = FirebaseFirestore.getInstance()

    private var sendingData: HashMap<String, String> = HashMap()

    //constants
    val ITEM_NAME_SPEND = "item_name"
    val ITEM_VALUE_SPEND = "item_value"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {

        })

        //getting views
        val rv = root.findViewById<RecyclerView>(R.id.rv_home)
        val anim = root.findViewById<LottieAnimationView>(R.id.anim_home)
        var txProfile = root.findViewById<TextView>(R.id.txProfile)

        var adapter = ElementAdapter(this, this)
        rv.layoutManager = LinearLayoutManager(root.context)
        rv.setHasFixedSize(true)
        rv.adapter = adapter

        val acct = GoogleSignIn.getLastSignedInAccount(root.context)
        if (acct != null) {
            var personPhoto = acct?.photoUrl

            txProfile.text = acct.displayName?.toUpperCase()
            Glide.with(root.context).load(personPhoto).into(root.findViewById(R.id.profile_image))
        }

        var data1 = UserData("1", "40.00", "Pizza")

        sendingData.put(ITEM_NAME_SPEND, data1.item)
        sendingData.put(ITEM_VALUE_SPEND, data1.value)

        insert(db, sendingData, root.context)
        getItems(db, adapter, anim)

        return root
    }

    fun insert(
        db: FirebaseFirestore,
        data: HashMap<String, String>,
        context: Context
    ) {
        db.collection("gastos")
            .add(data)
            .addOnSuccessListener { documento ->
                Toast.makeText(
                    context,
                    documento.id,
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {
                Toast.makeText(
                    context,
                    it.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }

    }

    private fun getItems(
        db: FirebaseFirestore,
        adapter: ElementAdapter,
        anim: LottieAnimationView
    ) {
        var lista = ArrayList<HashMap<String, String>>()
        db.collection("gastos").get().addOnSuccessListener { task ->
            for (document in task) {

                lista.add(document.data as HashMap<String, String>)
            }
            adapter.setAdapterList(lista)
            anim.pauseAnimation()
            anim.visibility = View.GONE
        }
    }

    override fun onItemClick(item: java.util.HashMap<String, String>, position: Int) {
        Toast.makeText(context, item["item_name"] + item["item_value"], Toast.LENGTH_SHORT).show()

    }

    override fun onLongItemClick(item: java.util.HashMap<String, String>, position: Int) {
        Toast.makeText(context, item["item_name"] + item["item_value"], Toast.LENGTH_SHORT).show()
    }
}