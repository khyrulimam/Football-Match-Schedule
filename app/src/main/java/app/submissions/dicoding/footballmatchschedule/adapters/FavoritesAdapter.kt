package app.submissions.dicoding.footballmatchschedule.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.submissions.dicoding.footballmatchschedule.R
import app.submissions.dicoding.footballmatchschedule.adapters.FavoritesAdapter.BaseFavoriteViewHolder
import app.submissions.dicoding.footballmatchschedule.db.tables.Favorites
import app.submissions.dicoding.footballmatchschedule.exts.fontGoogleProductBold
import app.submissions.dicoding.footballmatchschedule.exts.fontGoogleProductRegular
import app.submissions.dicoding.footballmatchschedule.exts.loadWithGlide
import app.submissions.dicoding.footballmatchschedule.models.holders.ItemType
import kotlinx.android.synthetic.main.next_match_item.view.*
import kotlinx.android.synthetic.main.single_news_item.view.*

class FavoritesAdapter(val favorites: List<Favorites>, val click: (Favorites) -> Unit) : RecyclerView.Adapter<BaseFavoriteViewHolder>() {

  lateinit var holder: BaseFavoriteViewHolder

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseFavoriteViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    when (viewType) {
      ItemType.NEXT -> holder = NextMatchViewHolder(inflater.inflate(R.layout.next_match_item, parent, false), click)
      ItemType.PAST -> holder = PreviousMatchViewHolder(inflater.inflate(R.layout.single_news_item, parent, false), click)
    }
    return holder
  }

  override fun getItemViewType(position: Int): Int = favorites[position].dataToObject().itemType.field

  override fun getItemCount(): Int = favorites.size

  override fun onBindViewHolder(holder: BaseFavoriteViewHolder, position: Int) {
    holder.bind(favorites[position])
  }

  class PreviousMatchViewHolder(view: View, val click: (Favorites) -> Unit) : BaseFavoriteViewHolder(view) {
    override fun bind(data: Favorites) {
      itemView?.apply {
        data.dataToObject().event.apply {
          winnerBanner { ivImageView.loadWithGlide(it) }
          tvTitle.text = headline()
          tvDate.text = getFormattedDate()
        }

        tvTitle.fontGoogleProductBold()
        tvDate.fontGoogleProductRegular()
        setOnClickListener { click(data) }
      }
    }
  }

  class NextMatchViewHolder(view: View, val click: (Favorites) -> Unit) : BaseFavoriteViewHolder(view) {
    override fun bind(data: Favorites) {
      itemView?.apply {
        data.dataToObject().event.apply {
          teamHomeBadge { ivHome.loadWithGlide(it) }
          teamAwayBadge { ivAway.loadWithGlide(it) }
          tvTime.text = getTime()
          tvHome.text = strHomeTeam
          tvAway.text = strAwayTeam
        }

        setOnClickListener { click(data) }
      }
    }
  }

  abstract class BaseFavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(data: Favorites)
  }
}