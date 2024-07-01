//ViewModel에서 전달받은 데이터를 관찰하고, RecyclerView를 설정하여 데이터를 표시

package com.example.tg.ui.guide

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tg.R
import com.example.tg.databinding.FragmentGuideBinding
import com.example.tg.ui.profile.ProfileDetailActivity

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
            val profile: ImageView = view.findViewById(R.id.profile)
            val name: TextView = view.findViewById(R.id.text_name)
            val phone: TextView = view.findViewById(R.id.text_phone)
            val email: TextView = view.findViewById(R.id.text_email)
            val location: TextView = view.findViewById(R.id.text_location)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_contact, parent, false)
            return ContactViewHolder(view)
        }

        override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
            val contact = contacts[position]
            holder.name.text = contact.name
            holder.phone.text = contact.phone
            holder.email.text = contact.email
            holder.location.text = contact.location

            // 기본 이미지 설정
            holder.profile.setImageResource(R.drawable.user_image)

            // 상세 이미지 리소스 ID 설정
            val context = holder.profile.context
            val imageResId = context.resources.getIdentifier(contact.imageName, "drawable", context.packageName)

            holder.profile.setOnClickListener {
                val intent = Intent(context, ProfileDetailActivity::class.java).apply {
                    putExtra("name", contact.name)
                    putExtra("phone", contact.phone)
                    putExtra("email", contact.email)
                    putExtra("location", contact.location)
                    putExtra("imageResId", imageResId)
                    putExtra("introduction", contact.introduction)
                    putExtra("instagramUrl", contact.instagramUrl)
                }
                startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return contacts.size
        }

        fun updateContacts(newContacts: List<Contact>) {
            val oldSize = contacts.size
            contacts = newContacts

            if (oldSize < contacts.size) { // 새로운 항목이 추가
                notifyItemRangeInserted(oldSize, contacts.size - oldSize)
            } else if (oldSize > contacts.size) { // 항목이 제거
                notifyItemRangeRemoved(contacts.size, oldSize - contacts.size)
            } else { // 항목이 변경
                notifyItemRangeChanged(0, contacts.size)
            }
        }
    }
}
