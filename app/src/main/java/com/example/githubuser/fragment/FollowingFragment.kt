package com.example.githubuser.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.core.ApiConfig
import com.example.githubuser.databinding.FragmentFollowingBinding
import com.example.githubuser.model.UsersResponse
import com.example.githubuser.viewmodel.DetailViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private lateinit var userAdapter: UserAdapter
    private val detailViewModel: DetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailViewModel.followingIsLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        detailViewModel.following.observe(viewLifecycleOwner) {
            setupRV(it)
        }
        detailViewModel.userDetail.observe(viewLifecycleOwner) {
            it?.login?.let { it1 -> detailViewModel.getFollowing(it1) }
        }
    }

    private fun setupRV(listGithubUser: ArrayList<UsersResponse>?) {
        UserAdapter(null)
        binding.rvGithubUsers.layoutManager = LinearLayoutManager(context)
        binding.rvGithubUsers.setHasFixedSize(true)
        userAdapter = UserAdapter(listGithubUser)
        binding.rvGithubUsers.adapter = userAdapter

        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UsersResponse) {
//                val toDetailActivity = HomeFragmentDirections.actionHomeFragmentToDetailActivity()
//                toDetailActivity.login = data.login!!
//                NavHostFragment.findNavController(this@FollowersFragment).navigate(toDetailActivity)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.rvGithubUsers.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.rvGithubUsers.visibility = View.VISIBLE
        }
    }
}