package com.example.githubClient

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.githubClient.arch.ResourceStatus
import com.example.githubClient.databinding.ActivityMainBinding
import com.example.githubClient.model.Repo
import com.example.githubClient.model.User
import com.example.githubClient.service.GitHubService
import com.example.githubClient.service.GithubApi
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.observers.ResourceObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private val viewModel by lazy {
        ViewModelProvider(this, MainActivityViewModelFactory()).get(MainActivityViewModel::class.java)
    }

    private val compositeDisposable : CompositeDisposable by lazy { CompositeDisposable() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setup databinding
        val binding : ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.mainActivityViewModel = viewModel

        initLiveDataObservers()

        repo_button.setOnClickListener {
                progressBar.visibility = VISIBLE
                handleBothRequests()
        }

    }

    override fun onPause() {
        progressBar.visibility = INVISIBLE
        super.onPause()
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    private fun initLiveDataObservers() {
        viewModel.dataStatus.observe(this, Observer {
            when(it.status) {
                ResourceStatus.SUCCESS -> {
                    val intent = Intent(applicationContext, RepositoryListActivity::class.java)
                    val bundle = Bundle()
                    bundle.putParcelable("user", viewModel.userModel.value)
                    bundle.putParcelableArrayList("dataset", viewModel.repoModel.value)
                    intent.putExtras(bundle)
                    clearNetworkResponse()
                    startActivity(intent)
                }
                ResourceStatus.LOADING -> {
                    //do nothing
                }
                ResourceStatus.ERROR -> {
                    progressBar.visibility = INVISIBLE
                    Toast.makeText(applicationContext, "An error occurred, please try again later", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun clearNetworkResponse() {
        viewModel.userModel.value = null
        viewModel.repoModel.value = null
    }

    private fun handleBothRequests() {
        val rs = object: ResourceObserver<Any>() {
            override fun onComplete() {
                //do nothing
            }

            override fun onNext(t: Any) {
                when (t) {
                    is User -> viewModel.userModel.value = t
                    is ArrayList<*> -> viewModel.repoModel.value = t as? ArrayList<Repo>
                    else -> Log.e(TAG, "error")
                }
            }

            override fun onError(e: Throwable) {
                viewModel.errorState.value = true
            }
        }
        val username = usernameTextInputEditText.text.toString()
        val disposable = Observable.concat(
                viewModel.fetchUserInfo(username).subscribeOn(Schedulers.io()),
                viewModel.fetchRepos(username).subscribeOn(Schedulers.io()))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(rs)
        compositeDisposable.add(disposable)
    }

}
