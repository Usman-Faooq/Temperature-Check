package com.buzzware.temperaturecheck.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.databinding.SearchCommunityMemberItemDesignBinding
import com.buzzware.temperaturecheck.model.UserModel
import com.google.firebase.firestore.FirebaseFirestore

class InviteCommunityAdapter(
    val context: Context,
    val arraylist: ArrayList<UserModel>,
    val listener: OnClicked,
    val groupID: String
) : RecyclerView.Adapter<InviteCommunityAdapter.ViewHolder>() {


    interface OnClicked
    {
        fun OnItemClicked(id: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InviteCommunityAdapter.ViewHolder {
        return ViewHolder(SearchCommunityMemberItemDesignBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return arraylist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = arraylist[position]

        holder.binding.userName.text = list.userName

        Glide.with(context)
            .load(list.image)
            .placeholder(R.mipmap.ic_launcher)
            .into(holder.binding.userRoundedImageView)


        getMemberStatus(groupID,list.id){memberStatus ->
            if (memberStatus == "accepted")
            {
                holder.binding.requestBtn.text = "Member"

            }else if (memberStatus == "requested"){
                holder.binding.requestBtn.text = "requested"
            }else{
                holder.binding.requestBtn.text = "request"
            }
        }

        holder.binding.requestBtn.setOnClickListener {
            if (holder.binding.requestBtn.text == "request")
            holder.binding.requestBtn.text = "requested"
            listener.OnItemClicked(list.id)
        }
    }



    fun getMemberStatus(groupId: String, idToCheck: String, callback: (String) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Groups").document(groupId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val communityMap = document.get("comunity") as? Map<String, String>
                    val status = communityMap?.get(idToCheck)

                    when (status) {
                        "accepted" -> callback("accepted")
                        "requested" -> callback("requested")
                        else -> callback("not_found") // ID not present in the map
                    }
                } else {
                    callback("not_found") // Document does not exist
                }
            }
            .addOnFailureListener {
                callback("error") // Handle failure case
            }
    }




    inner class ViewHolder(val binding : SearchCommunityMemberItemDesignBinding) : RecyclerView.ViewHolder(binding.root)


}