package rs.ac.pr.ftn.srusititrecidomaci

import androidx.lifecycle.ViewModel

class RezultatViewModel(gameName: String, score: String) : ViewModel() {
    val gameName: String = gameName
    val score: String = score
}