package ec.edu.uisek.githubclient.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ec.edu.uisek.githubclient.ui.components.RepoItem
import ec.edu.uisek.githubclient.ui.theme.GithubClientTheme

@Composable
fun RepoList(){
    Column (
        modifier = Modifier
            .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),

    ){
        RepoItem(
            "https://avatars.githubusercontent.com/u/216461812?v=4",
            "Repositorio de Andorid",
            "Este es un repositorio construido en Kotlin con Jetpack Compose",
            "Kotlin",
    )
        RepoItem(
            "https://avatars.githubusercontent.com/u/216461812?v=4",
            "Repositorio de DJango",
            "Este es un repositorio construido en Kotlin con Jetpack Compose",
            "Kotlin"
        )
        RepoItem(
            "https://avatars.githubusercontent.com/u/216461812?v=4",
            "Repositorio de Andorid",
            "Este es un repositorio construido en Kotlin con Jetpack Compose",
            "Kotlin"
        )

    }
}

@Preview(showBackground = true)
@Composable
fun RepoItemPreview () {
    GithubClientTheme {
        RepoList()
    }
}