package uz.gita.rounded_internship.presentation.viewModel

import android.os.CountDownTimer
import android.view.MotionEvent
import kotlinx.coroutines.flow.Flow
import uz.gita.rounded_internship.data.local.room.StateEntity
import uz.gita.rounded_internship.data.model.Type

interface HomeViewModel {
    val stateEntities: Flow<List<StateEntity>>
    val imgUrl: Flow<String>
    val duration: Flow<Long>
    val shortTimerDuration: Flow<Long>
    val clickCount: Flow<Int>
    val type: Flow<Type>

    fun insert(stateEntity: StateEntity)
    fun update(stateEntity: StateEntity)
    fun random(state: Boolean)
    fun setDialogState(boolean: Boolean)

    fun refreshTime()
    fun refreshShortTime()

    fun lock(event: MotionEvent)
    fun unlock(event: MotionEvent)
//used to identify which content to open when long or multiple click
    fun setType(type: Type)
}