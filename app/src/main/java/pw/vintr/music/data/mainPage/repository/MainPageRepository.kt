package pw.vintr.music.data.mainPage.repository

import pw.vintr.music.data.mainPage.source.MainPageRemoteDataSource

class MainPageRepository(private val remoteDataSource: MainPageRemoteDataSource) {

    suspend fun getMainPageItems() = remoteDataSource.getMainPageContent()
}
