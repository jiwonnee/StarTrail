package com.example.tg.ui.reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tg.databinding.FragmentGuideRankingsBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.widget.SearchView

class GuideRankingsFragment : Fragment() {

    private var _binding: FragmentGuideRankingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: GuideRankingAdapter
    private lateinit var guideList: List<Guide>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGuideRankingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 데이터 초기화
        guideList = getGuideRankings()

        // RecyclerView 설정
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = GuideRankingAdapter(guideList)
        recyclerView.adapter = adapter

        // SearchView 설정
        val searchView: SearchView = binding.searchView
        searchView.setQueryHint("Search for a name or hashtag")
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText)
                return true
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getGuideRankings(): List<Guide> {
        val jsonFileString = getJsonDataFromAsset("contacts.json")
        val gson = Gson()
        val listGuideType = object : TypeToken<List<Guide>>() {}.type
        val guideList: List<Guide> = gson.fromJson(jsonFileString, listGuideType)
        return guideList.sortedByDescending { it.rating }
    }

    private fun getJsonDataFromAsset(fileName: String): String {
        val jsonString: String?
        try {
            jsonString = context?.assets?.open(fileName)?.bufferedReader().use { it?.readText() }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return ""
        }
        return jsonString ?: ""
    }

    private fun filter(text: String?) {
        val filteredList = if (text.isNullOrEmpty()) {
            guideList
        } else {
            guideList.filter {
                it.name.contains(text, ignoreCase = true) ||
                        it.description.contains(text, ignoreCase = true)
            }
        }
        adapter.updateList(filteredList)
    }
}
