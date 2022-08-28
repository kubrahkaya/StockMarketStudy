package com.innovaocean.stockmarketstudy.presentation.companyListings

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.innovaocean.core.theme.StockMarketAppTheme

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    onTryAgainButtonClick: () -> Unit
) {
    Surface(modifier = modifier){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = painterResource(id = com.google.android.material.R.drawable.ic_clock_black_24dp),
                contentDescription = "No internet"
            )
            Text(
                text = "No internet",
                style = typography.body1,
                color = Color.DarkGray,
                modifier = Modifier.padding(top = 16.dp)
            )
            Button(
                onClick = onTryAgainButtonClick,
                modifier = Modifier
                    .width(250.dp)
                    .padding(top = 16.dp)
                    .background(Color.DarkGray, RoundedCornerShape(4.dp))
            ) {
                Text(
                    text = "Try again",
                    style = typography.button,
                    color = Color.White
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ErrorScreenPreview() {
    StockMarketAppTheme {
        ErrorScreen {}
    }
}