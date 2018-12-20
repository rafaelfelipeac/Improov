package com.rafaelfelipeac.readmore.ui.fragments.goalform

import com.rafaelfelipeac.readmore.database.GoalDAO
import com.rafaelfelipeac.readmore.models.Goal
import com.rafaelfelipeac.readmore.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GoalFormViewModel(private val goalDAO: GoalDAO) : BaseViewModel() {

    fun saveGoal(goal: Goal) {
        goalDAO.insert(goal)
        onSuccess()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnSubscribe { onStart() }
//            .doOnTerminate { onFinish() }
//            .subscribe(
//                {onSuccess()},
//                {onError("")}
//            )
    }
}