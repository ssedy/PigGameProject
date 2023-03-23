package com.sarahsedy.piggame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

const val LEADERBOARD_LOG = "lblog.txt"

class LeaderBoard : AppCompatActivity() {
    private lateinit var leaderBoardListAdapter: PigAdapter

    private var scoreList = ArrayList<LeaderBoardItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leader_board)

        val rView: RecyclerView = findViewById(R.id.rView)
        leaderBoardListAdapter = PigAdapter(scoreList)

        rView.layoutManager = LinearLayoutManager(applicationContext)
        rView.itemAnimator = DefaultItemAnimator()
        rView.adapter = leaderBoardListAdapter

        val extras = intent.extras

        if (extras != null) {
            val winList: ArrayList<LeaderBoardItem>? = extras.getParcelableArrayList("winnerList")
            val fileOutputStream: FileOutputStream = openFileOutput(LEADERBOARD_LOG, MODE_APPEND)
            val leaderBoardFile = OutputStreamWriter(fileOutputStream)

            winList?.forEach {
                leaderBoardFile.write(it.toCSV())
            }
            leaderBoardFile.close()
        }

        readLogData()
    }

    private fun readLogData() {
        val file = File(filesDir, LEADERBOARD_LOG)
        if (file.exists()) {
            File(filesDir, LEADERBOARD_LOG).forEachLine {
                val parts = it.split(",")

                val lbItem = LeaderBoardItem(parts[0], parts[1], parts[2])
                scoreList.add(lbItem)
                scoreList.reverse()
            }
        }
        leaderBoardListAdapter.notifyDataSetChanged()
    }

    fun showGame(v: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}