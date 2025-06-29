package ru.bigseized.queue.data.api

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import ru.bigseized.queue.core.ResultOfRequest
import ru.bigseized.queue.domain.DTO.UserDTO
import ru.bigseized.queue.domain.model.Queue
import ru.bigseized.queue.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QueueApi @Inject constructor(
    private val database: FirebaseFirestore,
) {

    private var listenerOfQueue = hashMapOf<String, ListenerRegistration>()

    companion object {
        private const val QUEUE_COLLECTION = "queues"
    }

    suspend fun createQueue(queue: Queue): ResultOfRequest<Queue> {
        var resultOfRequest: ResultOfRequest<Queue>
        try {
            val idOfNewQueue = database
                .collection(QUEUE_COLLECTION)
                .document()
                .id

            queue.id = idOfNewQueue
            database
                .collection(QUEUE_COLLECTION)
                .document(idOfNewQueue)
                .set(queue)
                .await()

            resultOfRequest = ResultOfRequest.Success(queue)
        } catch (e: Exception) {
            resultOfRequest = ResultOfRequest.Error(e.message!!)
        }

        return resultOfRequest
    }

    suspend fun getQueue(id: String): ResultOfRequest<Queue> {

        val resultOfRequest: ResultOfRequest<Queue> = try {
            val result = database
                .collection(QUEUE_COLLECTION)
                .document(id)
                .get()
                .await()

            ResultOfRequest.Success(result.toObject<Queue>()!!)
        } catch (e: Exception) {
            ResultOfRequest.Error(e.message ?: "")
        }

        return resultOfRequest
    }

    suspend fun updateNameOfUserInQueues(user: User, newUsername: String): ResultOfRequest<Unit> {
        var resultOfRequest: ResultOfRequest<Unit>

        try {
            for (queue in user.queues) {
                val result = database
                    .collection(QUEUE_COLLECTION)
                    .document(queue.id)
                    .get()
                    .await()

                val parcedResult = result.toObject<Queue>()
                for (currUser in parcedResult!!.users) {
                    if (currUser.id == user.id) {
                        currUser.name = newUsername
                        break
                    }
                }

                database
                    .collection(QUEUE_COLLECTION)
                    .document(queue.id)
                    .update("users", parcedResult.users)
                    .await()
            }

            resultOfRequest = ResultOfRequest.Success(Unit)

        } catch (e: Exception) {
            resultOfRequest = ResultOfRequest.Error(e.message ?: "")
        }

        return resultOfRequest
    }

    suspend fun addUserToQueue(user: UserDTO, id: String): ResultOfRequest<Unit> {

        val resultOfRequest: ResultOfRequest<Unit> = try {
            database
                .collection(QUEUE_COLLECTION)
                .document(id)
                .update("users", FieldValue.arrayUnion(user))
                .await()

            ResultOfRequest.Success(Unit)
        } catch (e: Exception) {
            ResultOfRequest.Error(e.message ?: "")
        }

        return resultOfRequest
    }

    suspend fun deleteUserFromQueue(user: UserDTO, id: String): ResultOfRequest<Unit> {

        val resultOfRequest: ResultOfRequest<Unit> = try {
            database
                .collection(QUEUE_COLLECTION)
                .document(id)
                .update("users", FieldValue.arrayRemove(user))
                .await()

            ResultOfRequest.Success(Unit)
        } catch (e: Exception) {
            ResultOfRequest.Error(e.message!!)
        }

        return resultOfRequest
    }

    suspend fun theNext(id: String): ResultOfRequest<Unit> {
        var resultOfRequest: ResultOfRequest<Unit>

        try {
            val result = database
                .collection(QUEUE_COLLECTION)
                .document(id)
                .get()
                .await()

            val currQueue = result.toObject<Queue>()
            currQueue!!.users.removeAt(0)

            database
                .collection(QUEUE_COLLECTION)
                .document(id)
                .set(currQueue)
                .await()

            resultOfRequest = ResultOfRequest.Success(Unit)
        } catch (e: Exception) {
            resultOfRequest = ResultOfRequest.Error(e.message!!)
        }

        return resultOfRequest
    }

    fun startListeningQueue(id: String, updateData: (queue: Queue?) -> Unit) {
        try {
            listenerOfQueue[id] = database
                .collection(QUEUE_COLLECTION)
                .document(id)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        return@addSnapshotListener
                    }

                    if (value != null && value.exists()) {
                        updateData(value.toObject<Queue>())
                    }
                }
        } catch (e: Exception) {
            Log.wtf("myTag", e.message!!)
        }
    }

    fun endListeningQueue(id: String) {
        listenerOfQueue[id]?.remove()
        listenerOfQueue.remove(id)
    }

    suspend fun makeUserAdmin(queue: Queue): ResultOfRequest<Unit> {

        val resultOfRequest: ResultOfRequest<Unit> = try {
            database
                .collection(QUEUE_COLLECTION)
                .document(queue.id)
                .update("users", queue.users)
                .await()

            ResultOfRequest.Success(Unit)
        } catch (e: Exception) {
            ResultOfRequest.Error(e.message!!)
        }

        return resultOfRequest
    }

}