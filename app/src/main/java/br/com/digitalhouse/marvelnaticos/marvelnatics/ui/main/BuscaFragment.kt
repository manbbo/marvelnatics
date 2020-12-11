package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.services.repo

class BuscaFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_busca, container, false).also { root -> // VIEW
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