package com.example.s_shopping.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.s_shopping.R
import com.example.s_shopping.common.gone
import com.example.s_shopping.common.viewBinding
import com.example.s_shopping.common.visible
import com.example.s_shopping.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(binding) {

            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

            val navController = navHostFragment.navController
            NavigationUI.setupWithNavController(bottomNav, navHostFragment.navController)
            //bottomNavı navigation ile bağlıyoruz.

            val barController = navHostFragment.navController
            NavigationUI.setupWithNavController(toolbar, barController)

            // Diğer sayfaları açarken BottomNavigationView'ı gizle
            navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.homeFragment,
                    R.id.cardFragment,
                    R.id.searchFragment,
                    R.id.favoriteFragment -> bottomNav.visible()

                    else -> bottomNav.gone()
                }
            }

            barController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.splashFragment -> toolbar.visibility = View.GONE
                }
            }
        }

        setContentView(binding.root)
    }
}