package crr.project.crirodrui

import crr.cliente.crirodrui.repositorios.UserRepository
import crr.cliente.crirodrui.viewmodels.UserViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<UserRepository> { UserRepository() }
    viewModel { UserViewModel(get()) }
}

