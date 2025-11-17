package com.example.musiletra.ui

enum class Routes(val route: String) {
    SONG_LIST("songList"),
    ADD_SONG("addSong"),
    EDIT_SONG("editSong/{songId}"),
    SONG_DETAILS("songDetail/{songId}"),
    ONLINE_SEARCH("onlineSearch"),
    PLAYLISTS("playlistList"),
    PLAYLIST_DETAILS("playlistDetail/{playlistId}"),
    PLAYLIST_INFO("playlistInfo/{playlistId}");

    companion object {
        fun editSong(songId: String) = "editSong/$songId"
        fun songDetails(songId: String) = "songDetail/$songId"
        fun playlistDetails(playlistId: Int) = "playlistDetail/$playlistId"
        fun playlistInfo(playlistId: Int) = "playlistInfo/$playlistId"
    }
}
