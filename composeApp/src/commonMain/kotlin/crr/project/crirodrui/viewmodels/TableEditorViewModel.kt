package crr.project.crirodrui.viewmodels

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import crr.project.crirodrui.app.Table
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.random.Random

class TableEditorViewModel : ViewModel() {

    var tables = MutableStateFlow(generateInitialTables())
    var selectedTable = MutableStateFlow<Table?>(null)
    var isEditMode = MutableStateFlow(false)
    var canvasSize = MutableStateFlow(IntSize(0, 0))

    fun toggleEditMode() {
        isEditMode.update { !it }
    }

    fun addTable(type: String) {
        val newTable = Table(
            id = Random.nextInt(),
            position = Offset(100f, 100f),
            name = type
        )
        tables.update { it + newTable }
    }

    fun updateTablePosition(id: Int, newPos: Offset) {
        tables.update { current ->
            current.map { if (it.id == id) it.copy(position = newPos) else it }
        }
    }

    fun deleteTable(id: Int) {
        tables.update { current -> current.filterNot { it.id == id } }
        if (selectedTable.value?.id == id) selectedTable.value = null
    }

    fun selectTable(table: Table?) {
        selectedTable.value = table
    }

    fun updateCanvasSize(size: IntSize) {
        canvasSize.value = size
    }

    fun saveTablesToFile(file: File) {
        viewModelScope.launch {
            try {
                val serializableList = tables.value.map {
                    SerializableTable(it.id, it.position.x, it.position.y, it.name)
                }
                val json = Json.encodeToString(serializableList)
                file.writeText(json)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadTablesFromFile(file: File) {
        viewModelScope.launch {
            if (!file.exists()) return@launch
            try {
                val json = file.readText()
                val list = Json.decodeFromString<List<SerializableTable>>(json)
                tables.value = list.map { Table(it.id, Offset(it.x, it.y), it.name) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

@Serializable
data class SerializableTable(
    val id: Int,
    val x: Float,
    val y: Float,
    val name: String
)

data class Table(
    val id: Int,
    val position: Offset,
    val name: String
)

fun generateInitialTables(): List<Table> = listOf(
    Table(id = 1, position = Offset(100f, 150f), name = "Mesa 1"),
    Table(id = 2, position = Offset(300f, 150f), name = "Mesa 2"),
    Table(id = 3, position = Offset(500f, 150f), name = "Mesa 3")
)