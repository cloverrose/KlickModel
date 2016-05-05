package com.github.cloverrose.klickmodel.examples

import java.util.Random
import com.github.cloverrose.klickmodel.domain.SearchResult
import com.github.cloverrose.klickmodel.domain.SearchSession
import com.github.cloverrose.klickmodel.models.ubm.UBM

fun generateData(): List<SearchSession> {
    val random = Random()
    val Q = 100
    val D = 10000
    val N = 1000
    val MAX_RANK = 10

    val sessions = IntRange(0, N - 1).map {
        val queryId = random.nextInt(Q)
        val results = IntRange(0, MAX_RANK - 1).map  {
            val resultId = random.nextInt(D)
            SearchResult(resultId.toString(), it == 0 && resultId % 2 == 0)
        }
        SearchSession(queryId.toString(), results)
    }
    return sessions
}

fun main(args: Array<String>) {
    val searchSessions = generateData()

    val clickModel = UBM()
    clickModel.train(searchSessions)
    val json: String = clickModel.toJson()

    val clickModel2 = UBM()
    clickModel2.fromJson(json)
    val json2: String = clickModel2.toJson()

    assert(json2.equals(json))
    println("OK")
}
