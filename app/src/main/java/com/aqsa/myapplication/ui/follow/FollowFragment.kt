package com.aqsa.myapplication.ui.follow

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aqsa.myapplication.R
import com.aqsa.myapplication.data.model.FollowResponse
import com.aqsa.myapplication.data.model.Resource
import com.aqsa.myapplication.data.model.SearchResponse
import com.aqsa.myapplication.databinding.FragmentFollowBinding
import com.aqsa.myapplication.ui.detail.DetailActivity
import com.aqsa.myapplication.ui.detail.DetailActivity.Companion.DETAIL_URL
import com.aqsa.myapplication.ui.detail.DetailActivity.Companion.TYPE
import com.aqsa.myapplication.ui.home.UserSearchAdapter
import com.aqsa.myapplication.viewmodel.SearchViewModel


class FollowFragment : Fragment() {

    val viewModel by lazy { ViewModelProvider(requireActivity()).get(SearchViewModel::class.java) }
    val followAdapter by lazy { FollowAdapter(requireContext()) }

    private var _binding: FragmentFollowBinding? = null
    val binding get() = _binding as FragmentFollowBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentFollowBinding.bind(inflater.inflate(R.layout.fragment_follow, container, false))
        // Inflate the layout for this fragment
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = arguments?.get(DETAIL_URL).toString()
        if (!url.isBlank() || url == "") {
            viewModel.followDetail(url)
        }

        setAdapterAndRecyclerView()
        setViewModelObserver()

    }

    private fun setViewModelObserver() {
        viewModel.responseFollow.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    val act = requireActivity() as DetailActivity
                    act.hideLoading()
                    Log.d("response search", "success")
                    Log.d("response search", it.toString())
                    it.data?.let { it1 ->
                        followAdapter.apply {
                            setData(it1)
                            notifyDataSetChanged()
                        }
                    }
                }
                is Resource.Loading -> {
                    Log.d("response search", "loading")
                }
                is Resource.Error -> {
                    Log.d("response search", it.toString())
                }
                else -> {
                    Log.d("response search", "Net")
                }
            }
        })
    }

    private fun setAdapterAndRecyclerView() {
        binding?.rvFollow?.apply {
            adapter = followAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }



        followAdapter.setInterface(object : FollowAdapter.FollowInterface {
            override fun onUserClick(user: FollowResponse.FollowResponseItem) {
                Toast.makeText(requireContext(), user.login, Toast.LENGTH_LONG).show()
                binding?.root?.let {
                    requireActivity().startActivity(
                        Intent(
                            requireContext(),
                            DetailActivity::class.java
                        ).putExtra(DetailActivity.USERNAME, user.login)
                    )
                }
            }


        })
    }



}