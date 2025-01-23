//package com.example.tv2
//
//import android.content.Intent
//import android.graphics.drawable.Drawable
//import android.os.Bundle
//import android.os.Handler
//import android.util.DisplayMetrics
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.FragmentActivity
//import androidx.leanback.app.BackgroundManager
//import androidx.leanback.app.BrowseSupportFragment
//import androidx.leanback.widget.*
//import androidx.recyclerview.widget.GridLayoutManager
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.target.CustomTarget
//import com.bumptech.glide.request.transition.Transition
//
//class MainActivity : FragmentActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.main_browse_fragment, MainFragment())
//                .commitNow()
//        }
//    }
//}
//
//class MainFragment : BrowseSupportFragment() {
//
//    private lateinit var mMetrics: DisplayMetrics
//    private lateinit var mBackgroundManager: BackgroundManager
//    private var mDefaultBackground: Drawable? = null
//    private val handler = Handler()
//    private val bgImageUrl = arrayOf(
//        "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review/bg.jpg",
//        "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Demo%20Slam/Google%20Demo%20Slam_%2020ft%20Search/bg.jpg",
//        "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Gmail%20Blue/bg.jpg",
//        "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Fiber%20to%20the%20Pole/bg.jpg",
//        "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Nose/bg.jpg"
//    )
//
//    private var currentIndex = 0
//    private var currentRowIndex = 0
//    private var currentColumnIndex = 0
//    private lateinit var gridView: VerticalGridView
//
//
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        title = "Android TV App"
//        prepareBackgroundManager()
//        setupUIElements()
//        loadRows()
//
//        // Auto-scroll every 5 seconds
//        handler.postDelayed(object : Runnable {
//            override fun run() {
//                autoScroll()
//                handler.postDelayed(this, 5000)
//            }
//        }, 5000)
//
//        setOnItemViewSelectedListener { _, item, _, _ ->
//            if (item is CardItem) {
//                updateBackground(item.productUrl)
//                Log.d("MainFragment", "Selected card: ${item.productTitle}")
//            }
//        }
//
////        setOnItemViewClickedListener { _, item, _, _ ->
////            if (item is CardItem) {
////                val intent = Intent(activity, DetailsActivity::class.java)
////                intent.putExtra("ITEM_TITLE", item.title)
////                startActivity(intent)
////            }
////        }
//    }
//
//    private fun prepareBackgroundManager() {
//        mBackgroundManager = BackgroundManager.getInstance(activity)
//        mBackgroundManager.attach(requireActivity().window)
//        mDefaultBackground = ContextCompat.getDrawable(requireActivity(), R.drawable.default_background)
//        mMetrics = DisplayMetrics()
//        requireActivity().windowManager.defaultDisplay.getMetrics(mMetrics)
//    }
//
////    private fun setupUIElements() {
////        brandColor = resources.getColor(R.color.selected_background)
////        headersState = HEADERS_ENABLED
////        isHeadersTransitionOnBackEnabled = true
////    }
//    private fun setupUIElements() {
//        brandColor = ContextCompat.getColor(requireContext(), R.color.default_background) // For compatibility
//        headersState = HEADERS_DISABLED // Disables headers entirely
//        isHeadersTransitionOnBackEnabled = false // Optional: No headers transition needed
//    }
//
//    private fun loadRows() {
//        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
//        val cardPresenter = CardPresenter()
//
//        for (i in 1..10) { // Create 3 rows as an example
//            val listRowAdapter = ArrayObjectAdapter(cardPresenter)
//            for (j in 1..4) {
//                val title = "Row $i - Item $j"
//                val description = "Description for $title"
//                val imageUrl = bgImageUrl[(i * j) % bgImageUrl.size]
//                listRowAdapter.add(CardItem(title, description, imageUrl))
//            }
//            rowsAdapter.add(ListRow(HeaderItem(i.toLong(), ""), listRowAdapter))
////            rowsAdapter.add(ListRow(HeaderItem(i.toLong(), "Row $i"), listRowAdapter))
//        }
//
//        adapter = rowsAdapter
//    }
//
//    private fun updateBackground(imageUrl: String) {
//        Glide.with(requireActivity())
//            .load(imageUrl)
//            .centerCrop()
//            .into(object : CustomTarget<Drawable>() {
//                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
////                   mBackgroundManager.drawable = resource
////                    mBackgroundManager.drawable = mDefaultBackground
//                    mBackgroundManager.drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.movie)
//                }
//
//                override fun onLoadCleared(placeholder: Drawable?) {
//
//                    mBackgroundManager.drawable = mDefaultBackground
//                }
//            })
//    }
//
//    private fun autoScroll() {
//        val nextPosition = (selectedPosition + 1) % (adapter?.size() ?: 1)
//        Log.d("MainFragment", "Next position: $nextPosition (Row: $currentRowIndex, Column: $currentColumnIndex)")
//        setSelectedPosition(nextPosition)
//    }
//
//
//}
//
//class CardPresenter : Presenter() {
//    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.card_view, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any?) {
//        val card = item as CardItem
//        val titleView = viewHolder.view.findViewById<TextView>(R.id.card_title)
//        val descriptionView = viewHolder.view.findViewById<TextView>(R.id.card_description)
//        val imageView = viewHolder.view.findViewById<ImageView>(R.id.card_background_image)
//
//        titleView.text = card.title
//        descriptionView.text = card.description
//
//        Glide.with(viewHolder.view.context)
//            .load(card.imageUrl)
//            .error(R.drawable.movie)
//            .into(imageView)
//    }
//
//    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
//        // Clean up resources if needed
//    }
//}
