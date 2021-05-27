package com.aqsa.myapplication.ui.detail

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aqsa.myapplication.R
import com.aqsa.myapplication.data.model.Resource
import com.aqsa.myapplication.data.model.UserResponse
import com.aqsa.myapplication.databinding.ActivityDetailBinding
import com.aqsa.myapplication.ui.follow.FollowFragment
import com.aqsa.myapplication.ui.home.HomeFragment
import com.aqsa.myapplication.util.URL
import com.aqsa.myapplication.viewmodel.SearchViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding as ActivityDetailBinding


    val viewModel by lazy { ViewModelProvider(this).get(SearchViewModel::class.java) }
    var username = ""

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        username = intent.getStringExtra(USERNAME).toString()
        viewModel.detailUser(username)

        viewModel.responseUserDetail.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.incLoading.root.visibility = View.GONE
                    Log.d("response search", "success")
                    Log.d("response search", it.toString())
                    it.data?.let {
                        setupUserData(it)
                    }
                }
                is Resource.Loading -> {
                    binding.incLoading.root.visibility = View.VISIBLE
                    Log.d("response search", "loading")
                }
                is Resource.Error -> {
                    hideLoading()
                    binding.incError.apply {
                        root.visibility = View.VISIBLE
                        srlError.setOnRefreshListener {
                            viewModel.detailUser(username)
                            srlError.isRefreshing = false
                            this.root.visibility = View.GONE
                        }
                        btnRefresh.setOnClickListener {
                            this.root.visibility = View.GONE
                        }
                    }
                    Log.d("response search", it.toString())
                }
                else -> {
                    Log.d("response search", "Net")
                }
            }
        })

        setupFM()
    }

    @SuppressLint("SetTextI18n")
    private fun setupUserData(it: UserResponse) {

        it.apply {
            binding.apply {
                loadGlide(avatarUrl)

                tvFollowers.text = "$followers Followers"
                tvFollowing.text = "$following Following"
                tvRepoCount.text = "Repo Count : $publicRepos"
                tv_about_me.text = "${getString(R.string.about_me)}\n$bio"
                if (login != "null") {
                    binding.tvUsername.text = login
                } else {
                    binding.tvUsername.text = "-"
                }
                binding.tvLocation.text = location
                if (company != "null") {
                    binding.tvCompany.text = company
                } else {
                    binding.tvCompany.text = getString(R.string.nothing)
                }

                if (location != "null") {
                    binding.tvLocation.text = location
                } else {
                    binding.tvLocation.text = getString(R.string.nothing)
                }

                if (bio == "null") {
                    binding.tvAboutMe.text = bio
                } else {
                    binding.tvAboutMe.text = getString(R.string.nothing)
                }


            }

        }
        binding.tvUsername.text = it.login
    }

    private fun replaceFragment(fragment: Fragment, bundle: Bundle) {
        fragment.arguments = bundle
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.design_bottom_sheet_slide_in,
                R.anim.design_bottom_sheet_slide_out
            )
            .replace(R.id.nav_host_fragment, fragment, fragment.javaClass.simpleName)
            .commit()
    }

    private fun setupFM() {
        val bundle = Bundle()
        val fragment = FollowFragment()
        bundle.putString(TYPE, "followers")
        bundle.putString(DETAIL_URL, "${URL.DETAIL_USER}$username/followers")
        fragment.arguments = bundle

        val mFragmentManager = supportFragmentManager
        mFragmentManager
            .beginTransaction()
            .add(
                R.id.nav_host_fragment,
                fragment, HomeFragment::class.java.simpleName
            )
            .commit()

        val bottomNav = binding.navFollow
        bottomNav.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.navigation_followers -> {
                val bundle = Bundle()
                val fragment = FollowFragment()
                bundle.putString(TYPE, "followers")
                bundle.putString(DETAIL_URL, "${URL.DETAIL_USER}$username/followers")
                replaceFragment(fragment, bundle)
                true
            }
            R.id.navigation_following -> {
                val bundle = Bundle()
                val fragment = FollowFragment()
                bundle.putString(TYPE, "following")
                bundle.putString(DETAIL_URL, "${URL.DETAIL_USER}$username/following")
                replaceFragment(fragment, bundle)
                true
            }
            else -> {
                false
            }
        }
    }

    companion object {
        const val DETAIL_URL = "url"
        const val TYPE = "type"
        const val USERNAME = "username"
    }

   fun hideLoading(){
        binding.incLoading.root.visibility = View.GONE
    }

    private fun loadGlide(avatarUrl: String) {
            Glide
                .with(binding.root)
                .load(avatarUrl)
                .centerCrop()
                .placeholder(R.drawable.placeholder_loading)
                .thumbnail(Glide.with(binding.root).load(R.raw.loading_animation))
                .dontAnimate()
                .into(binding.pict)
    }


}