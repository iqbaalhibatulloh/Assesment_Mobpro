package org.d3if0110.miniproject.ui.screen

import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Money
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.d3if0110.miniproject.R
import org.d3if0110.miniproject.model.MainScreenModel
import org.d3if0110.miniproject.model.Note
import org.d3if0110.miniproject.model.TabItem
import org.d3if0110.miniproject.navigation.Screen
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Currency
import java.util.Locale
import kotlin.math.absoluteValue

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, mainScreenModel: MainScreenModel) {
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

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
                showBottomSheet = true
            }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.tambah_catatan),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) { padding ->
        ScreenContent(Modifier.padding(padding), mainScreenModel, showBottomSheet) {
            showBottomSheet = false
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScreenContent(
    modifier: Modifier,
    mainScreenModel: MainScreenModel,
    showBottomSheet: Boolean,
    onDismissRequest: () -> Unit
) {

    val tabItems = listOf(
        TabItem(
            title = "Pemasukan",
            unSelectedIcon = Icons.Outlined.Money,
            selectedIcon = Icons.Filled.Money
        ),
        TabItem(
            title = "Pengeluaran",
            unSelectedIcon = Icons.Outlined.Payment,
            selectedIcon = Icons.Filled.Payment
        )
    )

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

    val data = mainScreenModel.data.observeAsState()
    val sheetState = rememberModalBottomSheetState()

    var nominal by rememberSaveable { mutableStateOf("") }
    var nominalError by rememberSaveable { mutableStateOf(false) }
    var selectedStatus by rememberSaveable { mutableStateOf("Pemasukan") }

    var keterangan by rememberSaveable { mutableStateOf("") }
    var keteranganError by rememberSaveable { mutableStateOf(false) }

    val radioOptions = listOf(
        stringResource(R.string.pemasukan),
        stringResource(R.string.pengeluaran)
    )

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxWidth()
                .background(Color.Gray)
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(
                    R.string.total_saldo,
                    data.value?.sumOf { it.nominal.toDouble() }
                        ?.let { formatCurrency(it.toFloat()) } ?: "Rp. 0"), color = Color.White)
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                text = stringResource(
                    R.string.total_pemasukan,
                    data.value?.filter { it.nominal > 0 }?.sumOf { it.nominal.toDouble() }
                        ?.let {
                            formatCurrency(
                                it
                                    .toFloat()
                            )
                        } ?: "Rp."), Modifier.weight(1f), color = Color.White)
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                text = stringResource(
                    R.string.total_pengeluaran,
                    data.value?.filter { it.nominal < 0 }?.sumOf { it.nominal.toDouble() }
                        ?.let {
                            formatCurrency(
                                it
                                    .toFloat()
                            )
                        } ?: "Rp. 0"), Modifier.weight(1f), color = Color.White)
        }
        Spacer(modifier = Modifier.size(15.dp))
        Column(
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth()
                .background(Color.Gray.copy(alpha = 0.5f))
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex
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
                        }
                    )
                }
                Tab(selected = false, onClick = {
                    val pengeluaranData =
                        data.value?.let { it ->
                            buildPengeluaranString(
                                context,
                                it.filter { it.status == "Pengeluaran" })
                        } // Ganti dengan list data pengeluaran
                    if (pengeluaranData != null) {
                        shareData(context, pengeluaranData)
                    } else {
                        Toast.makeText(context, R.string.list_kosong, Toast.LENGTH_SHORT).show()
                    }
                },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = stringResource(R.string.share_all_data)
                        )
                    },
                    text = {
                        Text(text = "Share Pengeluaran")
                    }
                )
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
            ) { index ->
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    if (data.value?.isEmpty() == true) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = stringResource(id = R.string.list_kosong))
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 64.dp)
                        ) {
                            val filteredData =
                                data.value?.filter { it.status == tabItems[index].title }

                            filteredData?.let {
                                itemsIndexed(it.reversed()) { _, note ->
                                    // Gunakan indeks yang sudah dibalik
                                    ItemNote(note = note) {}
                                    Divider()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                onDismissRequest()
            },
            sheetState = sheetState,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    value = nominal,
                    onValueChange = { nominal = it },
                    label = { Text(text = stringResource(R.string.nominal)) },
                    isError = nominalError,
                    trailingIcon = { IconPicker(nominalError, "") },
                    supportingText = { ErrorHint(nominalError) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    leadingIcon = {
                        Text(text = "Rp.")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(10.dp))
                OutlinedTextField(
                    value = keterangan,
                    onValueChange = { keterangan = it },
                    label = { Text(text = stringResource(R.string.keterangan)) },
                    isError = keteranganError,
                    trailingIcon = { IconPicker(keteranganError, "") },
                    supportingText = { ErrorHint(keteranganError) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Info, contentDescription = "")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(10.dp))
                Row(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                ) {
                    radioOptions.forEach { text ->
                        StatusOption(
                            label = text,
                            isSelected = selectedStatus == text,
                            modifier = Modifier
                                .selectable(
                                    selected = selectedStatus == text,
                                    onClick = {
                                        selectedStatus = text
                                    },
                                    role = Role.RadioButton
                                )
                                .weight(1f)
                                .padding(16.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.size(10.dp))
                Button(
                    onClick = {
                        nominalError = (nominal == "" || nominal == "0")
                        keteranganError = (keterangan == "")
                        if (nominalError || keteranganError) return@Button

                        if (selectedStatus == "Pemasukan") {
                            mainScreenModel.addNote(
                                Note(
                                    keterangan,
                                    nominal.toFloat(),
                                    LocalDate.now(),
                                    selectedStatus
                                )
                            )
                        } else {
                            mainScreenModel.addNote(
                                Note(
                                    keterangan,
                                    nominal.toFloat() * -1,
                                    LocalDate.now(),
                                    selectedStatus
                                )
                            )
                        }
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                ) {
                    Text(text = stringResource(R.string.tambah))
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ItemNote(note: Note, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
    ) {
        Text(
            text = note.keterangan,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold
        )
        Row {
            Text(
                modifier = Modifier.weight(1f),
                text = formatCurrency(note.nominal),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = formatDate(note.tanggal),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
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

@RequiresApi(Build.VERSION_CODES.O)
private fun buildPengeluaranString(context: Context, pengeluaranList: List<Note>): String {
    val stringBuilder = StringBuilder()
    stringBuilder.append("Laporan Pengeluaran: \n\n")
    for (pengeluaran: Note in pengeluaranList) {
        val pengeluaranStr = context.getString(
            R.string.share_template,
            pengeluaran.keterangan,
            formatCurrency(pengeluaran.nominal),
            formatDate(pengeluaran.tanggal)
        )
        stringBuilder.append(pengeluaranStr).append("\n\n")
    }
    return stringBuilder.toString()
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
fun PrevItem() {
    ItemNote(
        note = Note(
            "asknskna",
            4000f,
            LocalDate.now(),
            "Pemasukan"
        )
    ) {}
}

@Composable
fun IconPicker(isError: Boolean, unit: String) {
    if (isError) {
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    } else {
        Text(text = unit)
    }
}

@Composable
fun ErrorHint(isError: Boolean) {
    if (isError) {
        Text(text = stringResource(id = R.string.input_invalid))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PrevMainScreen() {
    MainScreen(navController = rememberNavController(), MainScreenModel())
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(date: LocalDate): String {
    return date.format(
        DateTimeFormatter.ofPattern(
            "EEEE, dd/MM/yyyy",
            Locale("in", "ID")
        )
    )
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