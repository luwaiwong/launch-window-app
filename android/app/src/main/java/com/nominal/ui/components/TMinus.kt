package com.nominal.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nominal.ui.theme.Foreground
import com.nominal.ui.theme.SubForeground
import com.nominal.utils.DateUtils
import kotlinx.coroutines.delay

@Composable
fun TMinus(
    launchDate: String,
    modifier: Modifier = Modifier,
    large: Boolean = false
) {
    var countdown by remember { mutableStateOf(DateUtils.getCountdown(launchDate)) }

    LaunchedEffect(launchDate) {
        while (true) {
            countdown = DateUtils.getCountdown(launchDate)
            delay(1000)
        }
    }

    val prefix = if (countdown.isPast) "T+" else "T-"
    val fontSize = if (large) 48.sp else 32.sp

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = prefix,
            style = MaterialTheme.typography.labelLarge,
            color = SubForeground,
            fontSize = if (large) 16.sp else 12.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TimeUnit(value = countdown.days, unit = "D", fontSize = fontSize)
            Text(
                text = ":",
                style = MaterialTheme.typography.displayLarge,
                color = Foreground,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            TimeUnit(value = countdown.hours, unit = "H", fontSize = fontSize)
            Text(
                text = ":",
                style = MaterialTheme.typography.displayLarge,
                color = Foreground,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            TimeUnit(value = countdown.minutes, unit = "M", fontSize = fontSize)
            Text(
                text = ":",
                style = MaterialTheme.typography.displayLarge,
                color = Foreground,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            TimeUnit(value = countdown.seconds, unit = "S", fontSize = fontSize)
        }
    }
}

@Composable
private fun TimeUnit(
    value: Long,
    unit: String,
    fontSize: androidx.compose.ui.unit.TextUnit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = String.format("%02d", value),
            style = MaterialTheme.typography.displayLarge,
            color = Foreground,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}
