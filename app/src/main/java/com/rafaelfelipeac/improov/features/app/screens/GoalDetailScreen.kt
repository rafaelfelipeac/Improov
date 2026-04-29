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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.formatToDateTime
import com.rafaelfelipeac.improov.core.extension.getNumberInExhibitionFormat
import com.rafaelfelipeac.improov.core.extension.getValueWithSymbol
import com.rafaelfelipeac.improov.core.ui.theme.Dimens
import com.rafaelfelipeac.improov.features.app.compose.collectAsStateCompat
import com.rafaelfelipeac.improov.features.app.compose.rememberAppViewModel
import com.rafaelfelipeac.improov.features.app.navigation.ImproovRoutes
import com.rafaelfelipeac.improov.features.commons.data.enums.GoalType
import com.rafaelfelipeac.improov.features.commons.domain.model.Goal
import com.rafaelfelipeac.improov.features.commons.domain.model.Historic
import com.rafaelfelipeac.improov.features.commons.domain.model.Item
import com.rafaelfelipeac.improov.features.goal.domain.rule.GoalRules
import com.rafaelfelipeac.improov.features.goal.domain.rule.HistoricRules
import com.rafaelfelipeac.improov.features.goal.domain.rule.ItemRules
import java.util.Date

@Composable
fun GoalDetailRoute(
    navController: NavHostController,
    goalId: Long,
) {
    val viewModel = rememberAppViewModel { goalDetailViewModel() } ?: return
    val goalFromVm by viewModel.goal.collectAsStateCompat(Goal())
    val itemsFromVm by viewModel.items.collectAsStateCompat(emptyList())
    val historicsFromVm by viewModel.historics.collectAsStateCompat(emptyList())
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    var goal by remember(goalId) { mutableStateOf<Goal?>(null) }
    var items by remember(goalId) { mutableStateOf(emptyList<Item>()) }
    var historics by remember(goalId) { mutableStateOf(emptyList<Historic>()) }
    var manualValue by rememberSaveable(goalId) { mutableStateOf("") }
    var showItemDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var editingItem by remember { mutableStateOf<Item?>(null) }

    LaunchedEffect(goalId) {
        viewModel.setGoalId(goalId)
        viewModel.loadData()
    }

    LaunchedEffect(goalFromVm.goalId) {
        if (goalFromVm.goalId > 0L) {
            goal = goalFromVm
        }
    }

    LaunchedEffect(itemsFromVm) { items = itemsFromVm }
    LaunchedEffect(historicsFromVm) { historics = historicsFromVm }
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    val currentGoal = goal
    if (currentGoal == null) {
        LoadingState()
        return
    }

    val progress = currentGoal.getPercentage().coerceIn(0F, 100F)
    val target = goalTarget(currentGoal)
    val count = currentGoal.value

    GoalDetailScreen(
        snackbarHostState = snackbarHostState,
        goal = currentGoal,
        items = items,
        historics = historics,
        manualValue = manualValue,
        onManualValueChange = { manualValue = it },
        onBack = { navController.navigateUp() },
        onEditGoal = { navController.navigate(ImproovRoutes.goalForm(goalId)) },
        onAddItem = {
            editingItem = null
            itemName = ""
            showItemDialog = true
        },
        onToggleItem = { item ->
            val updatedItem = ItemRules.toggleDone(item, Date())
            val updatedItems = items.map { if (it.itemId == updatedItem.itemId) updatedItem else it }
            items = updatedItems
            viewModel.saveItem(updatedItem)
            viewModel.getItems()
            val updatedGoal = currentGoal.copy()
            updatedGoal.value = updatedItems.count { it.done }.toFloat()
            updatedGoal.done = GoalRules.isComplete(updatedGoal)
            goal = updatedGoal
            viewModel.saveGoal(updatedGoal)
            if (updatedGoal.done) {
                snackbarMessage = context.getString(R.string.goal_message_goal_done)
            }
        },
        onSaveManualValue = {
            val value = manualValue.toFloatOrNull()
            if (value == null) {
                snackbarMessage = context.getString(R.string.goal_message_goal_value_invalid)
            } else {
                val historic = HistoricRules.createManual(goalId = currentGoal.goalId, value = value, date = Date())
                historics = historics + historic
                viewModel.saveHistoric(historic)
                viewModel.getHistorics()
                val updatedGoal = currentGoal.copy()
                updatedGoal.value = updatedGoal.value + value
                updatedGoal.done = GoalRules.isComplete(updatedGoal)
                goal = updatedGoal
                viewModel.saveGoal(updatedGoal)
                manualValue = ""
            }
        },
        onIncrement = {
            val step = currentGoal.incrementValue
            val historic = HistoricRules.createIncrement(goalId = currentGoal.goalId, value = step, date = Date())
            historics = historics + historic
            viewModel.saveHistoric(historic)
            viewModel.getHistorics()
            val updatedGoal = currentGoal.copy()
            updatedGoal.value += step
            updatedGoal.done = GoalRules.isComplete(updatedGoal)
            goal = updatedGoal
            viewModel.saveGoal(updatedGoal)
        },
        onDecrement = {
            val step = currentGoal.decrementValue
            val historic = HistoricRules.createDecrement(goalId = currentGoal.goalId, value = step, date = Date())
            historics = historics + historic
            viewModel.saveHistoric(historic)
            viewModel.getHistorics()
            val updatedGoal = currentGoal.copy()
            updatedGoal.value -= step
            updatedGoal.done = GoalRules.isComplete(updatedGoal)
            goal = updatedGoal
            viewModel.saveGoal(updatedGoal)
        },
        target = target,
        count = count,
        progress = progress,
        onEditItem = { item ->
            editingItem = item
            itemName = item.name
            showItemDialog = true
        },
    )

    if (showItemDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showItemDialog = false },
            title = {
                Text(
                    text = if (editingItem == null) {
                        stringResource(R.string.item_title_add)
                    } else {
                        stringResource(R.string.item_title_edit)
                    },
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMd)) {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.bottom_sheet_item_name_hint)) },
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val current = currentGoal
                        if (itemName.isBlank()) {
                            return@TextButton
                        }
                        if (editingItem == null) {
                            val item =
                                ItemRules.create(
                                    goalId = current.goalId,
                                    name = itemName,
                                    order = items.size + 1,
                                    date = Date(),
                                )
                            items = items + item
                            viewModel.saveItem(item)
                        } else {
                            val item = editingItem!!.copy(name = itemName, updatedDate = Date())
                            items = items.map { if (it.itemId == item.itemId) item else it }
                            viewModel.saveItem(item)
                        }
                        viewModel.getItems()
                        showItemDialog = false
                    },
                ) {
                    Text(text = stringResource(R.string.goal_button_save))
                }
            },
            dismissButton = {
                TextButton(onClick = { showItemDialog = false }) {
                    Text(text = stringResource(R.string.dialog_action_negative))
                }
            },
        )
    }
}

