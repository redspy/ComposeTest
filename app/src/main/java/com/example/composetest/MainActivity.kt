package com.example.composetest

import android.content.ContentValues.TAG
import android.content.Context
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
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    /** DynamicLink */
    private fun makeToast(text: String) {
        Toast.makeText(this@MainActivity, text, Toast.LENGTH_LONG).show()
    }
    private fun initDynamicLink() {
        val dynamicLinkData = intent.extras
        if (dynamicLinkData != null) {
            var dataStr = "DynamicLink 수신받은 값\n"
            for (key in dynamicLinkData.keySet()) {
                dataStr += "key: $key / value: ${dynamicLinkData.getString(key)}\n"
            }
            Toast.makeText(this@MainActivity, dataStr, Toast.LENGTH_LONG).show()
            // binding.tvToken.text = dataStr
        }
    }
    /*
    BJGlwc59TEw024WP7t9Mc0BsQxEeUvdpQZlGOYJDKeQD4_g3U9r6KHryguu5C-foJiv1QX9AYs7O-2uHCxCxAqc
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CheckLocation()

        InitializeFirebase()

        //SendNotification()

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
                        Card {
                            Button(onClick = {
                                SendNotification()
                            }) {

                            }
                        }
                    }
                }
            }
        }
    }

    private fun SendNotification() {
        val title = "Let's Go!"
        val message = "Together"
        val recipientToken =
            "eVh8cmA6TKyluftyVWOJ_j:APA91bFm2sUTNggIkmNw8ONpiQUbIic9F2EliKP9xa-4bjdHNLe0Ud4dxQxQXwZvxR9tZsynR_bW6JE8UfuSu00DTciGP1UtVjkGphUC3x1-jL9RRWKCcwLTrTxpUwdMdjEXFIRBR8Jd"
        if (title.isNotEmpty() && message.isNotEmpty() && recipientToken.isNotEmpty()) {
            PushNotification(
                NotificationData(title, message),
                recipientToken
            ).also {
                sendNotification(it)
            }
        }
    }

    private fun InitializeFirebase() {
        /** FCM설정, Token값 가져오기 */
        MyFirebaseMessagingService().getFirebaseToken()
        /** DynamicLink 수신확인 */
        initDynamicLink()
        val TOPIC = "/topics/myTopic2"
        MyFirebaseMessagingService.sharedPref = getSharedPreferences("sharedPref", MODE_PRIVATE)
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
    }

    private fun CheckLocation() {
        LocationHelper().startListeningUserLocation(
            this,
            object : LocationHelper.MyLocationListener {
                override fun onLocationChanged(location: Location) {
                    // Here you got user location :)
                    var locationText = "" + location.latitude + "," + location.longitude
                    Log.d("Location", "" + location.latitude + "," + location.longitude)
                    Toast.makeText(this@MainActivity, locationText, Toast.LENGTH_LONG).show()
                }
            })

        var lastLocation = LocationHelper().getLastLocation(this)
        var lostLocationTest = "" + lastLocation?.latitude + "," + lastLocation?.longitude
        Toast.makeText(this@MainActivity, lostLocationTest, Toast.LENGTH_LONG).show()
    }

    fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            } else {
                Log.e(TAG, response.errorBody().toString())
                makeToast(response.errorBody().toString())
            }
        } catch(e: Exception) {
            Log.e(TAG, e.toString())
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