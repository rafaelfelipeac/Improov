@file:OptIn(ExperimentalMaterial3Api::class)
@file:Suppress(
    "CyclomaticComplexMethod",
    "LongMethod",
    "LongParameterList",
    "MagicNumber",
    "NoUnusedImports",
    "ReturnCount",
    "TooManyFunctions",
)

package com.rafaelfelipeac.improov.features.app.screens

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rafaelfelipeac.improov.BuildConfig
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.LocaleHelper
import com.rafaelfelipeac.improov.core.extension.formatToDate
import com.rafaelfelipeac.improov.core.extension.formatToDateTime
import com.rafaelfelipeac.improov.core.extension.getNumberInExhibitionFormat
import com.rafaelfelipeac.improov.core.extension.getValueWithSymbol
import com.rafaelfelipeac.improov.core.ui.theme.Dimens
import com.rafaelfelipeac.improov.features.app.compose.collectAsStateCompat
import com.rafaelfelipeac.improov.features.app.compose.findActivity
import com.rafaelfelipeac.improov.features.app.compose.rememberAppViewModel
import com.rafaelfelipeac.improov.features.app.navigation.ImproovRoutes
import com.rafaelfelipeac.improov.features.commons.data.enums.GoalType
import com.rafaelfelipeac.improov.features.commons.domain.model.Goal
import com.rafaelfelipeac.improov.features.commons.domain.model.Historic
import com.rafaelfelipeac.improov.features.commons.domain.model.Item
import com.rafaelfelipeac.improov.features.goal.domain.rule.GoalRules
import com.rafaelfelipeac.improov.features.goal.domain.rule.HistoricRules
import com.rafaelfelipeac.improov.features.goal.domain.rule.ItemRules
import com.rafaelfelipeac.improov.features.goal.presentation.goaldetail.GoalDetailViewModel
import com.rafaelfelipeac.improov.features.goal.presentation.goalform.GoalFormViewModel
import com.rafaelfelipeac.improov.features.goal.presentation.goallist.GoalListViewModel
import com.rafaelfelipeac.improov.features.profile.presentation.profile.ProfileViewModel
import com.rafaelfelipeac.improov.features.profile.presentation.profileedit.ProfileEditViewModel
import com.rafaelfelipeac.improov.features.settings.presentation.settings.SettingsViewModel
import com.rafaelfelipeac.improov.features.settings.presentation.settingslanguage.SettingsLanguageViewModel
import com.rafaelfelipeac.improov.features.splash.presentation.SplashViewModel
import com.rafaelfelipeac.improov.features.welcome.presentation.WelcomeViewModel
import com.rafaelfelipeac.improov.features.backup.presentation.BackupViewModel
import java.io.File
import java.util.Date

@Composable
fun SplashRoute(navController: NavHostController) {
    val viewModel = rememberAppViewModel { splashViewModel() } ?: return
    val welcome by viewModel.welcome.collectAsStateCompat(false)

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    LaunchedEffect(welcome) {
        if (welcome) {
            navController.navigate(ImproovRoutes.GOAL_LIST) {
                popUpTo(ImproovRoutes.SPLASH) {
                    inclusive = true
                }
            }
        } else {
            navController.navigate(ImproovRoutes.WELCOME) {
                popUpTo(ImproovRoutes.SPLASH) {
                    inclusive = true
                }
            }
        }
    }

    SplashScreen()
}

