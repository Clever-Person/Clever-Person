package com.b2bound

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import com.b2bound.ui.B2BoundRoot
import com.b2bound.ui.theme.B2BoundTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { B2BoundTheme { B2BoundRoot() } }
    }
}
