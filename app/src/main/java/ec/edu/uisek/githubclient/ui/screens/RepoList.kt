package ec.edu.uisek.githubclient.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ec.edu.uisek.githubclient.models.Repository
import ec.edu.uisek.githubclient.ui.components.RepoItem
import ec.edu.uisek.githubclient.ui.viewModels.RepoListViewModel

@Composable
fun RepoList(
    modifier: Modifier = Modifier,
    viewModel: RepoListViewModel = viewModel(),
    onNavigateToForm: (ec.edu.uisek.githubclient.models.Repository?) -> Unit = {}
){
    val repos by viewModel.repos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMsg by viewModel.errorMsg.collectAsState()

    var repoToDelete by remember { mutableStateOf<Repository?>(null) }

    if (repoToDelete != null) {
        AlertDialog(
            onDismissRequest = { repoToDelete = null },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar el repositorio '${repoToDelete?.name}'? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        repoToDelete?.let { repo ->
                            viewModel.deleteRepository(repo.owner.login, repo.name)
                        }
                        repoToDelete = null
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { repoToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold (
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigateToForm(null) },
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,

            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Añadir Repositorio"
                )
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            errorMsg?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                        .padding(16.dp)
                )
            }
            if (!isLoading && errorMsg == null) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(repos.size) { i ->
                        val repo = repos[i]
                        RepoItem(
                            repository = repo,
                            onEdit = { onNavigateToForm(repo) },
                            onDelete = { repoToDelete = repo }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun RepoListPreview(){
    RepoList()

}