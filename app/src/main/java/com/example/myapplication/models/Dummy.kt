package com.example.myapplication.models

object Dummy {
    fun getWordlist(): List<Word> =
         listOf(
                Word("cat"),
                Word("bat"),
                Word("hat"),
                Word("fat"),
                Word("mat"),
                Word("sat"),
                Word("frat"),
                Word("bingo"),
                Word("apple"),
                Word("pineapple"),
                Word("amoeba"),
                Word("domino"),
                Word("butterfly effect")

        )

    fun getBooleanList(): MutableList<Boolean> = mutableListOf(
           false,
           false,
           false,
           false,
           false,
           false,
           false,
           false,
           false,
           false,
           false,
           false,
           false,
           false

    )

}