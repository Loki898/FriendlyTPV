package crr.project.crirodrui.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import crr.cliente.crirodrui.viewmodels.CategoryViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun tpv(){
    val categoryViewModel: CategoryViewModel = koinViewModel()
    val categorias by categoryViewModel.categories.collectAsState()
    val productos by categoryViewModel.products.collectAsState()
    categoryViewModel.unSelect()
    // Panel principal
    Column(modifier = Modifier.fillMaxSize().padding(8.dp).border(2.dp,Color.Cyan)) {

        // Parte superior: líneas detalle + total
        Row(Modifier.weight(1f).border(2.dp,Color.Cyan)) {
            // Líneas de detalle
            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
            ) {
                repeat(4) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(Color.LightGray)
                            .padding(8.dp)
                    ) {
                        Text("Línea detalle")
                    }
                }
            }

            // Total + botones
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight().border(2.dp,Color.Cyan),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text("Total", fontSize = 24.sp)
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .border(2.dp, Color.Black)
                        .padding(8.dp)
                ) {
                    Text("25.64€", fontSize = 20.sp)
                }

                Button(onClick = { }, modifier = Modifier.padding(4.dp)) {
                    Text("Terminar venta")
                }
                Button(onClick = { }, modifier = Modifier.padding(4.dp)) {
                    Text("Abrir caja")
                }
                Button(onClick = { }, modifier = Modifier.padding(4.dp)) {
                    Text("Cancelar pedido")
                }
            }
        }

        // Parte inferior: productos y categorías
        Row(Modifier.weight(1f).border(2.dp,Color.Cyan)) {
            // Productos
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier = Modifier
                    .weight(2f)
                    .padding(8.dp).border(2.dp,Color.Cyan)
            ) {
                items(productos.size) {
                    productos.get(it).nombre?.let { it1 -> ProductoBoton(it1) }
                }
            }

            // Categorías
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp).border(2.dp,Color.Cyan)
            ) {
                items(categorias.size) {
                    CategoriaBoton(categorias.get(it).nombre)
                }
            }
        }
    }
}
        @Composable
        fun IconWithText(label: String) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.Menu, contentDescription = label)
                Text(label, fontSize = 12.sp)
            }
        }

        @Composable
        fun ProductoBoton(nombre: String) {
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(80.dp)
                    .background(Color(0xFFFFCCBB), shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(nombre)
            }
        }

        @Composable
        fun CategoriaBoton(nombre: String) {
            val categoryViewModel: CategoryViewModel = koinViewModel()
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(80.dp)
                    .clickable {
                        categoryViewModel.setSelectedByName(nombre)
                    }
                    .background(Color(0xFF66DDEE), shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(nombre)
            }
        }
