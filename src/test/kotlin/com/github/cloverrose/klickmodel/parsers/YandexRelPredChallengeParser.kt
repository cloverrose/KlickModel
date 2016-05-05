package com.github.cloverrose.klickmodel.parsers
import java.io.File
import com.github.cloverrose.klickmodel.domain.SearchSession
import com.github.cloverrose.klickmodel.domain.SearchResult

object YandexRelPredChallengeParser: Parser() {

    override fun parse(sessionsFilename: String, sessionsMax: Int): List<SearchSession> {
        val sessionFile = File(sessionsFilename)

        val sessions: List<SearchSession> = sessionFile.useLines { it.map { it.trim().split("\t") }.groupBy { it[0] }.map {
            val queryOrClicks: Map<String, List<List<String>>> = it.value.groupBy { it[2] }
            val queryEntry = queryOrClicks["Q"]!!.first()
            val clickEntries: List<List<String>> = queryOrClicks.getOrElse("C") { emptyList() }

            val resultIds: List<String> = queryEntry.subList(5, queryEntry.size)
            val clickIds: List<String> = clickEntries.map { it[3] }
            val results: List<SearchResult> = resultIds.map { id -> SearchResult(id, id in clickIds) }
            val session: SearchSession = SearchSession(queryEntry[3], results)
            session
        }}.take(sessionsMax)
        return sessions
    }
}
