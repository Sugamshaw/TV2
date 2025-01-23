package newexample

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.VerticalGridView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.tv2.CardItem
import com.example.tv2.R

/**
 * Loads [MainFragment].
 */
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_browse_fragment, MainFragment())
                .commitNow()
        }
    }
}
data class CardItem(
    val title: String,
    val description: String,
    val imageUrl: String
)
class MainFragment : BrowseSupportFragment() {

    private lateinit var mMetrics: DisplayMetrics
    private lateinit var mBackgroundManager: BackgroundManager
    private var mDefaultBackground: Drawable? = null
    private val handler = Handler()
    private val bgImageUrl = arrayOf(
        "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review/bg.jpg",
        "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Demo%20Slam/Google%20Demo%20Slam_%2020ft%20Search/bg.jpg",
        "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Gmail%20Blue/bg.jpg",
        "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Fiber%20to%20the%20Pole/bg.jpg",
        "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Nose/bg.jpg"
    )

    private var currentIndex = 0
    private var currentRowIndex = 0
    private var currentColumnIndex = 0
    private lateinit var gridView: VerticalGridView



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        title = "Android TV App"
        prepareBackgroundManager()
        setupUIElements()
        loadRows()

        // Auto-scroll every 5 seconds
        handler.postDelayed(object : Runnable {
            override fun run() {
                autoScroll()
                handler.postDelayed(this, 5000)
            }
        }, 5000)

        setOnItemViewSelectedListener { _, item, _, _ ->
            if (item is CardItem) {
                updateBackground(item.productUrl)
                Log.d("MainFragment", "Selected card: ${item.productTitle}")
            }
        }

//        setOnItemViewClickedListener { _, item, _, _ ->
//            if (item is CardItem) {
//                val intent = Intent(activity, DetailsActivity::class.java)
//                intent.putExtra("ITEM_TITLE", item.title)
//                startActivity(intent)
//            }
//        }
    }

    private fun prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(activity)
        mBackgroundManager.attach(requireActivity().window)
        mDefaultBackground = ContextCompat.getDrawable(requireActivity(), R.drawable.default_background)
        mMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(mMetrics)
    }

    //    private fun setupUIElements() {
//        brandColor = resources.getColor(R.color.selected_background)
//        headersState = HEADERS_ENABLED
//        isHeadersTransitionOnBackEnabled = true
//    }
    private fun setupUIElements() {
        brandColor = ContextCompat.getColor(requireContext(), R.color.default_background) // For compatibility
        headersState = HEADERS_DISABLED // Disables headers entirely
        isHeadersTransitionOnBackEnabled = false // Optional: No headers transition needed
    }

    private fun loadRows() {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val cardPresenter = CardPresenter()

        for (i in 1..10) { // Create 3 rows as an example
            val listRowAdapter = ArrayObjectAdapter(cardPresenter)
            for (j in 1..6) {
                val price="99/kg"
                val title = "Row $i - New Item $j"
                val description = "Description for $title"
                val imageUrl = bgImageUrl[(i * j) % bgImageUrl.size]
                val mrpPrice="Mrp ₹450"
                listRowAdapter.add(CardItem(price,imageUrl,title, description, mrpPrice,i%4))
            }
            rowsAdapter.add(ListRow(HeaderItem(i.toLong(), ""), listRowAdapter))
//            rowsAdapter.add(ListRow(HeaderItem(i.toLong(), "Row $i"), listRowAdapter))
        }

        adapter = rowsAdapter
    }

    private fun updateBackground(imageUrl: String) {
        Glide.with(requireActivity())
            .load(imageUrl)
            .centerCrop()
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
//                   mBackgroundManager.drawable = resource
//                    mBackgroundManager.drawable = mDefaultBackground
                    mBackgroundManager.drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.movie)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    mBackgroundManager.drawable = mDefaultBackground
                }
            })
    }

    private fun autoScroll() {
        val nextPosition = (selectedPosition + 1) % (adapter?.size() ?: 1)
        Log.d("MainFragment", "Next position: $nextPosition (Row: $currentRowIndex, Column: $currentColumnIndex)")
        setSelectedPosition(nextPosition)
    }

