package com.example.s_shopping.ui.payment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.s_shopping.R
import com.example.s_shopping.common.viewBinding
import com.example.s_shopping.databinding.FragmentResultBinding

class ResultFragment : Fragment(R.layout.fragment_result) {

    private val binding by viewBinding(FragmentResultBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gifImg = pl.droidsonroids.gif.GifDrawable(resources, R.drawable.cargo)

        with(binding) {
            ivCargo.setImageDrawable(gifImg)

            btnHome.setOnClickListener {
                findNavController().navigate(ResultFragmentDirections.resultToHome())
            }
        }
    }
}