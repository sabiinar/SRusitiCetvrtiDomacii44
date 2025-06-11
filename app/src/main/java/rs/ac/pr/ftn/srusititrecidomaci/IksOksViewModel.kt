package rs.ac.pr.ftn.srusititrecidomaci

import android.graphics.Color
import androidx.lifecycle.ViewModel

class IksOksViewModel : ViewModel() {
    private val _occupiedFields = mutableMapOf<Int, Int>()
    private var _lastColor: Int? = null
    var timerSeconds: Long = 15
    var isTimerRunning: Boolean = false
    var currentPlayer: Int = 1 // 1 = Crveni, 2 = Plavi
    var gameResult: String? = null

    val occupiedFields: Map<Int, Int>
        get() = _occupiedFields

    var lastColor: Int?
        get() = _lastColor
        set(value) { _lastColor = value }

    // Winning combinations for 3x3 grid
    private val winPatterns = listOf(
        listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),  // rows
        listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),  // columns
        listOf(0, 4, 8), listOf(2, 4, 6)                   // diagonals
    )

    fun setField(index: Int, color: Int) {
        if (gameResult != null || _occupiedFields.containsKey(index)) return

        _occupiedFields[index] = color
        _lastColor = color

        // Check for winner
        if (checkWin(color)) {
            gameResult = if (color == Color.RED) {
                "Победио је Црвени играч!"
            } else {
                "Победио је Плави играч!"
            }
        }
        // Check for draw
        else if (_occupiedFields.size == 9) {
            gameResult = "Нерешено!"
        }
    }

    fun switchPlayer() {
        if (gameResult == null) {
            currentPlayer = if (currentPlayer == 1) 2 else 1
        }
    }

    fun resetGame() {
        _occupiedFields.clear()
        _lastColor = null
        timerSeconds = 15
        isTimerRunning = false
        currentPlayer = 1
        gameResult = null
    }

    // Допунски метод ако ти треба да чистиш посебно
    fun clearOccupiedFields() {
        _occupiedFields.clear()
    }

    // Check if player has winning pattern
    internal fun checkWin(playerColor: Int): Boolean {
        return winPatterns.any { pattern ->
            pattern.all { index ->
                _occupiedFields[index] == playerColor
            }
        }
    }
}
