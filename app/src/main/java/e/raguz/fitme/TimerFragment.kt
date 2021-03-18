package e.raguz.fitme

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import e.raguz.fitme.databinding.FragmentTimerBinding
import kotlin.properties.Delegates


class TimerFragment : Fragment(R.layout.fragment_timer) {

    private val args: TimerFragmentArgs by navArgs()
    private var idExercise by Delegates.notNull<Int>()
    private lateinit var nameOfExercise: String
    private var minutes by Delegates.notNull<Int>()
    private var timeUntilFinishedMilliSeconds = 0L

    enum class State{
        Playing,
        Paused
    }

    private lateinit var state : State
    private lateinit var binding: FragmentTimerBinding
    private lateinit var timer: CountDownTimer


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTimerBinding.bind(view)

        idExercise = args.id
        minutes = args.minutes
        nameOfExercise = args.name
        state = State.Playing

        timer = object: CountDownTimer((minutes * 60000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeUntilFinishedMilliSeconds = millisUntilFinished
                updateUI()
            }
            override fun onFinish() {
                finish()
            }

        }

        (requireActivity() as MainActivity).supportActionBar?.title = "$nameOfExercise - Timer"

        binding.buttonStart.setOnClickListener { v ->
            timer.start()
            binding.buttonPause.visibility = Button.VISIBLE
            binding.buttonPause.setBackgroundColor(0xffcccccc.toInt())
            binding.buttonPause.setTextColor(0xff000000.toInt())
            binding.buttonRestart.visibility = Button.VISIBLE
            binding.buttonStart.visibility = Button.INVISIBLE
            binding.progressCountdown.max = minutes * 60000
        }
        binding.buttonPause.setOnClickListener {
            if (state == State.Playing){
                state = State.Paused
                updateUI()
                timer.cancel()
            } else{
                state = State.Playing
                updateUI()
                timer = object: CountDownTimer(timeUntilFinishedMilliSeconds, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        timeUntilFinishedMilliSeconds = millisUntilFinished
                        updateUI()
                    }
                    override fun onFinish() {
                        finish()
                    }
                }.start()
            }
        }
        binding.buttonRestart.setOnClickListener {
            timer.cancel()
            timer = object: CountDownTimer((minutes * 60000).toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timeUntilFinishedMilliSeconds = millisUntilFinished
                    updateUI()
                }
                override fun onFinish() {
                    finish()
                }
            }.start()
        }
    }

    private fun updateUI() {
        if (state == State.Playing){
            binding.buttonPause.text = getString(R.string.message_Pause)
            binding.buttonPause.setBackgroundColor(getColor(requireContext(), R.color.title_color))
            binding.buttonPause.setTextColor(getColor(requireContext(), R.color.white))
        }
        else{
            binding.buttonPause.text = getString(R.string.message_Resume)
            binding.buttonPause.setBackgroundColor(getColor(requireContext(), R.color.colorPrimary))
            binding.buttonPause.setTextColor(getColor(requireContext(), R.color.white))

        }
        val minute = (timeUntilFinishedMilliSeconds / 1000) / 60
        val seconds = (timeUntilFinishedMilliSeconds / 1000) % 60

        val secondsString = if (seconds.toString().length == 2) seconds.toString()
        else "0$seconds"

        binding.textViewCountdown.text = "$minute:$secondsString"
        binding.progressCountdown.progress = ((minutes * 60000) - timeUntilFinishedMilliSeconds).toInt()
    }
    private fun finish() {
        binding.buttonPause.visibility = Button.INVISIBLE
        binding.buttonRestart.visibility = Button.INVISIBLE
        binding.textViewCountdown.text =getString(R.string.message_Done)

        val pref: SharedPreferences = requireContext().applicationContext
            .getSharedPreferences(getString(R.string.myPref), 0)
        val editor: SharedPreferences.Editor = pref.edit()

        editor.putInt("${idExercise.toString()}", minutes)
        editor.apply()
    }

    override fun onPause(){
        super.onPause()
        timer.cancel()

    }

}