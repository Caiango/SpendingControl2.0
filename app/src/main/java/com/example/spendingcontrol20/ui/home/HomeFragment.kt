package com.example.spendingcontrol20.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.spendingcontrol20.R
import com.example.spendingcontrol20.adapters.ElementAdapter
import com.example.spendingcontrol20.adapters.HomeAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment(), HomeAdapter.onClickListener,
    HomeAdapter.onLongClickListener {

    private lateinit var homeViewModel: HomeViewModel

    val db = FirebaseFirestore.getInstance()

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
                var txProfile = root.findViewById<TextView>(R.id.txProfile)

        var adapter = HomeAdapter(this, this)
        rv.layoutManager = LinearLayoutManager(root.context)
        rv.setHasFixedSize(true)
        rv.adapter = adapter

        val acct = GoogleSignIn.getLastSignedInAccount(root.context)
        if (acct != null) {
            var personPhoto = acct?.photoUrl

            txProfile.text = acct.displayName?.toUpperCase()
            Glide.with(root.context).load(personPhoto).into(root.findViewById(R.id.profile_image))
        }
        //getItems(db, adapter)

        return root
    }


    private fun getItems(
        db: FirebaseFirestore,
        adapter: HomeAdapter
    ) {
        var lista = ArrayList<HashMap<String, String>>()
        db.collection("112696860758529407157Desp").get().addOnSuccessListener { task ->
            for (document in task) {

                lista.add(document.data as HashMap<String, String>)
            }
            adapter.setAdapterList(lista)
        }
    }


}