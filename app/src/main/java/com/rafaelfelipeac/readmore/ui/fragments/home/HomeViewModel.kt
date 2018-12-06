package com.rafaelfelipeac.readmore.ui.fragments.home

import com.rafaelfelipeac.readmore.network.RMApi
import com.rafaelfelipeac.readmore.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeViewModel(private val rmAPI: RMApi) : BaseViewModel() {

    fun home(domain: String, email: String, password: String) {
//        val userRequest = UserRequest()
//        userRequest.domain = domain
//        userRequest.email = email
//        userRequest.password = password
//
//        huntzApi.loginUser(userRequest)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnSubscribe { onStart() }
//            .subscribe({
//                it.body()?.let {
//                    if (it.status) {
//                        var value = it.data
//                        onSuccess()
//                    } else {
//                        onError("onError - loginUser")
//                    }
//                    onFinish()
//                }
//            }, {
//                onFinish()
//                onError("onError - loginUser")
//            })
    }
}