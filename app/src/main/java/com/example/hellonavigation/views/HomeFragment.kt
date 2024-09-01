package com.example.hellonavigation.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hellonavigation.databinding.FragmentHomeBinding


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        val navController = findNavController()

        binding.buttonToPlantDetail.setOnClickListener {
            Log.d("HomeFragment", "Attempting to navigate to plant detail")
            navController.navigate(
                route = PlantDetail(
                    id = "bam",
                    name = "Bamboo"
                )
            )
        }

        binding.buttonToAnotherActivity.setOnClickListener {
            navController.navigate(route = AnotherRoute)
        }

        binding.buttonToFruitDetail.setOnClickListener {
            navController.navigate(route = FruitDetail(id = "APPL", Fruit("Apple", "Green")))
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment()
    }
}
