package com.ankur_anand.battery_charged_alarm.ui.dashboard

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startForegroundService
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ankur_anand.battery_charged_alarm.R
import com.ankur_anand.battery_charged_alarm.service.BatteryMonitorForegroundService
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import timber.log.Timber


@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun DashboardScreen(
    navController: NavHostController,
    viewModel: DashboardViewModel = hiltViewModel(),
) {

    val context = LocalContext.current

    val keyboardController = LocalSoftwareKeyboardController.current

    val percentageText = viewModel.percentageText
    val isPercentageTextFieldError = viewModel.isPercentageTextFieldError



    if (viewModel.showSuccessToast.value) {
        Toast.makeText(context, context.getString(R.string.saved_successfully), Toast.LENGTH_LONG)
            .show()
        startService(context = context)
        viewModel.resetToastValue(false)
    }

    val notificationPermissionState =
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)

    val onDonePressed: () -> Unit = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when (notificationPermissionState.status) {
                is PermissionStatus.Denied -> {

                }

                PermissionStatus.Granted -> {
                    keyboardController?.hide()
                    viewModel.savePercentage()
                }
            }
            notificationPermissionState.launchPermissionRequest()
        } else {
            keyboardController?.hide()
            viewModel.savePercentage()
        }

    }


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            GreetingText()

            Spacer(modifier = Modifier.height(20.dp))

            PercentageTextField(
                value = percentageText.value,
                onValueChanged = viewModel::setTextAndValidate,
                onDonePressed = onDonePressed,
                isError = isPercentageTextFieldError.value
            )

            Spacer(modifier = Modifier.height(20.dp))

            SaveButton(
                onDonePressed = onDonePressed,
                buttonText = stringResource(R.string.save),
                isError = isPercentageTextFieldError.value
            )

        }
    }
}

@Composable
fun GreetingText() {
    Text(text = stringResource(R.string.dashboard_greetings))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PercentageTextField(
    value: TextFieldValue,
    onValueChanged: (TextFieldValue) -> Unit,
    onDonePressed: () -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {

    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChanged,
            maxLines = 1,
            label = {
                Text(text = stringResource(R.string.percentage))
            },
            placeholder = {
                Text(text = stringResource(R.string.set_reminder_percentage))
            },
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onDonePressed()
                }
            ),
            isError = isError
        )

        AnimatedVisibility(visible = isError) {
            Text(
                modifier = Modifier.padding(
                    top = 4.dp,
                    start = 4.dp
                ),
                text = stringResource(R.string.textfield_error_message),
                color = MaterialTheme.colorScheme.error,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun ColumnScope.SaveButton(
    buttonText: String,
    onDonePressed: () -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
) {
    Button(
        modifier = modifier.align(Alignment.End),
        enabled = !isError,
        onClick = onDonePressed,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            modifier = Modifier.padding(4.dp),
            text = buttonText
        )
    }
}

private fun startService(context: Context) {
    if (!isServiceRunning(context)) {
        val intentService = Intent(context, BatteryMonitorForegroundService::class.java)
        startForegroundService(context, intentService)
    }
}

private fun isServiceRunning(context: Context): Boolean {
    try {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        for (service: ActivityManager.RunningServiceInfo in activityManager.getRunningServices(Int.MAX_VALUE)) {
            if (BatteryMonitorForegroundService::class.simpleName?.equals(service.service.className) == true) {
                return true
            }
        }
    } catch (e: SecurityException) {
        Timber.e(e)
        return false
    }

    return false
}



