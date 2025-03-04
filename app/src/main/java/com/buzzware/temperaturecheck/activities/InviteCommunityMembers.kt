package com.buzzware.temperaturecheck.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.adapters.InviteCommunityAdapter
import com.buzzware.temperaturecheck.databinding.ActivityInviteCommunityMembersBinding
import com.buzzware.temperaturecheck.fragments.BaseFragment
import com.buzzware.temperaturecheck.model.UserModel

class InviteCommunityMembers : BaseActivity() {

    private val binding : ActivityInviteCommunityMembersBinding by lazy {
        ActivityInviteCommunityMembersBinding.inflate(layoutInflater)
    }
    var communityList : ArrayList<UserModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setViews()
        setListener()


        setContentView(binding.root)

    }

    private fun setListener() {
        binding.userName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    // Clear the list if the search input is empty
                    communityList.clear()
                    binding.communityRecycler.visibility = View.GONE
                    binding.communityRecycler.visibility = View.VISIBLE
                } else {
                    // Perform search after a delay if text is not empty
//                    Handler.postDelayed({
//                        binding.emptyViewTV.visibility = View.GONE
//                        performSearch(s.toString())
//                    }, 1000)
                    performSearch(s.toString())
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setViews() {

    }


    private fun performSearch(queryText: String) {

        val endText = "$queryText\uf8ff"
        db.collection("Users")
            .orderBy("firstName")
            .whereGreaterThanOrEqualTo("firstName", queryText)
            .whereLessThanOrEqualTo("firstName", endText)
            .addSnapshotListener { queryDocumentSnapshots, e ->
                if (e != null) {
                    Log.d("LOGGER", "Error: ${e.message}")
                    return@addSnapshotListener
                }

                queryDocumentSnapshots?.let {
                    communityList.clear()
                    communityList.addAll(it.toObjects(UserModel::class.java))
                    Log.d("LOGGER", "Book List Size: ${communityList.size}")


                    val linearLayoutManager = LinearLayoutManager(this)
                    binding.communityRecycler.layoutManager = linearLayoutManager
                    binding.communityRecycler.adapter = InviteCommunityAdapter(this,communityList)

                }
            }
    }
}