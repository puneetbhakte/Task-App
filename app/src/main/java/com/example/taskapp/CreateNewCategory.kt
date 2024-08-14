package com.example.taskapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskapp.adapter.CategoryAdapter
import com.example.taskapp.adapter.ColorAdapter
import com.example.taskapp.adapter.IconAdapter
import com.example.taskapp.adapter.IconItemClickListener
import com.example.taskapp.adapter.ItemClickListener
import com.example.taskapp.database.Database
import com.example.taskapp.databinding.ActivityCreateNewCategoryBinding
import com.example.taskapp.model.Category
import com.example.taskapp.repository.Repository
import com.example.taskapp.viewmodel.ViewModel
import com.example.taskapp.viewmodel.ViewModelFactory

class CreateNewCategory : AppCompatActivity() , ItemClickListener, IconItemClickListener {
    private lateinit var binding: ActivityCreateNewCategoryBinding
    private lateinit var viewModel: ViewModel
    private lateinit var icon:ArrayList<Int>
    private lateinit var color:ArrayList<Int>
    private lateinit var iconAdapter: IconAdapter
    private lateinit var colorAdapter: ColorAdapter
    private lateinit var category: Category
    private  var selectedColor:Int? = null
    private  var selectedIcon:Int? = null
    private var title:String? = null
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var customCategory: ArrayList<Category>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateNewCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewModel()


    //        viewModel.getCategory().observe(this, Observer {
      //          categoryAdapter = CategoryAdapter(this,it,this)
        //        binding.catedoryRv.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
          //      binding.catedoryRv.adapter = categoryAdapter

            //})


        icon = ArrayList<Int>()
        icon.add(R.drawable.bread_1)
        icon.add(R.drawable.briefcase_1)
        icon.add(R.drawable.sport_1)
        icon.add(R.drawable.design__1__1)
        icon.add(R.drawable.vector)
        icon.add(R.drawable.home__2__1)
        icon.add(R.drawable.mortarboard_1)
        icon.add(R.drawable.music)
        icon.add(R.drawable.video_camera_1)
        icon.add(R.drawable.megaphone_1)


        color = ArrayList<Int>()
        color.add(R.color.yelow)
        color.add(R.color.pink)
        color.add(R.color.red)
        color.add(R.color.blue)
        color.add(R.color.light_blue)
        color.add(R.color.light_green)
        color.add(R.color.green)
        color.add(R.color.orange)
        color.add(R.color.brown)
        color.add(R.color.purple)

        binding.colorIv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        colorAdapter = ColorAdapter(this,color,this)
        binding.colorIv.adapter = colorAdapter

        binding.iconRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        iconAdapter = IconAdapter(this,icon,this)
        binding.iconRv.adapter = iconAdapter

        binding.save.setOnClickListener {
            title= binding.newCategory.text.toString()
            category = Category(0, title!!,selectedColor!!,selectedIcon!!)
            viewModel.insertCategory(category)
            onBackPressed()
        }
        binding.cancle.setOnClickListener {
            onBackPressed()
        }

    }

    private fun setUpViewModel() {
        val repository = Repository(Database(this))
        val viewModelProviderFactory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory)[ViewModel::class.java]
    }

    override fun onItemClick(position: Int) {
       selectedColor =  color[position]
    }

    override fun onIconItemClick(position: Int) {
        selectedIcon = icon[position]
    }
}