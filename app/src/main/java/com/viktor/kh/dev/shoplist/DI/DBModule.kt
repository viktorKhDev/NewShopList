package com.viktor.kh.dev.shoplist.DI

import android.content.Context
import androidx.room.Room
import com.viktor.kh.dev.shoplist.repository.db.room.ProductsDB
import com.viktor.kh.dev.shoplist.repository.db.room.RecipesDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DBModule {


    @Singleton
    @Provides
    fun providesProductsDB(@ApplicationContext context: Context):ProductsDB{
        return Room.databaseBuilder(
            context,
            ProductsDB::class.java,
            "ProductsDB"
        ).build()
    }


    @Singleton
    @Provides
    fun providesRecipesDB(@ApplicationContext context: Context):RecipesDB{
        return Room.databaseBuilder(
            context,
            RecipesDB::class.java,
            "RecipesDB"
        ).build()
    }

}