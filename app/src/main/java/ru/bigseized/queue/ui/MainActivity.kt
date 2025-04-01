package ru.bigseized.queue.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ru.bigseized.queue.ui.theme.QUEUETheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QUEUETheme {
                Navigation.Navigation()
            }
        }
    }
}
