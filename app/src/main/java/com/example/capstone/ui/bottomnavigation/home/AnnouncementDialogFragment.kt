package com.example.capstone.ui.bottomnavigation.home

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class AnnouncementDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val message = "1. Ensure your internet is active and the connection is stable.\n" +
                "2. Ensure the screen is in an upright or vertical position.\n" +
                "3. Position the front camera at eye level.\n" +
                "4. Ensure there is adequate lighting around the face."


        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Usage Instructions")
            .setMessage(message)
            .setPositiveButton("Close") { dialog, _ ->
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
