package com.example.quiz

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val questionView =  findViewById<TextView>(R.id.questionText);

        val quizlistview = findViewById<ListView>(R.id.QuizList);
        quizlistview.adapter= QuizAdapter(this);

    }

    private class QuizAdapter(context: Context) : BaseAdapter(){
        private val myContext: Context
        private val url = "https://ghibliapi.herokuapp.com"
        init {
            myContext = context
        }
        override fun getCount(): Int {
            return 5
        }

        override fun getItem(position: Int): Any {
            return "test"
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(myContext)
            val quizRow = layoutInflater.inflate(R.layout.quizrow_main, viewGroup,  false)
            val retrofit = Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build()
            val peopleService = retrofit.create(Ipeople::class.java)
            val peoples = peopleService.getPeople()
            var nameview = quizRow.findViewById<TextView>(R.id.nametextview)

            peoples.enqueue(object: Callback<List<People>>{
                override fun onFailure(call: Call<List<People>>, t: Throwable) {
                    Log.d("Error", "Api call failed")
                }

                override fun onResponse(call: Call<List<People>>, response: Response<List<People>>) {

                    val arraypoeples = response.body()
                    arraypoeples?.let{
                        for (people in it) {
                            Log.d("people: ", people.name)
                        }
                    }
                }
            })
            return quizRow
        }
    }
    interface Ipeople{
        @GET("people")
        fun getPeople(): Call<List<People>>
    }
    class People {
        var id: String? = null
        var name:String? = null
        var gender:String? = null
        var films:String? = null
        var age:String? = null
        var eye_color:String? = null
        var hair_color:String? = null
        var species:String? = null
        var url:String?= null
    }
}
