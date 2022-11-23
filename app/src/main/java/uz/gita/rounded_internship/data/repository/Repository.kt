package uz.gita.rounded_internship.data.repository

import kotlinx.coroutines.flow.Flow
import uz.gita.rounded_internship.data.local.room.StateEntity

interface Repository {
    suspend fun random(): Flow<String>
    suspend fun insert(stateEntity: StateEntity)
    suspend fun insert(list: List<StateEntity>)
    suspend fun update(stateEntity: StateEntity)
    fun getStateById(id: Int): Flow<StateEntity>
    fun states():Flow<List<StateEntity>>
    fun getStateList(): Flow<List<StateEntity>>
}