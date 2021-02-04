package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.adapters.ComicSearchAdapter
import br.com.digitalhouse.marvelnaticos.marvelnatics.services.repo
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.NetworkViewModel


class BuscaFragment : Fragment() {

    private lateinit var adapterComics: ComicSearchAdapter
    private lateinit var ctx: MainActivity
    private lateinit var rvBuscaRes: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var txtPesquisaUsr = ""
    private var isLoading = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) ctx = context
    }

    val networkViewModel: NetworkViewModel by viewModels()

    val viewModel: BuscaViewModel by viewModels<BuscaViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return BuscaViewModel(repo) as T
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_busca, container, false).also { root -> // VIEW
            root.findViewById<EditText>(R.id.txt_busca_pesquisar).also { txtPesquisar -> // TXT PESQUISAR
                txtPesquisar.setOnEditorActionListener { v, actionId, event -> // LISTENER DE ACTIONS
                    when (actionId) { // VERIFICA A ACTION REALIZADA
                        EditorInfo.IME_ACTION_SEARCH -> { // TIPO DEFINIDO NO XML
                            if (!(networkViewModel.networkAvaliable.value ?: false)) {
                                false
                            } else {
                                viewModel.clearList()
                                txtPesquisaUsr = txtPesquisar.text.toString()
                                viewModel.popListResult(txtPesquisaUsr)
                                val imm = ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                imm.hideSoftInputFromWindow(root.windowToken, 0)
                                txtPesquisar.focusable = View.FOCUSABLE
                                true
                            }

                        }
                        else -> {
                            txtPesquisar.focusable = View.NOT_FOCUSABLE
                            false
                        }
                    }
                }
            }
        }

        var totalResBusca = root.findViewById<TextView>(R.id.tv_busca_info)

        rvBuscaRes = root.findViewById<RecyclerView>(R.id.rv_busca_resultado)
        linearLayoutManager = LinearLayoutManager(context)
        rvBuscaRes.layoutManager = linearLayoutManager

        viewModel.mutableListComics.observe(viewLifecycleOwner) {
            rvBuscaRes.adapter = null
            rvBuscaRes.layoutManager = null

            adapterComics = ComicSearchAdapter(rvBuscaRes.context, it, ctx)
            rvBuscaRes.adapter = adapterComics
            viewModel.adapterComics.value = adapterComics

            linearLayoutManager.removeAllViews()
            rvBuscaRes.layoutManager = linearLayoutManager
        }

        viewModel.totRes.observe(viewLifecycleOwner) {
            totalResBusca.text = viewModel.totRes.value.toString() + " resultado(s) encontrado(s)"
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            isLoading = viewModel.isLoading.value == true
        }

        setScroller()
        return root;
    }

    override fun onPause() {
        super.onPause()
        context?.also { c -> networkViewModel.unregisterNetworkListener(c) }
    }

    override fun onResume() {
        super.onResume()
        context?.also { c -> networkViewModel.registerNetworkListener(c) }
    }


    fun setScroller() {
        rvBuscaRes.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rvBuscaRes: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rvBuscaRes, dx, dy)
                if (!(networkViewModel.networkAvaliable.value ?: false)) return

                val lItem = linearLayoutManager.childCount
                val vItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                val itens = adapterComics.itemCount

                if (!viewModel.isLoading() && !viewModel.isLastPage() && lItem + vItem >= itens && vItem >= 0) {
                    viewModel.popListResult(txtPesquisaUsr)
                    rvBuscaRes.post(Runnable {
                        viewModel.adapterComics.value!!.notifyItemInserted(viewModel.listComics.size - 1)
                    })
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