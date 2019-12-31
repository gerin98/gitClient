package com.example.githubClient.arch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * [LiveData] which publicly exposes [.setValue] and [.postValue] method.
 *
 *
 * ExplicitLiveData will reset the value LiveData value to null after every setValue (which is also
 * called in postValue once it can run on the main thread)
 *
 *
 * Use this method Navigation in which we do NOT want to remember the state of the LiveData
 *
 *
 * Ex. if we navigate to a flyer from search, once we hit back and then do a orientation
 * change so recreate the LiveData object and that will return the last value, if using normal
 * LiveData you would be sent back to the flyer view instead of staying on search
 *
 * @param <T> The type of data hold by this instance
<*/
class ExplicitLiveData<T> : MutableLiveData<T>() {

    override fun setValue(value: T?) {
        super.setValue(value)

        // if value is not null, we reset it
        if (value != null) {
            super.setValue(null)
        }
    }
}