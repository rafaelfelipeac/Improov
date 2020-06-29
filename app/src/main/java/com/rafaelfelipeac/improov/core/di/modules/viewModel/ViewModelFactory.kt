package com.rafaelfelipeac.improov.core.di.modules.viewModel

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
@Suppress("UNCHECKED_CAST")
class ViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass]
            ?: creators.asIterable().firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
            ?: throw IllegalArgumentException("Unknown ViewModel class $modelClass")

        return try {
            creator.get() as T
        } catch (e: ClassCastException) {
            throw ClassCastException()
        } catch (e: TypeCastException) {
            throw TypeCastException()
        } catch (e: KotlinNullPointerException) {
            throw KotlinNullPointerException()
        }
    }

    inline fun <reified T : ViewModel> get(activity: FragmentActivity): T =
        ViewModelProvider(activity, this)[T::class.java]

    inline fun <reified T : ViewModel> get(fragment: Fragment): T =
        ViewModelProvider(fragment, this)[T::class.java]
}
