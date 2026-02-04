package com.example.weatherapppp.data.auth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    suspend fun ensureSignedIn(): String {
        val current = auth.currentUser
        if (current != null) return current.uid

        val result = auth.signInAnonymously().await()
        return result.user?.uid ?: error("Anonymous sign-in failed")
    }
}