package com.itamecodes.vivek.bottlerocket_vivek.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.itamecodes.vivek.bottlerocket_vivek.R
import com.itamecodes.vivek.bottlerocket_vivek.databinding.StoreDetailBinding
import com.itamecodes.vivek.bottlerocket_vivek.viewmodels.StoreViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.store_detail.*

class StoreDetailFragment : Fragment() {

    private lateinit var mViewModel: StoreViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var binding: StoreDetailBinding =
            DataBindingUtil.inflate<StoreDetailBinding>(inflater, R.layout.store_detail, container, false)
        activity?.let {
            mViewModel = ViewModelProviders.of(it).get(StoreViewModel::class.java)
            mViewModel.selectedStore.observe(this, Observer {
                binding.store = mViewModel.selectedStore.value
                binding.executePendingBindings ()
                it.storeLogoURL?.let {
                    Picasso.get().load(it).into(logo_imageview)
                }
            })
            val name = mViewModel.selectedStore.value?.name
        }
        return binding.root
    }


}