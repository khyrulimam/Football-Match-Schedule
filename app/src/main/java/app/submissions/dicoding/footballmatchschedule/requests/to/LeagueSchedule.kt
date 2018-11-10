package app.submissions.dicoding.footballmatchschedule.requests.to

import app.submissions.dicoding.footballmatchschedule.models.Events
import app.submissions.dicoding.footballmatchschedule.models.Leagues
import app.submissions.dicoding.footballmatchschedule.requests.Retrofit
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface LeagueSchedule {

  @GET("eventsnextleague.php")
  fun next15(@Query("id") id: String): Observable<Events>

  @GET("eventspastleague.php")
  fun past15(@Query("id") id: String): Observable<Events>

  @GET("all_leagues.php")
  fun leagues(): Observable<Leagues>

  companion object {
    private val get: LeagueSchedule =
        Retrofit.client.create(LeagueSchedule::class.java)
  }

  object Request {
    val get by lazy { LeagueSchedule.get }
  }
}