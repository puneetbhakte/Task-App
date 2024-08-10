package com.example.taskapp

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.taskapp.database.Database
import com.example.taskapp.databinding.ActivityDetailTaskBinding
import com.example.taskapp.databinding.AddTaskBottomSheetBinding
import com.example.taskapp.model.Task
import com.example.taskapp.repository.Repository
import com.example.taskapp.viewmodel.ViewModel
import com.example.taskapp.viewmodel.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.min

class DetailTask : AppCompatActivity() {
    private lateinit var binding:ActivityDetailTaskBinding
    private lateinit var dialog: BottomSheetDialog
    private lateinit var bottomSheet: AddTaskBottomSheetBinding
    private lateinit var viewModel: ViewModel
    private lateinit var imageTextPairs: List<Pair<MaterialCardView, TextView>>
    private lateinit var imageTextPairsPriority: List<Pair<MaterialCardView, TextView>>
    private lateinit var catogaryDialogView: View
    private lateinit var dialogViewPriority: View
    private lateinit var task:Task
    var title:String? = null
    var description:String? = null
    var date:String? = null
    var minute:String? = null
    var hour:String? = null
    var priority:String? = null
    var category:String? = null
    var selectedPriorityCard: MaterialCardView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomSheet = AddTaskBottomSheetBinding.inflate(layoutInflater)
        dialog = BottomSheetDialog(
            this,
            com.google.android.material.R.style.Base_Theme_Material3_Dark_BottomSheetDialog
        )
        dialog.setContentView(bottomSheet.root)
        setUpViewModel()
        val id = intent.getIntExtra("id", 0)

        viewModel.getTaskById(id).observe(this, Observer {

            if (it != null){
                addDrawable(it.category)

            val drawableFlag = ContextCompat.getDrawable(this, R.drawable.flag)
            drawableFlag?.setBounds(0, 0, 60, 60)
            binding.priorityD.setCompoundDrawables(drawableFlag, null, null, null)
                if (it.hour.isNullOrBlank() || it.minute.isNullOrBlank()){
                    binding.timeD.text = "DeadLine : Not Defined"
                }else{
                    binding.timeD.text = "DeadLine: ${it.hour}:${it.minute}"
                }
            binding.titleD.text = it.title
            binding.descriptionD.text = it.description
                if ((it.category.isNullOrBlank())) {
                    binding.categoryD.text = "Not Defined"
                }else{
                    binding.categoryD.text = it.category
                }
                if (it.priority.isNullOrBlank()){
                    binding.priorityD.text = "Default"
                }else{
                    binding.priorityD.text = it.priority
                }


                title = binding.titleD.text.toString()
                category = it.category
                description = binding.descriptionD.toString()
                minute = it.minute
                date = it.date
                priority = it.priority
                hour = it.hour
                }
        })

        val calendar = Calendar.getInstance()
        val hours = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select a date")
            .build()
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(hours)
            .setMinute(minutes)
            .setTitleText("Select a time")
            .build()
        datePicker.addOnPositiveButtonClickListener { selection ->
            timePicker.show(supportFragmentManager,"Time Picker")
            val selectedDate = convertMillisecondsToDate(selection)
            date = selectedDate
        }
        timePicker.addOnPositiveButtonClickListener{
            hour = timePicker.hour.toString()
            minute = timePicker.minute.toString()

        }

        bottomSheet.timePicker.setOnClickListener {
            datePicker.show(supportFragmentManager, "DATE_PICKER")
        }

        binding.priorityD.setOnClickListener {
            showTaskPriorityDialog()
        }

        binding.categoryD.setOnClickListener {
            showCatogaryDialog()
        }

        binding.edit.setOnClickListener {
            bottomSheet.taskPriority.isVisible = false
            bottomSheet.timePicker.isVisible = false
            bottomSheet.category.isVisible = false
            bottomSheet.send.isVisible = false
            bottomSheet.detail.isVisible = true
            dialog.show()



        }

        bottomSheet.save.setOnClickListener {
           title = bottomSheet.titleTv.text.toString()
           description = bottomSheet.taskDescription.text.toString()
            binding.titleD.text = title
            binding.descriptionD.text = description
            dialog.dismiss()
        }
        bottomSheet.cancle.setOnClickListener {
            dialog.dismiss()
        }

        binding.add.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.delete.setOnClickListener {

            title = binding.titleD.text.toString()
            description = binding.descriptionD.text.toString()
            if (title.isNullOrBlank() || description.isNullOrBlank()) {
                Toast.makeText(this, "Enter Title and Description", Toast.LENGTH_SHORT).show()
            } else {
                task = Task(id, title!!, description!!, date, category, priority, minute, hour)
                viewModel.deleteTask(task)
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

        binding.timeD.setOnClickListener {
            timePicker.show(supportFragmentManager,"Time Picker")
        }

        binding.editTask.setOnClickListener {
            bottomSheet.titleTv.setText(title)
            bottomSheet.taskDescription.setText(description)
            bottomSheet.taskTitle.text = "Edit Details"

            if (title.isNullOrBlank() || description.isNullOrBlank()) {
                Toast.makeText(this, "Enter Title and Description", Toast.LENGTH_SHORT).show()
            } else {
                title = binding.titleD.text.toString()
                description = binding.descriptionD.text.toString()
                task = Task(id, title!!, description!!, date, category, priority, minute, hour)
                viewModel.upsertTask(task)
                dialog.dismiss()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }


        }
    }

    private fun setUpViewModel() {
        val repository = Repository(Database(this))
        val viewModelProviderFactory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory)[ViewModel::class.java]
    }

