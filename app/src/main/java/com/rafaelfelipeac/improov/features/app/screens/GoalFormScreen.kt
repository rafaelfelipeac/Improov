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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.ui.theme.Dimens
import com.rafaelfelipeac.improov.features.app.compose.collectAsStateCompat
import com.rafaelfelipeac.improov.features.app.compose.rememberAppViewModel
import com.rafaelfelipeac.improov.features.app.navigation.ImproovRoutes
import com.rafaelfelipeac.improov.features.commons.data.enums.GoalType
import com.rafaelfelipeac.improov.features.commons.domain.model.Goal
import com.rafaelfelipeac.improov.core.extension.getNumberInExhibitionFormat

@Composable
fun GoalFormRoute(
    navController: NavHostController,
    goalId: Long,
) {
    val viewModel = rememberAppViewModel { goalFormViewModel() } ?: return
    val currentGoal by viewModel.goal.collectAsStateCompat(Goal())
    val goals by viewModel.goals.collectAsStateCompat(emptyList())
    val firstTimeAdd by viewModel.firstTimeAdd.collectAsStateCompat(false)
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    var name by rememberSaveable(goalId) { mutableStateOf("") }
    var selectedType by rememberSaveable(goalId) { mutableStateOf(GoalType.GOAL_NONE) }
    var divideAndConquer by rememberSaveable(goalId) { mutableStateOf(false) }
    var singleValue by rememberSaveable(goalId) { mutableStateOf("") }
    var bronzeValue by rememberSaveable(goalId) { mutableStateOf("") }
    var silverValue by rememberSaveable(goalId) { mutableStateOf("") }
    var goldValue by rememberSaveable(goalId) { mutableStateOf("") }
    var incrementValue by rememberSaveable(goalId) { mutableStateOf("") }
    var decrementValue by rememberSaveable(goalId) { mutableStateOf("") }
    var originalType by rememberSaveable(goalId) { mutableStateOf(GoalType.GOAL_NONE) }
    var loadedGoalId by remember(goalId) { mutableStateOf(-1L) }
    var showTypeLockedDialog by remember { mutableStateOf(false) }
    var pendingType by remember { mutableStateOf(GoalType.GOAL_NONE) }
    var showTypeHelpDialog by remember { mutableStateOf(false) }
    var showValuesHelpDialog by remember { mutableStateOf(false) }

    LaunchedEffect(goalId) {
        if (goalId > 0L) {
            viewModel.setGoalId(goalId)
        }
        viewModel.loadData()
    }

    LaunchedEffect(currentGoal.goalId) {
        if (currentGoal.goalId <= 0L || loadedGoalId == currentGoal.goalId) {
            return@LaunchedEffect
        }

        loadedGoalId = currentGoal.goalId
        name = currentGoal.name
        selectedType = currentGoal.type
        originalType = currentGoal.type
        divideAndConquer = currentGoal.divideAndConquer
        singleValue = currentGoal.singleValue.getNumberInExhibitionFormat()
        bronzeValue = currentGoal.bronzeValue.getNumberInExhibitionFormat()
        silverValue = currentGoal.silverValue.getNumberInExhibitionFormat()
        goldValue = currentGoal.goldValue.getNumberInExhibitionFormat()
        incrementValue = currentGoal.incrementValue.getNumberInExhibitionFormat()
        decrementValue = currentGoal.decrementValue.getNumberInExhibitionFormat()
    }

    LaunchedEffect(Unit) {
        viewModel.savedGoal.collect { savedGoalId ->
            navController.navigate(ImproovRoutes.goalDetail(savedGoalId)) {
                popUpTo(ImproovRoutes.GOAL_FORM) { inclusive = true }
            }
        }
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    GoalFormScreen(
        snackbarHostState = snackbarHostState,
        goalId = goalId,
        name = name,
        onNameChange = { name = it },
        selectedType = selectedType,
        onSelectType = { type ->
            if (originalType != GoalType.GOAL_NONE && originalType != type) {
                pendingType = type
                showTypeLockedDialog = true
            } else {
                selectedType = type
            }
        },
        divideAndConquer = divideAndConquer,
        onDivideAndConquerChange = { divideAndConquer = it },
        onTypeHelpClick = { showTypeHelpDialog = true },
        onValuesHelpClick = { showValuesHelpDialog = true },
        singleValue = singleValue,
        onSingleValueChange = { singleValue = it },
        bronzeValue = bronzeValue,
        onBronzeValueChange = { bronzeValue = it },
        silverValue = silverValue,
        onSilverValueChange = { silverValue = it },
        goldValue = goldValue,
        onGoldValueChange = { goldValue = it },
        incrementValue = incrementValue,
        onIncrementValueChange = { incrementValue = it },
        decrementValue = decrementValue,
        onDecrementValueChange = { decrementValue = it },
        onSave = {
            val candidateGoal = buildGoalToSave(
                currentGoal = currentGoal,
                goalId = goalId,
                name = name,
                selectedType = selectedType,
                divideAndConquer = divideAndConquer,
                singleValue = singleValue,
                bronzeValue = bronzeValue,
                silverValue = silverValue,
                goldValue = goldValue,
                incrementValue = incrementValue,
                decrementValue = decrementValue,
                goalsSize = goals.size,
            )

            if (candidateGoal != null) {
                viewModel.saveGoal(candidateGoal)
                if (firstTimeAdd) {
                    viewModel.saveFirstTimeAdd(false)
                    viewModel.saveFirstTimeList(true)
                }
            } else {
                val message = when {
                    name.isBlank() -> context.getString(R.string.profile_edit_empty_fields)
                    selectedType == GoalType.GOAL_NONE -> context.getString(R.string.goal_form_empty_type_goal)
                    divideAndConquer && !isValidDivideAndConquer(bronzeValue, silverValue, goldValue) -> {
                        context.getString(R.string.goal_form_gold_silver_bronze_order)
                    }
                    selectedType == GoalType.GOAL_COUNTER && !isValidCounter(incrementValue, decrementValue) -> {
                        context.getString(R.string.goal_message_goal_value_invalid)
                    }
                    else -> context.getString(R.string.goal_message_goal_value_invalid)
                }
                snackbarMessage = message
            }
        },
        onBack = { navController.navigateUp() },
    )

    if (showTypeHelpDialog) {
        AlertDialog(
            onDismissRequest = { showTypeHelpDialog = false },
            title = { Text(text = stringResource(R.string.goal_form_type_help_title)) },
            text = { Text(text = stringResource(R.string.goal_form_type_help_message)) },
            confirmButton = {
                TextButton(onClick = { showTypeHelpDialog = false }) {
                    Text(text = stringResource(R.string.dialog_action_positive))
                }
            },
        )
    }

    if (showValuesHelpDialog) {
        AlertDialog(
            onDismissRequest = { showValuesHelpDialog = false },
            title = { Text(text = stringResource(R.string.goal_form_values_help_title)) },
            text = { Text(text = stringResource(R.string.goal_form_values_help_message)) },
            confirmButton = {
                TextButton(onClick = { showValuesHelpDialog = false }) {
                    Text(text = stringResource(R.string.dialog_action_positive))
                }
            },
        )
    }

    if (showTypeLockedDialog) {
        AlertDialog(
            onDismissRequest = { showTypeLockedDialog = false },
            title = { Text(text = stringResource(R.string.goal_form_type_title)) },
            text = { Text(text = stringResource(R.string.goal_form_goal_dialog_no_goal_type_change)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedType = pendingType
                        showTypeLockedDialog = false
                    },
                ) {
                    Text(text = stringResource(R.string.dialog_action_positive))
                }
            },
            dismissButton = {
                TextButton(onClick = { showTypeLockedDialog = false }) {
                    Text(text = stringResource(R.string.dialog_action_negative))
                }
            },
        )
    }
}

