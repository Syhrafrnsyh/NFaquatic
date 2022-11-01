package com.example.nfaquatic.retrofit

import androidx.room.*
import com.example.nfaquatic.model.Alamat

@Dao
interface DaoAlamat {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: Alamat)

    @Delete
    fun delete(data: Alamat)

    @Update
    fun update(data: Alamat): Int

    @Query("SELECT * from alamat ORDER BY id ASC")
    fun getAll(): List<Alamat>

    @Query("SELECT * FROM alamat WHERE id = :id LIMIT 1")
    fun getProduk(id: Int): Alamat

    @Query("SELECT * FROM alamat WHERE isSelected = :status LIMIT 1")
    fun getByStatus(status: Boolean): Alamat?

    @Query("DELETE FROM alamat")
    fun deleteAll(): Int
}