package com.vsple.datingapp.Adapters.DiscoverUser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.swipefeature.R

class UserImagesAdapter(
    private var userImageList: List<Int>,
) :
    RecyclerView.Adapter<UserImagesAdapter.MyViewHolder>() {
    private var onClickListener: OnClickListener? = null
    var selectedPosition: Int = 0

    // Create ViewHolder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_images, parent, false)
        return MyViewHolder(itemView)
    }

    // Bind data to ViewHolder
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // Set image resource
        holder.imageView.setImageResource(userImageList[position])

        // Set OnClickListener for the image view
        holder.imageView.setOnClickListener {
            if (userImageList.size > 0) {
                // Update the image on click
                val nextPosition = (selectedPosition + 1) % userImageList.size
                holder.imageView.setImageResource(userImageList[nextPosition])
                selectedPosition = nextPosition
                // Notify the listener about the button click
                onClickListener?.onButtonClick(selectedPosition, userImageList.size)
            }
        }
    }

    // Return the total number of items in the data set
    override fun getItemCount(): Int {
        return userImageList.size
    }

    // ViewHolder class
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView = view.findViewById<ImageView>(R.id.imageView)
    }

    // Interface for handling button clicks
    interface OnClickListener {
        fun onButtonClick(position: Int, listSize: Int)
    }

    // Set OnClickListener for the adapter
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
}
