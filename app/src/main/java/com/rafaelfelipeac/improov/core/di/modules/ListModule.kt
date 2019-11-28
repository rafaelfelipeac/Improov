package com.rafaelfelipeac.improov.core.di.modules

import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.improov.core.di.modules.viewModel.ViewModelKey
import com.rafaelfelipeac.improov.features.list.ListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ListModule {

    @Binds
    @IntoMap
    @ViewModelKey(ListViewModel::class)
    abstract fun bindListViewModel(listViewModel: ListViewModel): ViewModel
}
