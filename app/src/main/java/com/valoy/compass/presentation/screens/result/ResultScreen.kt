package com.valoy.compass.presentation.screens.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.valoy.compass.R
import com.valoy.compass.presentation.theme.dp_16
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ResultScreen(viewModel: ResultViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(dp_16),
            modifier = Modifier
                .padding(innerPadding)
                .padding(dp_16)
                .fillMaxSize(),
        ) {
            Tabs(categories = uiState.categories, selectedTabIndex = selectedTabIndex) { index ->
                selectedTabIndex = index
                viewModel.onTabClick(index)
            }
            EmojiList(items = uiState.items)
            TryAgainText(uiState.hasError)
        }
    }
}

@Composable
private fun TryAgainText(shouldShow: Boolean) {
    if (shouldShow) {
        Text(text = stringResource(id = R.string.error))
    }
}

@Composable
private fun Tabs(
    categories: ImmutableList<String>?,
    selectedTabIndex: Int = 0,
    onSelectedTabChange: (Int) -> Unit,
) {
    categories?.let { cats ->
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            contentColor = Color.Black,
            indicator = { tabPositions ->
                TabRowDefaults.PrimaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = Color.Black
                )
            }
        ) {
            cats.forEachIndexed { index, category ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { onSelectedTabChange(index) },
                    text = { Text(text = category) }
                )
            }
        }
    }
}

@Composable
private fun EmojiList(
    items: ImmutableList<String>?,
    modifier: Modifier = Modifier
) {
    items?.let { emojis ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = modifier
        ) {
            items(emojis.size) { index ->
                Text(text = emojis[index])
            }
        }
    }
}
