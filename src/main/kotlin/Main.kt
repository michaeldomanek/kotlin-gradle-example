import com.adamratzman.spotify.spotifyAppApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.io.File

suspend fun main() = runBlocking {
    val api = spotifyAppApi("", "").build()
    api.spotifyApiOptions.enableDebugMode = true
    api.spotifyApiOptions.useCache = false

    val albumIds = File("${System.getProperty("user.home")}/Downloads/albumIds.txt").readText()
        .replace("[\\[\\]]".toRegex(), "")
        .split(", ")

    while(true) {
        val tracks = albumIds.map { albumId ->
            async {
                api.albums.getAlbumTracks(albumId).getAllItemsNotNull()
            }
        }.awaitAll()

        println(tracks)
    }
}
