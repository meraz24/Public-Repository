package com.vsple.datingapp.Adapters.DiscoverUser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.swipefeature.R

class IndicatorAdapter(
    private var userImageList: Int,
    private var selectedPosition: Int
) : RecyclerView.Adapter<IndicatorAdapter.MyViewHolder>() {
    private var onClickListener: OnClickListener? = null

    // Create ViewHolder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_indicator, parent, false)
        return MyViewHolder(itemView)
    }

    // Bind data to ViewHolder
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // Set background drawable based on selected position
        val drawableResId =
            if (position == selectedPosition) R.drawable.bg_selected_item else R.drawable.bg_unselected_item
        holder.viewIndicator.setBackgroundResource(drawableResId)
    }

    // Return the total number of items in the data set
    override fun getItemCount(): Int {
        return userImageList
    }

    // ViewHolder class
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var viewIndicator = view.findViewById<View>(R.id.viewIndicator)
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
