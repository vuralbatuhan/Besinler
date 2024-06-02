package com.example.besinprojesi.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.besinprojesi.R
import com.example.besinprojesi.adapter.BesinRecyclerAdapter
import com.example.besinprojesi.databinding.FragmentBesinListeBinding
import com.example.besinprojesi.service.BesinAPI
import com.example.besinprojesi.viewmodel.BesinListeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class BesinListeFragment : Fragment() {

    private var _binding: FragmentBesinListeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: BesinListeViewModel
    private val besinRecyclerAdapter = BesinRecyclerAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBesinListeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[BesinListeViewModel::class.java]
        viewModel.refreshData()

        binding.besinRV.layoutManager = LinearLayoutManager(requireContext())
        binding.besinRV.adapter = besinRecyclerAdapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.besinRV.visibility = View.GONE
            binding.besinHataMesaji.visibility = View.GONE
            binding.besinYukleniyor.visibility = View.VISIBLE
            viewModel.refreshData()
            binding.swipeRefreshLayout.isRefreshing = false
        }
        observeLiveData()

    }

    private fun observeLiveData() {
        viewModel.besinler.observe(viewLifecycleOwner) {
            besinRecyclerAdapter.refreshBesinList(it)
            binding.besinRV.visibility = View.VISIBLE
        }

        viewModel.besinHataMessage.observe(viewLifecycleOwner) {
            if (it) {
                binding.besinHataMesaji.visibility = View.VISIBLE
                binding.besinRV.visibility = View.GONE
            } else {
                binding.besinHataMesaji.visibility = View.GONE
            }
        }

        viewModel.besinLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.besinHataMesaji.visibility = View.GONE
                binding.besinRV.visibility = View.GONE
                binding.besinYukleniyor.visibility = View.GONE
            } else {
                binding.besinYukleniyor.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}