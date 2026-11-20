package pw.vintr.music.core.domain.di

import org.koin.dsl.bind
import org.koin.dsl.module
import pw.vintr.music.core.domain.primaryLoader.PrimaryLoaderInteractor
import pw.vintr.music.core.domain.primaryLoader.PrimaryLoaderInteractorImpl

val coreDomainModule = module {
    interactor { PrimaryLoaderInteractorImpl() } bind PrimaryLoaderInteractor::class
}
