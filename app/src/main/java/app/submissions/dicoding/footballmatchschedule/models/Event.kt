package app.submissions.dicoding.footballmatchschedule.models

import android.annotation.SuppressLint
import android.os.Parcelable
import app.submissions.dicoding.footballmatchschedule.constants.Constants.DEFAULT_IMG_URL
import app.submissions.dicoding.footballmatchschedule.exts.handleSafely
import app.submissions.dicoding.footballmatchschedule.exts.toDate
import app.submissions.dicoding.footballmatchschedule.exts.toLocalDateWithDayName
import app.submissions.dicoding.footballmatchschedule.exts.toLocalTime
import app.submissions.dicoding.footballmatchschedule.fabric.GsonFabric
import app.submissions.dicoding.footballmatchschedule.requests.to.Lookup
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

@Parcelize
data class Event(
    @SerializedName("idEvent")
    @Expose
    var idEvent: String? = null,
    @SerializedName("idSoccerXML")
    @Expose
    var idSoccerXML: String? = null,
    @SerializedName("strEvent")
    @Expose
    var strEvent: String? = null,
    @SerializedName("strFilename")
    @Expose
    var strFilename: String? = null,
    @SerializedName("strSport")
    @Expose
    var strSport: String? = null,
    @SerializedName("idLeague")
    @Expose
    var idLeague: String? = null,
    @SerializedName("strLeague")
    @Expose
    var strLeague: String = "",
    @SerializedName("strSeason")
    @Expose
    var strSeason: String? = null,
    @SerializedName("strDescriptionEN")
    @Expose
    var strDescriptionEN: String? = null,
    @SerializedName("strHomeTeam")
    @Expose
    var strHomeTeam: String? = null,
    @SerializedName("strAwayTeam")
    @Expose
    var strAwayTeam: String? = null,
    @SerializedName("intHomeScore")
    @Expose
    var intHomeScore: Int = 0,
    @SerializedName("intRound")
    @Expose
    var intRound: String? = null,
    @SerializedName("intAwayScore")
    @Expose
    var intAwayScore: Int = 0,
    @SerializedName("intSpectators")
    @Expose
    var intSpectators: String? = null,
    @SerializedName("strHomeGoalDetails")
    @Expose
    var strHomeGoalDetails: String? = "",
    @SerializedName("strHomeRedCards")
    @Expose
    var strHomeRedCards: String? = "",
    @SerializedName("strHomeYellowCards")
    @Expose
    var strHomeYellowCards: String? = "",
    @SerializedName("strHomeLineupGoalkeeper")
    @Expose
    var strHomeLineupGoalkeeper: String? = null,
    @SerializedName("strHomeLineupDefense")
    @Expose
    var strHomeLineupDefense: String? = null,
    @SerializedName("strHomeLineupMidfield")
    @Expose
    var strHomeLineupMidfield: String? = null,
    @SerializedName("strHomeLineupForward")
    @Expose
    var strHomeLineupForward: String? = null,
    @SerializedName("strHomeLineupSubstitutes")
    @Expose
    var strHomeLineupSubstitutes: String? = null,
    @SerializedName("strHomeFormation")
    @Expose
    var strHomeFormation: String? = null,
    @SerializedName("strAwayRedCards")
    @Expose
    var strAwayRedCards: String? = "",
    @SerializedName("strAwayYellowCards")
    @Expose
    var strAwayYellowCards: String? = "",
    @SerializedName("strAwayGoalDetails")
    @Expose
    var strAwayGoalDetails: String? = "",
    @SerializedName("strAwayLineupGoalkeeper")
    @Expose
    var strAwayLineupGoalkeeper: String? = null,
    @SerializedName("strAwayLineupDefense")
    @Expose
    var strAwayLineupDefense: String? = null,
    @SerializedName("strAwayLineupMidfield")
    @Expose
    var strAwayLineupMidfield: String? = null,
    @SerializedName("strAwayLineupForward")
    @Expose
    var strAwayLineupForward: String? = null,
    @SerializedName("strAwayLineupSubstitutes")
    @Expose
    var strAwayLineupSubstitutes: String? = null,
    @SerializedName("strAwayFormation")
    @Expose
    var strAwayFormation: String? = null,
    @SerializedName("intHomeShots")
    @Expose
    var intHomeShots: String? = null,
    @SerializedName("intAwayShots")
    @Expose
    var intAwayShots: String? = null,
    @SerializedName("dateEvent")
    @Expose
    var dateEvent: String = "",
    @SerializedName("strDate")
    @Expose
    var strDate: String? = null,
    @SerializedName("strTime")
    @Expose
    var strTime: String? = null,
    @SerializedName("strTVStation")
    @Expose
    var strTVStation: String? = null,
    @SerializedName("idHomeTeam")
    @Expose
    var idHomeTeam: String = "",
    @SerializedName("idAwayTeam")
    @Expose
    var idAwayTeam: String = "",
    @SerializedName("strResult")
    @Expose
    var strResult: String? = null,
    @SerializedName("strCircuit")
    @Expose
    var strCircuit: String? = null,
    @SerializedName("strCountry")
    @Expose
    var strCountry: String? = null,
    @SerializedName("strCity")
    @Expose
    var strCity: String? = null,
    @SerializedName("strPoster")
    @Expose
    var strPoster: String? = null,
    @SerializedName("strFanart")
    @Expose
    var strFanart: String? = null,
    @SerializedName("strThumb")
    @Expose
    var strThumb: String? = null,
    @SerializedName("strBanner")
    @Expose
    var strBanner: String? = null,
    @SerializedName("strMap")
    @Expose
    var strMap: String? = null,
    @SerializedName("strLocked")
    @Expose
    var strLocked: String? = null,
    var homeBadge: String?,
    var awayBadge: String?
) : Parcelable, AnkoLogger {

  private fun winnerDescription(): String {
    var description = "The match is draw with score $intHomeScore:$intAwayScore"
    if (intHomeScore > intAwayScore)
      description = "$strHomeTeam wins the match with score $intHomeScore"
    if (intAwayScore > intHomeScore)
      description = "$strAwayTeam wins the match with score $intAwayScore"
    return description
  }

  fun simpleWinnerDescription(): String {
    var description = "Draw $intHomeScore:$intAwayScore"
    if (intHomeScore > intAwayScore)
      description = "$strHomeTeam wins!"
    if (intAwayScore > intHomeScore)
      description = "$strAwayTeam wins!"
    return description
  }

  fun strDateTime() = "$dateEvent $strTime"

  @SuppressLint("SimpleDateFormat")
  fun localTime(): String = strDateTime().toDate().toLocalTime()

  fun localDateWithDayName(): String = strDateTime().toDate().toLocalDateWithDayName()

  fun headline(): CharSequence? {
    return "$strEvent: ${winnerDescription()}"
  }

  fun teamHomeBadge(callback: (String) -> Unit) {
    getBadge(idHomeTeam, callback)
  }

  fun teamAwayBadge(callback: (String) -> Unit) {
    getBadge(idAwayTeam, callback)
  }

  private fun getBadge(teamId: String, callback: (String) -> Unit) {
    Lookup.Request.get.byTeam(teamId)
        .handleSafely()
        .subscribe({
          it as Teams
          callback(it.teams[0].strTeamBadge
              ?: DEFAULT_IMG_URL)
        }, { info(it.message) }).isDisposed
  }

  fun winnerBanner(callback: (String) -> Unit) {
    when (winnerTeamId()) {
      DRAW -> callback(DEFAULT_IMG_URL)
      else ->
        Lookup.Request.get.byTeam(winnerTeamId())
            .handleSafely()
            .subscribe({
              it as Teams
              callback(it.teams[0].strTeamFanart3
                  ?: DEFAULT_IMG_URL)
            }, {
              info(it.message)
            })
    }
  }

  private fun winnerTeamId(): String {
    if (intAwayScore > intHomeScore) return idAwayTeam
    else if (intHomeScore > intAwayScore) return idHomeTeam
    return DRAW
  }

  fun toJson(): String = GsonFabric.build.toJson(this)

  companion object {
    private const val DRAW = "DRAW"
  }

}

data class Events(val events: List<Event>)
data class Results(val results: List<Event>)