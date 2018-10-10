package app.submissions.dicoding.footballmatchschedule


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.view.View
import app.submissions.dicoding.footballmatchschedule.R.id.rvRecyclerView
import app.submissions.dicoding.footballmatchschedule.idlingresource.EspressoIdlingResource
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class MatchNewsTest {

  @Rule
  @JvmField
  var mActivityTestRule = ActivityTestRule(MatchNews::class.java)

  @Before
  fun setUp() {
    IdlingRegistry.getInstance().register(EspressoIdlingResource.mCountingIdlingResource)
  }

  @Test
  fun testPreviousMatchRecyclerView() {
    onView(previousMatchRecyclerView()).perform(
        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
  }

  private fun previousMatchRecyclerView(): Matcher<View> {
    return withIndex(withId(rvRecyclerView), 0)
  }

  private fun nextMatchRecyclerView(): Matcher<View> {
    return withIndex(withId(rvRecyclerView), 1)
  }

  private fun withIndex(matcher: Matcher<View>, index: Int): Matcher<View> {
    return object : TypeSafeMatcher<View>() {
      var currentIndex = 0

      override fun describeTo(description: Description) {
        description.appendText("with index: ")
        description.appendValue(index)
        matcher.describeTo(description)
      }

      override fun matchesSafely(view: View): Boolean {
        return matcher.matches(view) && currentIndex++ == index
      }
    }
  }
}
