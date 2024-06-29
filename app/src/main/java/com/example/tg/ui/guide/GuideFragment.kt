//ViewModel에서 전달받은 데이터를 관찰하고, RecyclerView를 설정하여 데이터를 표시

package com.example.tg.ui.guide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tg.databinding.FragmentGuideBinding

class GuideFragment : Fragment() {

    private var _binding: FragmentGuideBinding? = null
    private val binding get() = _binding!!

    private lateinit var guideViewModel: GuideViewModel
    private lateinit var adapter: ContactAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        guideViewModel = ViewModelProvider(this).get(GuideViewModel::class.java)
        _binding = FragmentGuideBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = ContactAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        guideViewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            adapter.updateContacts(contacts)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class ContactAdapter(private var contacts: List<Contact>) :
        RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

        inner class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val name: TextView = view.findViewById(android.R.id.text1)
            val phone: TextView = view.findViewById(android.R.id.text2)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_2, parent, false)
            return ContactViewHolder(view)
        }

        override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
            val contact = contacts[position]
            holder.name.text = contact.name
            holder.phone.text = contact.phone
        }

        override fun getItemCount(): Int {
            return contacts.size
        }

        fun updateContacts(newContacts: List<Contact>) {
            contacts = newContacts
            notifyDataSetChanged()
        }
    }
}
