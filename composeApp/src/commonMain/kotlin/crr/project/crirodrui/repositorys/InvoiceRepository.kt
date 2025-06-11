package crr.cliente.crirodrui.repositorios

import com.google.gson.JsonParser
import crr.project.crirodrui.elements.*
import io.github.vinceglb.filekit.core.PickerType
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

class InvoiceRepository {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun getInvoices():List<Invoice>{
        val invoicesHTTP= client.get("http://127.0.0.1:8080/invoices") {
            header("Accept", "application/json")
        }
        if(invoicesHTTP.bodyAsText().isNotEmpty() ){
            var invoices = Json.decodeFromString<List<Invoice>>(invoicesHTTP.bodyAsText())
            println(invoices)
            return invoices
        }else{
            return listOf()
        }
    }

    suspend fun sendInvoice(invoice: Invoice): HttpStatusCode {
        val token = File("token_actual.txt").readText()
        val response = client.post("http://127.0.0.1:8080/invoices") {
            bearerAuth(token)
            setBody(invoice)
            contentType(ContentType.Application.Json)
        }
        return response.status
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
    suspend fun addProduct(nombre:String,descripcion:String, precio: Double, stock:Int, tipo_iva:Int, categoria:Int): HttpStatusCode {
        val token = File("token_actual.txt").readText()
        val response = client.post("http://127.0.0.1:8080/categories") {
            contentType(ContentType.Application.Json)
            setBody(Category(nombre = nombre))
            bearerAuth(token)
        }
        return response.status
    }



}