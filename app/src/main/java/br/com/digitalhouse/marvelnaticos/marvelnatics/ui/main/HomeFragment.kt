package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import br.com.digitalhouse.marvelnaticos.marvelnatics.FavoritesActivity
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.adapters.CharacterAdapter
import br.com.digitalhouse.marvelnaticos.marvelnatics.adapters.ComicsAdapter
import br.com.digitalhouse.marvelnaticos.marvelnatics.adapters.HePAdapter
import br.com.digitalhouse.marvelnaticos.marvelnatics.interfaces.ComicClick
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Character
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Comic
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.colecao.ColecaoActivity
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.comics.ComicFragment
import br.com.digitalhouse.marvelnaticos.marvelnatics.util.Utils
import kotlinx.android.synthetic.main.toolbar.*
import org.w3c.dom.Text

class HomeFragment : Fragment(), ComicClick {

    private lateinit var ctx : MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) ctx = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        // Toolbar
        val toolbar: Toolbar = root.findViewById(R.id.include_toolbar)
        val colecao: TextView = toolbar.findViewById(R.id.btn_toolbar_colecao)
        val favoritos: TextView = toolbar.findViewById(R.id.btn_toolbar_favoritos)


        colecao.setOnClickListener{
            ctx.goToActivity(ColecaoActivity::class.java, R.anim.slide_in_right, R.anim.static_animation)
        }

        favoritos.setOnClickListener{
            ctx.goToActivity(FavoritesActivity::class.java, R.anim.slide_in_right, R.anim.static_animation)
        }

        // Personagens mais populares
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
        val includeHMA: View = root.findViewById(R.id.include_hma)
        val rvHistoriasMaisLidas: RecyclerView = includeHML.findViewById(R.id.rv_list_listImages)
        val rvHistoriasMaisAvaliadas: RecyclerView = includeHMA.findViewById(R.id.rv_list_listImages)

        val titleHML : TextView = includeHML.findViewById(R.id.tv_list_listName)
        val titleHMA : TextView = includeHMA.findViewById(R.id.tv_list_listName)

        titleHML.text = "Histórias mais lidas"
        titleHMA.text = "Histórias melhor avaliadas"

        rvHistoriasMaisLidas.adapter = ComicsAdapter(root.context, mutableListOf(
            Comic(),
            Comic(),
            Comic(),
            Comic(),
            Comic(),
            Comic()
        ))

        rvHistoriasMaisAvaliadas.adapter = rvHistoriasMaisLidas.adapter

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

    override fun onComicClick(position: Int): View.OnClickListener = View.OnClickListener {
        ctx.changeFragment()
        Log.v("t", "try")
    }
}