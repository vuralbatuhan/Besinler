package com.example.besinprojesi.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.besinprojesi.model.Besin

@Dao
interface BesinDAO {

    @Insert
    suspend fun insertAll(vararg besin: Besin) : List<Long>

    @Query("Select * from besin")
    suspend fun getAll() : List<Besin>

    @Query("Select * from besin where uuid = :besinId")
    suspend fun getBesin(besinId : Int) : Besin

    @Query("Delete from besin")
    suspend fun deleteAll()
}