package e.raguz.fitme

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.SystemClock
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import e.raguz.fitme.database.ExercisesDatabase
import e.raguz.fitme.databinding.ActivityMainBinding
import e.raguz.fitme.model.ExerciseDao
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {

    val NOTIFICATION_CHANNEL_ID = "10001"
    private val default_notification_channel_id = "default"
    private lateinit var binding: ActivityMainBinding
    private lateinit var exerciseDao: ExerciseDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exerciseDao = ExercisesDatabase.getDatabase(application).exerciseDao()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navController = findNavController(R.id.nav_host_fragment_container)
        binding.bottomNavigationView.setupWithNavController(navController)

        checkPreferences()
    }

    private fun checkPreferences() {
        val pref: SharedPreferences = applicationContext
            .getSharedPreferences(getString(R.string.dayPref), 0)
        val editor: SharedPreferences.Editor = pref.edit()

        val sdf = SimpleDateFormat(getString(R.string.date_format))
        val currentDate = sdf.format(Date())

        if (pref.getString(getString(R.string.key_day), "") != currentDate){
            editor.putString(getString(R.string.key_day), currentDate)
            editor.apply()
            deleteDoneExercises()
        }
    }

    private fun deleteDoneExercises() {
        val pref: SharedPreferences = applicationContext
            .getSharedPreferences(getString(R.string.myPref), 0)

        pref.edit().clear().apply()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.notification_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        GlobalScope.launch(Dispatchers.IO){
            val pref: SharedPreferences = applicationContext
                .getSharedPreferences(getString(R.string.myPref), 0)

            val myExercises = exerciseDao.exercises
            val doneExercises = myExercises.filter { pref.contains(it.id.toString()) }

            val percent = (doneExercises.size.toDouble() / myExercises.size.toDouble()) * 100.0

            val progressNotification = getNotification("${percent.roundToInt()}%")

            when (item.itemId) {
                R.id.morning -> {
                    scheduleNotification(progressNotification, 10)
                }
                R.id.afternoon -> {
                    scheduleNotification(progressNotification, 16)
                }
                R.id.evening -> {
                    scheduleNotification(progressNotification, 21)
                }
                R.id.test -> {
                    scheduleNotification(progressNotification, 0, true)
                }
            }
        }


        return when(item.itemId) {
            R.id.morning -> {
                showNotificationMessageDialog(getString(R.string.time_10AM))
                true
            }
            R.id.afternoon -> {
                showNotificationMessageDialog(getString(R.string.time_16PM))
                true
            }
            R.id.evening -> {
                showNotificationMessageDialog(getString(R.string.time_21PM))
                true
            }
            R.id.test -> {
                showNotificationMessageDialog(getString(R.string.time_5sec))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun scheduleNotification(notification: Notification, hour: Int, test: Boolean = false) {

        val notificationIntent = Intent(this, MyNotificationPublisher::class.java)
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, 1)
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification)

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = (getSystemService(Context.ALARM_SERVICE) as AlarmManager)

        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 1)

        if (!test) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
        else{
            val futureInMillis = SystemClock.elapsedRealtime() + 5000
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent)
        }

    }
    private fun getNotification(content: String): Notification {
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(
            this,
            default_notification_channel_id
        )
        builder.setContentTitle(getString(R.string.notificationTitle))
        builder.setContentText(content)
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setLargeIcon(BitmapFactory.decodeResource(this.resources,R.mipmap.ic_launcher))

        builder.setAutoCancel(true)
        builder.setChannelId(NOTIFICATION_CHANNEL_ID)
        return builder.build()
    }

    private fun showNotificationMessageDialog(time: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.dialog_title_notification))
            .setMessage("You will se your progress for today at $time or immediately if the scheduled time has passed.")
            .setPositiveButton(getString(R.string.dialog_ok)) { _, _ -> }
            .show()
    }
}