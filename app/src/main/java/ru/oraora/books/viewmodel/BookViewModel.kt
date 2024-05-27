package ru.oraora.books.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.oraora.books.BookApplication
import ru.oraora.books.data.models.Book
import ru.oraora.books.data.repository.BookRepository
import java.io.IOException
import java.util.Collections

class BookViewModel(private val bookRepository: BookRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(BookUiState())
    val uiState: StateFlow<BookUiState> = _uiState

    private val _searchHistory = mutableStateListOf<String>("A", "B", "C")
    val searchHistory: List<String> get() = Collections.unmodifiableList(_searchHistory)

    private val _deletedSearchHistory = mutableStateListOf<String>()
    val deletedSearchHistory: List<String>
        get() = Collections.unmodifiableList(
            _deletedSearchHistory
        )

    fun addHistory(query: String) {
        viewModelScope.launch {
            if (query.trim().isNotEmpty() && query !in _searchHistory) {
                _searchHistory.add(0, query)
            }
        }
    }

    fun removeHistory(history: String) {
        viewModelScope.launch {
            _deletedSearchHistory.add(history)
        }
    }

    fun realRemoveHistory() {
        for (deletedQuery in _deletedSearchHistory) {
            _searchHistory.remove(deletedQuery)
        }
        _deletedSearchHistory.clear()
    }


    fun clearHistory() {
        viewModelScope.launch {
            _searchHistory.clear()
            _deletedSearchHistory.clear()
        }
    }

    fun searchBooks() {
        viewModelScope.launch {
            // Ставим состояние, что мы загружаем информацию
            // а также сохраняем последний запрос
            _uiState.update {
                it.copy(
                    searchFrame = SearchFrame.Loading,
                    lastQuery = it.query
                )
            }

            // Загружаем данные с сервера
            // Если в процессе загрузки возникает ошибка то показываем экран ошибки
            _uiState.update {
                it.copy(
                    searchFrame = try {
                        SearchFrame.Success(bookRepository.getBooks(it.query))
                    } catch (e: IOException) {
                        SearchFrame.Error
                    } catch (e: HttpException) {
                        SearchFrame.Error
                    }
                )
            }
        }
    }

    fun changeSelectedBook(book: Book) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(selectedBook = book)
            }
        }
    }

    fun onQueryChange(query: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    query = query
                )
            }
        }
    }

    fun onSearchActiveChange(active: Boolean) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSearchActive = active
                )
            }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BookApplication)
                val bookRepository = application.container.bookRepository
                BookViewModel(bookRepository = bookRepository)
            }
        }
    }

}

