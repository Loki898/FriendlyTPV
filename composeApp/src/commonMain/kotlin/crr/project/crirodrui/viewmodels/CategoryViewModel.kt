package crr.cliente.crirodrui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import crr.cliente.crirodrui.repositorios.CategoryRepository
import crr.project.crirodrui.elements.Category
import crr.project.crirodrui.elements.Producto
import crr.project.crirodrui.elements.Rol
import crr.project.crirodrui.elements.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File


class CategoryViewModel(private val categoryRepository: CategoryRepository) : ViewModel() {
    var selected = MutableStateFlow(Category())
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
        selected.value = Category()
        refreshProducts()
    }

    fun setSelected(category: Category) {
        //selected.update { it.copy(user = user) }
        selected.value = category
        refreshProducts()
    }

    fun addCategory(nombre:String) {
        viewModelScope.launch {
            categoryRepository.addCategory(nombre)
            refreshCategories()
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            categoryRepository.deleteCategory(category)
            unSelect()
            refreshCategories()
        }
    }

    fun setSelectedByName(name:String) {
        categories.value.firstOrNull{
            it.nombre == name
        }?.let {
            setSelected(
                it
            )
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
    private fun refreshProducts(){
        viewModelScope.launch {
            products.value=categoryRepository.getProducts(selected.value.id_category)
        }
    }
    private fun refreshCategories(){
        viewModelScope.launch {
            categories.value=categoryRepository.getCategories()
            refreshProducts()
        }
    }
}