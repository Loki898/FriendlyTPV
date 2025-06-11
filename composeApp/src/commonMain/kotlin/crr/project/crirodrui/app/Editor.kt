package crr.project.crirodrui.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import crr.project.crirodrui.viewmodels.TableEditorViewModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.koinViewModel
import java.io.File
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun TableEditorScreen(viewModel: TableEditorViewModel = koinViewModel()) {
    val tables = viewModel.tables.collectAsState()
    val selectedTable = viewModel.selectedTable.collectAsState()
    val isEditMode = viewModel.isEditMode.collectAsState()
    val canvasSize = viewModel.canvasSize.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { viewModel.toggleEditMode() }) {
                Text(if (isEditMode.value) "Salir del modo edición" else "Modo edición")
            }
            Button(onClick = {
                val file = File("layout.json")
                viewModel.saveTablesToFile(file)
            }) {
                Text("Guardar")
            }
            Button(onClick = {
                val file = File("layout.json")
                viewModel.loadTablesFromFile(file)
            }) {
                Text("Cargar")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (isEditMode.value) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Mesa Cuadrada", "Mesa Redonda").forEach { type ->
                    Button(onClick = { viewModel.addTable(type) }) {
                        Text(type)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF))
            .onGloballyPositioned { coordinates ->
                viewModel.updateCanvasSize(coordinates.size)
            }
        ) {
            tables.value.forEach { table ->
                DraggableTable(
                    table = table,
                    isEditMode = isEditMode.value,
                    onClick = { viewModel.selectTable(table) },
                    onDragEnd = { newPos -> viewModel.updateTablePosition(table.id, newPos) },
                    onDelete = { viewModel.deleteTable(table.id) },
                    maxWidth = canvasSize.value.width.toFloat(),
                    maxHeight = canvasSize.value.height.toFloat()
                )
            }
        }
    }
}


@Composable
fun DraggableTable(
    table: Table,
    isEditMode: Boolean,
    onClick: () -> Unit,
    onDragEnd: (Offset) -> Unit,
    onDelete: () -> Unit,
    maxWidth: Float,
    maxHeight: Float
) {
    var offset by remember { mutableStateOf(table.position) }

    LaunchedEffect(table) {
        offset = table.position
    }

    val density = LocalDensity.current
    val tableSizePx = with(density) { 80.dp.toPx() }

    Box(
        modifier = Modifier
            .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
            .size(80.dp)
            .background(Color.LightGray, RoundedCornerShape(8.dp))
            .pointerInput(isEditMode) {
                if (isEditMode) {
                    detectDragGestures(
                        onDragEnd = { onDragEnd(offset) },
                        onDrag = { _, dragAmount ->
                            val newX = (offset.x + dragAmount.x).coerceIn(0f, maxWidth - tableSizePx)
                            val newY = (offset.y + dragAmount.y).coerceIn(0f, maxHeight - tableSizePx)
                            offset = Offset(newX, newY)
                        }
                    )
                }
            }
            .clickable(enabled = !isEditMode) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(table.name, textAlign = TextAlign.Center)
            if (isEditMode) {
                Spacer(modifier = Modifier.height(4.dp))
                Button(onClick = onDelete, modifier = Modifier.height(24.dp)) {
                    Text("Eliminar", fontSize = MaterialTheme.typography.labelSmall.fontSize)
                }
            }
        }
    }
}


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

@Serializable
data class SerializableTable(
    val id: Int,
    val x: Float,
    val y: Float,
    val name: String
)

fun List<Table>.toSerializable(): List<SerializableTable> = map {
    SerializableTable(it.id, it.position.x, it.position.y, it.name)
}

fun List<SerializableTable>.toTable(): List<Table> = map {
    Table(it.id, Offset(it.x, it.y), it.name)
}

fun saveTablesToFile(file: File, tables: List<Table>) {
    val json = Json.encodeToString(tables.toSerializable())
    file.writeText(json)
}

fun loadTablesFromFile(file: File): List<Table> {
    if (!file.exists()) {
        println("Archivo no existe: ${file.absolutePath}")
        return emptyList()
    }
    val json = file.readText()
    println("Contenido cargado: $json")
    return Json.decodeFromString<List<SerializableTable>>(json).toTable()
}