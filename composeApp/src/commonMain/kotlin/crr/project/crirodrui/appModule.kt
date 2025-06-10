package crr.project.crirodrui

import crr.cliente.crirodrui.repositorios.CategoryRepository
import crr.cliente.crirodrui.repositorios.InvoiceRepository
import crr.cliente.crirodrui.repositorios.ProductRepository
import crr.cliente.crirodrui.repositorios.UserRepository
import crr.cliente.crirodrui.viewmodels.CategoryViewModel
import crr.cliente.crirodrui.viewmodels.ProductViewModel
import crr.cliente.crirodrui.viewmodels.UserViewModel
import crr.project.crirodrui.viewmodels.InvoiceViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<UserRepository> { UserRepository() }
    single<CategoryRepository> { CategoryRepository() }
    single<ProductRepository> { ProductRepository() }
    single<InvoiceRepository> { InvoiceRepository() }
    viewModel { UserViewModel(get()) }
    viewModel { CategoryViewModel(get()) }
    viewModel { ProductViewModel(get(),get()) }
    viewModel { InvoiceViewModel(get()) }
}

