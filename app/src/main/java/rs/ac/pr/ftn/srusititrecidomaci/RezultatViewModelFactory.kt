package rs.ac.pr.ftn.srusititrecidomaci

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RezultatViewModelFactory(
    private val gameName: String,
    private val score: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RezultatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RezultatViewModel(gameName, score) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}