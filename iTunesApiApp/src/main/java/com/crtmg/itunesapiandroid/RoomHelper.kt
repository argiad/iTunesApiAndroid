package com.crtmg.itunesapiandroid

import android.content.Context
import androidx.room.*
import com.crtmg.itunesapi.iTunesItem

class RoomHelper {
    private var db: AppDatabase? = null

    fun initDB(context: Context) {
        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database-name"
        ).build()
    }

    fun isInFav(item: iTunesItem): Boolean {
        val id: Long = item.trackID!!
        return !(db?.userDao()?.loadAllByIds(listOf(id))).isNullOrEmpty()
    }

    fun changeState(like: Boolean, item: iTunesItem) {
        if (like != isInFav(item)) {
            if (like) {
                db?.userDao()?.insertAll(DBItem(item.trackID!!, item.jsonString))
            } else {
                (db?.userDao()?.loadAllByIds(listOf(item.trackID!!)))?.firstOrNull()?.let {
                    db?.userDao()?.delete(it)
                }
            }
        }
    }

    fun getFavList(): List<iTunesItem>? {
        return db?.userDao()?.getAll()?.map { iTunesItem.createFrom(it.itemJson) }
    }

}


@Entity
data class DBItem(
    @PrimaryKey val uid: Long,
    @ColumnInfo(name = "item_json") val itemJson: String
)


@Dao
interface UserDao {
    @Query("SELECT * FROM DBItem")
    fun getAll(): List<DBItem>

    @Query("SELECT * FROM DBItem WHERE uid IN (:trackIds)")
    fun loadAllByIds(trackIds: List<Long>): List<DBItem>

    @Insert
    fun insertAll(vararg users: DBItem)

    @Delete
    fun delete(user: DBItem)
}

@Database(entities = [DBItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
