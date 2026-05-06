package ec.edu.uisek.githubclient.services
import androidx.compose.ui.text.style.TextDirection
import ec.edu.uisek.githubclient.models.Repository
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(value = "user/repos")
    suspend fun getRepository (
        @Query("sort") sort: String = "Created",
        @Query("direction") direction: String = "desc",
        @Query("affiliation") afiliation: String = "owner",
        @Query("t") t: String = "${System.currentTimeMillis()}"
    ): List<Repository>
}