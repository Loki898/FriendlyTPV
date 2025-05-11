package crr.cliente.crirodrui.repositorios

import com.google.gson.JsonParser
import crr.project.crirodrui.elements.Rol
import crr.project.crirodrui.elements.User
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Token(val token: String)

class UserRepository {
    val client = HttpClient {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
                serializeNulls()
            }
        }
    }

    suspend fun getUserByUsername(username: String): User {
        var user = User()
        val usersHTTP = client.get("http://127.0.0.1:8080/") {
            header("Accept", "application/json")
        }

        var users = Json.decodeFromString<List<User>>(usersHTTP.bodyAsText())
        users.forEach {
            if (it.username == username) {
                user = it
            }
        }
        return user
    }

    suspend fun authUser(user: String, pass: String): String? {

        val response: HttpResponse = client.post("http://127.0.0.1:8080/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(User(username = user, password = pass))
        }
        if (response.status == HttpStatusCode.OK) {
            val token = Json.decodeFromString<Token>(response.bodyAsText())
            return token.token
        } else {
            return null
        }
    }


    suspend fun getUserNameById(id: String): String {
        val user = getUserById(userId = id)
        return user.username
    }


    suspend fun existUser(username: String): Boolean {
        val usersHTTP = client.get("http://127.0.0.1:8080/") {
            header("Accept", "application/json")
        }
        var users = Json.decodeFromString<List<User>>(usersHTTP.bodyAsText())
        users.forEach {
            if (it.username == username) {
                return true
            }
        }
        return false
    }

    suspend fun addUser(username: String, password: String): HttpStatusCode {
        val response = client.post("http://127.0.0.1:8080/users") {
            contentType(ContentType.Application.Json)
            setBody(User(username = username, password = password))
        }
        return response.status
    }

    suspend fun getUserById(userId: String): User {
        val user = User()
        val response: HttpResponse = client.get("http://127.0.0.1:8080/users/$userId") {
            header("Accept", "application/json")
        }
        var json = JsonParser.parseString(response.bodyAsText()).asJsonObject
        user.id = json.asJsonObject["id"].asString
        user.username = json.asJsonObject["username"].asString
        user.password = json.asJsonObject["password"].asString
        user.name = json.asJsonObject["name"].asString
        user.role = Rol.valueOf(json.asJsonObject["role"].asString)
        return user
    }
}