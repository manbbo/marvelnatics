package br.com.digitalhouse.marvelnaticos.marvelnatics.services

import android.util.Log
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

class Translator {

    fun getLanguage(text : String) : String{
        var language = "";
        val languageIdentifier = LanguageIdentification.getClient()
        languageIdentifier.identifyLanguage(text)
            .addOnSuccessListener { languageCode ->
                if (languageCode == "und") {
                    Log.i("LANG", "Can't identify language.")
                } else {
                    Log.i("LANG", "Language: $languageCode")
                }

                language = languageCode
            } .addOnFailureListener{
                    exception -> Log.i("LANG", "ERRO pegando a linguagem: \nLinguagem: $text \n trace: ${exception.stackTrace}")
            }

        return language
    }

    // Motherthonge = texto que vem da linguagem selecionada
    fun translate(motherThong : String, text : String) : String {
        var translatedText: String = ""

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(motherThong)
            .build()
        val translator = Translation.getClient(options)

        var conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                Log.i("LANG", "SUCCESSFULLY INITIALIZED")

                translator.translate(text)
                    .addOnSuccessListener {
                            translated -> translatedText = translated
                    }.addOnFailureListener { exception ->
                        Log.i("LANG", "ERROR translating:  \n" +
                                " Motherthong: $motherThong \n" +
                                "Trace: ${exception.stackTrace}")
                    }
            }
            .addOnFailureListener { exception ->
                Log.i("LANG", "Error inicializing: \n Motherthong: $motherThong \nTrace: ${exception.stackTrace}")
            }

        return translatedText
    }
}