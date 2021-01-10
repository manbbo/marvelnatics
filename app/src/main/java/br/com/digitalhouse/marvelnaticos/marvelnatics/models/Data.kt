package br.com.digitalhouse.marvelnaticos.marvelnatics.models

data class Data(val offset: Int, var results: ArrayList<Comic?>, var total: Int, var count: Int)