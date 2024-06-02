package com.example.besinprojesi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.besinprojesi.databinding.BesinRecyclerRowBinding
import com.example.besinprojesi.model.Besin
import com.example.besinprojesi.util.doPlaceHolder
import com.example.besinprojesi.util.downloadImage
import com.example.besinprojesi.view.BesinListeFragmentDirections

class BesinRecyclerAdapter(val besinList : ArrayList<Besin>) : RecyclerView.Adapter<BesinRecyclerAdapter.BesinViewHolder>() {

    class BesinViewHolder(val binding : BesinRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BesinViewHolder {
        val binding = BesinRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BesinViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return besinList.size
    }

    fun refreshBesinList(newBesinList : List<Besin>) {
        besinList.clear()
        besinList.addAll(newBesinList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BesinViewHolder, position: Int) {
        holder.binding.isim.text = besinList[position].isim
        holder.binding.kalori.text = besinList[position].kalori

        holder.itemView.setOnClickListener {
            val action = BesinListeFragmentDirections.actionBesinListeFragmentToBesinDetayFragment(besinList[position].uuid)
            Navigation.findNavController(it).navigate(action)
        }

        holder.binding.imageView.downloadImage(besinList[position].gorsel, doPlaceHolder(holder.itemView.context))

    }

}