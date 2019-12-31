package com.example.githubClient

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubClient.arch.ExplicitLiveData
import com.example.githubClient.arch.LiveDataWrapper
import com.example.githubClient.arch.ResourceStatus
import com.example.githubClient.model.Repo
import com.example.githubClient.model.User
import com.example.githubClient.service.GitHubService
import com.example.githubClient.service.GithubApi
import io.reactivex.Observable
import java.lang.Exception

class MainActivityViewModel: ViewModel() {

    companion object {
        private val TAG = MainActivityViewModel::class.java.simpleName
    }

    // databinding livedata
    val userName = MutableLiveData<String?>("")

    // network response livedata
    val userModel = MutableLiveData<User?>(null)
    val repoModel = MutableLiveData<ArrayList<Repo>?>(null)
    val errorState = ExplicitLiveData<Boolean>()

    val dataStatus = MediatorLiveData<LiveDataWrapper<Unit, Exception>>().apply {
        addSource(userModel) {
            value = wasDataFetchSuccessful()
        }
        addSource(repoModel) {
            value = wasDataFetchSuccessful()
        }
        addSource(errorState){
            value = wasDataFetchSuccessful()
        }
    }

    private fun wasDataFetchSuccessful() : LiveDataWrapper<Unit, Exception> {
        return if (errorState.value == true) {
            Log.e(TAG, "LDW error")
            LiveDataWrapper(ResourceStatus.ERROR, null, null)
        }
        else if (userModel.value != null && repoModel.value != null) {
            Log.e(TAG, "LDW success")
            LiveDataWrapper(ResourceStatus.SUCCESS, null, null)
        } else {
            Log.e(TAG, "LDW loading")
            LiveDataWrapper(ResourceStatus.LOADING, null, null)
        }
    }

    val valid = MediatorLiveData<Boolean>().apply {
        addSource(userName) {
            value = checkFields()
        }
    }

    private fun checkFields(): Boolean {
        return !userName.value.isNullOrBlank()
    }

    fun fetchRepos(usernameInput: String): Observable<ArrayList<Repo>> {
        val service = GithubApi.getRetrofitInstance().create(GitHubService::class.java)
        return service.listRepos(usernameInput)
    }

    fun fetchUserInfo(usernameInput: String): Observable<User> {
        val service = GithubApi.getRetrofitInstance().create(GitHubService::class.java)
        return service.getUser(usernameInput)
    }
}