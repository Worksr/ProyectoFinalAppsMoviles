package com.example.cursosescolares.ui.Navegation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cursosescolares.ViewModel.CourseViewModel
import com.example.cursosescolares.ViewModel.CourseViewModelFactory
import com.example.cursosescolares.ui.Screens.AllCoursesScreen
import com.example.cursosescolares.ui.Screens.MyCoursesScreen
import com.example.cursosescolares.ui.Screens.TopCoursesScreen
import com.example.cursosescolares.ui.Login.LoginScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.repository.CourseRepository
import com.dao.CourseDao
import com.example.cursosescolares.ui.Splash.SplashScreen

@Composable
fun AppNav(dao: CourseDao? = null) {
    val nav = rememberNavController()

    val repo = remember {
        CourseRepository(
            db = FirebaseFirestore.getInstance(),
            auth = FirebaseAuth.getInstance(),
            dao = dao
        )
    }
    val vm: CourseViewModel = viewModel(factory = CourseViewModelFactory(repo))

    NavHost(navController = nav, startDestination = "splash") {
        composable("splash") { SplashScreen(nav) }
        composable("login") { LoginScreen(nav) }

        composable("top") {
            TopCoursesScreen(viewModel = vm) { courseId ->
                nav.navigate("detail/$courseId")
            }
        }
        composable("all") {
            AllCoursesScreen(viewModel = vm) { courseId ->
                nav.navigate("detail/$courseId")
            }
        }
        composable("mine") {
            MyCoursesScreen(viewModel = vm) { courseId ->
                nav.navigate("detail/$courseId")
            }
        }
        composable(
            route = "detail/{courseId}",
            arguments = listOf(navArgument("courseId") { type = NavType.StringType })
        ) {
            // Aquí pondrás la pantalla de detalle más adelante
        }
    }
}

