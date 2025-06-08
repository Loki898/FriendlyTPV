package crr.cliente.crirodrui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import crr.cliente.crirodrui.repositorios.CategoryRepository
import crr.project.crirodrui.elements.Category
import crr.project.crirodrui.elements.Producto
import crr.project.crirodrui.elements.Rol
import crr.project.crirodrui.elements.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File


class ProductViewModel(private val categoryRepository: CategoryRepository) : ViewModel() {
    var selected = MutableStateFlow(Producto())
    var status = MutableStateFlow(0)
    var categories = MutableStateFlow(listOf<Category>())
    var products= MutableStateFlow(listOf<Producto>())
    var userSelected = MutableStateFlow(User())

    init {
        viewModelScope.launch {
            categories.value = categoryRepository.getCategories()


        }
    }

    fun unSelect() {
        //selected.update { it.copy(user = User()) }
        selected.value = Producto()
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
}