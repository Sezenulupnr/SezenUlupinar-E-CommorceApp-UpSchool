package com.example.s_shopping.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.s_shopping.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

    private var _accountState = MutableLiveData<AccountState>()
    val accountState: LiveData<AccountState> get() = _accountState

    fun logOut() = viewModelScope.launch {
        authRepository.logOut()
        _accountState.value = AccountState.GoToSignIn
    }
}

sealed interface AccountState {
    object Loading : AccountState
    object GoToSignIn : AccountState
}
