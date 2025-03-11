package com.buzzware.temperaturecheck.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.buzzware.temperaturecheck.adapters.RequestCommunityAdapter
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.FragmentInviteCommunityBinding
import com.buzzware.temperaturecheck.model.GroupModel


class InviteCommunityFragment(val requestList: ArrayList<GroupModel>?) : BaseFragment(),RequestCommunityAdapter.OnAccepted {

    private val binding : FragmentInviteCommunityBinding by lazy {
        FragmentInviteCommunityBinding.inflate(layoutInflater)
    }

    lateinit var requestCommunityAdapter: RequestCommunityAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{




        getInviations()
        return binding.root
    }




    private fun getInviations() {
        requestList?.forEach { it1 ->
            db.collection("Users")
                .document(it1.userId)
                .get()
                .addOnSuccessListener { doc ->
                    it1.userName = doc.getString("firstName") + doc.getString("lastName")
                    it1.image = doc.getString("image")!!

                    val linearLayoutManager = LinearLayoutManager(context)
                    binding.requestRecycler.layoutManager = linearLayoutManager
                    requestCommunityAdapter = RequestCommunityAdapter(requireContext(),requestList,this)
                    binding.requestRecycler.adapter = requestCommunityAdapter



                }
                .addOnFailureListener { exception ->
                    showAlert(exception.message)
                }
        }

        //requestList?.clear()
    }

    override fun OnAcceptedClicked(id: String, position: Int) {

        db.collection("Groups")
            .document(id)
            .update("comunity.${Constants.currentUser.id}","accepted")
            .addOnSuccessListener {

            }
            .addOnFailureListener { exception ->
                showAlert(exception.message)
            }

    }

}