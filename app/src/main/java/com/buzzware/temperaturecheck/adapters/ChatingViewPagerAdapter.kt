package com.buzzware.temperaturecheck.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.buzzware.temperaturecheck.fragments.ChatFragment

class ChatingViewPagerAdapter(private var fragments: List<Fragment>, chatFragment: ChatFragment) : RecyclerView.Adapter<ChatingViewPagerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val frameLayout = FrameLayout(parent.context)
        frameLayout.id = View.generateViewId()
        frameLayout.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return ViewHolder(frameLayout)
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fragment = fragments[position]
        val fragmentManager = (holder.itemView.context as FragmentActivity).supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(holder.itemView.id, fragment)
            .commit()
    }

    fun updateFragments(newFragments: List<Fragment>) {
        this.fragments = newFragments
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}