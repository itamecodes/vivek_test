package com.itamecodes.vivek.bottlerocket_vivek.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itamecodes.vivek.bottlerocket_vivek.databinding.StoreListItemBinding
import com.itamecodes.vivek.bottlerocket_vivek.models.Store
import com.itamecodes.vivek.bottlerocket_vivek.viewmodels.StoreViewModel
import com.squareup.picasso.Picasso

class StoreListAdapter(var storelist: MutableList<Store>, val mViewModel: StoreViewModel) :
    RecyclerView.Adapter<StoreListAdapter.StoreListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreListViewHolder {
        val binding = StoreListItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
        var layoutParams =
            RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        binding.root.layoutParams = layoutParams
        return StoreListViewHolder(binding)
    }

    override fun getItemCount(): Int = storelist.size


    override fun onBindViewHolder(holder: StoreListViewHolder, position: Int) {
        var store = storelist[position]
        holder.apply {
            binding.viewModel = mViewModel
            binding.store = store
            Picasso.get().load(store.storeLogoURL).resize(400,100).centerCrop().into(binding.logo)
            binding.executePendingBindings()
        }
    }

    fun updateData(list: List<Store>) {
        storelist.clear()
        storelist.addAll(list)
        notifyDataSetChanged()
    }

    inner class StoreListViewHolder(val binding: StoreListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}