package com.example.mybmi

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mybmi.ui.theme.Purple40
import com.example.mybmi.ui.theme.White
import kotlinx.coroutines.launch

@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.MainScreen.route){
        composable(Screen.MainScreen.route){
            MainScreen(navController = navController)
        }
        composable(
            route = "${Screen.Result.route}/{bmi}",
            arguments = listOf(
                navArgument("bmi") { type = NavType.FloatType }
            )

        ){entry ->
            val bmi = entry.arguments?.getFloat("bmi")
            Result(bmi = bmi?.toString())
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController){
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()

    var ageFieldState by remember {
        mutableStateOf("")
    }
    var heightFieldState by remember {
        mutableStateOf("")
    }
    var weightFieldState by remember {
        mutableStateOf("")
    }

    Scaffold(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(snackbarData = it)
            }
        },
        topBar = {
            Topbar(
                onReset = {
                    ageFieldState = ""
                    heightFieldState = ""
                    weightFieldState = ""
                }
            )
        }


    )

    {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
        ) {
            Spacer(modifier = Modifier.height(26.dp))
            TextField(
                value = ageFieldState,
                label = {
                    Text(text = "Age")
                },
                onValueChange = {
                    ageFieldState = it
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(26.dp))
            TextField(
                value = heightFieldState,
                label = {
                    Text(text = "Height (in cm)")
                },
                onValueChange = {
                    heightFieldState = it
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(26.dp))
            TextField(
                value = weightFieldState,
                label = {
                    Text(text = "Weight (in Kgs)")
                },
                onValueChange = {
                    weightFieldState = it
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(26.dp))
            ElevatedButton(onClick = {
                val weight = weightFieldState.toFloatOrNull() ?: 0f
                val height = heightFieldState.toFloatOrNull() ?: 0f
                if (weight > 0f && height > 0f){
                    val bmi = (weight/(height*height))*10000
//                    print("the bmi is $bmi")
//                    Log.d("MyTag", "BMI: $bmi")
                    navController.navigate(Screen.Result.route + "/$bmi")
                }

            }
            ) {
                Text(text = "Calculate")
            }
            Spacer(modifier = Modifier.height(50.dp))
            val painter = painterResource(id = R.drawable.bmi1)
            val description = "bmi"
            val title = "bmi"

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(16.dp)
                ){
                    ImageCard(painter = painter,
                        contentDescriptor = description,
                        title = title
                    )

                }

            }

        }
    }
}
@Composable
fun Result(bmi : String?) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(text = "RESULT:", fontWeight = FontWeight.Bold, fontSize = 26.sp)

        Spacer(modifier = Modifier.height(16.dp))
        Box(

            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){
            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Your BMI is:",
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "$bmi",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Topbar(onReset: () -> Unit) {
    val context = LocalContext.current.applicationContext
    TopAppBar(title = { Text(text = "My BMI") },

        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = White,
            titleContentColor = Purple40
        ),
        actions = {
            IconButton(onClick = onReset ) {
                Icon(imageVector = Icons.Filled.Refresh, contentDescription = "reset", tint = Purple40)
            }
        }

    )
}

@Composable
fun ImageCard(
    painter: Painter,
    contentDescriptor: String,
    title: String,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current.applicationContext
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Box(modifier = Modifier.height(200.dp)){
            Image(painter = painter, contentDescription = contentDescriptor,
                contentScale = ContentScale.FillBounds
            )

        }
    }
}