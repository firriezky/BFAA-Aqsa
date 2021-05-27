package com.aqsa.myapplication.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aqsa.myapplication.R
import com.aqsa.myapplication.data.db.UserDatabaseManager
import com.aqsa.myapplication.data.model.FavoriteModel
import com.aqsa.myapplication.databinding.FragmentFavoriteBinding
import com.aqsa.myapplication.ui.detail.DetailActivity

class FavoriteFragment : Fragment() {


    private val db: UserDatabaseManager by lazy {
        UserDatabaseManager(requireContext())
    }

    private val favoriteAdapter by lazy { UserFavoriteAdapter(requireContext()) }

    val listFavorite = mutableListOf<FavoriteModel>()

    var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding as FragmentFavoriteBinding

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.bind(
            inflater.inflate(
                R.layout.fragment_favorite,
                container,
                false
            )
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listFavorite.addAll(db.getData())

        checkIfListEmpty()

        binding.rvFavorite.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoriteAdapter
            favoriteAdapter.setData(listFavorite)
            favoriteAdapter.notifyDataSetChanged()
        }

        favoriteAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                checkIfListEmpty()
                super.onChanged()
            }
        })

        favoriteAdapter.setInterface(object : UserFavoriteAdapter.FavoriteAdapterInterface {
            override fun onUserClick(user: FavoriteModel) {
                requireActivity().startActivity(
                    Intent(
                        requireContext(),
                        DetailActivity::class.java
                    ).putExtra(DetailActivity.USERNAME, user.username)
                )
            }
        })

    }

    fun checkIfListEmpty() {
        if (listFavorite.size == 0) {
            binding.layoutIdle.visibility = View.VISIBLE
        } else {
            binding.layoutIdle.visibility = View.GONE
        }
    }
}