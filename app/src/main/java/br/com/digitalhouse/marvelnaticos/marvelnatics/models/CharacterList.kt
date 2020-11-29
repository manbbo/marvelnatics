package br.com.digitalhouse.marvelnaticos.marvelnatics.models

class CharacterList ( val available:Int,
                      val returned: Int,
                      val collectionURI: String,
                      val items: ArrayList<CharacterSummary>) {
}