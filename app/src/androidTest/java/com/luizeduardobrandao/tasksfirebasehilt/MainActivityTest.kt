package com.luizeduardobrandao.tasksfirebasehilt

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule val hiltRule = HiltAndroidRule(this)
    @get:Rule val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before fun init() {
        hiltRule.inject()
    }

    @Test
    fun mostraFragmentLoginQuandoNaoAutenticado() {
        // Aqui você configura um FakeAuthModule antes de iniciar a Activity
        // e depois verifica se o fragment de Login está na tela.
    }
}