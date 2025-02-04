package com.example.beeradviser

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.TypedValue
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    // Объявляем глобальную переменную для Bitmap
    private lateinit var originalBitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val findBeer = findViewById<Button>(R.id.find_beer)
        findBeer.setOnClickListener {
            val beerColor = findViewById<Spinner>(R.id.beer_color)
            val color = beerColor.selectedItem
            val beerList = getBeers(color.toString())
            val beers = beerList.reduce { str, item -> str + '\n' + item }
            val brands = findViewById<TextView>(R.id.brands)
            brands.text = beers
            setBeerImg(beerColor.selectedItemPosition)
        }
        // Создаем объект Options
        val options = BitmapFactory.Options().apply {
            // Отключаем автоматическое масштабирование
            inScaled = false
            // Указываем плотность ресурса (например, для xhdpi — 320)
            inDensity = TypedValue.DENSITY_DEFAULT
            // Указываем целевую плотность (плотность экрана устройства)
            inTargetDensity = resources.displayMetrics.densityDpi
        }
        // Загружаем изображение с учетом настроек
        originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.beer_fix, options)
    }

    private fun getBeers(color: String): List<String> {
        return when (color) {
            "Light" -> listOf("Jail Pale Ale", "Lager Lite")
            "Amber" -> listOf("Jack Amber", "Red Moose")
            "Brown" -> listOf("Brown Bear Beer", "Bock Brownie")
            else -> listOf("Gout Stout", "Dark Daniel")
        }
    }
    @SuppressLint("ResourceType")
    private fun setBeerImg(idx: Int) {
        // Проверяем размеры изображения
        val imageView: ImageView = findViewById(R.id.imageView)
        val array = resources.obtainTypedArray(R.array.beer_detail)
        val croppedBitmap = Bitmap.createBitmap(
            originalBitmap,
            array.getTextArray(1)[idx].toString().toInt(),
            array.getTextArray(2)[idx].toString().toInt(),
            resources.getInteger(R.integer.BitMap_pos_w),
            resources.getInteger(R.integer.BitMap_pos_h)
        )
        // Устанавливаем вырезанный Bitmap в ImageView
        imageView.setImageBitmap(croppedBitmap)
        // Убедитесь, что scaleType установлен правильно
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER

        array.recycle()
    }
}