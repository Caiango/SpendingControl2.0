package com.example.spendingcontrol20.ui.spending

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.example.spendingcontrol20.R
import com.example.spendingcontrol20.utils.DialogManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SpendingFragment : Fragment() {

    private lateinit var spendingViewModel: SpendingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        spendingViewModel =
            ViewModelProvider(this).get(SpendingViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_spending, container, false)
        val anim = root.findViewById<LottieAnimationView>(R.id.animation_spend)
        val floatBtn = root.findViewById<FloatingActionButton>(R.id.floatingSpend)

        floatBtn.setOnClickListener {
            DialogManager.dialogAdd(root.context, "Adicionar Despesa", "Despesa") { anim(anim) }
        }
        spendingViewModel.text.observe(viewLifecycleOwner, Observer {

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