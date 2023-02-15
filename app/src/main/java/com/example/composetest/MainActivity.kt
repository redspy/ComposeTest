package com.example.composetest

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composetest.ui.theme.ComposeTestTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LocationHelper().startListeningUserLocation(this , object : LocationHelper.MyLocationListener {
            override fun onLocationChanged(location: Location) {
                // Here you got user location :)
                var locationText = "" + location.latitude + "," + location.longitude
                Log.d("Location","" + location.latitude + "," + location.longitude)
                Toast.makeText(this@MainActivity, locationText, Toast.LENGTH_LONG).show()
            }
        })

        var lastLocation = LocationHelper().getLastLocation(this)
        var lostLocationTest = "" + lastLocation?.latitude + "," + lastLocation?.longitude
        Toast.makeText(this@MainActivity, lostLocationTest, Toast.LENGTH_LONG).show()


        setContent {
            ComposeTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column {
                        //TestButton("Bad")
                        //Greeting("Android")
                        CardCompose()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CardCompose() {
    Card {
        Column {
            Row(modifier = Modifier.padding(all = 8.dp)) {
                Column(verticalArrangement = Arrangement.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.pocket_mon_picachu),
                        contentDescription = stringResource(id = R.string.layout_text_with_picture_text)
                    )
                }
            Spacer(modifier = Modifier.size(15.dp))

            Surface(shape = MaterialTheme.shapes.medium, elevation = 1.dp) {
                Text(
                    text = "HOT",
                    modifier = Modifier.padding(all = 4.dp),
                    style = MaterialTheme.typography.body2
                )
            }

            Column {
                Text("피카츄", fontSize = 30.sp, color = Color.Black, textAlign = TextAlign.Center)
                Text(stringResource(id = R.string.pocket_mon_picachu_description), textAlign = TextAlign.Left)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    var changeableName = name
    Text(text = "Hello!! $changeableName!")
}
@Composable
fun TestButton(name: String) {
    var changeableName = name
    Button(
        onClick = {
            changeableName = "Good!!"
        },

        ) {
        Text(text = changeableName)
    }

}

//@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeTestTheme {
        Greeting("Android")
    }
}