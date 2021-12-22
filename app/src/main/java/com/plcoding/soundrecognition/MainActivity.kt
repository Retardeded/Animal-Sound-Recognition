package com.plcoding.soundrecognition

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.plcoding.soundrecognition.databinding.ActivityMainBinding
import com.plcoding.soundrecognition.soundprocessing.GraphHandler
import com.plcoding.soundrecognition.viewmodels.MainViewModel
import com.plcoding.soundrecognition.server.SoundServiceHandler
import com.plcoding.soundrecognition.soundprocessing.RecordHandler
import com.plcoding.soundrecognition.soundprocessing.RecordHandler.Companion.isPlaying
import com.plcoding.soundrecognition.soundprocessing.RecordHandler.Companion.isRecording
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import android.widget.Toast

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val graphHandler: GraphHandler by viewModels()
    lateinit var recordHandler: RecordHandler
    private val viewModel: MainViewModel by viewModels()
    lateinit var fileName:String
    private val MY_PERMISSIONS_RECORD_AUDIO = 1

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fileName = "${externalCacheDir?.absolutePath}/currentSound.3gp"
        recordHandler = RecordHandler(graphHandler, this)

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

        requestAudioPermissions()
    }

    private fun requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.RECORD_AUDIO
                )
            ) {
                Toast.makeText(this, "Please grant permissions to record audio", Toast.LENGTH_LONG)
                    .show()

                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.RECORD_AUDIO),
                    MY_PERMISSIONS_RECORD_AUDIO
                )
            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.RECORD_AUDIO),
                    MY_PERMISSIONS_RECORD_AUDIO
                )
            }
        } else if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            )
            == PackageManager.PERMISSION_GRANTED
        ) {

            //
        }
    }

    override fun onStart() {
        super.onStart()
        graphHandler.initGraphView(binding.graphTime, binding.graphAplitude, binding.graphFreqFull)
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
                    recordHandler.startRecording(fileName)
                    invalidateOptionsMenu()
                }
                R.id.device_access_mic_muted -> {
                    recordHandler.stopRecording()
                    invalidateOptionsMenu()
                }
                R.id.device_access_audio_play -> {
                    recordHandler.startPlaying(binding.tvResult, binding.textAnimalName, binding.textAnimalType, fileName)
                    //invalidateOptionsMenu()
                }
                R.id.device_access_audio_stop -> {
                    recordHandler.stopPlaying()
                    //invalidateOptionsMenu()
                }
                R.id.get_sounds -> {
                    viewModel.getSounds()
                }
                R.id.get_sound_types-> {
                    viewModel.getTypes()
                }
                R.id.get_sound -> {
                    viewModel.getSound(binding.textAnimalName.text.toString(), graphHandler.dataGraphs)
                    //serviceHandler.getSound(binding.tvResult, binding.textAnimalName, graphHandler.dataGraphs)
                }
                R.id.upload_sound -> {
                    val sound = graphHandler.createDataSound(true, binding.textAnimalName, binding.textAnimalType)
                    viewModel.postSound(sound)
                }
                R.id.delete_sound -> {
                    //serviceHandler.deleteSound(binding.tvResult, binding.textAnimalName)
                    viewModel.deleteSound(binding.textAnimalName.text.toString())
                }
                R.id.check_sound_time -> {
                    val sound = graphHandler.createDataSound(true, binding.textAnimalName, binding.textAnimalType)
                    viewModel.checkSoundTimeDomain(sound)
                }
                R.id.check_sound_power -> {
                    val sound = graphHandler.createDataSound(true, binding.textAnimalName, binding.textAnimalType)
                    viewModel.checkSoundPowerSpectrum(sound)
                }
                R.id.check_sound_freq -> {
                    val sound = graphHandler.createDataSound(true, binding.textAnimalName, binding.textAnimalType)
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