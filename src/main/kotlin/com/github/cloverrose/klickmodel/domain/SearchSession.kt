package com.github.cloverrose.klickmodel.domain

data class SearchSession(val query: String, val webResults: List<SearchResult>) {
    fun getClicks(): List<Boolean> = this.webResults.map { it.isClicked }

    fun getLastClickRank(): Int {
        val clicks = this.getClicks()
        val lastClickRank = clicks.lastIndexOf(true)
        return if (lastClickRank == -1) {
            clicks.size
        } else {
            lastClickRank
        }
    }
}
