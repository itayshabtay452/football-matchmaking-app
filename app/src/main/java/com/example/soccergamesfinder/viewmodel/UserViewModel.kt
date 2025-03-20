package com.example.soccergamesfinder.viewmodel


import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccergamesfinder.data.User
import com.example.soccergamesfinder.repository.UserRepository
import com.example.soccergamesfinder.utils.UserValidator
import com.example.soccergamesfinder.utils.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    private val _userExists = MutableStateFlow<Boolean?>(null)
    val userExists: StateFlow<Boolean?> get() = _userExists

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> get() = _userId

    private val _imageValidation = MutableStateFlow<ValidationResult>(ValidationResult.Success)
    val imageValidation: StateFlow<ValidationResult>  get() = _imageValidation

    private val _nameValidation = MutableStateFlow<ValidationResult>(ValidationResult.Success)
    val nameValidation: StateFlow<ValidationResult> get() = _nameValidation

    private val _nicknameValidation = MutableStateFlow<ValidationResult>(ValidationResult.Success)
    val nicknameValidation: StateFlow<ValidationResult> get() = _nicknameValidation

    private val _ageValidation = MutableStateFlow<ValidationResult>(ValidationResult.Success)
    val ageValidation: StateFlow<ValidationResult> get() = _ageValidation

    private val _locationValidation = MutableStateFlow<ValidationResult>(ValidationResult.Success)
    val locationValidation: StateFlow<ValidationResult> get() = _locationValidation


    fun loadUser() {
        viewModelScope.launch {
            _user.value = userRepository.getUser()
            _userId.value = getId()
        }
    }

    fun checkIfUserExists() {
        viewModelScope.launch {
            _userExists.value = userRepository.hasUserData()
        }
    }

    fun validateAndSaveUser(name: String, nickname: String, age: Int?, latitude: Double?,
                            longitude: Double?, imageUri: Uri?, imageSize: Long?){

        _nameValidation.value = UserValidator.validateName(name)
        _nicknameValidation.value = UserValidator.validateNickname(nickname)
        _ageValidation.value = UserValidator.validateAge(age)
        _locationValidation.value = UserValidator.validateLocation(latitude, longitude)
        _imageValidation.value = UserValidator.validateImageData(imageUri, imageSize)

        val allValid = listOf(_nameValidation, _nicknameValidation, _ageValidation,
            _locationValidation, _imageValidation).all { it.value is ValidationResult.Success }

        if (allValid) {
            viewModelScope.launch {
                userRepository.createUser(User(age ?: 0, name, nickname, latitude, longitude),
                    imageUri)
                _user.value = userRepository.getUser()

            }
        }
    }

    fun logout() {
        _userExists.value = null
        _user.value = null
    }

    private fun getId(): String? {
        return userRepository.getUserId()
    }

    fun getImageSize(imageUri: Uri): Long? {
        return try {
            context.contentResolver.openAssetFileDescriptor(imageUri, "r")?.use { descriptor ->
                descriptor.length
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}