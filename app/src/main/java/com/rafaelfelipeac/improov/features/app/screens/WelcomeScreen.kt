@file:Suppress(
    "LongMethod",
    "LongParameterList",
    "CyclomaticComplexMethod",
    "MagicNumber",
    "NoUnusedImports",
    "MaximumLineLength",
    "Wrapping",
    "ArgumentListWrapping",
    "ReturnCount",
)

package com.rafaelfelipeac.improov.features.app.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.ui.theme.Dimens
import com.rafaelfelipeac.improov.features.app.compose.collectAsStateCompat
import com.rafaelfelipeac.improov.features.app.compose.rememberAppViewModel
import com.rafaelfelipeac.improov.features.app.navigation.ImproovRoutes

private data class WelcomePage(
    val title: String,
    val message: String,
)

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WelcomeRoute(navController: NavHostController) {
    val viewModel = rememberAppViewModel { welcomeViewModel() } ?: return
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val pages = remember {
        listOf(
            WelcomePage(
                title = context.getString(R.string.welcome_title),
                message = context.getString(R.string.welcome_a_message),
            ),
            WelcomePage(
                title = context.getString(R.string.welcome_title),
                message = context.getString(R.string.welcome_b_message),
            ),
            WelcomePage(
                title = context.getString(R.string.welcome_title),
                message = context.getString(R.string.welcome_c_message),
            ),
        )
    }
    val pagerState = rememberPagerState(pageCount = { pages.size })

    LaunchedEffect(Unit) {
        viewModel.saved.collect {
            navController.navigate(ImproovRoutes.GOAL_LIST) {
                popUpTo(ImproovRoutes.WELCOME) { inclusive = true }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.welcome_title)) },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = Dimens.ScreenHorizontalPadding, vertical = Dimens.ScreenVerticalPadding),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
            ) { page ->
                WelcomeCard(page = pages[page], modifier = Modifier.fillMaxSize())
            }

            Column(
                modifier = Modifier.padding(top = Dimens.SpacingXl),
                horizontalAlignment = CenterHorizontally,
            ) {
                PageDots(
                    pageCount = pages.size,
                    currentPage = pagerState.currentPage,
                    modifier = Modifier.padding(bottom = Dimens.SpacingMd),
                )
                AnimatedVisibility(visible = pagerState.currentPage == pages.lastIndex) {
                    ElevatedButton(onClick = { viewModel.saveWelcome(true) }) {
                        Text(text = stringResource(R.string.welcome_start))
                    }
                }
            }
        }
    }
}

@Composable
private fun WelcomeCard(page: WelcomePage, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier,
        colors =
            CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(Dimens.ScreenHorizontalPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally,
        ) {
            Text(
                text = page.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(Dimens.SpacingLg))
            Text(
                text = page.message,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun PageDots(pageCount: Int, currentPage: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSm),
    ) {
        repeat(pageCount) { index ->
            val active = index == currentPage
            Box(
                modifier =
                    Modifier
                        .size(if (active) 12.dp else 8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                        ),
            )
        }
    }
}
