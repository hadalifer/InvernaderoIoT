package com.example.invernaderoiot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.invernaderoiot.ui.theme.InvernaderoIoTTheme

// Colores personalizados
private val BackgroundGreen = Color(0xFF304C42)   // fondo
private val CardBackground = Color(0xF5F5F7F6)    // cards claritas
private val TitleGreen = Color(0xFF1E3B32)        // títulos de las cards
private val BodyGray = Color(0xFF4A4A4A)          // texto normal

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private val viewModel: EstadoPlantaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InvernaderoIoTTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundGreen   // fondo verde desde la raíz
                ) {
                    EstadoPlantaScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstadoPlantaScreen(viewModel: EstadoPlantaViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = BackgroundGreen,
        topBar = {} // <-- Quitamos el TopAppBar por completo
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(BackgroundGreen)
        ) {
            when (uiState) {
                is EstadoPlantaUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White
                    )
                }

                is EstadoPlantaUiState.Error -> {
                    val msg = (uiState as EstadoPlantaUiState.Error).message
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Ocurrió un error 😢", color = Color.White)
                        Spacer(Modifier.height(8.dp))
                        Text(msg, color = Color.White)
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.cargarEstado() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = TitleGreen
                            ),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text("Reintentar")
                        }
                    }
                }

                is EstadoPlantaUiState.Success -> {
                    val data = (uiState as EstadoPlantaUiState.Success).data
                    EstadoPlantaContent(
                        data = data,
                        onRefresh = { viewModel.cargarEstado() }
                    )
                }
            }
        }
    }
}
@Composable
fun EstadoPlantaContent(
    data: EstadoPlantaResponse,
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGreen)
            .verticalScroll(rememberScrollState())
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Mini Invernadero",
            modifier = Modifier
                .size(250.dp)
                .padding(all = 0.dp)
        )

        Text(
            text = "Estado de tu plantita 💚",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Text(
            text = "Última actualización:\n${formatTimestamp(data.timestamp)}",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFE0EAE6),
            fontWeight = FontWeight.Bold
        )

        // 👉 Aquí van tus cards, igual que las tenías
        // (no las cambio para no mover nada que ya está funcionando)

        // --- TARJETA HUMEDAD ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = CardBackground
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Humedad", color = TitleGreen, fontWeight = FontWeight.SemiBold)
                Text("Humedad actual: ${"%.1f".format(data.humedad)} %", color = BodyGray)
                Text("Estado: ${data.estadoHumedad}", fontWeight = FontWeight.SemiBold, color = TitleGreen)
                Text(data.mensajeHumedad, color = BodyGray)
            }
        }

        // --- TARJETA LUZ ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Luz", color = TitleGreen, fontWeight = FontWeight.SemiBold)
                Text(
                    if (data.hayLuz) "Actualmente tiene luz ☀️"
                    else "Actualmente está sin luz 🌑",
                    color = BodyGray
                )
                Text("Estado: ${data.estadoLuz}", fontWeight = FontWeight.SemiBold, color = TitleGreen)
                Text(data.mensajeLuz, color = BodyGray)
            }
        }

        // --- TARJETA RESUMEN EMOCIONAL ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Resumen emocional 🌿", color = TitleGreen, fontWeight = FontWeight.SemiBold)
                Text(
                    when (data.estadoHumedad) {
                        "CRÍTICA" -> "Tu planta está sufriendo mucho, necesita tu atención YA. 💔 Gracias por cuidar tanto de tu plantita."
                        "BAJA" -> "Tu planta empieza a tener sed, un riego pronto la haría muy feliz. 💧"
                        "IDEAL" -> "Tu plantita está feliz, gracias por cuidar tanto de tu plantita. 🥹💚"
                        "MEDIA" -> "Tu plantita tiene mucha humedad, también la cuidas cuando evitas encharcarla. ☔"
                        else -> "Tu plantita está estable. 💚"
                    },
                    color = BodyGray
                )
            }
        }

        // BOTÓN
        Button(
            onClick = onRefresh,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = TitleGreen
            ),
            shape = RoundedCornerShape(50)
        ) {
            Text("Actualizar estado")
        }
    }
}



