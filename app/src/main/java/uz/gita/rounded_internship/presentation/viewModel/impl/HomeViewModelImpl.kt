package uz.gita.rounded_internship.presentation.viewModel.impl

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.CountDownTimer
import android.os.Environment
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.gita.rounded_internship.data.local.room.StateEntity
import uz.gita.rounded_internship.data.local.sharedPref.SharedPref
import uz.gita.rounded_internship.data.model.Type
import uz.gita.rounded_internship.data.repository.Repository
import uz.gita.rounded_internship.presentation.viewModel.HomeViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModelImpl @Inject constructor(
    private val repository: Repository,
    private val sharedPref: SharedPref
) : HomeViewModel, ViewModel() {

    override val stateEntities: MutableSharedFlow<List<StateEntity>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override val imgUrl: MutableSharedFlow<String> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override val duration = MutableStateFlow(0L)
    override val shortTimerDuration = MutableStateFlow(0L)
    override val clickCount = MutableStateFlow(0)
    override var type: MutableSharedFlow<Type> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)


    init {
        random(sharedPref.getDialogState())
        //inserting init values to local dataBase
        viewModelScope.launch(IO) {
            if (sharedPref.getIsFirst()) {
                repository.getStateList().collectLatest {
                    repository.insert(it)
                }
                sharedPref.setIsFirst(false)
            }
        }
        //getting list of states
        viewModelScope.launch(IO) {
            repository.states().collectLatest {
                stateEntities.emit(it)
            }
        }
    }

    private val timer = object : CountDownTimer(5000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            duration.value += 1000
        }

        override fun onFinish() {}
    }

    private val shortTimer = object : CountDownTimer(1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            shortTimerDuration.value += 1000L
        }

        override fun onFinish() {
            clickCount.value = 0
            shortTimerDuration.value = 0
        }
    }

    override fun insert(stateEntity: StateEntity) {
        viewModelScope.launch(IO) {
            repository.insert(stateEntity)
        }
    }

    override fun update(stateEntity: StateEntity) {
        viewModelScope.launch(IO) {
            repository.update(stateEntity)
        }
    }

    override fun random(state: Boolean) {
        if (!state) {
            viewModelScope.launch(IO) {
                repository.random().collectLatest { imgUrl.emit(it) }
            }
        } else return
    }

    override fun setDialogState(boolean: Boolean) {
        sharedPref.setDialogState(boolean)
    }

    override fun refreshTime() {
        duration.value = 0L
        type = MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    }

    override fun refreshShortTime() {
        shortTimerDuration.value = 0L
        clickCount.value = 0
        type = MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    }

    override fun lock(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (shortTimerDuration.value < 2000L) {
                    shortTimer.start()
                }
                ++clickCount.value
            }
        }
    }

    override fun unlock(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                timer.start()
            }
            MotionEvent.ACTION_UP -> {
                if (duration.value != 5000L) {
                    timer.cancel()
                    this.duration.value = 0L
                }
            }
            //swiping activates Action_Down but later Action_Cancel
            //and Action_Up never occurs so reInitialized values.
            //removing this line causes misfunctioning
            MotionEvent.ACTION_CANCEL -> {
                timer.cancel()
                duration.value = 0
                shortTimer.cancel()
                shortTimerDuration.value = 0L
            }
        }
    }

    override fun setType(type: Type) {
        this.type.tryEmit(type)
    }
}