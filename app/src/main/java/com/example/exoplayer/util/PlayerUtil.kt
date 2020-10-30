package com.example.exoplayer.util

fun matchDetails(inputString: String, whatToFind: String, startIndex: Int = 0): Int {
    val matchIndex = inputString.indexOf(whatToFind, startIndex)
    return matchIndex
}