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
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GainsFragment : Fragment() {

    private lateinit var gainsViewModel: GainsViewModel

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

        floatBtn.setOnClickListener {
            DialogManager.dialogAdd(root.context, "Adicionar Ganho", "Ganho") { anim(anim) }
        }

        val textView: TextView = root.findViewById(R.id.text_notifications)
        gainsViewModel.text.observe(viewLifecycleOwner, Observer {value ->
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
}