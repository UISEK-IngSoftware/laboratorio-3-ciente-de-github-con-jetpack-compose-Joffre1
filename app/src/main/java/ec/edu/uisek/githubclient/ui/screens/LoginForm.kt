import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ec.edu.uisek.githubclient.services.AuthService
import ec.edu.uisek.githubclient.services.RetrofitClient
import ec.edu.uisek.githubclient.ui.theme.GithubClientTheme

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch

@Composable
fun LoginForm(
    onLoginSuccess:() -> Unit = {}
){

    val context = LocalContext.current
    val authService = remember { AuthService(context) }
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var token by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = "Ingreso a cliente GitHub",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = token,
            onValueChange = { token = it },
            label = { Text("Token de GitHub") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            enabled = !isLoading
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch {
                    isLoading = true
                    errorMessage = null
                    authService.saveAuth(username, token)

                    try {
                        val response = RetrofitClient.apiService.validateToken()
                        if (response.isSuccessful) {
                            val githubUser = response.body()
                            // Validamos que el token pertenezca al usuario ingresado
                            if (githubUser?.login?.equals(username.trim(), ignoreCase = true) == true) {
                                onLoginSuccess()
                            } else {
                                authService.logout()
                                errorMessage = "El token pertenece a '${githubUser?.login}', no a '$username'"
                            }
                        } else {
                            authService.logout()
                            errorMessage = "Token inválido o expirado (Error 401)"
                        }
                    } catch (e: Exception) {
                        authService.logout()
                        errorMessage = "Error: ${e.localizedMessage ?: "Fallo de conexión"}"
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = username.isNotBlank() && token.isNotBlank() && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text(text = "Iniciar Sesión")
            }
        }

    }
}

@Preview (showBackground = true)
@Composable
fun LoginPreview(){
    GithubClientTheme {
        LoginForm()
    }
}