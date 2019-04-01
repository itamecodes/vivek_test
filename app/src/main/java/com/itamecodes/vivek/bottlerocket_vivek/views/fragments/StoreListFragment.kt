package com.itamecodes.vivek.bottlerocket_vivek.views.fragments

import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.itamecodes.vivek.bottlerocket_vivek.R
import com.itamecodes.vivek.bottlerocket_vivek.adapters.StoreListAdapter
import com.itamecodes.vivek.bottlerocket_vivek.databinding.StoreListBinding
import com.itamecodes.vivek.bottlerocket_vivek.models.Store
import com.itamecodes.vivek.bottlerocket_vivek.utils.Event
import com.itamecodes.vivek.bottlerocket_vivek.viewmodels.StoreViewModel
import kotlinx.android.synthetic.main.store_list.*


class StoreListFragment: Fragment(){

    private lateinit var mViewModel: StoreViewModel
    private var mStoreList = mutableListOf<Store>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var binding:StoreListBinding = DataBindingUtil.inflate<StoreListBinding>(inflater,R.layout.store_list,container,false)
        var view = binding.root
        activity?.let {
            mViewModel = ViewModelProviders.of(it).get(StoreViewModel::class.java)
            binding.viewModel = mViewModel
        }
        binding.setLifecycleOwner(this)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var adapter = StoreListAdapter(mStoreList,mViewModel)
        store_lv.layoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
        val divider = DividerItemDecoration(activity,DividerItemDecoration.VERTICAL)
        divider.setDrawable(ShapeDrawable().apply {
            intrinsicHeight = resources.getDimensionPixelOffset(R.dimen.dimen_2)
            paint.color = Color.BLACK
        })
        store_lv.addItemDecoration(divider)
        store_lv.adapter=adapter

        mViewModel.storeListLiveData.observe(this, Observer {
            if(it.isEmpty()) {
                store_lv.visibility =View.GONE
                no_data_message.visibility=View.VISIBLE
            }else{
                store_lv.visibility =View.VISIBLE
                no_data_message.visibility=View.GONE
                adapter.updateData(it)
            }

        })

        mViewModel.shouldShowSnackbarNetworkConnection.observe(this, Observer {
            if(it && ((mViewModel.storeListLiveData.value?.isEmpty()?:true))) {
                mViewModel.getData()
            }
        })

        mViewModel.navigateToDetail.observe(this, Observer{
            navigateToDetail(it)
        })



        mViewModel.storeListLiveData.value?.let{
            if(it.isEmpty()){
                mViewModel.getData()
            }
        }?:run{
            mViewModel.getData()
        }

        mViewModel.errorServer.observe(this, Observer {
            if(it){
                showServerErrorSnackBar(storelist_container)
            }
        })


    }

    private fun showServerErrorSnackBar(view:View) {
        Snackbar.make(view,getString(R.string.server_error), Snackbar.LENGTH_SHORT).show()
    }

    private fun navigateToDetail(event:Event<Boolean>){
        event.getContentIfNotHandled()?.let{
            val action = StoreListFragmentDirections.actionStoreListFragmentToStoreDetailFragment()
            findNavController(this).navigate(action)
        }

    }




}