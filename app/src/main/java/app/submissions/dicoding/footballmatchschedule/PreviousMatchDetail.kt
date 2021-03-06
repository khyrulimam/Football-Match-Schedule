package app.submissions.dicoding.footballmatchschedule

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.view.Menu
import android.view.MenuItem
import app.submissions.dicoding.footballmatchschedule.adapters.FragmentPagerAdapter
import app.submissions.dicoding.footballmatchschedule.constants.Constants
import app.submissions.dicoding.footballmatchschedule.db.tables.Favorites
import app.submissions.dicoding.footballmatchschedule.exts.*
import app.submissions.dicoding.footballmatchschedule.fragments.Lineups
import app.submissions.dicoding.footballmatchschedule.fragments.Timeline
import app.submissions.dicoding.footballmatchschedule.models.Event
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.see_detail.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.ctx
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.design.snackbar

class PreviousMatchDetail : AppCompatActivity(), AnkoLogger, AppBarLayout.OnOffsetChangedListener {

  private var collapsedMenu: Menu? = null
  private var isAppBarExpanded: Boolean = false
  private var isFavorite: Boolean = false
  private lateinit var id: String
  private var event: Event? = null
  private var processedFavoriteId: Long = -1L

  @SuppressLint("SetTextI18n")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.see_detail)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    app_bar.addOnOffsetChangedListener(this)
    fab.setOnClickListener { toggleFavorite() }
    event = intent.getParcelableExtra(Constants.EVENT_DATA)

    val favoriteEvent = intent.getParcelableExtra<Favorites>(Constants.FAVORITE_DATA)
    if (favoriteEvent != null) {
      isFavorite = true
      id = favoriteEvent.id.toString()
      event = favoriteEvent.dataToObject() as Event?
    }

    showData()
    setupTabs()
  }

  private fun setupTabs() {
    val timeline = Timeline()
    val lineups = Lineups()
    val bundle = Bundle()
    bundle.putParcelable(Constants.EVENT_DATA, event)
    timeline.arguments = bundle
    lineups.arguments = bundle
    val adapter = FragmentPagerAdapter(supportFragmentManager, listOf(
        FragmentPagerAdapter.FragmentData("Timeline", timeline),
        FragmentPagerAdapter.FragmentData("Lineup", lineups)
    ))
    tabContainer.adapter = adapter
    tabs.setupWithViewPager(tabContainer)
  }

  @SuppressLint("SetTextI18n")
  private fun showData() {
    event?.apply {

      winnerBanner {
        ivImgHeader.loadImageUrlAsBitmap(it) { resource ->
          resource?.let { bitmap ->
            Blurry.with(ctx).async().from(bitmap).into(ivImgHeader)
            Palette.from(bitmap).generate { palette ->
              val color = palette.getMutedColor(ContextCompat.getColor(ctx, R.color.colorAccent))
              toolbar_layout.setContentScrimColor(color)
            }
          }
          ivImgHeader.startScaleAnimation()
        }
      }

      tvAway.text = strAwayTeam
      tvAway.fontGoogleProductRegular()
      teamAwayBadge { ivAway.loadWithGlide(it) }

      tvHome.text = strHomeTeam
      tvHome.fontGoogleProductRegular()
      teamHomeBadge { ivHome.loadWithGlide(it) }

      tvTime.text = localTime()
      tvTime.fontGoogleProductRegular()

      tvDate.text = localDateWithDayName()
      tvDate.fontGoogleProductRegular()

      tvLeague.text = strLeague
      tvLeague.fontGoogleProductRegular()

      tvScoreResult.text = "$intHomeScore : $intAwayScore"
    }
  }

  private fun toggleFavorite() {
    if (isFavorite)
      removeFromFavorite()
    else
      addToFavorite()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.favorite_menu, menu)
    collapsedMenu = menu
    toggleFavoriteIcon()
    return super.onCreateOptionsMenu(menu)
  }

  private fun toggleFavoriteIcon() {
    if (isFavorite) {
      fab.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.un_favorite_border_color))
      collapsedMenu?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.un_favorite_border_color)
    } else {
      fab.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.add_favorite_border_color))
      collapsedMenu?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.add_favorite_border_color)
    }
  }

  override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
    collapsedMenu?.getItem(0)?.isVisible = !isAppBarExpanded
    return super.onPrepareOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.favorite_item -> toggleFavorite()
      android.R.id.home -> onBackPressed()
    }
    return super.onOptionsItemSelected(item)
  }

  private fun addToFavorite() {
    try {
      database().use {
        val returnedId = insert(Favorites.TABLE_NAME,
            Favorites.DATA to event?.toJson(),
            Favorites.TYPE to Favorites.ItemType.PAST,
            Favorites.CLASS_NAME to Event::class.java.name)

        id = returnedId.toString()
      }
      isFavorite = true
      snackbar(tabContainer, getString(R.string.favorited)).show()
      processedFavoriteId = -1L
    } catch (e: Exception) {
      snackbar(tabContainer, e.localizedMessage).show()
    }
  }

  private fun removeFromFavorite() {
    try {
      database().use {
        delete(Favorites.TABLE_NAME, "(${Favorites.ID} = {id})",
            "id" to id)
      }
      isFavorite = false
      processedFavoriteId = id.toLong()
      snackbar(tabContainer, getString(R.string.removed_from_favorite))
    } catch (e: Exception) {
      snackbar(tabContainer, e.localizedMessage).show()
    }
  }

  override fun onBackPressed() {
    intent = Intent()
    intent.putExtra(app.submissions.dicoding.footballmatchschedule.fragments.Favorites.FAVORITE_ID, processedFavoriteId)
    setResult(Activity.RESULT_OK, intent)
    finish()
  }

  override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
    if (Math.abs(verticalOffset) - app_bar.totalScrollRange == 0) {
      isAppBarExpanded = false
      toolbar_layout.title = event?.simpleWinnerDescription()
      invalidateOptionsMenu()
    } else {
      toolbar_layout.title = ""
      isAppBarExpanded = true
      invalidateOptionsMenu()
    }
  }

}
