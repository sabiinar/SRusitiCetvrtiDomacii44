package rs.ac.pr.ftn.srusititrecidomaci

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

class IksOksFragment : Fragment(R.layout.fragment_iks_oks) {

    private val viewModel: IksOksViewModel by viewModels()
    private lateinit var etInput: EditText
    private lateinit var btnRed: Button
    private lateinit var btnBlue: Button
    private lateinit var btnNewGame: Button
    private lateinit var btnNazad: Button
    private lateinit var btnDone: Button
    private lateinit var tvTimer: TextView
    private lateinit var tvPlayerTurn: TextView
    private lateinit var fields: List<TextView>
    private var gameTimer: CountDownTimer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)

        if (savedInstanceState != null) {
            etInput.setText(savedInstanceState.getString("input_text", ""))
        }

        updatePlayerTurn()

        viewModel.occupiedFields.forEach { (index, color) ->
            fields[index].setBackgroundColor(color)
        }

        if (viewModel.isTimerRunning) {
            startTimer(viewModel.timerSeconds * 1000)
        } else if (viewModel.timerSeconds == 0L) {
            disableGameInteraction()
            tvTimer.text = "Време је истекло!"
        }

        if (viewModel.gameResult != null) {
            endGameWithResult(viewModel.gameResult!!)
        }

        btnRed.setOnClickListener {
            if (!viewModel.isTimerRunning) return@setOnClickListener
            val color = Color.RED
            if (viewModel.lastColor == color) {
                showToast("На потезу је плави играч!")
            } else {
                colorField(color)
                viewModel.lastColor = color
            }
        }

        btnBlue.setOnClickListener {
            if (!viewModel.isTimerRunning) return@setOnClickListener
            val color = ContextCompat.getColor(requireContext(), R.color.blue)
            if (viewModel.lastColor == color) {
                showToast("На потезу је црвени играч!")
            } else {
                colorField(color)
                viewModel.lastColor = color
            }
        }

        btnNewGame.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Потврда")
                .setMessage("Желите ли започети нову игру?")
                .setPositiveButton("ДА") { _, _ -> resetGame() }
                .setNegativeButton("НЕ") { dialog, _ -> dialog.dismiss() }
                .show()
        }

        btnNazad.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        btnDone.setOnClickListener {
            val result = viewModel.gameResult ?: "Игра у току"
            navigateToResults(result)
        }
    }

    override fun onPause() {
        super.onPause()
        gameTimer?.cancel()
        viewModel.isTimerRunning = false
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.timerSeconds > 0) {
            startTimer(viewModel.timerSeconds * 1000)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("input_text", etInput.text.toString())
    }

    private fun initViews(view: View) {
        etInput = view.findViewById(R.id.etInput)
        btnRed = view.findViewById(R.id.btnRed)
        btnBlue = view.findViewById(R.id.btnBlue)
        btnNewGame = view.findViewById(R.id.btnNewGame)
        btnNazad = view.findViewById(R.id.btnBack)
        btnDone = view.findViewById(R.id.btnDone)
        tvTimer = view.findViewById(R.id.tvTimer)
        tvPlayerTurn = view.findViewById(R.id.tvPlayerTurn)

        fields = listOf(
            view.findViewById(R.id.p1), view.findViewById(R.id.p2), view.findViewById(R.id.p3),
            view.findViewById(R.id.p4), view.findViewById(R.id.p5), view.findViewById(R.id.p6),
            view.findViewById(R.id.p7), view.findViewById(R.id.p8), view.findViewById(R.id.p9)
        )
    }

    private fun startTimer(durationMillis: Long) {
        gameTimer?.cancel()

        gameTimer = object : CountDownTimer(durationMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                viewModel.timerSeconds = secondsLeft
                tvTimer.text = "Преостало време: ${secondsLeft}с"
            }

            override fun onFinish() {
                viewModel.isTimerRunning = false
                viewModel.timerSeconds = 0
                tvTimer.text = "Време је истекло!"
                showToast("Време за игру је истекло!")
                disableGameInteraction()
            }
        }.start()

        viewModel.isTimerRunning = true
    }

    private fun colorField(color: Int) {
        val inputText = etInput.text.toString().trim()
        if (inputText.isEmpty()) {
            showToast("Унеси број поља!")
            return
        }

        val input = inputText.toIntOrNull() ?: run {
            showToast("Неисправан број!")
            return
        }

        if (input !in 1..9) {
            showToast("Број мора бити између 1 и 9!")
            return
        }

        val index = input - 1

        if (viewModel.occupiedFields.containsKey(index)) {
            showToast("Поље је већ заузето!")
            return
        }

        fields[index].setBackgroundColor(color)
        viewModel.setField(index, color)

        if (viewModel.gameResult != null) {
            endGameWithResult(viewModel.gameResult!!)
            return
        }

        viewModel.switchPlayer()
        updatePlayerTurn()
        gameTimer?.cancel()
        viewModel.timerSeconds = 15
        startTimer(15000)
    }

    private fun resetGame() {
        fields.forEach {
            it.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
        etInput.text.clear()
        viewModel.resetGame()
        updatePlayerTurn()
        enableGameInteraction()
        gameTimer?.cancel()
        viewModel.timerSeconds = 15
        startTimer(15000)
    }

    private fun updatePlayerTurn() {
        val playerText = if (viewModel.currentPlayer == 1) {
            "На потезу: Црвени играч"
        } else {
            "На потезу: Плави играч"
        }
        tvPlayerTurn.text = playerText
    }

    private fun disableGameInteraction() {
        btnRed.isEnabled = false
        btnBlue.isEnabled = false
        etInput.isEnabled = false
    }

    private fun enableGameInteraction() {
        btnRed.isEnabled = true
        btnBlue.isEnabled = true
        etInput.isEnabled = true
    }

    private fun endGameWithResult(result: String) {
        showToast(result)
        disableGameInteraction()
        tvPlayerTurn.text = result
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToResults(result: String) {

        val bundle = Bundle().apply {
            putString("result_key", result)
        }
        findNavController().navigate(R.id.rezultatFragment, bundle)
    }
}

