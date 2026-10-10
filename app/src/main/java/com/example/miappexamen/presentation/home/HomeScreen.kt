package com.example.miappexamen.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.miappexamen.data.model.CountryModel

private val mockCountries = listOf(
    CountryModel(
        "México",
        41,
        "https://flagcdn.com/w320/mx.png"
    ),
    CountryModel(
        "Perú",
        51,
        "https://flagcdn.com/w320/pe.png"
    ),
    CountryModel(
        "Argentina",
        2,
        "https://flagcdn.com/w320/ar.png"
    ),
    CountryModel(
        "Portugal",
        8,
        "https://flagcdn.com/w320/pt.png"
    ),
    CountryModel(
        "Brasil",
        6,
        "https://flagcdn.com/w320/br.png"
    ),
    CountryModel(
        "España",
        1,
        "https://flagcdn.com/w320/es.png"
    )
)

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Ranking de países",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Datos de ejemplo",
            style = MaterialTheme.typography.bodySmall
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = mockCountries,
                key = { country -> country.name }
            ) { country ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                country.imageUrl
                            ),
                            contentDescription = "Bandera de ${country.name}",
                            modifier = Modifier.size(60.dp),
                            contentScale = ContentScale.Fit
                        )

                        Column {
                            Text(
                                text = country.name,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = "Posición: #${country.ranking}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}