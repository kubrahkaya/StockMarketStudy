package com.innovaocean.stockmarketstudy.presentation.companyListings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.Divider
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.innovaocean.core.extensions.collectAsStateDefault
import com.innovaocean.stockmarketstudy.R
import com.innovaocean.stockmarketstudy.presentation.destinations.DetailScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true) // sets this as the start destination of the default nav graph
@Destination
@Composable
fun CompanyListingsScreen(
    navigator: DestinationsNavigator,
    viewModel: CompanyListingsViewModel = hiltViewModel()
) {
   val state = viewModel.uiState.collectAsStateDefault()

    when(state.value){
        is CompanyListUiState.Loading-> {
            LoadingContent()
        }
        is CompanyListUiState.Loaded -> {
            val companies = (state.value as CompanyListUiState.Loaded).companies

            val swipeRefreshState = rememberSwipeRefreshState(false)
            val keyboardController = LocalSoftwareKeyboardController.current
            Column(modifier = Modifier.fillMaxSize()) {
                val textValue = remember { mutableStateOf("") }
                OutlinedTextField(
                    value = textValue.value,
                    onValueChange = {
                        textValue.value = it
                        viewModel.onSearchQueryChanged(it.lowercase()) },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    placeholder = {
                        Text(text = "Search...")
                    },
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
                )
                SwipeRefresh(state = swipeRefreshState,
                    onRefresh = { viewModel.onRefresh() }) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(
                            items = companies,
                            itemContent = { item ->
                                CompanyItem(company = item,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            navigator.navigate(
                                                DetailScreenDestination()
                                            )
                                        }
                                        .padding(16.dp))
                                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                            }
                        )
                    }
                }
            }
        }
        is CompanyListUiState.Error -> {
            ErrorScreen {
                viewModel.onRefresh()
            }
        }
        is CompanyListUiState.Empty -> {
            EmptyContent()
        }
    }
}

@Composable
private fun LoadingContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) { CircularProgressIndicator(color = Color.Magenta) }
}

@Composable
private fun EmptyContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) { Text(text = stringResource(id = R.string.empty_company_list))}
}