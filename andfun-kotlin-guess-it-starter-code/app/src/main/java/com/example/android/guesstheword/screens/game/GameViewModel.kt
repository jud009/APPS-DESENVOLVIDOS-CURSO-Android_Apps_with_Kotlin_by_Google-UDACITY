package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.view.animation.Transformation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

//fragment only display the data
class GameViewModel : ViewModel() {
    companion object {
        const val DONE = 0L
        const val ONE_SECOND = 1000L
        const val COUNTDOWN_TIME = 10000L
    }

    private val _word = MutableLiveData<String>()
        val word: LiveData<String> get() = _word

    private val _time = MutableLiveData<Long>()
        val time: LiveData<Long> get() = _time

    private val _score = MutableLiveData<Int>()
        val score: LiveData<Int> get() = _score

    private val _gameFinished = MutableLiveData<Boolean>()
        val gameFinished: LiveData<Boolean> get() = _gameFinished


    private lateinit var wordList: MutableList<String>
    private var countDown: CountDownTimer

    val currentTime = Transformations.map(time) {time ->
        DateUtils.formatElapsedTime(time)
    }

    init {
        resetList()
        nextWord()
        _score.value = 0

        countDown = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _time.value = (millisUntilFinished / ONE_SECOND)
            }

            override fun onFinish() {
                _time.value = DONE
                _gameFinished.value = true
            }
        }.start()
    }

    fun onSkip() {

        _score.value = score.value?.minus(1)
        nextWord()

    }

    fun onCorrect() {
        _score.value = score.value?.plus(1)
        nextWord()
    }

    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            _gameFinished.value = true
        } else {
            _word.value = wordList.removeAt(0)
        }

    }

    fun onGameFinishedCompleted() {
        _gameFinished.value = false
    }

    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    override fun onCleared() {
        super.onCleared()
        countDown.cancel()
    }
}