@Composable
private fun GoalFormScreen(
    snackbarHostState: SnackbarHostState,
    goalId: Long,
    name: String,
    onNameChange: (String) -> Unit,
    selectedType: GoalType,
    onSelectType: (GoalType) -> Unit,
    divideAndConquer: Boolean,
    onDivideAndConquerChange: (Boolean) -> Unit,
    onTypeHelpClick: () -> Unit,
    onValuesHelpClick: () -> Unit,
    singleValue: String,
    onSingleValueChange: (String) -> Unit,
    bronzeValue: String,
    onBronzeValueChange: (String) -> Unit,
    silverValue: String,
    onSilverValueChange: (String) -> Unit,
    goldValue: String,
    onGoldValueChange: (String) -> Unit,
    incrementValue: String,
    onIncrementValueChange: (String) -> Unit,
    decrementValue: String,
    onDecrementValueChange: (String) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
) {
    val isEditMode = goalId > 0L

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ScreenTopBar(
                title = if (isEditMode) {
                    stringResource(R.string.goal_form_title_update)
                } else {
                    stringResource(R.string.goal_form_title_new)
                },
                navigation = {
                    BackNavigationButton(onClick = onBack)
                },
                actions = {
                    TextButton(onClick = onSave) {
                        Text(text = stringResource(R.string.goal_button_save))
                    }
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .padding(padding)
                    .padding(horizontal = Dimens.ScreenHorizontalPadding)
                    .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMd),
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.goal_form_goal_name_hint)) },
                singleLine = true,
            )

            GoalTypeSelector(
                selectedType = selectedType,
                onSelectType = onSelectType,
                onHelpClick = onTypeHelpClick,
            )

            DivideAndConquerSwitch(
                checked = divideAndConquer,
                onCheckedChange = onDivideAndConquerChange,
                onHelpClick = onValuesHelpClick,
            )

            AnimatedVisibility(visible = divideAndConquer) {
                Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMd)) {
                    NumberField(
                        value = bronzeValue,
                        onValueChange = onBronzeValueChange,
                        label = stringResource(R.string.goal_form_bronze_hint),
                    )
                    NumberField(
                        value = silverValue,
                        onValueChange = onSilverValueChange,
                        label = stringResource(R.string.goal_form_silver_hint),
                    )
                    NumberField(
                        value = goldValue,
                        onValueChange = onGoldValueChange,
                        label = stringResource(R.string.goal_form_gold_hint),
                    )
                }
            }

            AnimatedVisibility(visible = !divideAndConquer) {
                NumberField(
                    value = singleValue,
                    onValueChange = onSingleValueChange,
                    label = stringResource(R.string.goal_new_value),
                )
            }

            AnimatedVisibility(visible = selectedType == GoalType.GOAL_COUNTER) {
                Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMd)) {
                    NumberField(
                        value = incrementValue,
                        onValueChange = onIncrementValueChange,
                        label = stringResource(R.string.goal_form_goal_counter_inc_value_hint),
                    )
                    NumberField(
                        value = decrementValue,
                        onValueChange = onDecrementValueChange,
                        label = stringResource(R.string.goal_form_goal_counter_dec_value_hint),
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingLg))
            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.goal_button_save))
            }
        }
    }

}

