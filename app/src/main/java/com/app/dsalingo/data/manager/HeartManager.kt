package com.app.dsalingo.data.manager

import android.content.Context
import android.content.SharedPreferences
import com.app.dsalingo.data.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeartManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val MAX_HEARTS = 5
        const val RECHARGE_INTERVAL_SECONDS = 300 // 5 minutes per heart
        private const val PREFS_NAME = "dsalingo_hearts_prefs"
        private const val KEY_HEARTS = "current_hearts"
        private const val KEY_LAST_TIMESTAMP = "last_loss_timestamp"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _hearts = MutableStateFlow(prefs.getInt(KEY_HEARTS, MAX_HEARTS))
    val hearts: StateFlow<Int> = _hearts.asStateFlow()

    private val _secondsRemaining = MutableStateFlow(0)
    val secondsRemaining: StateFlow<Int> = _secondsRemaining.asStateFlow()

    private val _isFull = MutableStateFlow(_hearts.value >= MAX_HEARTS)
    val isFull: StateFlow<Boolean> = _isFull.asStateFlow()

    private var tickerJob: Job? = null

    init {
        calculateOfflineRecharge()
        startTicker()
    }

    /**
     * Calculate hearts recharged while app was closed / backgrounded
     */
    fun calculateOfflineRecharge() {
        val currentHearts = prefs.getInt(KEY_HEARTS, MAX_HEARTS)
        if (currentHearts >= MAX_HEARTS) {
            _hearts.value = MAX_HEARTS
            _secondsRemaining.value = 0
            _isFull.value = true
            return
        }

        val lastTimestamp = prefs.getLong(KEY_LAST_TIMESTAMP, System.currentTimeMillis())
        val now = System.currentTimeMillis()
        val elapsedSeconds = ((now - lastTimestamp) / 1000).toInt().coerceAtLeast(0)

        val heartsEarned = elapsedSeconds / RECHARGE_INTERVAL_SECONDS
        val newHearts = (currentHearts + heartsEarned).coerceAtMost(MAX_HEARTS)

        if (newHearts >= MAX_HEARTS) {
            _hearts.value = MAX_HEARTS
            _secondsRemaining.value = 0
            _isFull.value = true
            saveState(MAX_HEARTS, now)
        } else {
            val remainderSeconds = elapsedSeconds % RECHARGE_INTERVAL_SECONDS
            val remainingUntilNext = RECHARGE_INTERVAL_SECONDS - remainderSeconds
            _hearts.value = newHearts
            _secondsRemaining.value = remainingUntilNext
            _isFull.value = false
            // Adjust base timestamp forward by whole earned intervals
            val adjustedTimestamp = lastTimestamp + (heartsEarned * RECHARGE_INTERVAL_SECONDS * 1000L)
            saveState(newHearts, adjustedTimestamp)
        }
    }

    private fun startTicker() {
        tickerJob?.cancel()
        tickerJob = scope.launch {
            while (isActive) {
                delay(1000L)
                if (_hearts.value < MAX_HEARTS) {
                    _isFull.value = false
                    if (_secondsRemaining.value > 1) {
                        _secondsRemaining.value -= 1
                    } else {
                        // Heart recharged!
                        val updatedHearts = (_hearts.value + 1).coerceAtMost(MAX_HEARTS)
                        _hearts.value = updatedHearts
                        if (updatedHearts >= MAX_HEARTS) {
                            _secondsRemaining.value = 0
                            _isFull.value = true
                            saveState(MAX_HEARTS, System.currentTimeMillis())
                        } else {
                            _secondsRemaining.value = RECHARGE_INTERVAL_SECONDS
                            saveState(updatedHearts, System.currentTimeMillis())
                        }
                    }
                } else {
                    _isFull.value = true
                    _secondsRemaining.value = 0
                }
            }
        }
    }

    fun loseHeart(): Boolean {
        if (_hearts.value > 0) {
            val newHearts = _hearts.value - 1
            _hearts.value = newHearts
            _isFull.value = false
            
            // If it was previously full, start the countdown fresh
            if (_secondsRemaining.value <= 0) {
                _secondsRemaining.value = RECHARGE_INTERVAL_SECONDS
            }
            saveState(newHearts, System.currentTimeMillis())
            return true
        }
        return false
    }

    fun gainHeart(amount: Int = 1) {
        val newHearts = (_hearts.value + amount).coerceAtMost(MAX_HEARTS)
        _hearts.value = newHearts
        if (newHearts >= MAX_HEARTS) {
            _secondsRemaining.value = 0
            _isFull.value = true
        }
        saveState(newHearts, System.currentTimeMillis())
    }

    fun refillHearts() {
        _hearts.value = MAX_HEARTS
        _secondsRemaining.value = 0
        _isFull.value = true
        saveState(MAX_HEARTS, System.currentTimeMillis())
    }

    fun syncWithUser(user: User) {
        if (user.isPro) {
            // Pro users get infinite / max hearts
            refillHearts()
            return
        }
        if (user.hearts in 0..MAX_HEARTS && user.hearts != _hearts.value) {
            _hearts.value = user.hearts
            if (_hearts.value < MAX_HEARTS && _secondsRemaining.value <= 0) {
                _secondsRemaining.value = RECHARGE_INTERVAL_SECONDS
            }
            saveState(_hearts.value, System.currentTimeMillis())
        }
    }

    private fun saveState(hearts: Int, timestamp: Long) {
        prefs.edit()
            .putInt(KEY_HEARTS, hearts)
            .putLong(KEY_LAST_TIMESTAMP, timestamp)
            .apply()
    }

    fun getFormattedTimeRemaining(): String {
        val seconds = _secondsRemaining.value
        val mins = seconds / 60
        val secs = seconds % 60
        return String.format("%02d:%02d", mins, secs)
    }

    fun getRechargeProgress(): Float {
        if (_isFull.value) return 1f
        val seconds = _secondsRemaining.value.toFloat()
        return 1f - (seconds / RECHARGE_INTERVAL_SECONDS.toFloat()).coerceIn(0f, 1f)
    }
}
