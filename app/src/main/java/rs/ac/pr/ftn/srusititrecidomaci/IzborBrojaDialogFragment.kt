package rs.ac.pr.ftn.srusititrecidomaci

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class IzborBrojaDialogFragment(
    private val onBrojOdabran: (String) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val brojevi = arrayOf("1", "2", "3")

        return AlertDialog.Builder(requireContext())
            .setTitle("Изаберите број текста")
            .setItems(brojevi) { _, which ->
                onBrojOdabran(brojevi[which])
            }
            .setNegativeButton("Откажи", null)
            .create()
    }
}
