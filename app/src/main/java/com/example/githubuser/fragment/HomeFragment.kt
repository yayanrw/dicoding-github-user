package com.example.githubuser.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.core.ApiConfig
import com.example.githubuser.databinding.FragmentHomeBinding
import com.example.githubuser.model.UsersResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUsers()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getUsers() {
        showLoading(true)
        val client = ApiConfig.getApiService().fetchUsers()
        client.enqueue(object : Callback<ArrayList<UsersResponse>> {
            override fun onResponse(
                call: Call<ArrayList<UsersResponse>>,
                response: Response<ArrayList<UsersResponse>>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setupRV(responseBody)
                    }
                } else {
                    showToast(response.message())
                }
            }

            override fun onFailure(call: Call<ArrayList<UsersResponse>>, t: Throwable) {
                showLoading(false)
                showToast(t.message.toString())
            }
        })
    }

    private fun setupRV(listGithubUser: ArrayList<UsersResponse>) {
        binding.rvGithubUsers.layoutManager = LinearLayoutManager(context)
        binding.rvGithubUsers.setHasFixedSize(true)
        userAdapter = UserAdapter(listGithubUser)
        binding.rvGithubUsers.adapter = userAdapter
        binding.rvGithubUsers.visibility = View.VISIBLE

        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UsersResponse) {
                NavHostFragment.findNavController(this@HomeFragment).navigate(R.id.action_homeFragment_to_detailActivity)
            }
        })
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showToast(messages: String) {
        Toast.makeText(context, messages, Toast.LENGTH_SHORT).show()
    }
}