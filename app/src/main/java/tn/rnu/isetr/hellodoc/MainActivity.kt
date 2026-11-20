package tn.rnu.isetr.hellodoc

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import tn.rnu.isetr.hellodoc.databinding.ActivityMainBinding
import tn.rnu.isetr.hellodoc.utils.TokenManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController

        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        navController.addOnDestinationChangedListener { _, destination, _ ->

            val isHome = destination.id == R.id.homeFragment

            binding.toolbar.visibility =
                if (isHome) View.VISIBLE else View.GONE

            binding.drawerLayout.setDrawerLockMode(
                if (isHome) {
                    DrawerLayout.LOCK_MODE_UNLOCKED
                } else {
                    DrawerLayout.LOCK_MODE_LOCKED_CLOSED
                }
            )

            drawerToggle.isDrawerIndicatorEnabled = isHome
            drawerToggle.syncState()

            if (isHome && !TokenManager.getToken(this).isNullOrBlank()) {
                updateDrawerHeader()
            }
        }

        binding.navigationView.setNavigationItemSelectedListener { item ->

            when (item.itemId) {

                R.id.nav_home -> {
                    // Retour à l'accueil
                    navController.navigate(R.id.homeFragment) {
                        popUpTo(R.id.homeFragment) {
                            inclusive = true
                        }
                    }
                }

                R.id.nav_logout -> {
                    TokenManager.clearAuthData(this)

                    // Retour à l'accueil public
                    navController.navigate(R.id.homeFragment) {
                        popUpTo(R.id.homeFragment) {
                            inclusive = true
                        }
                    }

                    Toast.makeText(
                        this,
                        "Déconnexion réussie",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    Toast.makeText(
                        this,
                        "Cette fonctionnalité sera disponible prochainement.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            binding.drawerLayout.closeDrawers()
            true
        }
    }

    private fun updateDrawerHeader() {
        val headerView = binding.navigationView.getHeaderView(0)

        val tvName = headerView.findViewById<TextView>(R.id.tvDrawerName)
        val tvRole = headerView.findViewById<TextView>(R.id.tvDrawerRole)

        tvName.text = "Hello Doc"
        tvRole.text = "Espace Patient"
    }
}