//    private fun autoScroll() {
//        val rowsAdapter = adapter as? ArrayObjectAdapter ?: return
//
//        // Ensure the row index is within bounds
//        if (currentRowIndex >= rowsAdapter.size()) {
//            currentRowIndex = 0 // Reset to the first row
//        }
//
//        val listRow = rowsAdapter[currentRowIndex] as? ListRow ?: return
//        val listRowAdapter = listRow.adapter as? ArrayObjectAdapter ?: return
//
//        // Navigate to the next column in the current row
//        currentColumnIndex = (currentColumnIndex + 1) % listRowAdapter.size()
//
//        // Update the selected position of the row
//        setSelectedPosition(currentRowIndex)
//
//        // Access the RecyclerView backing the current row
//        val verticalGridView = verticalGridView
//        val rowViewHolder = verticalGridView?.findViewHolderForAdapterPosition(currentRowIndex) as? ListRowViewHolder
//        val horizontalGridView = rowViewHolder?.gridView
//
//        horizontalGridView?.apply {
//            // Ensure column index is within bounds
//            if (currentColumnIndex >= childCount) {
//                currentColumnIndex = 0
//            }
//
//            // Request focus for the desired column
//            val childView = getChildAt(currentColumnIndex)
//            childView?.requestFocus()
//        }
//
//        // Log for debugging
//        Log.d(
//            "MainFragment",
//            "Auto-scrolling Row: $currentRowIndex, Column: $currentColumnIndex"
//        )
//    }


}

class CardPresenter() : Presenter() {
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any?) {
        val card = item as CardItem
        val priceView = viewHolder.view.findViewById<TextView>(R.id.price_text)
        val imageView = viewHolder.view.findViewById<ImageView>(R.id.product_image)
        val titleView = viewHolder.view.findViewById<TextView>(R.id.product_title)
        val descriptionView = viewHolder.view.findViewById<TextView>(R.id.product_description)
        val mrpView = viewHolder.view.findViewById<TextView>(R.id.mrp_price)
        val linearbackground=viewHolder.view.findViewById<LinearLayout>(R.id.linearbackground)

        priceView.text=card.price
        Glide.with(viewHolder.view.context)
            .load(card.productUrl)
            .error(R.drawable.default_background)
            .into(imageView)

        titleView.text = card.productTitle
        descriptionView.text = card.productDescription
        mrpView.text=card.mrpPrice

        // Set the background color of the CardView dynamically
        val cardView = viewHolder.view.findViewById<CardView>(R.id.card_view) // Assuming you have an ID for the CardView
        val colourCardBackground = when (card.choice) {
            3 -> R.color.card_background1
            2 -> R.color.card_background2
            1 -> R.color.card_background3
            0 -> R.color.card_background4
            else -> R.color.card_background1
        }

        // Set background tint programmatically
        cardView.setBackgroundTintList(
            ContextCompat.getColorStateList(viewHolder.view.context, colourCardBackground)
        )


        val choice=card.choice
        val colourPriceView =when(choice)
        {
            3->R.color.prize_colour1
            2->R.color.prize_colour2
            1->R.color.prize_colour3
            0->R.color.prize_colour4
            else -> R.color.prize_colour1
        }

        val drawablePrize = ContextCompat.getDrawable(viewHolder.view.context, R.drawable.price_bg)
        if (drawablePrize is GradientDrawable) {
            // Change the color programmatically
            drawablePrize.setColor(ContextCompat.getColor(viewHolder.view.context, colourPriceView))
        }
        priceView.background = drawablePrize

        val colourCardNameDescriptionView =when(choice)
        {
            3->R.color.card_name_description1
            2->R.color.card_name_description2
            1->R.color.card_name_description3
            0->R.color.card_name_description4
            else -> R.color.card_name_description1
        }

        val drawableCardNameDescription = ContextCompat.getDrawable(viewHolder.view.context, R.drawable.card_name_description_bg)
        if (drawableCardNameDescription is GradientDrawable) {
            // Change the color programmatically
            drawableCardNameDescription.setColor(ContextCompat.getColor(viewHolder.view.context, colourCardNameDescriptionView))
        }
        linearbackground.background = drawableCardNameDescription

    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        // Clean up resources if needed
    }
}

