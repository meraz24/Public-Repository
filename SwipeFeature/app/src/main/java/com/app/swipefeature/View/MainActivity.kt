package com.app.swipefeature.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.DefaultItemAnimator
import com.app.swipefeature.Adapter.DiscoverUserAdapter
import com.app.swipefeature.Model.Item
import com.app.swipefeature.R
import com.app.swipefeature.SwipeViewUtil.CardStackLayoutManager
import com.app.swipefeature.SwipeViewUtil.CardStackListener
import com.app.swipefeature.SwipeViewUtil.Direction
import com.app.swipefeature.SwipeViewUtil.SwipeableMethod
import com.app.swipefeature.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), CardStackListener {
    private lateinit var layoutManager: CardStackLayoutManager
    private lateinit var binding: ActivityMainBinding
    private var adapter: DiscoverUserAdapter? = null
    private lateinit var userList: List<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize CardStackLayoutManager with desired settings
        layoutManager = CardStackLayoutManager(this@MainActivity, this).apply {
            setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
            setOverlayInterpolator(LinearInterpolator())
        }

        // Sample user data
        userList = listOf(
            Item(
                "No Description",
                23,
                "Lisa Martin",
                listOf(
                    R.drawable.test_image_three,
                    R.drawable.test_image,
                    R.drawable.test_image_two,
                    R.drawable.test_image,
                    R.drawable.test_image_three
                )
            ),
            Item(
                "No Description",
                22,
                "Kalvin Crook",
                listOf(
                    R.drawable.test_image,
                    R.drawable.test_image_two,
                    R.drawable.test_image_three,
                    R.drawable.test_image_two
                )
            ),
            Item(
                "No Description",
                30,
                "Kim Joseph",
                listOf(
                    R.drawable.test_image,
                    R.drawable.test_image_two,
                    R.drawable.test_image_three
                )
            )
            // Add more items as needed
        )

        // Check if there are items in the user list before setting the adapter
        if (userList.size > 0) {
            setAdapter(userList)
        }
    }

    // Function to set up the RecyclerView adapter and layoutManager
    private fun setAdapter(list: List<Item>) {
        // Set CardStackLayoutManager to the RecyclerView
        binding?.stackView?.layoutManager = layoutManager
        // Create and set DiscoverUserAdapter to the RecyclerView
        adapter = DiscoverUserAdapter(this@MainActivity, list)
        binding?.stackView?.adapter = adapter
        // Disable change animations for item animator if it is DefaultItemAnimator
        binding?.stackView?.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    // CardStackListener methods for handling card actions
    override fun onCardDragging(direction: Direction?, ratio: Float) {
        Log.d("TAG", "onCardDragging: " + direction)
        // Display appropriate image based on swipe direction
        var directionValue = direction.toString()
        if (directionValue == "Left") {
            binding?.imageLikeDislike?.visibility = View.VISIBLE
            binding?.imageLikeDislike?.setImageResource(R.drawable.icon_dislike)
        } else {
            binding?.imageLikeDislike?.visibility = View.VISIBLE
            binding?.imageLikeDislike?.setImageResource(R.drawable.icon_like)
        }
    }

    override fun onCardSwiped(direction: Direction?) {
        Log.d("TAG", "onCardSwiped: " + direction)
        // Additional actions when a card is swiped
        var directionValue = direction.toString()
    }

    override fun onCardRewound() {
        Log.d("TAG", "onCardRewound")
        // Additional actions when a card is rewound
    }

    override fun onCardCanceled() {
        Log.d("TAG", "onCardCanceled")
        // Hide the like/dislike image when card is canceled
        binding?.imageLikeDislike?.visibility = View.GONE
    }

    override fun onCardAppeared(view: View?, position: Int) {
        Log.d("TAG", "onCardAppeared" + position)
        // Additional actions when a card appears
    }

    override fun onCardDisappeared(view: View?, position: Int) {
        // Hide like/dislike image when a card disappears
        binding?.imageLikeDislike?.visibility = View.GONE
        // Check if the last card has disappeared, and update visibility accordingly
        if (position == userList.size - 1) {
            binding?.stackView?.visibility = View.GONE
            binding?.rlViewNoData?.visibility = View.VISIBLE
            Log.d("TAG", "onCardDisappeared: " + position.toString())
        }
    }
}
