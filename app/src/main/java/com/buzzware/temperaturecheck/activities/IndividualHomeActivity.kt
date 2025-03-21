package com.buzzware.temperaturecheck.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
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
import com.buzzware.temperaturecheck.fragments.IndividualHomeFragment
import com.buzzware.temperaturecheck.fragments.InviteCommunityFragment
import com.buzzware.temperaturecheck.fragments.ProfileFragment
import com.buzzware.temperaturecheck.model.GroupModel
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

    private var requestList : ArrayList<GroupModel>? = ArrayList<GroupModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val User_Id = intent.getStringExtra("User_ID") ?: ""

        if (User_Id != "")
        {
            getUserData(User_Id)
        }

        if (Constants.intentUserID != ""){
            startActivity(Intent(this, GroupInvitationActivity::class.java))
        }

        setDrawer()
        googleSignIn()
        setView()
        setListener()

        getInvitationData()

    }

    private fun getInvitationData() {


        db.collection("Groups").addSnapshotListener { snapshots, error ->
            if (error != null) {
                showAlert(error.message)
                return@addSnapshotListener
            }
            requestList?.clear()

            if (snapshots != null) {
                var count = 0

                for (document in snapshots.documents) {

                    val comunityMap = document.get("comunity") as? Map<String, String>
                    if (comunityMap?.get(Constants.currentUser.id) == "requested"){
                        count++
                        val model = document.toObject(GroupModel::class.java)
                        requestList?.add(model!!)

                    }
                }
                if (count != 0)
                {
                    binding.navView.notification.visibility = View.VISIBLE
                    binding.navView.notification.text = count.toString()
                }else
                {
                    binding.navView.notification.visibility = View.GONE
                }

            }
        }

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

    override fun onResume() {
        super.onResume()
        binding.titleTV.text = "Temperature-Check"
    }

    override fun onRestart() {
        super.onRestart()
        binding.titleTV.text = "Temperature-Check"
    }

    private fun setView() {

        binding.titleTV.text = "Temperature-Check"


        loadFragment(IndividualHomeFragment())

        //binding.navView.communityLayout.visibility = View.GONE

        Glide.with(this)
            .load(Constants.currentUser.image)
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .into(binding.navView.navUserIV)
        Glide.with(this)
            .load(Constants.currentUser.image)
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .into(binding.profileIV)

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
            setInviteCommunityFragment(requestList)
        }
        binding.navView.myCommunityLayout.setOnClickListener {
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
        binding.navView.logOut.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(this,LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        binding.logoutIV.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(this,LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
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

    private fun setInviteCommunityFragment(requestList: ArrayList<GroupModel>?) {
        binding.titleTV.text = "Community Invitation"
        binding.profileIV.visibility = View.GONE
        binding.logoutIV.visibility = View.GONE
        loadFragmentBackStack(InviteCommunityFragment(requestList))
    }

    private fun setProfileFragment() {
        binding.titleTV.text = "Profile"
        binding.profileIV.visibility = View.GONE
        binding.logoutIV.visibility = View.VISIBLE
        loadFragmentBackStack(ProfileFragment())

    }

    private fun setMessagesFragment() {
        binding.titleTV.text = "Messages"
        binding.profileIV.visibility = View.GONE
        binding.logoutIV.visibility = View.GONE
        loadFragmentBackStack(ChatFragment())
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(fadeIn, fadeOut)
        transaction.replace(binding.container.id, fragment)
        transaction.commit()

    }

    private fun loadFragmentBackStack(fragment: Fragment) {

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

    override fun onBackPressed() {
        super.onBackPressed()
        val currentFragment = supportFragmentManager.findFragmentById(R.id.container)
        when (currentFragment){
            is IndividualHomeFragment ->{binding.titleTV.text = "Temperature-Check"}
        }
    }

}