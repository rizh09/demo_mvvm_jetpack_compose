package com.example.demo_mvvm_jetpack_compose.di.module

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.demo_mvvm_jetpack_compose.database.DatabaseQuoteResult
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton


//Purpose of DatabaseModule
//it demos how to implement ROOM with hilt
@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    //Inject instances with @Provides for class we dont own. e.g like external  libraray
    //@ApplicationContext provide the instance of application context. this is build-int feature of hilt
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): QuoteResultDatabase {
        return Room.databaseBuilder(context, QuoteResultDatabase::class.java, "quote-result-db")
            .build()
    }

    @Singleton
    @Provides
    fun providesQuotesResultDao(db: QuoteResultDatabase): QuoteResultDao {
        return db.quoteResultDao()
    }

}

@Database(entities = [DatabaseQuoteResult::class], version = 1)
abstract class QuoteResultDatabase : RoomDatabase() {
    abstract fun quoteResultDao(): QuoteResultDao
}

@Dao
interface QuoteResultDao {
    @Query("select * from databasequoteresult")
    //we define the flow type in DAO to get live updates e.g quotesResult added, removed or updated.
    //ref : https://medium.com/androiddevelopers/room-flow-273acffe5b57
    fun getQuoteResult(): Flow<List<DatabaseQuoteResult>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(quoteResult: List<DatabaseQuoteResult>)
}