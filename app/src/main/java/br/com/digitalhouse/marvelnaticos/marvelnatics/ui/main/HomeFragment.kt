package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.adapters.CharacterAdapter
import br.com.digitalhouse.marvelnaticos.marvelnatics.adapters.ComicsAdapter
import br.com.digitalhouse.marvelnaticos.marvelnatics.adapters.HePAdapter
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Character
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Comic
import br.com.digitalhouse.marvelnaticos.marvelnatics.util.Utils

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        // Personagens mais populares
        val includePmP: View = root.findViewById(R.id.include_pmp)
        val rvPmP: RecyclerView = root.findViewById(R.id.rv_list_charsList)
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

        // Historias em destaques
        val includeHeP: View = root.findViewById(R.id.include_hep)
        val vpHeP: ViewPager = includeHeP.findViewById(R.id.vp_hed)
        vpHeP.adapter = HePAdapter(root.context, mutableListOf(
            Comic(),
            Comic(),
            Comic(),
            Comic(),
            Comic()
        ))

        vpHeP.setOnTouchListener { v, event ->
            v.parent?.requestDisallowInterceptTouchEvent(true)

            v?.onTouchEvent(event) ?: true
        }
        vpHeP.pageMargin = 10

//        vpHeP.setOnTouchListener { v, event ->
//            v.parent?.requestDisallowInterceptTouchEvent(true)
//            false
//        }

        // Historias mais lidas
        val includeHML: View = root.findViewById(R.id.include_hml)
        val rvHistoriasMaisLidas: RecyclerView = includeHML.findViewById(R.id.rv_list_listImages)
        val spanCount = Utils.calculateSpan(root.context, 114)

        rvHistoriasMaisLidas.adapter = ComicsAdapter(root.context, mutableListOf(
            Comic(),
            Comic(),
            Comic(),
            Comic(),
            Comic(),
            Comic()
        ))
        rvHistoriasMaisLidas.layoutManager = GridLayoutManager(root.context, spanCount)
        return root
    }

    companion object {

        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int) = HomeFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_SECTION_NUMBER, sectionNumber)
            }
        }
    }
}