package pw.vintr.music.domain.mainPage.useCase

import pw.vintr.music.data.mainPage.repository.MainPageRepository
import pw.vintr.music.domain.mainPage.model.MainPageItemModel
import pw.vintr.music.domain.mainPage.model.toModel

class GetMainPageContentUseCase(
    private val mainPageRepository: MainPageRepository,
) {

    suspend operator fun invoke(): List<MainPageItemModel> = mainPageRepository
        .getMainPageItems()
        .map { it.toModel() }
}
