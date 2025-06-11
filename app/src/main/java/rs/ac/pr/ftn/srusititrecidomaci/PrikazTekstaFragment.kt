package rs.ac.pr.ftn.srusititrecidomaci

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

class PrikazTekstaFragment : Fragment(R.layout.fragment_prikaz_teksta) {


    private val viewModel: PrikazTekstaViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val poljeIzbor = view.findViewById<TextView>(R.id.izabraniBroj)
        val dugmePrikazi = view.findViewById<Button>(R.id.dugmePrikazi)
        val dugmeNazad = view.findViewById<Button>(R.id.dugmeNazad)
        val dugmeGotovo = view.findViewById<Button>(R.id.dugmeGotovo) // Novo dugme
        val rezultatText = view.findViewById<TextView>(R.id.rezultatText)
        val rezultatScrollView = view.findViewById<ScrollView>(R.id.rezultatScrollView)

        rezultatText.movementMethod = ScrollingMovementMethod.getInstance()

        // Restauracija stanja iz ViewModel-a
        viewModel.izabraniBroj?.let {
            poljeIzbor.text = "Изабрани број: $it"
        }

        // Klik na polje za izbor broja otvara dijalog
        poljeIzbor.setOnClickListener {
            val dialog = IzborBrojaDialogFragment { izabrano ->
                viewModel.setIzabraniBroj(izabrano)
                poljeIzbor.text = "Изабрани број: $izabrano"
            }
            dialog.show(parentFragmentManager, "izborBroja")
        }

        // Dugme "Прикажи"
        dugmePrikazi.setOnClickListener {
            val izabrani = viewModel.izabraniBroj
            if (izabrani == null) {
                poljeIzbor.text = "Одаберите број!"
                return@setOnClickListener
            }

            val tekst = when (izabrani) {
                "1" -> getString(R.string.tekst_1)
                "2" -> getString(R.string.tekst_2)
                "3" -> getString(R.string.tekst_3)
                else -> getString(R.string.nepoznat_broj)
            }

            rezultatText.text = tekst
            rezultatScrollView.visibility = View.VISIBLE
        }

        // Dugme "Назад"
        dugmeNazad.setOnClickListener {
            findNavController().navigateUp()
        }

        // Dugme "Готово" - novo
        dugmeGotovo.setOnClickListener {
            val izabrani = viewModel.izabraniBroj
            if (izabrani == null) {
                Toast.makeText(
                    requireContext(),
                    "Морате изабрати број пре него што будете готови!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val bundle = Bundle().apply {
                putString("GAME_NAME", "Текст игра")
                putString("RESULT_TEXT", "Изабрани број: $izabrani")
            }

            // Navigacija ka fragmentu za rezultate
            findNavController().navigate(
                R.id.action_prikazTekstaFragment_to_rezultatFragment,
                bundle
            )
        }
    }
}
