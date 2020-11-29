package br.com.digitalhouse.marvelnaticos.marvelnatics.models

class User (val email : String,
            val name: String,
            val password : String,
            val photoURL: String,
            val emailVerified: Boolean,
            val uid: Int) {
}