package crr.cliente.crirodrui.repositorios

import com.google.gson.JsonParser
import crr.project.crirodrui.elements.Category
import crr.project.crirodrui.elements.Producto
import crr.project.crirodrui.elements.Rol
import crr.project.crirodrui.elements.User
import io.github.vinceglb.filekit.core.PickerType
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.util.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

class CategoryRepository {
    val client = HttpClient {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
                serializeNulls()
            }
        }
    }

    suspend fun getCategories(): List<Category> {
        val categoriesHTTP = client.get("http://127.0.0.1:8080/categories") {
            header("Accept", "application/json")
        }
        var categories = Json.decodeFromString<List<Category>>(categoriesHTTP.bodyAsText())
        return categories
    }

    suspend fun getProducts(id_categoria:Int):List<Producto>{
        val productosHTTP= client.get("http://127.0.0.1:8080/products/category/$id_categoria") {
            header("Accept", "application/json")
        }
        if(productosHTTP.bodyAsText().isNotEmpty() ){
            var productos = Json.decodeFromString<List<Producto>>(productosHTTP.bodyAsText())
            return productos
        }else{
            return listOf()
        }
    }

    suspend fun addCategory(nombre:String): HttpStatusCode {
        val token = File("token_actual.txt").readText()
        val response = client.post("http://127.0.0.1:8080/categories") {
            contentType(ContentType.Application.Json)
            setBody(Category(nombre = nombre))
            bearerAuth(token)
        }
        return response.status
    }


}