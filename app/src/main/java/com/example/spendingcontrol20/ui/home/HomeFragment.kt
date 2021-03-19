package com.example.spendingcontrol20.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.spendingcontrol20.R
import com.google.android.gms.auth.api.signin.GoogleSignIn

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

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
        val acct = GoogleSignIn.getLastSignedInAccount(root.context)
        if (acct != null) {
            var personPhoto = acct?.photoUrl
            var txProfile = root.findViewById<TextView>(R.id.txProfile)
            txProfile.text = acct.displayName?.toUpperCase()
            Glide.with(root.context).load(personPhoto).into(root.findViewById(R.id.profile_image))
        }
        return root
    }
}