@Composable
private fun SplashScreen() {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.background,
                        ),
                    ),
                ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingLg),
        ) {
            Text(
                text = stringResource(R.string.app_name_release),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold,
            )
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
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
                popUpTo(ImproovRoutes.WELCOME) {
                    inclusive = true
                }
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
                horizontalAlignment = Alignment.CenterHorizontally,
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
            horizontalAlignment = Alignment.CenterHorizontally,
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
    Scaffold(
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
        floatingActionButton = {
            ElevatedButton(onClick = onAddGoal) {
                Text(text = stringResource(R.string.menu_add))
            }
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
                        .fillMaxSize()
                        .padding(horizontal = Dimens.ScreenHorizontalPadding),
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
                popUpTo(ImproovRoutes.GOAL_FORM) {
                    inclusive = true
                }
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
                    TextButton(onClick = onBack) {
                        Text(text = stringResource(R.string.dialog_action_negative))
                    }
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
            )

            DivideAndConquerSwitch(
                checked = divideAndConquer,
                onCheckedChange = onDivideAndConquerChange,
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
) {
    Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSm)) {
        Text(
            text = stringResource(R.string.goal_form_type_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
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
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.weight(1f)) {
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

    LaunchedEffect(itemsFromVm) {
        items = itemsFromVm
    }

    LaunchedEffect(historicsFromVm) {
        historics = historicsFromVm
    }

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
        onEditGoal = {
            navController.navigate(ImproovRoutes.goalForm(goalId))
        },
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
        AlertDialog(
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
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ScreenTopBar(
                title = stringResource(R.string.goal_title),
                navigation = {
                    TextButton(onClick = onBack) {
                        Text(text = stringResource(R.string.dialog_action_negative))
                    }
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
                ElevatedButton(onClick = onAddItem) {
                    Text(text = stringResource(R.string.menu_add))
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
                    item {
                        Button(
                            onClick = onAddItem,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(text = stringResource(R.string.menu_add))
                        }
                    }
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

@Composable
fun ProfileRoute(navController: NavHostController) {
    val viewModel = rememberAppViewModel { profileViewModel() } ?: return
    val name by viewModel.name.collectAsStateCompat("")
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    LaunchedEffect(Unit) {
        viewModel.saved.collect {
            navController.navigate(ImproovRoutes.WELCOME) {
                popUpTo(ImproovRoutes.PROFILE) {
                    inclusive = true
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.generated.collect {
            snackbarMessage = context.getString(R.string.profile_data_created)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.clean.collect {
            snackbarMessage = context.getString(R.string.profile_data_cleared)
        }
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    ProfileScreen(
        name = name,
        snackbarHostState = snackbarHostState,
        onAddGoal = { navController.navigate(ImproovRoutes.goalForm()) },
        onEditProfile = { navController.navigate(ImproovRoutes.PROFILE_EDIT) },
        onBackup = { navController.navigate(ImproovRoutes.BACKUP) },
        onSettings = { navController.navigate(ImproovRoutes.SETTINGS) },
        onShowWelcome = {
            viewModel.saveWelcome(false)
            viewModel.saveFirstTimeAdd(true)
            viewModel.saveFirstTimeList(false)
        },
        onGenerateData = { viewModel.generateData() },
        onClearData = { viewModel.clearData() },
    )
}

@Composable
private fun ProfileScreen(
    name: String,
    snackbarHostState: SnackbarHostState,
    onAddGoal: () -> Unit,
    onEditProfile: () -> Unit,
    onBackup: () -> Unit,
    onSettings: () -> Unit,
    onShowWelcome: () -> Unit,
    onGenerateData: () -> Unit,
    onClearData: () -> Unit,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { ScreenTopBar(title = stringResource(R.string.profile_title)) },
        floatingActionButton = {
            ElevatedButton(onClick = onAddGoal) {
                Text(text = stringResource(R.string.menu_add))
            }
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
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(Dimens.ScreenHorizontalPadding),
                    verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSm),
                ) {
                    Text(
                        text = if (name.isNotBlank()) name else stringResource(R.string.profile_add_name_message),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    TextButton(onClick = onEditProfile) {
                        Text(
                            text = if (name.isNotBlank()) {
                                stringResource(R.string.profile_edit_name_message)
                            } else {
                                stringResource(R.string.profile_add_name_message)
                            },
                        )
                    }
                }
            }

            ActionCard(
                title = stringResource(R.string.profile_show_welcome_message),
                onClick = onShowWelcome,
            )
            ActionCard(
                title = stringResource(R.string.profile_button_backup),
                onClick = onBackup,
            )
            ActionCard(
                title = stringResource(R.string.settings_title),
                onClick = onSettings,
            )

            if (BuildConfig.DEBUG) {
                ActionCard(
                    title = stringResource(R.string.profile_generate_data),
                    onClick = onGenerateData,
                )
                ActionCard(
                    title = stringResource(R.string.profile_clear_data),
                    onClick = onClearData,
                )
            }
        }
    }
}

@Composable
fun ProfileEditRoute(navController: NavHostController) {
    val viewModel = rememberAppViewModel { profileEditViewModel() } ?: return
    val name by viewModel.name.collectAsStateCompat("")
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    var value by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    LaunchedEffect(name) {
        value = name
    }

    LaunchedEffect(Unit) {
        viewModel.saved.collect {
            navController.navigateUp()
        }
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    ProfileEditScreen(
        value = value,
        snackbarHostState = snackbarHostState,
        onValueChange = { value = it },
        onSave = {
            if (value.isBlank()) {
                snackbarMessage = context.getString(R.string.profile_edit_empty_fields)
            } else {
                viewModel.saveName(value)
            }
        },
        onBack = { navController.navigateUp() },
    )
}

@Composable
private fun ProfileEditScreen(
    value: String,
    snackbarHostState: SnackbarHostState,
    onValueChange: (String) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ScreenTopBar(
                title = stringResource(R.string.profile_edit_title),
                navigation = {
                    TextButton(onClick = onBack) {
                        Text(text = stringResource(R.string.dialog_action_negative))
                    }
                },
                actions = {
                    TextButton(onClick = onSave) {
                        Text(text = stringResource(R.string.profile_edit_save_message))
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
                    .padding(horizontal = Dimens.ScreenHorizontalPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMd),
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.profile_edit_name_hint)) },
                singleLine = true,
            )
            Button(onClick = onSave, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(R.string.profile_edit_save_message))
            }
        }
    }
}

@Composable
fun SettingsRoute(navController: NavHostController) {
    rememberAppViewModel { settingsViewModel() }
    val context = LocalContext.current
    SettingsScreen(
        onBack = { navController.navigateUp() },
        onLanguage = { navController.navigate(ImproovRoutes.SETTINGS_LANGUAGE) },
        onRate = { openMarketPage(context) },
        onContact = { openFeedbackMail(context) },
    )
}

@Composable
private fun SettingsScreen(
    onBack: () -> Unit,
    onLanguage: () -> Unit,
    onRate: () -> Unit,
    onContact: () -> Unit,
) {
    Scaffold(
        topBar = {
            ScreenTopBar(
                title = stringResource(R.string.settings_title),
                navigation = {
                    TextButton(onClick = onBack) {
                        Text(text = stringResource(R.string.dialog_action_negative))
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
                    .padding(horizontal = Dimens.ScreenHorizontalPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMd),
        ) {
            ActionCard(
                title = stringResource(R.string.settings_language_title),
                subtitle = stringResource(R.string.settings_language_message),
                onClick = onLanguage,
            )
            ActionCard(
                title = stringResource(R.string.settings_about_rate_title),
                subtitle = stringResource(R.string.settings_about_rate_summary),
                onClick = onRate,
            )
            ActionCard(
                title = stringResource(R.string.settings_about_contact_title),
                subtitle = stringResource(R.string.settings_about_contact_summary),
                onClick = onContact,
            )
            ActionCard(
                title = stringResource(R.string.settings_about_version_title),
                subtitle = BuildConfig.VERSION_NAME,
                onClick = {},
            )
        }
    }
}

@Composable
fun SettingsLanguageRoute(navController: NavHostController) {
    val viewModel = rememberAppViewModel { settingsLanguageViewModel() } ?: return
    val language by viewModel.language.collectAsStateCompat("")
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    LaunchedEffect(language) {
        if (language.isNotBlank()) {
            LocaleHelper.setLocale(context, language)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.saved.collect {
            context.findActivity()?.recreate()
            navController.navigateUp()
        }
    }

    SettingsLanguageScreen(
        currentLanguage = language,
        snackbarHostState = snackbarHostState,
        onBack = { navController.navigateUp() },
        onLanguageSelected = { viewModel.saveLanguage(it) },
    )
}

@Composable
private fun SettingsLanguageScreen(
    currentLanguage: String,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onLanguageSelected: (String) -> Unit,
) {
    val context = LocalContext.current
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ScreenTopBar(
                title = stringResource(R.string.settings_language_language_title),
                navigation = {
                    TextButton(onClick = onBack) {
                        Text(text = stringResource(R.string.dialog_action_negative))
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
                    .padding(horizontal = Dimens.ScreenHorizontalPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMd),
        ) {
            val portugueseKey = context.getString(R.string.settings_language_key_portuguese)
            val englishKey = context.getString(R.string.settings_language_key_english)
            LanguageRow(
                selected = currentLanguage == portugueseKey,
                title = stringResource(R.string.settings_language_radio_portuguese),
                onClick = { onLanguageSelected(portugueseKey) },
            )
            LanguageRow(
                selected = currentLanguage == englishKey,
                title = stringResource(R.string.settings_language_radio_english),
                onClick = { onLanguageSelected(englishKey) },
            )
        }
    }
}

@Composable
private fun LanguageRow(selected: Boolean, title: String, onClick: () -> Unit) {
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
        ) {
            RadioButton(selected = selected, onClick = onClick)
            Spacer(modifier = Modifier.width(Dimens.SpacingSm))
            Text(text = title)
        }
    }
}

@Composable
fun BackupRoute(navController: NavHostController) {
    val viewModel = rememberAppViewModel { backupViewModel() } ?: return
    val exportDate by viewModel.exportDate.collectAsStateCompat(0L)
    val importDate by viewModel.importDate.collectAsStateCompat(0L)
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var showPermissionDialog by remember { mutableStateOf(false) }
    var pendingPermissionAction by remember { mutableStateOf<(() -> Unit)?>(null) }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    val writePermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.values.all { it }
            if (granted) {
                pendingPermissionAction?.invoke()
            } else {
                showPermissionDialog = true
            }
        }
    val fileLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                val text = readTextFromUri(context, uri)
                if (text.isNotBlank()) {
                    viewModel.importDatabase(text)
                } else {
                    snackbarMessage = context.getString(R.string.backup_import_error)
                }
            }
        }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    LaunchedEffect(Unit) {
        viewModel.export.collect { json ->
            if (json.isNotBlank()) {
                viewModel.getExportDate()
                val file = saveBackupFile(context, json)
                shareFile(context, file)
                snackbarMessage = context.getString(R.string.backup_export_success)
            } else {
                snackbarMessage = context.getString(R.string.backup_export_error)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.import.collect { success ->
            if (success) {
                viewModel.getImportDate()
                snackbarMessage = context.getString(R.string.backup_import_success)
            } else {
                snackbarMessage = context.getString(R.string.backup_import_error)
            }
        }
    }

    BackupScreen(
        exportDate = exportDate,
        importDate = importDate,
        snackbarHostState = snackbarHostState,
        onBack = { navController.navigateUp() },
        onExport = {
            val action = { viewModel.exportDatabase() }
            pendingPermissionAction = action
            if (hasStoragePermissions(context)) {
                action()
            } else {
                writePermissionLauncher.launch(
                    arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    ),
                )
            }
        },
        onImport = {
            val action = { fileLauncher.launch(arrayOf("*/*")) }
            pendingPermissionAction = action
            if (hasStoragePermissions(context)) {
                action()
            } else {
                writePermissionLauncher.launch(
                    arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    ),
                )
            }
        },
    )

    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text(text = stringResource(R.string.backup_title)) },
            text = { Text(text = stringResource(R.string.backup_permission_storage_settings_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showPermissionDialog = false
                        openAppSettings(context)
                    },
                ) {
                    Text(text = stringResource(R.string.backup_permission_storage_settings_positive))
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionDialog = false }) {
                    Text(text = stringResource(R.string.backup_permission_storage_settings_negative))
                }
            },
        )
    }
}

@Composable
private fun BackupScreen(
    exportDate: Long,
    importDate: Long,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onExport: () -> Unit,
    onImport: () -> Unit,
) {
    val context = LocalContext.current
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ScreenTopBar(
                title = stringResource(R.string.backup_title),
                navigation = {
                    TextButton(onClick = onBack) {
                        Text(text = stringResource(R.string.dialog_action_negative))
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
            Text(
                text = stringResource(R.string.backup_message),
                style = MaterialTheme.typography.bodyLarge,
            )

            ActionCard(
                title = stringResource(R.string.backup_button_export),
                subtitle =
                    if (exportDate > 0) {
                        stringResource(R.string.backup_date_export, Date(exportDate).formatToDate(context))
                    } else {
                        stringResource(R.string.backup_button_export)
                    },
                onClick = onExport,
            )
            ActionCard(
                title = stringResource(R.string.backup_button_import),
                subtitle =
                    if (importDate > 0) {
                        stringResource(R.string.backup_date_import, Date(importDate).formatToDate(context))
                    } else {
                        stringResource(R.string.backup_button_import)
                    },
                onClick = onImport,
            )
        }
    }
}

@Composable
private fun ScreenTopBar(
    title: String,
    navigation: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = { navigation?.invoke() },
        actions = actions,
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
    )
}

@Composable
private fun EmptyState(
    modifier: Modifier = Modifier,
    title: String,
    message: String,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.padding(Dimens.ScreenHorizontalPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMd),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )
            Text(text = message, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun ActionCard(
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
    ) {
        Column(
            modifier = Modifier.padding(Dimens.ScreenHorizontalPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingXs),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

private data class WelcomePage(
    val title: String,
    val message: String,
)

private fun goalTarget(goal: Goal?): Float {
    if (goal == null) {
        return 0F
    }

    return if (goal.divideAndConquer) {
        goal.goldValue
    } else {
        goal.singleValue
    }
}

private fun goalTypeLabel(type: GoalType): String {
    return when (type) {
        GoalType.GOAL_LIST -> "List"
        GoalType.GOAL_COUNTER -> "Counter"
        GoalType.GOAL_FINAL -> "Final value"
        GoalType.GOAL_NONE -> "None"
    }
}

private fun buildGoalToSave(
    currentGoal: Goal?,
    goalId: Long,
    name: String,
    selectedType: GoalType,
    divideAndConquer: Boolean,
    singleValue: String,
    bronzeValue: String,
    silverValue: String,
    goldValue: String,
    incrementValue: String,
    decrementValue: String,
    goalsSize: Int,
): Goal? {
    if (name.isBlank()) {
        return null
    }
    if (selectedType == GoalType.GOAL_NONE) {
        return null
    }
    if (divideAndConquer && !isValidDivideAndConquer(bronzeValue, silverValue, goldValue)) {
        return null
    }
    if (selectedType == GoalType.GOAL_COUNTER && !isValidCounter(incrementValue, decrementValue)) {
        return null
    }

    val goal = currentGoal?.copy() ?: Goal()
    goal.name = name
    goal.type = selectedType
    goal.divideAndConquer = divideAndConquer

    if (goalId == 0L && goal.goalId == 0L) {
        goal.value = 0F
        goal.done = false
        goal.order = GoalRules.createNextOrder(goalsSize)
        goal.createdDate = Date()
    } else {
        goal.updatedDate = Date()
    }

    if (selectedType == GoalType.GOAL_COUNTER) {
        goal.incrementValue = incrementValue.toFloatOrNull() ?: 0F
        goal.decrementValue = decrementValue.toFloatOrNull() ?: 0F
    }

    if (divideAndConquer) {
        goal.bronzeValue = bronzeValue.toFloatOrNull() ?: 0F
        goal.silverValue = silverValue.toFloatOrNull() ?: 0F
        goal.goldValue = goldValue.toFloatOrNull() ?: 0F
    } else {
        goal.singleValue = singleValue.toFloatOrNull() ?: 0F
    }

    return goal
}

private fun isValidDivideAndConquer(bronzeValue: String, silverValue: String, goldValue: String): Boolean {
    val bronze = bronzeValue.toFloatOrNull() ?: return false
    val silver = silverValue.toFloatOrNull() ?: return false
    val gold = goldValue.toFloatOrNull() ?: return false
    return GoalRules.hasValidDivideAndConquerValues(bronze, silver, gold)
}

private fun isValidCounter(incrementValue: String, decrementValue: String): Boolean {
    val increment = incrementValue.toFloatOrNull() ?: return false
    val decrement = decrementValue.toFloatOrNull() ?: return false
    return GoalRules.hasValidCounterValues(increment, decrement)
}

private fun openMarketPage(context: Context) {
    val appPackageName = context.packageName
    val marketUri = Uri.parse("market://details?id=$appPackageName")
    val playStoreUri = Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")

    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, marketUri))
    } catch (_: ActivityNotFoundException) {
        context.startActivity(Intent(Intent.ACTION_VIEW, playStoreUri))
    }
}

private fun openFeedbackMail(context: Context) {
    val emailIntent =
        Intent(
            Intent.ACTION_SENDTO,
            Uri.fromParts("mailto", context.getString(R.string.settings_email_to), null),
        ).apply {
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.settings_email_title))
        }

    context.startActivity(
        Intent.createChooser(
            emailIntent,
            context.getString(R.string.settings_email_sender_title),
        ),
    )
}

private fun hasStoragePermissions(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        true
    } else {
        context.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
            android.content.pm.PackageManager.PERMISSION_GRANTED &&
            context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
            android.content.pm.PackageManager.PERMISSION_GRANTED
    }
}

private fun saveBackupFile(context: Context, jsonDatabase: String): File {
    val file =
        File(
            Environment.getExternalStorageDirectory()?.path + context.getString(R.string.backup_file_path),
            context.getString(R.string.backup_file_name),
        )
    file.parentFile?.mkdirs()
    file.writeText(jsonDatabase)
    return file
}

private fun shareFile(context: Context, file: File) {
    val uri =
        androidx.core.content.FileProvider.getUriForFile(
            context,
            context.getString(R.string.file_provider_path),
            file,
        )

    val intent =
        Intent(Intent.ACTION_SEND).apply {
            type = context.getString(R.string.backup_file_type)
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.backup_sharing_file_title))
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.backup_sharing_file_description))
        }

    context.startActivity(
        Intent.createChooser(intent, context.getString(R.string.backup_sharing_file_choose)),
    )
}

private fun readTextFromUri(context: Context, uri: Uri): String {
    return runCatching {
        context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }.orEmpty()
    }.getOrDefault("")
}

private fun openAppSettings(context: Context) {
    val intent =
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null),
        )
    context.startActivity(intent)
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}
