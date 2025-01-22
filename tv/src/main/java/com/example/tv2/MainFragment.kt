//package com.example.tv2
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.fragment.app.Fragment
//import androidx.core.content.ContextCompat
//import androidx.leanback.app.BrowseFragment
//import androidx.leanback.widget.ArrayObjectAdapter
//import androidx.leanback.widget.ListRow
//import androidx.leanback.widget.ListRowPresenter
//import androidx.leanback.widget.HeaderItem
//import com.bumptech.glide.Glide
//import android.os.Handler
//import android.util.DisplayMetrics
//import android.graphics.drawable.Drawable
//
//
//class MainFragment : BrowseFragment() {
//
//    private lateinit var mMetrics: DisplayMetrics
//    private var mDefaultBackground: Drawable? = null
//    private val handler = Handler()
//    private val bgImageUrls = listOf(
//        "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review/bg.jpg",
//        "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Demo%20Slam/Google%20Demo%20Slam_%2020ft%20Search/bg.jpg",
//        "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Gmail%20Blue/bg.jpg",
//        "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Fiber%20to%20the%20Pole/bg.jpg",
//        "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Nose/bg.jpg"
//    )
//
//    private var currentIndex = 0
//    private lateinit var imageView: ImageView
//    private lateinit var descriptionView: TextView
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        imageView = view.findViewById(R.id.image_view)
//        descriptionView = view.findViewById(R.id.description_view)
//
//        mDefaultBackground = ContextCompat.getDrawable(requireContext(), R.drawable.default_background)
//
//        startImageSlideShow()
//
//        loadRows()  // Call loadRows to populate the rows with cards
//    }
//
//    private fun loadRows() {
//        // Create an ArrayObjectAdapter for the ListRowPresenter
//        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
//
//        // Create the CardPresenter to display each card
//        val cardPresenter = CardPresenter()
//
//        // Loop to create some rows and cards
//        for (i in 1..3) { // Create 3 rows as an example
//            // Create an ArrayObjectAdapter for each row of cards
//            val listRowAdapter = ArrayObjectAdapter(cardPresenter)
//
//            // Add 5 cards to each row
//            for (j in 1..5) {
//                val title = "Row $i - Item $j"
//                val description = "Description for $title"
//                val imageUrl = bgImageUrls[(i * j) % bgImageUrls.size]
//                listRowAdapter.add(CardItem(title, description, imageUrl))
//            }
//
//            // Add the row to the rowsAdapter
//            rowsAdapter.add(ListRow(HeaderItem(i.toLong(), "Row $i"), listRowAdapter))
//        }
//
//        // Set the adapter for the fragment (using the BrowseFragment's adapter property)
//        adapter = rowsAdapter  // 'adapter' is inherited from BrowseFragment
//    }
//
//    private fun startImageSlideShow() {
//        handler.postDelayed(object : Runnable {
//            override fun run() {
//                updateImageAndDescription()
//                currentIndex = (currentIndex + 1) % bgImageUrls.size
//                handler.postDelayed(this, 5000) // Slide every 5 seconds
//            }
//        }, 5000)
//    }
//
//    private fun updateImageAndDescription() {
//        val imageUrl = bgImageUrls[currentIndex]
//        val description = "Image ${currentIndex + 1} of ${bgImageUrls.size}"
//
//        // Update image
//        Glide.with(this)
//            .load(imageUrl)
//            .centerCrop()
//            .placeholder(mDefaultBackground)
//            .into(imageView)
//
//        // Update description
//        descriptionView.text = description
//    }
//}
