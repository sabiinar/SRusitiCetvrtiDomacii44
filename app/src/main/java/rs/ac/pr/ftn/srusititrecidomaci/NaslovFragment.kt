package rs.ac.pr.ftn.srusititrecidomaci

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class NaslovFragment : Fragment(R.layout.fragment_naslov) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btnPrikazTeksta).setOnClickListener {
            findNavController().navigate(R.id.action_naslovFragment_to_prikazTekstaFragment)
        }

        view.findViewById<Button>(R.id.btnIksOks).setOnClickListener {
            findNavController().navigate(R.id.action_naslovFragment_to_iksOksFragment)
        }

        view.findViewById<Button>(R.id.btnAbout).setOnClickListener {
            findNavController().navigate(R.id.action_naslovFragment_to_aboutFragment)
        }
    }
}
