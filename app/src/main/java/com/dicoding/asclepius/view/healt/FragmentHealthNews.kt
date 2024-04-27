package com.dicoding.asclepius.view.healt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.FragmentHealthNewsBinding
import com.dicoding.asclepius.databinding.FragmentHomeBinding
import com.dicoding.asclepius.view.home.adapter.AdapterBeritaKesehatan
import com.dicoding.asclepius.view.home.adapter.AdapterRiwayatScan
import com.dicoding.asclepius.view.home.viewmodel.HealthNewsViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentHealthNews.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentHealthNews : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentHealthNewsBinding? = null
    private lateinit var viewModel: HealthNewsViewModel
    private lateinit var adapter: AdapterBeritaKesehatan

    private val binding get()= _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHealthNewsBinding.inflate(inflater,container,false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(HealthNewsViewModel::class.java)
        _binding!!.idRecUser.layoutManager = LinearLayoutManager(activity)
        adapter = AdapterBeritaKesehatan(emptyList(), onItemClick = this::onItemClick)
        _binding!!.idRecUser.adapter = adapter

        viewModel.articles.observe(viewLifecycleOwner, Observer { articles ->
            Toast.makeText(requireContext(), "Data loaded: ${articles.size}", Toast.LENGTH_SHORT).show()
            adapter.updateData(articles) // Pastikan adapter Anda memiliki metode untuk mengupdate datanya
        })
        viewModel.fetchArticles()


    }

    private fun onItemClick(s: String) {

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentHealthNews.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentHealthNews().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}