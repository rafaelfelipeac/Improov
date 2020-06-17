package com.rafaelfelipeac.improov.core.di.modules

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.improov.core.di.modules.viewModel.FragmentKey
import com.rafaelfelipeac.improov.core.di.modules.viewModel.ViewModelKey
import com.rafaelfelipeac.improov.features.profile.data.respository.FirstTimeAddRepositoryImpl
import com.rafaelfelipeac.improov.features.profile.data.respository.FirstTimeListRepositoryImpl
import com.rafaelfelipeac.improov.features.profile.data.respository.NameRepositoryImpl
import com.rafaelfelipeac.improov.features.profile.data.respository.WelcomeRepositoryImpl
import com.rafaelfelipeac.improov.features.profile.domain.repository.FirstTimeAddRepository
import com.rafaelfelipeac.improov.features.profile.domain.repository.FirstTimeListRepository
import com.rafaelfelipeac.improov.features.profile.domain.repository.NameRepository
import com.rafaelfelipeac.improov.features.profile.domain.repository.WelcomeRepository
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
    abstract fun nameRepository(nameRepositoryImpl: NameRepositoryImpl): NameRepository

    @Binds
    abstract fun welcomeRepository(welcomeRepositoryImpl: WelcomeRepositoryImpl): WelcomeRepository

    @Binds
    abstract fun firstTimeAddRepository
                (firstTimeAddRepositoryImpl: FirstTimeAddRepositoryImpl): FirstTimeAddRepository

    @Binds
    abstract fun firstTimeListRepository
                (firstTimeListRepositoryImpl: FirstTimeListRepositoryImpl): FirstTimeListRepository

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
