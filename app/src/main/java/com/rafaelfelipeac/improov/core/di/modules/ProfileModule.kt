package com.rafaelfelipeac.improov.core.di.modules

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.improov.core.di.key.FragmentKey
import com.rafaelfelipeac.improov.core.di.key.ViewModelKey
import com.rafaelfelipeac.improov.features.profile.data.*
import com.rafaelfelipeac.improov.features.profile.domain.repository.*
import com.rafaelfelipeac.improov.features.profile.presentation.profile.ProfileFragment
import com.rafaelfelipeac.improov.features.profile.presentation.profile.ProfileViewModel
import com.rafaelfelipeac.improov.features.profile.presentation.profileedit.ProfileEditFragment
import com.rafaelfelipeac.improov.features.profile.presentation.profileedit.ProfileEditViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ProfileModule {

    @Binds
    abstract fun nameRepository(nameDataSource: NameDataSource): NameRepository

    @Binds
    abstract fun welcomeRepository(welcomeDataSource: WelcomeDataSource): WelcomeRepository

    @Binds
    abstract fun firstTimeAddRepository(
        firstTimeAddDataSource: FirstTimeAddDataSource
    ): FirstTimeAddRepository

    @Binds
    abstract fun firstTimeListRepository(
        firstTimeListDataSource: FirstTimeListDataSource
    ): FirstTimeListRepository

    @Binds
    abstract fun goalRepository(
        goalDataSource: DataDataSource
    ): DataRepository

    @Binds
    @IntoMap
    @FragmentKey(ProfileFragment::class)
    abstract fun bindProfileFragment(profileFragment: ProfileFragment): Fragment

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(profileViewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @FragmentKey(ProfileEditFragment::class)
    abstract fun bindProfileNameFragment(profileEditFragment: ProfileEditFragment): Fragment

    @Binds
    @IntoMap
    @ViewModelKey(ProfileEditViewModel::class)
    abstract fun bindProfileEditViewModel(profileEditViewModel: ProfileEditViewModel): ViewModel
}
