package com.buzzware.temperaturecheck.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.buzzware.temperaturecheck.activities.MyGroupDetail
import com.buzzware.temperaturecheck.adapters.MyGroupCommunityAdapter
import com.buzzware.temperaturecheck.databinding.FragmentMyGroupCommunityBinding
import com.buzzware.temperaturecheck.model.GroupModel

class MyGroupCommunity : BaseFragment(),MyGroupCommunityAdapter.OnClicked {

    private val binding : FragmentMyGroupCommunityBinding by lazy {
        FragmentMyGroupCommunityBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View{


        setViews()
        setListener()


        return binding.root
    }

    private fun setListener() {

        binding.addTeam.setOnClickListener {
            BottomFragmentAddGroup().show(parentFragmentManager,"")
        }

    }

    private fun setViews(){
        getMyGroupData()

    }

    private fun getMyGroupData()
    {
        db.collection("Groups")
            .whereEqualTo("userId","cJsN8ylqXTRl1TeV4dRTS2M3Y7E3")
            .addSnapshotListener { value, error ->
                if (error != null)
                {
                    showAlert(error.message)
                }else{
                    val model = value?.toObjects(GroupModel::class.java) as ArrayList<GroupModel>
                    model.sortedByDescending { it.date }
                    val linearLayoutManager = LinearLayoutManager(context)
                    binding.myGroupRecycler.layoutManager = linearLayoutManager
                    binding.myGroupRecycler.adapter = MyGroupCommunityAdapter(requireContext(),model,this)

                }

            }
    }

    override fun OnItemClicked(name: String, community: HashMap<String, String>, groupID: String) {

        val intent = Intent(context,MyGroupDetail::class.java)
        intent.putExtra("groupName",name)
        intent.putExtra("groupMembers", community)
        intent.putExtra("groupID", groupID)
        startActivity(intent)
    }
}