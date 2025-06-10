package crr.cliente.crirodrui.viewmodels

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import crr.cliente.crirodrui.repositorios.CategoryRepository
import crr.cliente.crirodrui.repositorios.ProductRepository
import crr.project.crirodrui.elements.Category
import crr.project.crirodrui.elements.Producto
import crr.project.crirodrui.elements.Rol
import crr.project.crirodrui.elements.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import java.io.File


class ProductViewModel(private val categoryRepository: CategoryRepository, val productRepository: ProductRepository) : ViewModel() {

    var actual_category= MutableStateFlow(Category())
    var selected = MutableStateFlow(Producto())
    var status = MutableStateFlow(0)
    var categories = MutableStateFlow(listOf<Category>())
    var products= MutableStateFlow(listOf<Producto>())

    init {
        viewModelScope.launch {
            categories.value = categoryRepository.getCategories()

        }
    }
    fun getProductById(id: Int): Producto? {
        return products.value.find { it.id_producto == id }
    }

    fun unSelect() {
        //selected.update { it.copy(user = User()) }
        selected.value = Producto()
    }

    fun setCategory(category: Category) {
        actual_category.value = category
        refreshProducts()
    }
    fun setSelected(product: Producto) {
        //selected.update { it.copy(user = user) }
        selected.value = product
    }

    fun addProduct(nombre:String,descripcion:String, precio: Double, stock:Int, tipo_iva:Int, categoria:Int) {
        viewModelScope.launch {
            categoryRepository.addCategory(nombre)
            refreshCategories()
        }
    }

    fun setProducts(productsNew:List<Producto>){
        products.value=productsNew
    }

    fun getProductByName(name:String):Producto? {
        return products.value.firstOrNull { it.nombre?.contains(name) == true }
    }

    fun reloadStatus(){
        status.value = 0
    }
    fun errorPasswords(){
        status.value = -1
    }

    private fun refreshUsers(){
        viewModelScope.launch {
            categories.value = categoryRepository.getCategories()
        }

    }

    private fun refreshCategories(){
        viewModelScope.launch {
            categories.value=categoryRepository.getCategories()
        }
    }

    private fun refreshProducts(){
        viewModelScope.launch {
            products.update { productRepository.getProducts(actual_category.value.id_category) }
        }
    }
}