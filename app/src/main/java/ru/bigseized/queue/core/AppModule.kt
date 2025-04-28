package ru.bigseized.queue.core

import android.app.Application
import androidx.room.Room
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.bigseized.queue.data.api.QueueApi
import ru.bigseized.queue.data.api.UserApi
import ru.bigseized.queue.data.dataBase.UserDAO
import ru.bigseized.queue.data.dataBase.UserDataBase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideUserDataBase(app: Application): UserDataBase {
        return Room.databaseBuilder(
            app,
            UserDataBase::class.java,
            UserDataBase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserApi(auth: FirebaseAuth, database: FirebaseFirestore): UserApi {
        return UserApi(auth, database)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideUserDao(userDataBase: UserDataBase): UserDAO {
        return userDataBase.userDAO
    }

    @Provides
    @Singleton
    fun provideDataBase(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Provides
    @Singleton
    fun provideQueueApi(auth: FirebaseAuth, database: FirebaseFirestore): QueueApi {
        return QueueApi(auth, database)
    }

}