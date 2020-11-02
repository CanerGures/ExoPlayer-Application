package com.example.exoplayer.util

fun matchDetails(inputString: String, whatToFind: String, startIndex: Int = 0): Int {
    return inputString.indexOf(whatToFind, startIndex)
}