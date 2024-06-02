package com.example.besinprojesi.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.besinprojesi.databinding.FragmentBesinDetayBinding
import com.example.besinprojesi.util.doPlaceHolder
import com.example.besinprojesi.util.downloadImage
import com.example.besinprojesi.viewmodel.BesinDetayViewModel

class BesinDetayFragment : Fragment() {
    private var _binding: FragmentBesinDetayBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: BesinDetayViewModel
    var besinId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBesinDetayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[BesinDetayViewModel::class.java]

        arguments?.let {
            besinId = BesinDetayFragmentArgs.fromBundle(it).besinId
        }

        viewModel.getRoomData(besinId)
        observeLiveData()

    }

    private fun observeLiveData() {
        viewModel.besinLiveData.observe(viewLifecycleOwner) {
            binding.besinIsim.text = it.isim
            binding.besinKalori.text = it.kalori
            binding.besinProtein.text = it.protein
            binding.besinKarbonhidrat.text = it.karbonhidrat
            binding.besinYag.text = it.yag
            binding.besinImage.downloadImage(it.gorsel, doPlaceHolder(requireContext()))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}