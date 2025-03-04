package com.buzzware.temperaturecheck.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.ActivityHomeBinding
import com.buzzware.temperaturecheck.fragments.ChatFragment
import com.buzzware.temperaturecheck.fragments.CheckInFragment
import com.buzzware.temperaturecheck.fragments.CommunityFragment
import com.buzzware.temperaturecheck.fragments.HomeFragment
import com.buzzware.temperaturecheck.fragments.ProfileFragment
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle

class HomeActivity : BaseActivity(), HomeFragment.ItemClickListeners {

    private val binding : ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setDrawer()
        setView()
        setListener()

    }

    private fun setDrawer() {

        val drawerToggle = DuoDrawerToggle(this, binding.drawer,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.drawer.setDrawerListener(drawerToggle)
        drawerToggle.syncState()

    }

    private fun checkOpenOrCloseDrawer() {
        if (binding.drawer.isDrawerOpen) {
            binding.drawer.closeDrawer(Gravity.LEFT)
        } else {
            binding.drawer.openDrawer(Gravity.LEFT)
        }
    }

    private fun setView() {

        loadFragment(HomeFragment())

        binding.navView.communityLayout.visibility = View.VISIBLE

        Glide.with(this)
            .load(Constants.currentUser.image)
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .into(binding.navView.navUserIV)

        binding.navView.navUserIV.setOnClickListener { showToast("Toast") }

    }

    private fun setListener() {

        binding.menuIV.setOnClickListener { checkOpenOrCloseDrawer() }

        binding.navView.closeDrawer.setOnClickListener { checkOpenOrCloseDrawer() }

        binding.navView.homeLayout.setOnClickListener {
            checkOpenOrCloseDrawer()
            setHomeFragment()
        }
        binding.navView.checkInLayout.setOnClickListener {
            checkOpenOrCloseDrawer()
            setCheckInFragment()
        }
        binding.navView.communityLayout.setOnClickListener {
            checkOpenOrCloseDrawer()
            setCommunityFragment()
        }
        binding.navView.profileLayout.setOnClickListener {
            checkOpenOrCloseDrawer()
            setProfileFragment()
        }
        binding.navView.MessagesLayout.setOnClickListener {
            checkOpenOrCloseDrawer()
            setMessagesFragment()
        }

        binding.profileIV.setOnClickListener {
            setProfileFragment()
        }

        binding.navView.findTherapistLayout.setOnClickListener {
            checkOpenOrCloseDrawer()
            startActivity(Intent(this, FindTherapistActivity::class.java))
            overridePendingTransition(fadeIn, fadeOut)
        }
    }

    private fun setHomeFragment() {
        binding.titleTV.text = "Temperature-Check"
        binding.profileIV.visibility = View.VISIBLE
        binding.logoutIV.visibility = View.GONE
        loadFragment(HomeFragment())
    }

    private fun setCheckInFragment() {
        binding.titleTV.text = "My Checkins"
        binding.profileIV.visibility = View.GONE
        binding.logoutIV.visibility = View.GONE
        loadFragmentBackStack(CheckInFragment())

    }

    private fun setCommunityFragment() {
        binding.titleTV.text = "My Community"
        binding.profileIV.visibility = View.GONE
        binding.logoutIV.visibility = View.GONE
        loadFragmentBackStack(CommunityFragment())
    }

    private fun setMessagesFragment() {
        binding.titleTV.text = "Messages"
        binding.profileIV.visibility = View.GONE
        binding.logoutIV.visibility = View.GONE
        loadFragmentBackStack(ChatFragment())
    }

    private fun setProfileFragment() {
        binding.titleTV.text = "Profile"
        binding.profileIV.visibility = View.GONE
        binding.logoutIV.visibility = View.VISIBLE
        loadFragmentBackStack(ProfileFragment())

    }

    private fun loadFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(fadeIn, fadeOut)
        transaction.replace(binding.container.id, fragment)
        transaction.commit()

    }

    private fun loadFragmentBackStack(fragment: Fragment) {

        // Clear all fragments in the back stack

        supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(fadeIn, fadeOut)
        transaction.replace(binding.container.id, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }

    override fun onCommunityClick() {
        setCommunityFragment()
    }

    override fun onCheckInClick() {
        setCheckInFragment()
    }
}