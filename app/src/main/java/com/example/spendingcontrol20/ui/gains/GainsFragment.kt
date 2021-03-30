package com.example.spendingcontrol20.ui.gains

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.example.spendingcontrol20.R
import com.example.spendingcontrol20.utils.DialogManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.floatingactionbutton.FloatingActionButton

lateinit var superAnim: LottieAnimationView

class GainsFragment : Fragment() {

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
        val anim = root.findViewById<LottieAnimationView>(R.id.animation_gain)
        val floatBtn = root.findViewById<FloatingActionButton>(R.id.floatingGain)
        val acct = GoogleSignIn.getLastSignedInAccount(root.context)

        if (acct != null) {
            userId = acct.id!!
        }


        floatBtn.setOnClickListener {
            var UID = getRandomString()
            DialogManager.dialogAdd(
                root.context,
                "Adicionar Ganho",
                "Ganho",
                userId,
                "Ganho",
                UID
            ) { anim(anim) }
        }

        val textView: TextView = root.findViewById(R.id.text_notifications)

        gainsViewModel.text.observe(viewLifecycleOwner, Observer { value ->
            textView.text = value
        })
        return root
    }

    fun anim(anim: LottieAnimationView) {
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

}