package com.viktor.kh.dev.shoplist.DI

import android.content.Context
import androidx.room.Room
import com.viktor.kh.dev.shoplist.repository.db.room.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DBModule {


    @Singleton
    @Provides
    fun providesProductListsDB(@ApplicationContext context: Context):ProductListsDB{
        return Room.databaseBuilder(
            context,
            ProductListsDB::class.java,
            "ProductListsDB"
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
    fun providesProductListsDao( db: ProductListsDB): ProductListsDao{
        return db.productListsDao()
    }



    @Singleton
    @Provides
    fun providesRecipesDao( db: RecipesDB): RecipesDao{
        return db.recipesDao()
    }

    @Singleton
    @Provides
    fun providesProductsDao(db: ProductsDB ): ProductsDao{
        return db.productsDao()
    }

}
