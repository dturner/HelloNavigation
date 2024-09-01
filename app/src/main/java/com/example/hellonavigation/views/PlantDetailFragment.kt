package com.example.hellonavigation.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.toRoute
import com.example.hellonavigation.R
import com.example.hellonavigation.databinding.FragmentPlantDetailBinding

/**
 * A simple [Fragment] subclass.
 * Use the [PlantDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlantDetailFragment : Fragment() {

    private lateinit var binding: FragmentPlantDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlantDetailBinding.inflate(inflater, container, false)

        val navController = findNavController()
        val route = navController.getCurrentRoute<PlantDetail>()

        binding.plantDetails.text = "id: ${route.id}, name: ${route.name}"

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PlantDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(plantDetail: PlantDetail) =
            PlantDetailFragment().apply {
                arguments = Bundle().apply {
                    // TODO
                }
            }
    }
}