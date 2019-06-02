package com.arekbiela.currencyexchange.model.local

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import io.reactivex.schedulers.Schedulers


@Database(
    entities = [UserCurrencySelectionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RoomUserCurrencySelectionDatabase : RoomDatabase() {

    abstract fun userCurrencySelectionDao(): UserCurrencySelectionDao

    companion object {

        private const val DB_NAME = "user_currency_selection_database"
        
        @Volatile private var INSTANCE: RoomUserCurrencySelectionDatabase? = null

        fun getInstance(context: Context): RoomUserCurrencySelectionDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: create(context).also { INSTANCE = it }
            }

        fun create(context: Context): RoomUserCurrencySelectionDatabase {
            return Room.databaseBuilder(
                context,
                RoomUserCurrencySelectionDatabase::class.java,
                DB_NAME)
                .addCallback(object: Callback() {
                    @SuppressLint("CheckResult")
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        INSTANCE
                            ?.userCurrencySelectionDao()
                            ?.insert(UserCurrencySelectionEntity(0, "EUR", "100.0"))
                            ?.subscribeOn(Schedulers.io())
                            ?.subscribe()
                    }
                })
                .build()
        }
    }
}
