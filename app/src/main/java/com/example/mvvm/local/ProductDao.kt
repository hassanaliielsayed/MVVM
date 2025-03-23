package com.example.mvvm.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.courotines.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("select * from products ")
    fun getFavProducts(): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(product: Product): Long

    @Delete
    suspend fun delete(product: Product): Int
}