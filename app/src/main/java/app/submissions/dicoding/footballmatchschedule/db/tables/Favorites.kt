package app.submissions.dicoding.footballmatchschedule.db.tables

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Favorites(val id: Long?, val data: String?, val type: Int, val className: String) : Parcelable {
  companion object {
    const val TABLE_NAME = "favorites"
    const val ID = "id"
    const val DATA = "data"
    const val TYPE = "type"
    const val CLASS_NAME = "class_name"
  }

  object ItemType {
    const val PAST = 0
    const val NEXT = 1
    const val TEAM = 2
  }
}