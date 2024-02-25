package com.app.swipefeature.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.swipefeature.Model.Item
import com.app.swipefeature.R
import com.vsple.datingapp.Adapters.DiscoverUser.IndicatorAdapter
import com.vsple.datingapp.Adapters.DiscoverUser.UserImagesAdapter

class DiscoverUserAdapter(
    private var context: Context,
    private var userImageList: List<Item>
) : RecyclerView.Adapter<DiscoverUserAdapter.MyViewHolder>() {
    private var onClickListener: OnClickListener? = null
    private var imagePosition: Int = 0

    // Create ViewHolder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_discover_user_details, parent, false)
        return MyViewHolder(itemView)
    }

    // Bind data to ViewHolder
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // Set user details
        holder.tvName.text = userImageList[position].name
        holder.tvAge.text = userImageList[position].age.toString()

        // Set up UserImagesAdapter for user photos
        val innerAdapter = UserImagesAdapter(userImageList[position].images)
        holder.rvUserPhotosView.adapter = innerAdapter
        holder.rvUserPhotosView.layoutManager =
            LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)

        // Set OnClickListener for user photos
        innerAdapter?.setOnClickListener(object : UserImagesAdapter.OnClickListener {
            override fun onButtonClick(position: Int, listSize: Int) {
                if (position in 0 until listSize) {
                    imagePosition = position
                    // Set up IndicatorAdapter for image indicator
                    val indicatorAdapter = IndicatorAdapter(listSize, imagePosition)
                    holder.rvIndicator.adapter = indicatorAdapter
                    holder.rvIndicator.layoutManager =
                        LinearLayoutManager(
                            holder.itemView.context,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                }
            }
        })

        // Set up IndicatorAdapter for image indicator
        val indicatorAdapter = IndicatorAdapter(userImageList[position].images.size, imagePosition)
        holder.rvIndicator.adapter = indicatorAdapter
        holder.rvIndicator.layoutManager =
            LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
    }

    // Return the total number of items in the data set
    override fun getItemCount(): Int {
        return userImageList.size
    }

    // ViewHolder class
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvName = view.findViewById<TextView>(R.id.tvName)
        var tvAge = view.findViewById<TextView>(R.id.tvAge)
        var rvUserPhotosView = view.findViewById<RecyclerView>(R.id.rvUserPhotosView)
        var rvIndicator = view.findViewById<RecyclerView>(R.id.rvIndicator)
    }

    // Interface for handling button clicks
    interface OnClickListener {
        fun onButtonClick(position: Int)
    }

    // Set OnClickListener for the adapter
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }


}
