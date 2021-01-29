package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import br.com.digitalhouse.marvelnaticos.marvelnatics.FavoritesActivity
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.adapters.*
import br.com.digitalhouse.marvelnaticos.marvelnatics.api.Credentials
import br.com.digitalhouse.marvelnaticos.marvelnatics.interfaces.ComicClickListener
import br.com.digitalhouse.marvelnaticos.marvelnatics.interfaces.HePClickListener
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Character
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Comic
import br.com.digitalhouse.marvelnaticos.marvelnatics.services.repo
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.colecao.ColecaoActivity
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.comic.ComicFragment
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import kotlin.concurrent.thread


class HomeFragment : Fragment() {

    private lateinit var ctx: MainActivity
    private lateinit var adapterComicsML: ComicListAdapter
    private lateinit var adapterComicsMA: ComicListAdapter

    private val networkViewModel: NetworkViewModel by viewModels()

    val viewModel: HomeViewModel by viewModels<HomeViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HomeViewModel(repo) as T
            }
        }
    }

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


        colecao.setOnClickListener {
            ctx.goToActivity(
                ColecaoActivity::class.java,
                R.anim.slide_in_right,
                R.anim.static_animation
            )
        }

        favoritos.setOnClickListener {
            ctx.goToActivity(
                FavoritesActivity::class.java,
                R.anim.slide_in_right,
                R.anim.static_animation
            )
        }

        // Personagens mais populares
        val includePmP: View = root.findViewById(R.id.include_pmp)
        val rvPmP: RecyclerView = includePmP.findViewById(R.id.rv_list_charsList)
        rvPmP.adapter = CharacterAdapter(
            root.context, mutableListOf(
                //Character(),
                //Character(),
                //Character(),
                //Character(),
                //Character(),
                //Character(),
                //Character(),
                //Character(),
                // Character()
            )
        )

        // Historias em destaques
        val includeHeP: View = root.findViewById(R.id.include_hep)
        val vpHeP: ViewPager = includeHeP.findViewById(R.id.vp_hed)

        vpHeP.pageMargin = 10

        // Historias mais lidas
        val includeHML: View = root.findViewById(R.id.include_hml)
        val includeHMA: View = root.findViewById(R.id.include_hma)
        val rvHistoriasMaisLidas: RecyclerView = includeHML.findViewById(R.id.rv_list_listImages)
        val rvHistoriasMaisAvaliadas: RecyclerView =
            includeHMA.findViewById(R.id.rv_list_listImages)

        val titleHML: TextView = includeHML.findViewById(R.id.tv_list_listName)
        val titleHMA: TextView = includeHMA.findViewById(R.id.tv_list_listName)

        titleHML.text = "Histórias mais lidas"
        titleHMA.text = "Histórias melhor avaliadas"

        viewModel.listComics.observe(viewLifecycleOwner) {
            adapterComicsML = ComicListAdapter(rvHistoriasMaisLidas.context, it, ctx)
            adapterComicsMA = ComicListAdapter(rvHistoriasMaisAvaliadas.context, it, ctx)
            rvHistoriasMaisLidas.adapter = adapterComicsML
            rvHistoriasMaisAvaliadas.adapter = adapterComicsMA
            vpHeP.adapter = HePAdapter(vpHeP.context, it, ctx)
        }

        networkViewModel.networkAvaliable.observe(viewLifecycleOwner) { avaliable ->
            if (avaliable) viewModel.popListResult("Iron")
        }
        return root
    }

    override fun onPause() {
        super.onPause()
        context?.also { c -> networkViewModel.unregisterNetworkListener(c) }
    }

    override fun onResume() {
        super.onResume()
        context?.also { c -> networkViewModel.registerNetworkListener(c) }
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