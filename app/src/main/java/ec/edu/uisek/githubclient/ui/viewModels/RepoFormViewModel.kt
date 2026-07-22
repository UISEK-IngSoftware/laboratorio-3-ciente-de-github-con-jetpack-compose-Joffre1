package ec.edu.uisek.githubclient.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.uisek.githubclient.models.RepositoryPayload
import ec.edu.uisek.githubclient.services.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RepoFormViewModel: ViewModel() {
    private val apiService = RetrofitClient.apiService

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess.asStateFlow()
    private val _errorMsg = MutableStateFlow<String?>(null)
    val errorMsg: StateFlow<String?> = _errorMsg.asStateFlow()

    fun createRepository(name: String, description: String?){
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null
            try {
                val payload = RepositoryPayload(
                    name = name,
                    description = description
                )
                apiService.createRepository(payload)
                _isSuccess.value = true
            } catch (e: Exception){
                _errorMsg.value = "Error al crear el repositorio: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }

        }
    }

    fun updateRepository(owner: String, oldName: String, newName: String, description: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null
            try {
                val payload = RepositoryPayload(newName, description)
                apiService.updateRepository(owner, oldName, payload)
                _isSuccess.value = true
            } catch (e: retrofit2.HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                _errorMsg.value = "Error 422: Verifique que el nombre sea válido y único. Detalle: $errorBody"
            } catch (e: Exception) {
                _errorMsg.value = "Error al actualizar: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetSuccess (){
        _isSuccess.value = false
    }
}