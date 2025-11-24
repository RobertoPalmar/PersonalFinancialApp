package com.rpalmar.financialapp.views.ui.animations

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.rpalmar.financialapp.models.SummaryAnimationDirection
import com.rpalmar.financialapp.providers.sealeds.MainSectionContent

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CarouselAnimatedSummary(
    currentSection: MainSectionContent,
    previousSection: MainSectionContent?,
    content: @Composable (MainSectionContent) -> Unit
) {
    val animationDirection = remember(previousSection, currentSection) {
        if (previousSection == null) SummaryAnimationDirection.NONE
        else getSummaryAnimationDirection(previousSection, currentSection)
    }

    AnimatedContent(
        targetState = currentSection,
        transitionSpec = {
            when (animationDirection) {
                SummaryAnimationDirection.NONE -> fadeIn(tween(0)) with fadeOut(tween(0))
                SummaryAnimationDirection.FORWARD -> slideInHorizontally { width -> width } + fadeIn() with
                        slideOutHorizontally { width -> -width } + fadeOut()
                SummaryAnimationDirection.BACKWARD -> slideInHorizontally { width -> -width } + fadeIn() with
                        slideOutHorizontally { width -> width } + fadeOut()
            }
        }
    ) { section ->
        content(section)
    }
}

fun getSummaryAnimationDirection(
    old: MainSectionContent,
    new: MainSectionContent
): SummaryAnimationDirection {
    val generalSections = listOf(
        MainSectionContent.Home::class,
        MainSectionContent.Accounts::class,
        MainSectionContent.Transactions::class,
        MainSectionContent.Categories::class
    )
    val detailSections = listOf(
        MainSectionContent.AccountDetail::class
    )

    val oldClass = old::class
    val newClass = new::class

    // No animar si es la misma sección o entre secciones generales
    if (oldClass == newClass || (oldClass in generalSections && newClass in generalSections)) {
        return SummaryAnimationDirection.NONE
    }

    return when {
        oldClass in generalSections && newClass in detailSections -> SummaryAnimationDirection.FORWARD
        oldClass in detailSections && newClass in generalSections -> SummaryAnimationDirection.BACKWARD
        else -> SummaryAnimationDirection.NONE
    }
}
