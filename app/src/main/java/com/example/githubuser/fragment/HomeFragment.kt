package com.example.githubuser.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.databinding.FragmentHomeBinding
import com.example.githubuser.model.UsersResponse
import com.example.githubuser.viewmodel.MainViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var userAdapter: UserAdapter
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        mainViewModel.errorMsg.observe(viewLifecycleOwner) {
            showToast(it)
        }
        mainViewModel.users.observe(viewLifecycleOwner) {
            setupRV(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.search_action)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { mainViewModel.getUserSearch(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        searchView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {

            }

            override fun onViewDetachedFromWindow(v: View?) {
                mainViewModel.getUsers()
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupRV(listGithubUser: ArrayList<UsersResponse>?) {
        UserAdapter(null)
        binding.rvGithubUsers.layoutManager = LinearLayoutManager(context)
        binding.rvGithubUsers.setHasFixedSize(true)
        userAdapter = UserAdapter(listGithubUser)
        binding.rvGithubUsers.adapter = userAdapter

        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UsersResponse) {
                val toDetailActivity = HomeFragmentDirections.actionHomeFragmentToDetailActivity()
                toDetailActivity.login = data.login!!
                NavHostFragment.findNavController(this@HomeFragment).navigate(toDetailActivity)
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

    private fun showToast(messages: String) {
        Toast.makeText(context, messages, Toast.LENGTH_SHORT).show()
    }
}