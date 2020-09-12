package com.rafaelfelipeac.improov.core.di.modules

import android.content.Context
import com.rafaelfelipeac.improov.app.App
import dagger.Binds
import dagger.Module

@Module
abstract class ContextModule {

    // Fazer bind do context não é muito bom porque é melhor ser explicito em qual context você
    // tá obtendo, visto que eles não são compativeis
    @Binds
    abstract fun bindContext(app: App): Context
}
