package br.com.digitalhouse.marvelnaticos.marvelnatics.models

class Comic (val id: Int,
             val title: String,
             val description: String,
             val dates: ArrayList<ComicDate>,
             val series: SeriesSummary,
             val thumbnail : Image,
             val images: ArrayList<Image>,
             val prices : ArrayList<ComicPrice>,
             val characters: CharacterList,
             val creators: CreatorList) {
}