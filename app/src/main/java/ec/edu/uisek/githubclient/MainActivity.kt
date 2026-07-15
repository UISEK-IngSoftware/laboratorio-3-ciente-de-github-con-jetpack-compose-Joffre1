package ec.edu.uisek.githubclient

import LoginForm
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import ec.edu.uisek.githubclient.models.Repository
import ec.edu.uisek.githubclient.services.AuthService
import ec.edu.uisek.githubclient.ui.screens.RepoForm
import ec.edu.uisek.githubclient.ui.screens.RepoList
import ec.edu.uisek.githubclient.ui.theme.GithubClientTheme

import ec.edu.uisek.githubclient.ui.viewModels.RepoListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authService= AuthService(this)

        setContent {
            GithubClientTheme {
                val listViewModel: RepoListViewModel= viewModel ()
                var currentScreen by remember { mutableStateOf(
                    if (authService.isLoggedIn()) "repoList" else "login") }
                var selectedRepo by remember { mutableStateOf<Repository?>(null) }

                when (currentScreen) {
                    "login" -> LoginForm (
                        onLoginSuccess = {currentScreen = "repoList"}

                    )
                    "repoList" -> RepoList (
                        onNavigateToForm = {
                            currentScreen = "repoForm"
                        },
                        onLogout = {
                            authService.logout()
                            currentScreen = "login"
                        }
                    )
                    "repoForm" -> RepoForm(
                        repoToEdit = selectedRepo,
                        onSaveSuccess = {
                            listViewModel.fetchRepos()
                            currentScreen = "repoList"
                        },
                        onBackClick = { currentScreen = "repoList" }
                    )
                }
            }
        }
    }
}
