package com.example.capstone.ui.bottomnavigation.home

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.capstone.R

class AnnouncementDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val message = """
            Pengumuman:
            1. Poin pertama
            2. Poin kedua
            3. Poin ketiga
            4. Poin keempat
            5. Poin kelima
        """.trimIndent()

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Pengumuman")
            .setMessage(message)
            .setPositiveButton("Tutup") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = builder.create()

        return dialog
    }

    companion object {
        const val TAG = "AnnouncementDialogFragment"

        fun newInstance(): AnnouncementDialogFragment {
            return AnnouncementDialogFragment()
        }
    }
}
