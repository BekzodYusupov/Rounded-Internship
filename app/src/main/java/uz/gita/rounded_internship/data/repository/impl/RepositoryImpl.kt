package uz.gita.rounded_internship.data.repository.impl

import android.util.Log
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import uz.gita.rounded_internship.data.local.room.StateDao
import uz.gita.rounded_internship.data.local.room.StateEntity
import uz.gita.rounded_internship.data.remote.DialogApi
import uz.gita.rounded_internship.data.repository.Repository
import uz.gita.rounded_internship.utils.hasConnection
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val dialogApi: DialogApi,
    private val stateDao: StateDao
) : Repository {

    override suspend fun random(): Flow<String> = flow {
        val response = dialogApi.random()


        if (response.isSuccessful && hasConnection()) {
            response.body()?.urls?.small?.let {
                emit(it)
            }
        }
    }.flowOn(IO).catch {
        Log.d("@@@", "message - ${it.message}")
    }

    override suspend fun insert(stateEntity: StateEntity) {
        stateDao.insert(stateEntity)
    }

    override suspend fun insert(list: List<StateEntity>) {
        stateDao.insert(list)
    }

    override suspend fun update(stateEntity: StateEntity) {
        stateDao.update(stateEntity)
    }

    override fun getStateById(id: Int): Flow<StateEntity> {
        return stateDao.getStateById(id)
    }

    override fun states(): Flow<List<StateEntity>> {
        return stateDao.states()
    }

    override fun getStateList(): Flow<List<StateEntity>> = flow {
        emit(list)
    }.flowOn(IO)

    private val list = arrayListOf<StateEntity>(
        StateEntity(0, false),
        StateEntity(0, true),
        StateEntity(0, true),
        StateEntity(0, true),
        StateEntity(0, true),
    )
}