package rs.ac.pr.ftn.srusititrecidomaci



import androidx.lifecycle.ViewModel

class PrikazTekstaViewModel : ViewModel() {
    private var _izabraniBroj: String? = null

    // Enkapsulacija podataka sa getter i setter
    val izabraniBroj: String?
        get() = _izabraniBroj

    fun setIzabraniBroj(broj: String?) {
        _izabraniBroj = broj
    }
}