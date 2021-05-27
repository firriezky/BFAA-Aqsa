package com.aqsa.myapplication.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aqsa.myapplication.data.model.Resource
import com.aqsa.myapplication.data.model.SearchResponse
import com.aqsa.myapplication.databinding.FragmentHomeBinding
import com.aqsa.myapplication.ui.detail.DetailActivity
import com.aqsa.myapplication.ui.detail.DetailActivity.Companion.USERNAME
import com.aqsa.myapplication.viewmodel.SearchViewModel


class HomeFragment : Fragment() {

    val searchAdapter by lazy {
        UserSearchAdapter(requireContext())
    }

    private lateinit var searchViewModel: SearchViewModel

    var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding as FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        (activity as AppCompatActivity?)?.actionBar?.hide()

        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        setAdapterAndRecyclerView()
        setSearchViewListener()
        setViewModelObserver()
    }

    private fun setAdapterAndRecyclerView() {
        binding.rvSearchUser.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }



        searchAdapter.setInterface(object : UserSearchAdapter.UserSearchInterface {
            override fun onUserClick(user: SearchResponse.Item) {
                Toast.makeText(requireContext(), user.login, Toast.LENGTH_LONG).show()
                binding.root.let {
                    requireActivity().startActivity(
                        Intent(
                            requireContext(),
                            DetailActivity::class.java
                        ).putExtra(USERNAME, user.login)
                    )
                }
            }



        })
    }

    private fun setSearchViewListener() {
        val searchCont = binding.searchView as SearchView
        searchCont.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchViewModel.searchUser(query.toString())
                binding.layoutIdle.visibility = View.GONE
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.toString().isEmpty()) {
                    searchAdapter.notifyDataSetChanged()
                    searchAdapter.clearData()
                    binding.layoutIdle.visibility = View.VISIBLE
                } else {
                    binding.layoutIdle.visibility = View.GONE
                }

                return false
            }

        })
    }

    private fun setViewModelObserver() {
        searchViewModel.responseUserList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.incLoading.root.visibility = View.GONE
                    Log.d("response search", "success")
                    Log.d("response search", it.toString())
                    it.data?.items?.let { it1 ->
                        searchAdapter.apply {
                            setData(it1)
                            notifyDataSetChanged()
                        }
                    }
                }
                is Resource.Loading -> {
                    binding.incLoading.root.visibility = View.VISIBLE
                    Log.d("response search", "loading")
                }
                is Resource.Error -> {
                    binding.incLoading.root.visibility = View.GONE

                    binding.incError.apply {
                        root.visibility = View.VISIBLE
                        srlError.setOnRefreshListener {
                            srlError.isRefreshing=false
                            this.root.visibility=View.GONE
                        }

                    }
                    Log.d("response search", it.toString())
                }
                else -> {
                    binding.incLoading.root.visibility = View.GONE
                    Log.d("response search", "Net")
                }
            }
        })
    }
}