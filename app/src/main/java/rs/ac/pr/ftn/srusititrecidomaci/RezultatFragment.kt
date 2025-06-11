package rs.ac.pr.ftn.srusititrecidomaci

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import rs.ac.pr.ftn.srusititrecidomaci.databinding.FragmentRezultatBinding

class RezultatFragment : Fragment(R.layout.fragment_rezultat) {

    private var _binding: FragmentRezultatBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRezultatBinding.bind(view)

        // Узми аргументе из Bundle-а, ако их нема постави подразумеване вредности
        val gameName = arguments?.getString("game_name") ?: "Игра"
        val resultText = arguments?.getString("result_key") ?: "Нема резултата"

        val viewModel = ViewModelProvider(
            this,
            RezultatViewModelFactory(gameName, resultText)
        ).get(RezultatViewModel::class.java)

        binding.tvGameName.text = viewModel.gameName
        binding.tvScore.text = viewModel.score
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
