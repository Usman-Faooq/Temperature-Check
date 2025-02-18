package com.buzzware.temperaturecheck.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
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
import com.buzzware.temperaturecheck.fragments.CheckInFragment
import com.buzzware.temperaturecheck.fragments.CommunityFragment
import com.buzzware.temperaturecheck.fragments.HomeFragment
import com.buzzware.temperaturecheck.fragments.IndividualHomeFragment
import com.buzzware.temperaturecheck.fragments.ProfileFragment
import com.buzzware.temperaturecheck.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.messaging.FirebaseMessaging
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle

class IndividualHomeActivity : BaseActivity(), IndividualHomeFragment.ItemClickListeners {

    private val binding : ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val User_Id = intent.getStringExtra("User_ID") ?: ""

        if (User_Id != "")
        {
            getUserData(User_Id)
        }

        setDrawer()
        googleSignIn()
        setView()
        setListener()

    }

    private fun googleSignIn() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun getUserData(userId : String){
        mDialog.show()

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            db.collection("Users").document(userId).update("token", it.toString())
        }

        db.collection("Users")
            .document(userId)
            .get()
            .addOnSuccessListener{ document ->
                if (document.exists())
                {
                    mDialog.dismiss()
                    Constants.currentUser = document.toObject(UserModel::class.java)!!
                }
                else
                {
                    mDialog.dismiss()
                    finish()
                }

            }
            .addOnFailureListener{ exception ->
                mDialog.dismiss()
                showAlert(exception.message.toString())
            }



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

        loadFragment(IndividualHomeFragment())

        binding.navView.communityLayout.visibility = View.GONE

        Glide.with(this)
            .load(Constants.currentUser.image)
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .into(binding.navView.navUserIV)

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

        binding.navView.profileLayout.setOnClickListener {
            checkOpenOrCloseDrawer()
            setProfileFragment()
        }

        binding.profileIV.setOnClickListener {
            setProfileFragment()
        }

        binding.navView.findTherapistLayout.setOnClickListener {
            checkOpenOrCloseDrawer()
            startActivity(Intent(this, FindTherapistActivity::class.java))
            overridePendingTransition(fadeIn, fadeOut)
        }

        binding.logoutIV.setOnClickListener {
            googleSignInClient.signOut().addOnCompleteListener {
                mAuth.signOut()
                val intent = Intent(this,LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }

        }

    }

    private fun setHomeFragment() {
        binding.titleTV.text = "Temperature-Check"
        binding.profileIV.visibility = View.VISIBLE
        binding.logoutIV.visibility = View.GONE
        loadFragment(IndividualHomeFragment())
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

    override fun onCheckInClick() {
        setCheckInFragment()
    }

}