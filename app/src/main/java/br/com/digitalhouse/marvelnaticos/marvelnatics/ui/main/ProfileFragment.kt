package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.digitalhouse.marvelnaticos.marvelnatics.R

class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        return root
    }

    companion object {

        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int) = ProfileFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_SECTION_NUMBER, sectionNumber)
            }
        }
    }
}