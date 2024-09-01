package com.example.hellonavigation.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.hellonavigation.R
import com.example.hellonavigation.databinding.FragmentFruitDetailBinding

/**
 * A simple [Fragment] subclass.
 * Use the [FruitDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FruitDetailFragment : Fragment() {

    private lateinit var binding: FragmentFruitDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFruitDetailBinding.inflate(inflater, container, false)

        val fruitDetail = findNavController().getCurrentRoute<FruitDetail>()
        val fruit = fruitDetail.fruit

        binding.fruitDetailText.text = "Fruit name: ${fruit.name} color: ${fruit.color}"
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = FruitDetailFragment()
    }
}