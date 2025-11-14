package com.example.vitalia_doctors.views.nav

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vitalia_doctors.appointment.data.repository.AppointmentRepositoryImpl
import com.example.vitalia_doctors.appointment.domain.usecase.*
import com.example.vitalia_doctors.appointment.ui.AppointmentCreateScreen
import com.example.vitalia_doctors.appointment.ui.AppointmentDetailScreen
import com.example.vitalia_doctors.appointment.ui.AppointmentListScreen
import com.example.vitalia_doctors.appointment.ui.AppointmentViewModel
import com.example.vitalia_doctors.appointment.ui.AppointmentViewModelFactory
import com.example.vitalia_doctors.model.client.RetrofitClient

@Composable
fun CareNavigation() {
    val navController = rememberNavController()

    // Instanciación manual de dependencias. En un proyecto más grande, se usaría un inyector de dependencias como Dagger o Hilt.
    val appointmentRepository = AppointmentRepositoryImpl(RetrofitClient.appointmentApiService)
    val getAppointmentsByDoctorUseCase = GetAppointmentsByDoctorUseCase(appointmentRepository)
    val getAppointmentByIdUseCase = GetAppointmentByIdUseCase(appointmentRepository)
    val createAppointmentUseCase = CreateAppointmentUseCase(appointmentRepository)
    val updateAppointmentUseCase = UpdateAppointmentUseCase(appointmentRepository)
    val deleteAppointmentUseCase = DeleteAppointmentUseCase(appointmentRepository)

    val viewModelFactory = AppointmentViewModelFactory(
        getAppointmentsByDoctorUseCase,
        getAppointmentByIdUseCase,
        createAppointmentUseCase,
        updateAppointmentUseCase,
        deleteAppointmentUseCase
    )

    val viewModel: AppointmentViewModel = viewModel(factory = viewModelFactory)

    NavHost(navController = navController, startDestination = "appointments_list") {
        composable("appointments_list") {
            AppointmentListScreen(navController = navController, viewModel = viewModel)
        }
        composable("appointments_detail/{appointmentId}") { backStackEntry ->
            val appointmentId = backStackEntry.arguments?.getString("appointmentId")?.toLongOrNull()
            AppointmentDetailScreen(navController = navController, viewModel = viewModel, appointmentId = appointmentId)
        }
        composable("appointments_create") {
            AppointmentCreateScreen(navController = navController, viewModel = viewModel)
        }
    }
}
