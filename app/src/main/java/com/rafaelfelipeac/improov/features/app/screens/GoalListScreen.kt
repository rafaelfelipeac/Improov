@file:Suppress(
    "LongMethod",
    "LongParameterList",
    "CyclomaticComplexMethod",
    "MagicNumber",
    "Wrapping",
    "ArgumentListWrapping",
    "ReturnCount",
)

package com.rafaelfelipeac.improov.features.app.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.formatToDate
import com.rafaelfelipeac.improov.core.extension.getNumberInExhibitionFormat
import com.rafaelfelipeac.improov.core.ui.theme.Dimens
import com.rafaelfelipeac.improov.features.app.compose.collectAsStateCompat
import com.rafaelfelipeac.improov.features.app.compose.rememberAppViewModel
import com.rafaelfelipeac.improov.features.app.navigation.ImproovRoutes
import com.rafaelfelipeac.improov.features.commons.domain.model.Goal

@Composable
fun GoalListRoute(navController: NavHostController) {
    val viewModel = rememberAppViewModel { goalListViewModel() } ?: return
    val goals by viewModel.goals.collectAsStateCompat(emptyList())
    val firstTimeList by viewModel.firstTimeList.collectAsStateCompat(false)
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    LaunchedEffect(firstTimeList) {
        if (firstTimeList) {
            viewModel.saveFirstTimeList(false)
        }
    }

    GoalListScreen(
        goals = goals,
        snackbarHostState = snackbarHostState,
        onAddGoal = { navController.navigate(ImproovRoutes.goalForm()) },
        onOpenGoal = { goal -> navController.navigate(ImproovRoutes.goalDetail(goal.goalId)) },
        onOpenProfile = { navController.navigate(ImproovRoutes.PROFILE) },
    )
}

@Composable
private fun GoalListScreen(
    goals: List<Goal>,
    snackbarHostState: SnackbarHostState,
    onAddGoal: () -> Unit,
    onOpenGoal: (Goal) -> Unit,
    onOpenProfile: () -> Unit,
) {
    androidx.compose.material3.Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ScreenTopBar(
                title = stringResource(R.string.list_title),
                actions = {
                    TextButton(onClick = onOpenProfile) {
                        Text(stringResource(R.string.profile_title))
                    }
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        if (goals.isEmpty()) {
            EmptyState(
                modifier =
                    Modifier
                        .padding(padding)
                        .fillMaxSize(),
                title = stringResource(R.string.list_title),
                message = stringResource(R.string.list_placeholder_message),
            )
        } else {
            LazyColumn(
                state = rememberLazyListState(),
                modifier =
                    Modifier
                        .padding(padding)
                        .fillMaxWidth(),
                contentPadding = PaddingValues(vertical = Dimens.ScreenVerticalPadding),
                verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMd),
            ) {
                items(goals, key = { it.goalId }) { goal ->
                    GoalCard(goal = goal, onClick = { onOpenGoal(goal) })
                }
            }
        }
    }
}

@Composable
private fun GoalCard(goal: Goal, onClick: () -> Unit) {
    val target = goalTarget(goal)
    val progress = goal.getPercentage().coerceIn(0F, 100F)

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
    ) {
        Column(
            modifier = Modifier.padding(Dimens.ScreenHorizontalPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSm),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = goal.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = goalTypeLabel(goal.type),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (goal.done) {
                    Text(
                        text = stringResource(R.string.goal_message_goal_done),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            Text(
                text = "${goal.value.getNumberInExhibitionFormat()} / ${target.getNumberInExhibitionFormat()}",
                style = MaterialTheme.typography.bodyLarge,
            )
            LinearProgressIndicator(
                progress = { progress / 100F },
                modifier = Modifier.fillMaxWidth(),
            )
            goal.date?.let {
                Text(
                    text = it.formatToDate(LocalContext.current).toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
