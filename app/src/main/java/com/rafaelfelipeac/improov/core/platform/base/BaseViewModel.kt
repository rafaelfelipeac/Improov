package com.rafaelfelipeac.improov.core.platform.base

import androidx.lifecycle.ViewModel

// Essa classe é 100% inutil se você não tem alguma classe que só dependa da BaseViewModel e vai
// chamar esse método
open class BaseViewModel : ViewModel() {

    open fun loadData() { }
}
