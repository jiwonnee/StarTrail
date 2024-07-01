package com.example.tg.ui.guide

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

data class Contact(
    val name: String,
    val phone: String,
    val email: String,
    val location: String,
    val imageName: String,
    val introduction: String,
    val instagramUrl: String
)

class GuideViewModel(application: Application) : AndroidViewModel(application) {

    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>> = _contacts

    init {
        loadContacts()
    }

    private fun loadContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            val assetManager = getApplication<Application>().assets
            val inputStream = assetManager.open("contacts.json")
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val jsonString = bufferedReader.use { it.readText() }
            val jsonArray = JSONArray(jsonString)

            val contactList = mutableListOf<Contact>()
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val contact = Contact(
                    name = jsonObject.getString("name"),
                    phone = jsonObject.getString("phone"),
                    email = jsonObject.getString("email"),
                    location = jsonObject.getString("location"),
                    imageName = jsonObject.getString("image"),
                    introduction = jsonObject.getString("introduction"),
                    instagramUrl = jsonObject.getString("instagramUrl")
                )
                contactList.add(contact)
            }

            _contacts.postValue(contactList)
        }
    }
}