@Composable
private fun GoalTypeSelector(
    selectedType: GoalType,
    onSelectType: (GoalType) -> Unit,
    onHelpClick: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSm)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.goal_form_type_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            HelpQuestionButton(onClick = onHelpClick)
        }
        GoalTypeRow(
            selected = selectedType == GoalType.GOAL_LIST,
            title = stringResource(R.string.goal_form_radio_type_list),
            onClick = { onSelectType(GoalType.GOAL_LIST) },
        )
        GoalTypeRow(
            selected = selectedType == GoalType.GOAL_COUNTER,
            title = stringResource(R.string.goal_form_radio_type_counter),
            onClick = { onSelectType(GoalType.GOAL_COUNTER) },
        )
        GoalTypeRow(
            selected = selectedType == GoalType.GOAL_FINAL,
            title = stringResource(R.string.goal_form_radio_type_final),
            onClick = { onSelectType(GoalType.GOAL_FINAL) },
        )
    }
}

@Composable
private fun GoalTypeRow(selected: Boolean, title: String, onClick: () -> Unit) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = onClick)
                .padding(horizontal = Dimens.SpacingSm, vertical = Dimens.SpacingXs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Spacer(modifier = Modifier.width(Dimens.SpacingSm))
        Text(text = title)
    }
}

@Composable
private fun DivideAndConquerSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onHelpClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingXs),
        ) {
            Text(
                text = stringResource(R.string.goal_form_divide_and_conquer_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = stringResource(R.string.goal_form_goal_counter),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        HelpQuestionButton(onClick = onHelpClick)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun NumberField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = true,
    )
}
