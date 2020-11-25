package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import br.com.digitalhouse.marvelnaticos.marvelnatics.FavoritesActivity
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.adapters.CharacterAdapter
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Character

class ProfileFragment : Fragment() {

    private lateinit var ctx : MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) ctx = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        val includePmP: View = root.findViewById(R.id.include_pmp)
        val rvPmP: RecyclerView = includePmP.findViewById(R.id.rv_list_charsList)
        rvPmP.adapter = CharacterAdapter(root.context, mutableListOf(
                Character(),
                Character(),
                Character(),
                Character(),
                Character(),
                Character(),
                Character(),
                Character(),
                Character()
        ))

        val favoritos : AppCompatButton = root.findViewById(R.id.bt_favorites_profile)

        favoritos.setOnClickListener{
            ctx.goToActivity(FavoritesActivity::class.java, R.anim.slide_in_bottom, R.anim.static_animation)
        }

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