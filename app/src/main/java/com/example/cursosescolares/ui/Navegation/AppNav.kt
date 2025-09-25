package com.example.cursosescolares.ui.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cursosescolares.ui.Splash.SplashScreen
import com.example.cursosescolares.ui.Login.LoginScreen
import com.example.cursosescolares.ui.Screens.MyCoursesScreen
import com.example.cursosescolares.ui.Screens.SettingsScreen
import com.example.cursosescolares.ViewModel.CourseViewModel

@Composable
fun AppNav(nav: NavHostController, viewModel: CourseViewModel) {

    NavHost(navController = nav, startDestination = "splash") {

        composable("splash") {
            SplashScreen(nav)
        }

        composable("login") {
            LoginScreen(nav = nav)      // dentro de LoginScreen haz el navigate("mycourses") como en el paso 1
        }

        composable("mycourses") {
            MyCoursesScreen(
                viewModel = viewModel,
                onCourseClick = { courseId ->
                    nav.navigate("detail/$courseId")
                },
                onOpenSettings = {
                    nav.navigate("settings")
                }
            )
        }

        composable("settings") {
            SettingsScreen()
        }

        // si ya tienes detail/{courseId} déjalo como estaba…
    }
}