@Composable
private fun GoalDetailScreen(
    snackbarHostState: SnackbarHostState,
    goal: Goal,
    items: List<Item>,
    historics: List<Historic>,
    manualValue: String,
    onManualValueChange: (String) -> Unit,
    onBack: () -> Unit,
    onEditGoal: () -> Unit,
    onAddItem: () -> Unit,
    onToggleItem: (Item) -> Unit,
    onSaveManualValue: () -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    target: Float,
    count: Float,
    progress: Float,
    onEditItem: (Item) -> Unit,
) {
    androidx.compose.material3.Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ScreenTopBar(
                title = stringResource(R.string.goal_title),
                navigation = {
                    BackNavigationButton(onClick = onBack)
                },
                actions = {
                    TextButton(onClick = onEditGoal) {
                        Text(text = stringResource(R.string.menu_edit))
                    }
                },
            )
        },
        floatingActionButton = {
            if (goal.type == GoalType.GOAL_LIST) {
                FloatingActionButton(onClick = onAddItem) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.menu_add),
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        LazyColumn(
            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(horizontal = Dimens.ScreenHorizontalPadding),
            contentPadding = PaddingValues(vertical = Dimens.ScreenVerticalPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMd),
        ) {
            item {
                GoalSummaryCard(goal = goal, target = target, count = count, progress = progress)
            }

            when (goal.type) {
                GoalType.GOAL_LIST -> {
                    items(items, key = { it.itemId }) { item ->
                        ItemRow(item = item, onClick = { onToggleItem(item) }, onEdit = { onEditItem(item) })
                    }
                }
                GoalType.GOAL_COUNTER -> {
                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSm)) {
                            Button(onClick = onDecrement, modifier = Modifier.weight(1f)) {
                                Text(text = "-")
                            }
                            Button(onClick = onIncrement, modifier = Modifier.weight(1f)) {
                                Text(text = "+")
                            }
                        }
                    }
                    item {
                        OutlinedTextField(
                            value = manualValue,
                            onValueChange = onManualValueChange,
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(stringResource(R.string.goal_new_value)) },
                        )
                    }
                    item {
                        Button(
                            onClick = onSaveManualValue,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(text = stringResource(R.string.goal_button_save))
                        }
                    }
                    items(historics, key = { it.historicId }) { historic ->
                        HistoricRow(historic = historic)
                    }
                }
                GoalType.GOAL_FINAL -> {
                    item {
                        OutlinedTextField(
                            value = manualValue,
                            onValueChange = onManualValueChange,
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(stringResource(R.string.goal_new_value)) },
                        )
                    }
                    item {
                        Button(
                            onClick = onSaveManualValue,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(text = stringResource(R.string.goal_button_save))
                        }
                    }
                    items(historics, key = { it.historicId }) { historic ->
                        HistoricRow(historic = historic)
                    }
                }
                GoalType.GOAL_NONE -> Unit
            }
        }
    }
}

