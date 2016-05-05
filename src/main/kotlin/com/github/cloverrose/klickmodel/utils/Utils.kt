package com.github.cloverrose.klickmodel.utils

import com.github.cloverrose.klickmodel.domain.SearchSession

object Utils {
    fun getUniqueQueries(searchSessions: List<SearchSession>) = searchSessions.map { it.query }.toSet()
    fun filterSessions(searchSessions: List<SearchSession>, queries: Set<String>) = searchSessions.filter { it.query in queries }
}