    private fun showTaskPriorityDialog() {
        dialogViewPriority = LayoutInflater.from(this).inflate(R.layout.task_priority, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogViewPriority)
        val alertDialog = dialogBuilder.create()
        alertDialog.show()

        imageTextPairsPriority = listOf(
            Pair(dialogViewPriority.findViewById(R.id.one), dialogViewPriority.findViewById(R.id.one_tv)),
            Pair(dialogViewPriority.findViewById(R.id.two), dialogViewPriority.findViewById(R.id.two_tv)),
            Pair(dialogViewPriority.findViewById(R.id.three), dialogViewPriority.findViewById(R.id.three_tv)),
            Pair(dialogViewPriority.findViewById(R.id.four), dialogViewPriority.findViewById(R.id.four_tv)),
            Pair(dialogViewPriority.findViewById(R.id.five), dialogViewPriority.findViewById(R.id.five_tv)),
            Pair(dialogViewPriority.findViewById(R.id.six),dialogViewPriority.findViewById(R.id.six_tv)),
            Pair(dialogViewPriority.findViewById(R.id.seven), dialogViewPriority.findViewById(R.id.seven_tv)),
            Pair(dialogViewPriority.findViewById(R.id.eight), dialogViewPriority.findViewById(R.id.eight_tv)),
            Pair(dialogViewPriority.findViewById(R.id.nine), dialogViewPriority.findViewById(R.id.nine_tv)),
            Pair(dialogViewPriority.findViewById(R.id.ten), dialogViewPriority.findViewById(R.id.ten_tv)),
        )
        imageTextPairsPriority.forEach { pair ->
            pair.first?.setOnClickListener {
                selectedPriorityCard?.setCardBackgroundColor(getResources().getColor(R.color.grey))

                // Set the current card background
                pair.first.setCardBackgroundColor(getResources().getColor(R.color.purple))

                // Update the selected priority and selected card
                priority = pair.second.text.toString()
                selectedPriorityCard = pair.first
            }
        }

        val save =  dialogViewPriority.findViewById<MaterialButton>(R.id.save)
        val cancle =  dialogViewPriority.findViewById<MaterialButton>(R.id.cancle)
        cancle.setOnClickListener {
            priority = null
            alertDialog.dismiss()
        }
        save.setOnClickListener {
            binding.priorityD.text = priority
            alertDialog.dismiss()
        }

    }

    private fun showCatogaryDialog(){
        catogaryDialogView = LayoutInflater.from(this).inflate(R.layout.category, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(catogaryDialogView)
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
        imageTextPairs = listOf(
            Pair(catogaryDialogView.findViewById(R.id.grocery), catogaryDialogView.findViewById(R.id.grocery_tv)),
            Pair(catogaryDialogView.findViewById(R.id.work), catogaryDialogView.findViewById(R.id.work_tv)),
            Pair(catogaryDialogView.findViewById(R.id.sport), catogaryDialogView.findViewById(R.id.sport_tv)),
            Pair(catogaryDialogView.findViewById(R.id.design), catogaryDialogView.findViewById(R.id.design_tv)),
            Pair(catogaryDialogView.findViewById(R.id.university), catogaryDialogView.findViewById(R.id.university_tv)),
            Pair(catogaryDialogView.findViewById(R.id.social),catogaryDialogView.findViewById(R.id.social_tv)),
            Pair(catogaryDialogView.findViewById(R.id.music), catogaryDialogView.findViewById(R.id.music_tv)),
            Pair(catogaryDialogView.findViewById(R.id.health), catogaryDialogView.findViewById(R.id.health_tv)),
            Pair(catogaryDialogView.findViewById(R.id.movie), catogaryDialogView.findViewById(R.id.movie_tv)),
            Pair(catogaryDialogView.findViewById(R.id.home), catogaryDialogView.findViewById(R.id.home_tv)),

            )
        imageTextPairs.forEach { pair ->
            pair.first?.setOnClickListener {
                Toast.makeText(this, pair.second?.text.toString(), Toast.LENGTH_SHORT).show()
                category = pair.second.text.toString()
                binding.categoryD.text = category
                addDrawable(category)
                alertDialog.dismiss()
            }


        }

    }

    private fun convertMillisecondsToDate(milliseconds: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date(milliseconds)
        return sdf.format(date)
    }

    private fun addDrawable(string: String?){
        val drawable: Drawable?
        when (string) {
            "University" -> drawable = ContextCompat.getDrawable(this, R.drawable.mortarboard_1)
            "Grocery" -> drawable = ContextCompat.getDrawable(this, R.drawable.bread_1)
            "Work" -> drawable = ContextCompat.getDrawable(this, R.drawable.briefcase_1)
            "Sport" -> drawable = ContextCompat.getDrawable(this, R.drawable.sport_1)
            "Design" -> drawable = ContextCompat.getDrawable(this, R.drawable.design__1__1)
            "Social" -> drawable = ContextCompat.getDrawable(this, R.drawable.megaphone_1)
            "Music" -> drawable = ContextCompat.getDrawable(this, R.drawable.music)
            "Health" -> drawable = ContextCompat.getDrawable(this, R.drawable.vector)
            "Movie" -> drawable = ContextCompat.getDrawable(this, R.drawable.video_camera_1)
            "Home" -> drawable = ContextCompat.getDrawable(this, R.drawable.home__2__1)
            else -> drawable = null
        }
        drawable?.setBounds(0, 0, 60, 60)
        binding.categoryD.setCompoundDrawables(drawable, null, null, null)

    }


}