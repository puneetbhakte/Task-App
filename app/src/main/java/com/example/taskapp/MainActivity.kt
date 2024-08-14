package com.example.taskapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.adapter.Adapter
import com.example.taskapp.adapter.CategoryAdapter
import com.example.taskapp.adapter.CategoryItemClickListener
import com.example.taskapp.database.Database
import com.example.taskapp.databinding.ActivityMainBinding
import com.example.taskapp.databinding.AddTaskBottomSheetBinding
import com.example.taskapp.model.Category
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

class MainActivity : AppCompatActivity(),CategoryItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dialog: BottomSheetDialog
    private lateinit var bottomSheet: AddTaskBottomSheetBinding
    private lateinit var viewModel: ViewModel
    private lateinit var catogaryDialogView: View
    private lateinit var dialogViewPriority: View
    private lateinit var adapter:Adapter
    private lateinit var task:Task
    private lateinit var alertDialog:AlertDialog
    private lateinit var categoryAdapter: CategoryAdapter
    var title:String? = null
    var description:String? = null
    var date:String? = null
    var minute:String? = null
    var hour:String? = null
    var priority:String? = null
    var category:String? = null
    var selectedPriorityCard: MaterialCardView? = null
    private lateinit var customCategory: List<Category>
    private  var selectedColor:Int? = null
    private  var selectedIcon:Int = 0
    private var name:String? = null
    private lateinit var imageTextPairsPriority: List<Pair<MaterialCardView, TextView>>
    private lateinit var imageTextPairs: List<Pair<MaterialCardView, TextView>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomSheet = AddTaskBottomSheetBinding.inflate(layoutInflater)
        dialog = BottomSheetDialog(this, com.google.android.material.R.style.Base_Theme_Material3_Dark_BottomSheetDialog)
        dialog.setContentView(bottomSheet.root)

        setUpViewModel()

        viewModel.getTask().observe(this, Observer {
            if (it.isEmpty()){
                binding.emptyTask.isVisible = true
                binding.emptyTaskTv.isVisible = true
                binding.emptyTaskTview.isVisible = true
                binding.rvData.isVisible = false
                binding.search.isVisible = false
                adapter = Adapter(this,it)
            }else{
                adapter = Adapter(this, it)
                binding.rvData.isVisible = true
                binding.search.isVisible = true
                binding.emptyTask.isVisible = false
                binding.emptyTaskTv.isVisible = false
                binding.emptyTaskTview.isVisible = false
                binding.rvData.layoutManager = LinearLayoutManager(this)
                binding.rvData.adapter = adapter
            }
            binding.search.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    adapter.filter(s.toString())
                }
            }
            )
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



        binding.addTask.setOnClickListener {
            bottomSheet.taskPriority.isVisible = true
            bottomSheet.timePicker.isVisible = true
            bottomSheet.category.isVisible = true
            bottomSheet.send.isVisible = true
            bottomSheet.detail.isVisible = false
            bottomSheet.taskTitle.text = "Add Task"
            dialog.show()
        }

        bottomSheet.timePicker.setOnClickListener {
            datePicker.show(supportFragmentManager, "DATE_PICKER")
        }

        bottomSheet.taskPriority.setOnClickListener {
            showTaskPriorityDialog()
        }

        bottomSheet.category.setOnClickListener {
            showCatogaryDialog()
        }

        binding.rvData.setOnClickListener {
            val intent = Intent(this,DetailTask::class.java)
            startActivity(intent)
        }

        bottomSheet.send.setOnClickListener {
            title = bottomSheet.titleTv.text.toString()
            description = bottomSheet.taskDescription.text.toString()
            if (title.isNullOrBlank() || description.isNullOrBlank()||minute.isNullOrBlank()||hour.isNullOrBlank()){
                Toast.makeText(this, "Enter Title,Description and Date Time", Toast.LENGTH_SHORT).show()
            }else{
                task = Task(0, title!!, description!!,date,category,priority,minute,hour)
                viewModel.upsertTask(task)
                dialog.dismiss()

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
            alertDialog.dismiss()
        }

    }

    private fun showCatogaryDialog() {
        catogaryDialogView = LayoutInflater.from(this).inflate(R.layout.category, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(catogaryDialogView)
        alertDialog = dialogBuilder.create()
        alertDialog.show()

        imageTextPairs = listOf(
            Pair(
                catogaryDialogView.findViewById(R.id.grocery),
                catogaryDialogView.findViewById(R.id.grocery_tv)
            ),
            Pair(
                catogaryDialogView.findViewById(R.id.work),
                catogaryDialogView.findViewById(R.id.work_tv)
            ),
            Pair(
                catogaryDialogView.findViewById(R.id.sport),
                catogaryDialogView.findViewById(R.id.sport_tv)
            ),
            Pair(
                catogaryDialogView.findViewById(R.id.design),
                catogaryDialogView.findViewById(R.id.design_tv)
            ),
            Pair(
                catogaryDialogView.findViewById(R.id.university),
                catogaryDialogView.findViewById(R.id.university_tv)
            ),
            Pair(
                catogaryDialogView.findViewById(R.id.social),
                catogaryDialogView.findViewById(R.id.social_tv)
            ),
            Pair(
                catogaryDialogView.findViewById(R.id.music),
                catogaryDialogView.findViewById(R.id.music_tv)
            ),
            Pair(
                catogaryDialogView.findViewById(R.id.health),
                catogaryDialogView.findViewById(R.id.health_tv)
            ),
            Pair(
                catogaryDialogView.findViewById(R.id.movie),
                catogaryDialogView.findViewById(R.id.movie_tv)
            ),
            Pair(
                catogaryDialogView.findViewById(R.id.home),
                catogaryDialogView.findViewById(R.id.home_tv)
            ),

            )
        imageTextPairs.forEach { pair ->
            pair.first?.setOnClickListener {
                Toast.makeText(this, pair.second?.text.toString(), Toast.LENGTH_SHORT).show()
                category = pair.second.text.toString()
                alertDialog.dismiss()
            }


            catogaryDialogView.findViewById<MaterialCardView>(R.id.add).setOnClickListener {
                val intent = Intent(this, CreateNewCategory::class.java)
                startActivity(intent)
            }

            viewModel.getCategory().observe(this, Observer {
                categoryAdapter = CategoryAdapter(this,it,this)
                customCategory = it
                catogaryDialogView.findViewById<RecyclerView>(R.id.catedory_rv).layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
                catogaryDialogView.findViewById<RecyclerView>(R.id.catedory_rv).adapter = categoryAdapter

            })




        }
    }

    private fun convertMillisecondsToDate(milliseconds: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date(milliseconds)
        return sdf.format(date)
    }

    override fun onCategoryClick(position: Int) {
        category = customCategory[position].name
        selectedIcon = customCategory[position].icon
        selectedColor = customCategory[position].color
        Toast.makeText(this, category, Toast.LENGTH_SHORT).show()
        alertDialog.dismiss()
    }
}