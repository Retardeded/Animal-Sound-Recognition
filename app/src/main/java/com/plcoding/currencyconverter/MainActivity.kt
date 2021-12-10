package com.plcoding.currencyconverter

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.plcoding.currencyconverter.databinding.ActivityMainBinding
import com.plcoding.currencyconverter.soundprocessing.GraphHandler
import com.plcoding.currencyconverter.main.MainViewModel
import com.plcoding.currencyconverter.server.SoundServiceHandler
import com.plcoding.currencyconverter.soundprocessing.RecordHandler
import com.plcoding.currencyconverter.soundprocessing.RecordHandler.Companion.isPlaying
import com.plcoding.currencyconverter.soundprocessing.RecordHandler.Companion.isRecording
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var graphHandler: GraphHandler
    lateinit var recordHandler: RecordHandler
    private val viewModel: MainViewModel by viewModels()
    lateinit var serviceHandler: SoundServiceHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        graphHandler = GraphHandler(binding.graphTime, binding.graphAplitude, binding.graphFreqFull)
        recordHandler = RecordHandler(graphHandler, "${externalCacheDir?.absolutePath}/audiometers.3gp")
        serviceHandler = SoundServiceHandler()


        lifecycleScope.launchWhenStarted {
            viewModel.conversion.collect { event ->
                when(event) {
                    is MainViewModel.SoundEvent.Success -> {
                        binding.progressBar.isVisible = false
                        binding.tvResult.setTextColor(Color.BLACK)
                        binding.tvResult.text = event.resultText
                    }
                    is MainViewModel.SoundEvent.Failure -> {
                        binding.progressBar.isVisible = false
                        binding.tvResult.setTextColor(Color.RED)
                        binding.tvResult.text = event.errorText
                    }
                    is MainViewModel.SoundEvent.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        graphHandler.initGraphView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        GlobalScope.launch(Dispatchers.IO) {
            when (item.itemId) {
                R.id.device_access_mic -> {
                    recordHandler.startRecording()
                    invalidateOptionsMenu()
                }
                R.id.device_access_mic_muted -> {
                    recordHandler.stopRecording()
                    invalidateOptionsMenu()
                }
                R.id.device_access_audio_play -> {
                    recordHandler.startPlaying(binding.tvResult, binding.textAnimalName)
                    invalidateOptionsMenu()
                }
                R.id.device_access_audio_stop -> {
                    recordHandler.stopPlaying()
                    invalidateOptionsMenu()
                }
                R.id.get_sounds -> {
                    viewModel.getSounds()
                }
                R.id.get_sound_types-> {
                    viewModel.getTypes()
                }
                R.id.get_sound -> {
                    serviceHandler.getSound(binding.tvResult, binding.textAnimalName)
                }
                R.id.upload_sound -> {
                    val sound = recordHandler.createDataSound(true, binding.textAnimalName)
                    viewModel.postSound(sound)
                }
                R.id.delete_sound -> {
                    viewModel.deleteSound(binding.textAnimalName.toString())
                }
                R.id.check_sound_time -> {
                    val sound = recordHandler.createDataSound(true, binding.textAnimalName)
                    viewModel.checkSoundTimeDomain(sound)
                }
                R.id.check_sound_power -> {
                    val sound = recordHandler.createDataSound(true, binding.textAnimalName)
                    viewModel.checkSoundPowerSpectrum(sound)
                }
                R.id.check_sound_freq -> {
                    val sound = recordHandler.createDataSound(true, binding.textAnimalName)
                    viewModel.checkSoundFrequencyDomain(sound)
                }
            }
            return@launch
        }

        return item?.let { super.onOptionsItemSelected(it) }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val recordAudioOption = menu!!.findItem(R.id.device_access_mic)
        val stopRecordAudioOption = menu.findItem(R.id.device_access_mic_muted)
        val playSoundOption = menu.findItem(R.id.device_access_audio_play)
        val stopPlaySoundOption = menu.findItem(R.id.device_access_audio_stop)

        if (isRecording) {
            changeMenuOptionVisibility(recordAudioOption, false)
            changeMenuOptionVisibility(stopRecordAudioOption, true)
        } else {
            changeMenuOptionVisibility(recordAudioOption, true)
            changeMenuOptionVisibility(stopRecordAudioOption, false)
        }

        if(isPlaying) {
            changeMenuOptionVisibility(playSoundOption, false)
            changeMenuOptionVisibility(stopPlaySoundOption, true)
        } else {
            changeMenuOptionVisibility(playSoundOption, true)
            changeMenuOptionVisibility(stopPlaySoundOption, false)
        }

        return super.onPrepareOptionsMenu(menu)
    }

    private fun changeMenuOptionVisibility(option: MenuItem, status:Boolean) {
        option.isEnabled = status
        option.isVisible = status
    }

}