@Composable
private fun GoalSummaryCard(
    goal: Goal,
    target: Float,
    count: Float,
    progress: Float,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(Dimens.ScreenHorizontalPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSm),
        ) {
            Text(
                text = goal.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "${count.getNumberInExhibitionFormat()} / ${target.getNumberInExhibitionFormat()}",
                style = MaterialTheme.typography.bodyLarge,
            )
            LinearProgressIndicator(
                progress = { progress / 100F },
                modifier = Modifier.fillMaxWidth(),
            )
            if (goal.divideAndConquer) {
                Text(
                    text =
                        "${goal.bronzeValue.getNumberInExhibitionFormat()} · " +
                            "${goal.silverValue.getNumberInExhibitionFormat()} · " +
                            "${goal.goldValue.getNumberInExhibitionFormat()}",
                    style = MaterialTheme.typography.bodySmall,
                )
            } else {
                Text(
                    text = goal.singleValue.getNumberInExhibitionFormat(),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun ItemRow(
    item: Item,
    onClick: () -> Unit,
    onEdit: () -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(Dimens.ScreenHorizontalPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (item.done) FontWeight.Normal else FontWeight.SemiBold,
                )
                AnimatedVisibility(visible = item.date != null) {
                    Text(
                        text = item.date?.formatToDateTime(LocalContext.current)?.toString().orEmpty(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onEdit) {
                    Text(text = stringResource(R.string.menu_edit))
                }
                Text(
                    text = if (item.done) "☑" else "☐",
                    modifier = Modifier.clickable(onClick = onClick),
                )
            }
        }
    }
}

@Composable
private fun HistoricRow(historic: Historic) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(Dimens.ScreenHorizontalPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingXs),
        ) {
            Text(
                text = historic.value.getValueWithSymbol(),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = historic.date?.formatToDateTime(LocalContext.current)?.toString().orEmpty(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
