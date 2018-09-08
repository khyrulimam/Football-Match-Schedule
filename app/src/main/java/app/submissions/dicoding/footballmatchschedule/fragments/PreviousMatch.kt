package app.submissions.dicoding.footballmatchschedule.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.submissions.dicoding.footballmatchschedule.BuildConfig
import app.submissions.dicoding.footballmatchschedule.R
import app.submissions.dicoding.footballmatchschedule.SeeDetail
import app.submissions.dicoding.footballmatchschedule.adapters.RecyclerViewAdapterWithItemViewPager
import app.submissions.dicoding.footballmatchschedule.constants.Constants
import app.submissions.dicoding.footballmatchschedule.exts.gone
import app.submissions.dicoding.footballmatchschedule.exts.visible
import app.submissions.dicoding.footballmatchschedule.models.Event
import app.submissions.dicoding.footballmatchschedule.models.holders.MatchNewsHolder
import app.submissions.dicoding.footballmatchschedule.presenters.PreviousMatchPresenter
import app.submissions.dicoding.footballmatchschedule.presenters.behavior.PreviousMatchBehavior
import kotlinx.android.synthetic.main.recycler_view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.okButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.startActivity

class PreviousMatch : Fragment(), AnkoLogger {

  private val presenter: PreviousMatchPresenter = PreviousMatchPresenter(MyBehavior())
  private var dataSchedules: MutableList<MatchNewsHolder> = mutableListOf()
  private val seeDetail: (Event) -> Unit = {
    startActivity<SeeDetail>(Constants.EVENT_DATA to it)
//    startActivity(intentFor<SeeDetail>(BuildConfig.EVENT_DATA to it),
//        activity?.let { it1 -> ActivityOptionsCompat.makeSceneTransitionAnimation(it1).toBundle() })
  }
  private var adapter = RecyclerViewAdapterWithItemViewPager(dataSchedules, seeDetail)

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.recycler_view, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    presenter.getData()
    rvRecyclerView.adapter = adapter
    rvRecyclerView.layoutManager = LinearLayoutManager(ctx)
  }

  override fun onPause() {
    presenter.dispose()
    super.onPause()
  }

  override fun onStop() {
    presenter.dispose()
    super.onStop()
  }

  inner class MyBehavior : PreviousMatchBehavior() {

    override fun showData(items: List<MatchNewsHolder>) {
      dataSchedules.clear()
      dataSchedules.addAll(items)
      adapter.notifyDataSetChanged()
    }

    override fun showLoading() {
      shimmer.startShimmer()
      shimmer.visible()
    }

    override fun hideLoading() {
      shimmer.stopShimmer()
      shimmer.gone()
    }

    override fun onError(msg: String) {
      alert(msg) {
        okButton { }
      }
    }

  }

}