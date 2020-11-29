package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import br.com.digitalhouse.marvelnaticos.marvelnatics.R

class BuscaFragment : Fragment() {
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
                                Toast.makeText( // TOAST DE TESTES
                                    root.context,
                                    "Pesquisa: ${txtPesquisar.text.toString()}",
                                    Toast.LENGTH_LONG
                                ).show()
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