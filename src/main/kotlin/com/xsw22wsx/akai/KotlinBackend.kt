package com.xsw22wsx.akai

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

const val API_BASE_URL = "https://akai-recruitment.herokuapp.com"

val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        json()
    }
}

val sentences = arrayOf(
    "Taki mamy klimat",
    "Wszędzie dobrze ale w domu najlepiej",
    "Wyskoczył jak Filip z konopii",
    "Gdzie kucharek sześć tam nie ma co jeść",
    "Nie ma to jak w domu",
    "Konduktorze łaskawy zabierz nas do Warszawy",
    "Jeżeli nie zjesz obiadu to nie dostaniesz deseru",
    "Bez pracy nie ma kołaczy",
    "Kto sieje wiatr ten zbiera burzę",
    "Być szybkim jak wiatr",
    "Kopać pod kimś dołki",
    "Gdzie raki zimują",
    "Gdzie pieprz rośnie",
    "Swoją drogą to gdzie rośnie pieprz?",
    "Mam nadzieję, że poradzisz sobie z tym zadaniem bez problemu",
    "Nie powinno sprawić żadnego problemu, bo Google jest dozwolony"
)

@Serializable data class Book(
    val id: String,
    val title: String,
    val author: String,
    val rating: Double,
)

fun main() = runBlocking {
    println("books:")
    client
        .get("$API_BASE_URL/book")
        .body<Collection<Book>>()
        .groupBy { it.author }
        .entries
        .map { it.key to it.value.map(Book::rating).average() }
        .sortedByDescending { it.second }
        .take(3)
        .forEach { println("${it.first} - ${it.second}") }

    println("\nword-count:")
    sentences
        .flatMap { "\\s*(\\p{javaLetter}+)\\s*".toRegex().findAll(it).map { it.value } }
        .map { it.lowercase() }
        .groupBy { it }
        .entries
        .maxByOrNull { it.value.size }!!
        .let { println("${it.key} - ${it.value.size}") }
}