    package ec.edu.uisek.githubclient.services

    import ec.edu.uisek.githubclient.models.Repository
    import ec.edu.uisek.githubclient.models.RepositoryPayload
    import retrofit2.http.Body
    import retrofit2.http.DELETE
    import retrofit2.http.GET
    import retrofit2.http.PATCH
    import retrofit2.http.POST
    import retrofit2.http.Path
    import retrofit2.http.Query

    interface ApiService {
        @GET("user")
        suspend fun validateToken(): retrofit2.Response<ec.edu.uisek.githubclient.models.GithubUser>

        @GET(value = "user/repos")
        suspend fun getRepository (
            @Query("sort") sort: String = "Created",
            @Query("direction") direction: String = "desc",
            @Query("affiliation") affiliation: String = "owner",
            @Query("t") t: String = "${System.currentTimeMillis()}"
        ): List<Repository>

        @POST("user/repos")
        suspend fun createRepository (
            @Body payload: RepositoryPayload
        ): Repository

        @PATCH("repos/{owner}/{repo}")
        suspend fun updateRepository(
            @Path("owner") owner: String,
            @Path("repo") repo: String,
            @Body payload: RepositoryPayload
        ): Repository

        @DELETE("repos/{owner}/{repo}")
        suspend fun deleteRepository(
            @Path("owner") owner: String,
            @Path("repo") repo: String
        ): retrofit2.Response<Unit>
    }