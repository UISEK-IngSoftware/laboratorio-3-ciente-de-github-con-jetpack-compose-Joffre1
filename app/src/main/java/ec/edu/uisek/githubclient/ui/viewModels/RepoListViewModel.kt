package ec.edu.uisek.githubclient.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import ec.edu.uisek.githubclient.models.Repository
import ec.edu.uisek.githubclientcompose.services.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RepoListViewModel: ViewModel(){
    //Maneja el estado de la lista de repositorios
    private val _repos = MutableStateFlow<List<Repository>>(emptyList())
    val repos : StateFlow<List<Repository>> = _repos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMsg = MutableStateFlow<String?>(null)
    val errorMsg: StateFlow<String?> = _errorMsg.asStateFlow()

    init{
        fetchRepos()
    }

    fun fetchRepos(){
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null
            try {
                val response = RetrofitClient.apiService.getRepository()
                _repos.value = response
            } catch (e: Exception){
                _errorMsg.value = "Error al cargar los respositorios: ${e.localizedMessage}"
                e.printStackTrace()
            }finally {
                _isLoading.value = false
            }
        }
    }
}

