package com.example.fitnesstracker.ToolBarIcons

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnesstracker.NavigationApp.apiWorkouts.ExerciseDetailsActivity
import com.example.fitnesstracker.NavigationApp.apiWorkouts.ExerciseEntity
import com.example.fitnesstracker.R
import com.example.fitnesstracker.databinding.FragmentSearchBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@FlowPreview
class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        setupViewModel()
        setupRecyclerViews()
        setupSearchInput()
        observeData()
        checkDatabaseContent()
    }

    private fun setupViewModel() {
        val dao = AppDatabase.getInstance(requireContext()).searchDao()
        viewModel = ViewModelProvider(
            this, SearchViewModelFactory(dao)
        )[SearchViewModel::class.java]
    }

    private fun setupRecyclerViews() {
        // Results RecyclerView
        binding.rvResults.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ResultAdapter { exercise ->
                navigateToExerciseDetail(exercise)
            }
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }

        // Suggestions RecyclerView
        binding.rvSuggestions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = RecentSearchAdapter(onItemClick = { query ->
                setSearchQuery(query)
                viewModel.performSearch(query)
            }, onItemLongClick = { search ->
                showDeleteSearchDialog(search)
            })
        }
    }

    private fun setSearchQuery(query: String) {
        binding.etSearch.setText(query)
        binding.etSearch.setSelection(query.length)
    }

    private fun navigateToExerciseDetail(exercise: ExerciseEntity) {
        try {
            Intent(requireContext(), ExerciseDetailsActivity::class.java).apply {
//                putExtra("exercises", exercise)
                // إضافة بيانات إضافية بشكل منفصل إذا لزم الأمر
                putExtra("name", exercise.name)
                putExtra("target", exercise.target)
                putExtra("gifUrl", exercise.gifUrl)
                putExtra("secondaryMuscles", exercise.secondaryMuscles)
                putExtra("bodyPart", exercise.bodyPart)
                // إذا كانت instructions عبارة عن List<String>
                putStringArrayListExtra(
                    "instructions", exercise.instructions.split("\n") as ArrayList<String>
                )
                startActivity(this)
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error opening details", Toast.LENGTH_SHORT).show()
            Log.e("SearchFragment", "Navigation error", e)
        }
    }

    private fun showDeleteSearchDialog(search: RecentSearch) {
        MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.delete_search_title))
//            .setMessage(getString(R.string.delete_search_message, search.query))
            .setPositiveButton(getString(R.string.delete_search_title)) { _, _ ->
                viewModel.deleteSearch(search)
            }.setNegativeButton(getString(R.string.cancel), null).show()
    }

    private fun setupSearchInput() {
        binding.etSearch.doAfterTextChanged { editable ->
            editable?.toString()?.let { query ->
                if (query.isNotEmpty()) {
                    viewModel.saveQuery(query)
                    showSuggestions()
                } else {
                    showSuggestions()
                    clearResults()
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.searchQueryFlow.debounce(300).onEach { query ->
                if (query.isNotEmpty()) {
                    viewModel.performSearch(query)
                }
            }.launchIn(this)
        }
    }

    private fun showSuggestions() {
        binding.rvSuggestions.visibility = View.VISIBLE
        binding.rvResults.visibility = View.GONE
    }

    private fun clearResults() {
        (binding.rvResults.adapter as? ResultAdapter)?.submitList(emptyList())
    }

    private fun checkDatabaseContent() {
        lifecycleScope.launch {
            val count = withContext(Dispatchers.IO) {
                AppDatabase.getInstance(requireContext()).searchDao().getExerciseCount()
            }

            if (count == 0) {
                showEmptyDatabaseMessage()
            }
        }
    }

    private fun showEmptyDatabaseMessage() {
        binding.tvEmptyState.text = getString(R.string.empty_database_message)
        binding.tvEmptyState.visibility = View.VISIBLE
    }

    private fun observeData() {
        viewModel.searchResults.observe(viewLifecycleOwner) { results ->
            updateResultsView(results)
        }

        viewModel.recentSearches.observe(viewLifecycleOwner) { searches ->
            updateSuggestionsView(searches)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            showErrorMessage(message)
        }
    }

    private fun updateResultsView(results: List<ExerciseEntity>) {
        (binding.rvResults.adapter as? ResultAdapter)?.submitList(results)

        if (results.isEmpty()) {
            showNoResultsMessage()
        } else {
            showResults()
        }
    }

    private fun showNoResultsMessage() {
        binding.tvEmptyState.text = getString(R.string.no_results_found)
        binding.tvEmptyState.visibility = View.VISIBLE
        binding.rvResults.visibility = View.GONE
    }

    private fun showResults() {
        binding.tvEmptyState.visibility = View.GONE
        binding.rvResults.visibility = View.VISIBLE
    }

    private fun updateSuggestionsView(searches: List<RecentSearch>) {
        if (searches.isNotEmpty() && binding.etSearch.text.isNullOrEmpty()) {
            showSuggestionsList(searches)
        } else if (binding.etSearch.text.isNullOrEmpty()) {
            hideSuggestions()
        }
    }

    private fun showSuggestionsList(searches: List<RecentSearch>) {
        binding.rvSuggestions.visibility = View.VISIBLE
        binding.rvResults.visibility = View.GONE
        (binding.rvSuggestions.adapter as? RecentSearchAdapter)?.submitList(searches)
    }

    private fun hideSuggestions() {
        binding.rvSuggestions.visibility = View.GONE
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}