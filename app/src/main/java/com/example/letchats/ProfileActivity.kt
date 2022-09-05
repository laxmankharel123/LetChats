package com.example.letchats


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.letchats.databinding.ProfileAcitivityBinding


class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ProfileAcitivityBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.profile_acitivity)
        // setContentView(R.layout.profile_acitivity)
        navController()

    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)


    }


    private fun navController() {
        setSupportActionBar(findViewById(R.id.toolBar))
        navController = Navigation.findNavController(this, R.id.activity_main_nav_host_fragment)
        val config = AppBarConfiguration(navController.graph)
        findViewById<Toolbar>(R.id.toolBar).setupWithNavController(navController, config)
        setupWithNavController(binding.bottomNav, navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            val toolBar = findViewById<Toolbar>(R.id.toolBar)
            if (destination.id == R.id.groupFragment) {
                toolBar.visibility = View.VISIBLE
                // binding.bottomNav.visibility = View.GONE
            } else {
                toolBar.visibility = View.VISIBLE
                binding.bottomNav.visibility = View.VISIBLE
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.homescreen_item_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == R.id.toolbarProfilePicture) {
            openProfile()
            return true
        }
        return false
        // R.id.service_camera -> Toast.makeText(this, "you clicked camera", Toast.LENGTH_SHORT).show()
        //  R.id.service_search -> Toast.makeText(this, "you clicked searched", Toast.LENGTH_SHORT).show()
        //  R.id.toolbarProfilePicture

    }

    private fun openProfile() {

        val intent = Intent(this, ProfileFragment::class.java)

        startActivity(intent)
       finish()

        /*val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.activity_main_nav_host_fragment, ProfileFragment(), null).commit()
        binding.bottomNav.visibility = View.GONE
        binding.toolBar.visibility = View.GONE*/
    }



}