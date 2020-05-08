package com.realtyagency.tm.data.storage

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class Pref(context: Context) {

    private var masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private var sharedPreferences = EncryptedSharedPreferences.create(
        FILE_NAME,
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    var authToken: String?
        get() = sharedPreferences.getString(KEY_TOKEN, null)
        set(value) {
            sharedPreferences.edit {
                putString(KEY_TOKEN, value)
            }
        }

    fun clearToken() {
        sharedPreferences.edit {
            remove(KEY_TOKEN)
        }
    }

    companion object {
        const val FILE_NAME = "OrgkhimPreference"
        const val KEY_TOKEN = "token"
    }
}