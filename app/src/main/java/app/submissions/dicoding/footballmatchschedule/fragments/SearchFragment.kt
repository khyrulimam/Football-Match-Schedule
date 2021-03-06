package app.submissions.dicoding.footballmatchschedule.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.submissions.dicoding.footballmatchschedule.R
import app.submissions.dicoding.footballmatchschedule.TeamDetail
import app.submissions.dicoding.footballmatchschedule.adapters.SearchResultAdapter
import app.submissions.dicoding.footballmatchschedule.constants.Constants
import app.submissions.dicoding.footballmatchschedule.exts.gone
import app.submissions.dicoding.footballmatchschedule.exts.visible
import app.submissions.dicoding.footballmatchschedule.models.Team
import app.submissions.dicoding.footballmatchschedule.presenters.SearchPresenter
import app.submissions.dicoding.footballmatchschedule.presenters.behavior.SearchBehavior
import kotlinx.android.synthetic.main.recycler_view_with_context_bg.*
import kotlinx.android.synthetic.main.search_fragment.*
import org.jetbrains.anko.okButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.startActivity

class SearchFragment : Fragment(), TextWatcher {


  private val presenter = SearchPresenter(MyBehavior())
  private val data: MutableList<Team> = mutableListOf()
  private val adapter: SearchResultAdapter = SearchResultAdapter(data) {
    startActivity<TeamDetail>(Constants.TEAM_DATA to it)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.search_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    context?.let { ivContextImage.setImageDrawable(ContextCompat.getDrawable(it, R.drawable.ic_search_grey_24dp)) }
    tvContextTitle.text = getString(R.string.search_team)
    etSearch.requestFocus()
    etSearch.addTextChangedListener(this)
    etSearch.setOnEditorActionListener { v, _, _ ->
      search(v.text.toString())
      true
    }
    rvRecyclerView.adapter = adapter
    val gridLayoutManager = GridLayoutManager(context, 2)
    gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
      override fun getSpanSize(position: Int): Int {
        return if (data.size % 2 != 0 && position == 0) 2
        else 1
      }

    }
    rvRecyclerView.layoutManager = gridLayoutManager
  }

  override fun onPause() {
    presenter.dispose()
    super.onPause()
  }

  override fun onStop() {
    presenter.dispose()
    super.onStop()
  }

  override fun afterTextChanged(s: Editable?) {
    Log.i(javaClass.name, s.toString())
  }

  override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    Log.i(javaClass.name, s.toString())
  }

  override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    if (count > 5) search(s.toString())
  }

  private fun search(query: String) {
    presenter.getData(query)
  }

  inner class MyBehavior : SearchBehavior() {
    override fun showData(teams: List<Team>) {
      data.clear()
      data.addAll(teams)
      adapter.notifyDataSetChanged()
    }

    override fun showLoading() {
      shimmer.visible()
    }

    override fun hideLoading() {
      shimmer.gone()
    }

    override fun onError(msg: String) {
      alert(msg) { okButton { } }
    }
  }


}