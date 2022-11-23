package uz.gita.rounded_internship.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface StateDao {
    @Insert
    fun insert(stateEntity: StateEntity)

    @Update
    fun update(stateEntity: StateEntity)

    @Insert
    fun insert(stateList: List<StateEntity>)

    @Query("SELECT * FROM state WHERE id = :id")
    fun getStateById(id: Int): Flow<StateEntity>

    @Query("SELECT * FROM state")
    fun states():Flow<List<StateEntity>>
}
