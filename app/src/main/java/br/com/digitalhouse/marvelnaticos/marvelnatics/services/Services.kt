package br.com.digitalhouse.marvelnaticos.marvelnatics.services

import br.com.digitalhouse.marvelnaticos.marvelnatics.api.Credentials
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Character
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Comic
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Res
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface Repository {
    /*
        Para utlização do repositorio será necessario passar como paramentro a
        publicKey = Chave publica
        hash = md5 formado por (DataTime + private + publicKey)
        ts = Ao DateTime informado no hash

        foi disponibilizado a classe Credentials, onde contem as variaveis de chave publica
        e chave privada, e na classe Utils foi disponibilizado a funcao hashFormat
        que retorna o hash já em md5
    */

    /*
        Tem como parametros opcionais:
            title: titulo exatamente como é escrito
            titleStartsWith: o começo do titulo começa com,
            characters: Recebe uma lista do Id do personagem ou dos personagens para que seja
            retornada as comics no qual esse personagem aparece
    */
    @GET("comics")
    suspend fun getComics(
        @Query("apikey") publicKey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: String,
        @Query("titleStartsWith") titleStartWith: String? = null
    ): Res

    /*
        Recebe o Id de uma comic e retorna os dados apenas dessa comic
     */
    @GET("comics/{comicId}")
    suspend fun getComic(
        @Query("apikey") publicKey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: String,
        @Path("comicId") comicId: Int
    ): Call<Comic>

    /*
        Tem como parametros opcionais:
            name: titulo exatamente como é escrito
            nameStartsWith: o começo do nome começa com,
            comics: Recebe uma lista do Id da comic ou das comics para que seja
            retornada os personagens que aparecem nessas comics
     */
    @GET("characters")
    fun getCharacters(
        @Query("apikey") publicKey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: String,
        @Query("name") name: String? = null,
        @Query("nameStartsWith") nameStartsWith: String? = null,
        @Query("comics") comics: List<Int>? = null
    ): Call<Character>

    /*
        Recebe o Id de um personagem e retorna os dados apenas desse personagem
     */
    @GET("characters/{characterId}")
    fun getCharacter(
        @Query("apikey") publicKey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: String,
        @Path("characterId") comicId: Int
    ): Call<Character>
}

// Url da API
val url = "https://gateway.marvel.com:443/v1/public/"

// Cria a instancia do retrofit
val retrofit = Retrofit.Builder()
    .baseUrl(url)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

// Variavel a ser importada para utilizar da API
val repo = retrofit.create(Repository::class.java)
