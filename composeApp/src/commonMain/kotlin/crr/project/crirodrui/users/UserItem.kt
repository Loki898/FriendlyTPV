package org.example.damgramclient.ui.posts

//import androidx.compose.ui.graphics.toPainter
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toPainter
import androidx.compose.ui.unit.dp
import crr.cliente.crirodrui.repositorios.UserRepository
import crr.cliente.crirodrui.viewmodels.UserViewModel
import crr.project.crirodrui.elements.User
import org.koin.compose.viewmodel.koinViewModel
import java.io.ByteArrayInputStream
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.imageio.ImageIO
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
@Composable
fun UserItem(
    item: User,
    vm: UserViewModel = koinViewModel(),
    vmUser: UserViewModel = koinViewModel(),
    userRep: UserRepository = UserRepository()
) {

    Box(
        modifier =Modifier.size(150.dp, 50.dp).border(2.dp, MaterialTheme.colorScheme.primary, RectangleShape).background(Color.LightGray).clickable {
            vm.selected.value.user = item
        },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(all = 10.dp)
        ) {
            Text("User : ${item.username}")
        }
        //val imagenString = item.image

        /*Column(modifier = Modifier.wrapContentSize()) {
    if (imagenString != null && imagenString.isNotBlank()) {
        val imagenB64 = Base64.decode(imagenString.toByteArray())
        val bitmap = ImageIO.read(ByteArrayInputStream(imagenB64)).toPainter()
        Image(
            bitmap, contentDescription = "Imagen del post", modifier = Modifier.size(250.dp, 200.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }*/
    }
Spacer(modifier = Modifier.height(8.dp))
}


fun timestampToDate(timestamp: Long): String {
val instant = Instant.ofEpochSecond(timestamp / 1000)
val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(ZoneId.systemDefault())
return formatter.format(instant)
}