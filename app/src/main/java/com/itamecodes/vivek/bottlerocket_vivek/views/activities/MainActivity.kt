package com.itamecodes.vivek.bottlerocket_vivek.views.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.itamecodes.vivek.bottlerocket_vivek.R
import kotlinx.android.synthetic.main.activity_main.*
import com.itamecodes.vivek.bottlerocket_vivek.viewmodels.StoreViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var mSnackbar: Snackbar? =null

    private lateinit var mViewModel: StoreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        var navController = NavHostFragment.findNavController(nav_host_fragment)
        appBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        mViewModel = ViewModelProviders.of(this).get(StoreViewModel::class.java)
        mViewModel.shouldShowSnackbarNetworkConnection.observe(this, Observer {
            showSnackBar(container,getString(R.string.no_internet_connection),it)
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavHostFragment.findNavController(nav_host_fragment).navigateUp() || super.onSupportNavigateUp()
    }


    private fun showSnackBar(view: View, message: String, connected: Boolean) {
        if(!connected) mSnackbar =  Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
        mSnackbar?.let {
            if (!connected) {
                it.show()
            } else if (it.isShown) {
                it.dismiss()
            }
        }

    }
}
