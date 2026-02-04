package com.example.weatherapppp.data.favorites

import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FavoritesRepository(
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance()
) {
    private fun userRef(uid: String): DatabaseReference =
        db.getReference("favorites").child(uid)

    fun observeFavorites(uid: String): Flow<List<FavoriteCity>> = callbackFlow {
        val ref = userRef(uid)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { it.getValue(FavoriteCity::class.java) }
                    .sortedByDescending { it.createdAt }
                trySend(list)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    suspend fun addFavorite(uid: String, city: String, note: String) {
        val ref = userRef(uid).push()
        val id = ref.key ?: error("Failed to generate id")

        val item = FavoriteCity(
            id = id,
            city = city.trim(),
            note = note.trim(),
            createdAt = System.currentTimeMillis(),
            createdBy = uid
        )
        ref.setValue(item).await()
    }

    suspend fun updateFavorite(uid: String, id: String, city: String, note: String) {
        val updates = mapOf(
            "city" to city.trim(),
            "note" to note.trim()
        )
        userRef(uid).child(id).updateChildren(updates).await()
    }

    suspend fun deleteFavorite(uid: String, id: String) {
        userRef(uid).child(id).removeValue().await()
    }
}