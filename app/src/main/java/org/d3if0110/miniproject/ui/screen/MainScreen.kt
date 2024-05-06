package org.d3if0110.miniproject.ui.screen

import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if0110.miniproject.R
import org.d3if0110.miniproject.database.RecordDb
import org.d3if0110.miniproject.model.Record
import org.d3if0110.miniproject.navigation.Screen
import org.d3if0110.miniproject.util.SettingsDataStore
import org.d3if0110.miniproject.util.ViewModelFactory
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, isDark: Boolean) {
    val context = LocalContext.current
    val db = RecordDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: MainViewModel = viewModel(factory = factory)

    val dataStore = SettingsDataStore(LocalContext.current)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.appName))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            dataStore.saveTheme(!isDark)
                        }
                    }) {
                        Icon(
                            painter = painterResource(
                                if (isDark) R.drawable.baseline_light_mode_24
                                else R.drawable.baseline_dark_mode_24
                            ),
                            contentDescription = stringResource(
                                if (isDark) R.string.light
                                else R.string.dark
                            ),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    IconButton(onClick = {
                        navController.navigate(Screen.About.route)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.FormBaru.route)
            }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.tambah_record),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) { padding ->
        ScreenContent(Modifier.padding(padding), viewModel, context, navController)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScreenContent(
    modifier: Modifier,
    viewModel: MainViewModel,
    context: Context,
    navController: NavHostController
) {

    val tabItems = MainViewModel.tabItems

    var selectedTabIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    val pagerState = rememberPagerState {
        tabItems.size
    }

    val scope = rememberCoroutineScope()
    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
    }

    val data by viewModel.data.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxWidth()
                .padding(8.dp)
                .background(color = MaterialTheme.colorScheme.primary),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(
                    R.string.total_saldo,
                    formatCurrency(data.sumOf { it.nominal.toDouble() }.toFloat()),
                ),
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = stringResource(
                    R.string.total_pemasukan,
                    data.filter { it.nominal > 0 }.sumOf { it.nominal.toDouble() }
                        .let {
                            formatCurrency(
                                it
                                    .toFloat()
                            )
                        }),
                color = MaterialTheme.colorScheme.onPrimary
                )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = stringResource(
                    R.string.total_pengeluaran,
                    data.filter { it.nominal < 0 }.sumOf { it.nominal.toDouble() }
                        .let {
                            formatCurrency(
                                it
                                    .toFloat()
                            )
                        }),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Column(
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth()
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                tabItems.forEachIndexed { index, item ->
                    Tab(
                        selected = index == selectedTabIndex,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                            selectedTabIndex = index
                        },
                        text = {
                            Text(text = item.title)
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == selectedTabIndex) item.selectedIcon else item.unSelectedIcon,
                                contentDescription = item.title
                            )
                        },
                        modifier = Modifier.background(MaterialTheme.colorScheme.primary),
                    )
                }
                Tab(selected = false, onClick = {
                    val pengeluaranData =
                        buildPengeluaranString(
                            context,
                            data.filter { it.status == "Pengeluaran"})
                    if (pengeluaranData != null) {
                        shareData(context, pengeluaranData)
                    } else {
                        Toast.makeText(context, R.string.list_empty_expense, Toast.LENGTH_SHORT).show()
                    }
                },
                    modifier = Modifier.background(MaterialTheme.colorScheme.primary),
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = stringResource(R.string.share_all_data)
                        )
                    },
                    text = {
                        Text(text = stringResource(R.string.share_pengeluaran))
                    }
                )
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
            ) { index ->
                Column(
                    modifier = Modifier.fillMaxSize()
                        .background(MaterialTheme.colorScheme.secondary)
                ) {
                    val filteredData =
                        data.filter { it.status == tabItems[index].title }
                    if (filteredData.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(painter = painterResource(id = R.drawable.empty_removebg_preview), contentDescription = "", modifier = Modifier.aspectRatio(2f))
                            Spacer(modifier = Modifier.size(8.dp))
                            if(tabItems[index].title == "Pemasukan") {
                                Text(
                                    text = stringResource(R.string.list_empty_income),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            } else {
                                Text(
                                    text = stringResource(R.string.list_empty_expense),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSecondary
                                    )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 84.dp)
                        ) {
                            items(filteredData) { note ->
                                ItemNote(record = note) {
                                    navController.navigate(Screen.FormUbah.withId(note.id))
                                }
                                Divider()
                            }
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ItemNote(record: Record, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
    ) {
        Text(
            text = record.keterangan,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Row {
            Text(
                modifier = Modifier.weight(1f),
                text = formatCurrency(record.nominal),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Text(
                text = formatDate(Date(record.tanggal)),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

private fun shareData(context: Context, message: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}

private fun formatDate(date: Date): String {
    return SimpleDateFormat("EEEE, dd/MM/yyyy", Locale("in", "ID")).format(date)
}

@RequiresApi(Build.VERSION_CODES.O)
private fun buildPengeluaranString(context: Context, pengeluaranList: List<Record>): String? {
    return if(pengeluaranList.isNotEmpty()){
        val stringBuilder = StringBuilder()
        stringBuilder.append("Laporan Pengeluaran: \n\n")
        for (pengeluaran: Record in pengeluaranList) {
            val pengeluaranStr = context.getString(
                R.string.share_template,
                pengeluaran.keterangan,
                pengeluaran.nominal.toString(),
                Date(pengeluaran.tanggal),
            )
            stringBuilder.append(pengeluaranStr).append("\n\n")
        }
        stringBuilder.toString()
    } else {
        null
    }
}


@Composable
fun StatusOption(label: String, isSelected: Boolean, modifier: Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = isSelected, onClick = null)
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PrevMainScreen() {
    MainScreen(navController = rememberNavController(), false)
}

fun formatCurrency(amount: Float): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    formatter.currency = Currency.getInstance("IDR")
    return if (amount < 0) {
        "– ${formatter.format(amount.absoluteValue)}"
    } else {
        formatter.format(amount)
    }
}