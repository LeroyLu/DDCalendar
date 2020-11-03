package com.leroylu.db.dao

import androidx.paging.DataSource
import androidx.room.*
import com.leroylu.db.bean.calendar.Vtuber

/**
 * @author jiaj.lu
 * @date 2020/10/29
 * @description
 */
@Dao
interface VtuberDao {

    @Query("SELECT * FROM vtuber")
    fun selectAll(): List<Vtuber>

    @Query("SELECT * FROM vtuber")
    fun selectAllPaging(): DataSource.Factory<Int, Vtuber>

    @Query("SELECT * FROM vtuber WHERE vid = :uid")
    fun getById(uid: Int): Vtuber?

    @Insert
    fun insertAll(vararg vtuber: Vtuber)

    @Insert
    fun insertAll(vtuber: List<Vtuber>)

    @Update
    fun updateAll(vararg vtuber: Vtuber)

    @Update
    fun updateAll(vtuber: List<Vtuber>)

    @Delete
    fun delete(vararg vtuber: Vtuber)

    @Query("DELETE FROM vtuber")
    fun deleteAll()
}