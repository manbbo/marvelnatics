package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.adapters.ComicSearchAdapter
import br.com.digitalhouse.marvelnaticos.marvelnatics.services.repo

class BuscaFragment : Fragment() {

    private lateinit var adapterComics: ComicSearchAdapter
    private lateinit var ctx: MainActivity
    private lateinit var rvBuscaRes: RecyclerView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) ctx = context
    }

    val viewModel: BuscaViewModel by viewModels<BuscaViewModel>{
        object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return BuscaViewModel(repo) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_busca, container, false).also { root -> // VIEW
            root.findViewById<EditText>(R.id.txt_busca_pesquisar)
                .also { txtPesquisar -> // TXT PESQUISAR
                    txtPesquisar.setOnEditorActionListener { v, actionId, event -> // LISTENER DE ACTIONS
                        when (actionId) { // VERIFICA A ACTION REALIZADA
                            EditorInfo.IME_ACTION_SEARCH -> { // TIPO DEFINIDO NO XML
                                viewModel.popListResult(txtPesquisar.text.toString())
                                true
                            }
                            else -> false
                        }
                    }
                }
        }

        var totalResBusca = root.findViewById<TextView>(R.id.tv_busca_info)

        rvBuscaRes = root.findViewById<RecyclerView>(R.id.rv_busca_resultado)
        rvBuscaRes.layoutManager = LinearLayoutManager(context)

        viewModel.listComics.observe(viewLifecycleOwner){
            adapterComics = ComicSearchAdapter(rvBuscaRes.context, it, ctx)
            rvBuscaRes.adapter = adapterComics
        }

        viewModel.totRes.observe(viewLifecycleOwner){
            totalResBusca.text = viewModel.totRes.value.toString() + " resultado(s) encontrado(s)"
        }

        setScroller()
        return root;
    }

    fun setScroller(){
        rvBuscaRes.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0){
                }
            }
        })
    }

    companion object {

        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int) = BuscaFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_SECTION_NUMBER, sectionNumber)
            }
        }
    }
}