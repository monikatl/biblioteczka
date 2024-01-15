package com.example.biblioteczka

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.biblioteczka.databinding.ActivityMainBinding
import com.example.biblioteczka.model.Person
import com.example.biblioteczka.ui.dashboard.PersonFragment
import com.example.biblioteczka.ui.home.SendDialogFragment
import com.example.biblioteczka.ui.home.fragments.HomeFragment
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import kotlin.system.exitProcess

class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val CONTACTS_PERMISSION_CODE = 123
    private val MY_PERMISSIONS_REQUEST_SEND_SMS = 234
    lateinit var persons: MutableList<Person>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_person, R.id.navigation_history
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        readContactsPermission()
    }

    fun smsPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED
        ) {

        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.SEND_SMS),
                MY_PERMISSIONS_REQUEST_SEND_SMS
            )
        }
    }

    private fun callPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CALL_PHONE),
                CONTACTS_PERMISSION_CODE
            )
        }
    }

    private fun readContactsPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_CONTACTS),
                CONTACTS_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CONTACTS_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    lifecycleScope.launch { loadContacts() }
                    smsPermissions()
                } else {
                    showNecessityPermissionsDialog()
                }
            }
            MY_PERMISSIONS_REQUEST_SEND_SMS -> { callPermissions() }
        }
    }

    private fun showNecessityPermissionsDialog() {
        AlertDialog.Builder(this)
            .setTitle("Wymagana zgoda!")
            .setMessage("Jeżeli nie wyrazisz zgody na DOSTĘP DO KONTAKTÓW aplikacja nie będzie mogła spełcić swojej funkcji i zostanie zamknięta!")
            .setNegativeButton("Zamknij") { _, _ -> finish()}
            .setPositiveButton("Przejdź do zgody") {_, _ -> readContactsPermission()}
            .setCancelable(false)
            .create()
            .show()
    }


    private suspend fun loadContacts() {
        val contentResolver: ContentResolver = this.contentResolver
        withContext(Dispatchers.IO) {
            val deffered = async { fetchContacts(contentResolver) }
            val loadComplete = deffered.await()
            if (loadComplete) {
                savePersons()
            }
            withContext(Dispatchers.Main) {
                onDataLoaded()
            }
        }

    }

    private fun onDataLoaded() {
        val fragment = supportFragmentManager.findFragmentById(R.id.navigation_person) as? PersonFragment
        fragment?.onResume()
    }

    private fun savePersons() {
        lifecycleScope.launch {
            try {
                for (i in persons) {
                    (application as BookcaseApplication).personRepository.addPerson(i)
                }
            } catch (e: Exception) {
                Log.e("SavePersons", "Error saving persons: ${e.message}", e)
            }
        }
    }

    @SuppressLint("Range")
    fun fetchContacts(contentResolver: ContentResolver): Boolean {
        val contacts = mutableListOf<Person>()

        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        cursor?.use {
            while (cursor.moveToNext()) {

                val contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val contactPhoneNumber = getContactPhoneNumber(contentResolver, contactId)
                val photo = getContactPhoto(contentResolver,contactId)

                val contact =Person(contactName, contactPhoneNumber?:"", photo)
                println("Photo " + photo.toString())
                contacts.add(contact)
            }
        }

        cursor?.close()
        persons = contacts
        return true
    }

    @SuppressLint("Range")
    private fun getContactPhoneNumber(contentResolver: ContentResolver, contactId: String): String? {
        val phones = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
            null,
            null
        )

        var phoneNumber: String? = null

        phones?.use {
            if (phones.moveToFirst()) {
                phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            }
        }

        phones?.close()

        return phoneNumber
    }

    @SuppressLint("Range")
    private fun getContactPhoto(contentResolver: ContentResolver, contactId: String): Bitmap? {
        val photoUri: Uri? = ContentUris.withAppendedId(
            ContactsContract.Contacts.CONTENT_URI,
            contactId.toLong()
        )

        val inputStream = ContactsContract.Contacts.openContactPhotoInputStream(
            contentResolver,
            photoUri!!
        )

        return inputStream?.use { // Use Kotlin's use function to automatically close the stream
            uriToBitmap(photoUri)
        }
    }

    private fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_send -> {
                val fragment = supportFragmentManager.findFragmentById(R.id.navigation_home) as? HomeFragment
                fragment?.showSendDialog()
                return true
            }
            R.id.action_backup -> {
                return true
            }
            R.id.action_info -> {
                return true
            }
            R.id.action_settings -> {
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


}