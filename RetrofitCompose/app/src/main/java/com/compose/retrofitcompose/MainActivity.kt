package com.compose.retrofitcompose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.retrofitcompose.model.CryptoModel
import com.compose.retrofitcompose.service.CryptoAPI
import com.compose.retrofitcompose.ui.theme.RetrofitComposeTheme
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RetrofitComposeTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var cryptoModels = remember { mutableStateListOf<CryptoModel>() }
    val BASE_URL = "https://raw.githubusercontent.com/"
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CryptoAPI::class.java)
    val call = retrofit.getData()
    call.enqueue(object: Callback<List<CryptoModel>> {
        override fun onResponse(
            call: Call<List<CryptoModel>>,
            response: Response<List<CryptoModel>>
        ) {
            if(response.isSuccessful) {
                response.body()?.let {
                    cryptoModels.addAll(it)
                }
            }
        }

        override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
            t.printStackTrace()
        }
    })

    Scaffold(topBar = { AppBar() }) {
        CryptoList(cryptoList = cryptoModels)
    }
}

@Composable
fun AppBar() {
    TopAppBar(contentPadding = PaddingValues(10.dp)) {
        Text("CryptoMoneys App", fontWeight = FontWeight.Bold, fontSize = 20.sp)
    }
}

@Composable
fun CryptoList(cryptoList: List<CryptoModel>) {
    LazyColumn(contentPadding = PaddingValues(5.dp)) {
        items(cryptoList) { crypto ->
            CryptoRow(crypto = crypto)
        }
    }
}

@Composable
fun CryptoRow(crypto: CryptoModel) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable {
                Toast.makeText(
                    context,
                    "Clicked: ${crypto.currency} and current price is  ${crypto.price}",
                    Toast.LENGTH_SHORT
                ).show()
            },
        elevation = 10.dp,
        shape = RoundedCornerShape(10.dp),
    ) {
    Row(
        modifier = Modifier.size(width = 370.dp, height = 100.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        ) {
        Column(modifier = Modifier
            .background(color = MaterialTheme.colors.onError)
            .width(200.dp)
            ) {
            Text(text = crypto.currency,
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(2.dp),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = crypto.price,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(2.dp),
            )
        }
        Image(bitmap = ImageBitmap.
        imageResource(id = R.drawable.indir),
            contentDescription = "graphics",
            modifier = Modifier.requiredSize(150.dp)
            )
       }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RetrofitComposeTheme {
        CryptoRow(crypto = CryptoModel("BTC","46.786"))
    }
}