package ru.bigseized.queue.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.bigseized.queue.core.Constants
import ru.bigseized.queue.data.dto.SignInDTO
import ru.bigseized.queue.data.dto.SignUpDTO
import ru.bigseized.queue.data.dto.UserDTO
import ru.bigseized.queue.domain.model.User

interface UserApi {

    companion object {
        const val BASE_URL = "https://parseapi.back4app.com"
    }

    @Headers(
        "X-Parse-Application-Id: ${Constants.applicationId}",
        "X-Parse-REST-API-Key: ${Constants.restApiKey}",
        "X-Parse-Revocable-Session: 1",
        "Content-Type: application/json"
    )
    @POST("/users")
    suspend fun signUp(
        @Body user: User
    ): Response<SignUpDTO>

    @Headers(
        "X-Parse-Application-Id: ${Constants.applicationId}",
        "X-Parse-REST-API-Key: ${Constants.restApiKey}",
        "X-Parse-Revocable-Session: 1"
    )
    @GET("/login")
    suspend fun signIn(
        @Query("username") username: String,
        @Query("password") password: String
    ): Response<SignInDTO>

    @Headers(
        "X-Parse-Application-Id: ${Constants.applicationId}",
        "X-Parse-REST-API-Key: ${Constants.restApiKey}"
    )
    @GET("/users/{objectId}")
    suspend fun getUser(
        @Path("objectId") objectId: String
    ): Response<UserDTO>

    @Headers(
        "X-Parse-Application-Id: ${Constants.applicationId}",
        "X-Parse-REST-API-Key: ${Constants.restApiKey}"
    )
    @POST("/logout")
    suspend fun logOut(
        @Header("sessionToken") sessionToken: String
    ): Response<Unit>

}