package com.sarahsedy.piggame

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    private lateinit var btnRoll: Button
    private lateinit var btnHold: Button
    private lateinit var tvComputer: TextView
    private lateinit var tvYou: TextView
    private lateinit var tvDiceTotalNum: TextView
    private lateinit var tvPGamesWon: TextView
    private lateinit var tvComGamesWon: TextView
    private lateinit var tvPTotalScore: TextView
    private lateinit var tvComTotalScore: TextView
    private lateinit var tvPTurnTotal: TextView
    private lateinit var tvComTurnTotal: TextView
    private lateinit var ivDiceOne: ImageView
    private lateinit var ivDiceTwo: ImageView
    private lateinit var ivWinOrLose: ImageView

    private var turnScore = 0
    private var totalScore = 0
    var comTotal = 0
    var comTurn = 0
    var diceTotal = 0
    private var gamesWon = 0
    private var comGamesWon = 0

    private var winner: String = ""
    private var winnerList = ArrayList<LeaderBoardItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initApp()
    }

    private fun initApp() {
        ivDiceOne = findViewById(R.id.ivDiceOne)
        ivDiceTwo = findViewById(R.id.ivDiceTwo)
        ivWinOrLose = findViewById(R.id.ivWinOrLose)

        btnHold = findViewById(R.id.btnHold)
        btnRoll = findViewById(R.id.btnRoll)

        tvDiceTotalNum = findViewById(R.id.tvDiceTotalNum)
        tvPTurnTotal = findViewById(R.id.tvPTurnTotal)
        tvPGamesWon = findViewById(R.id.tvPGamesWon)
        tvPTotalScore = findViewById(R.id.tvPTotalScore)
        tvComGamesWon = findViewById(R.id.tvComGamesWon)
        tvComTotalScore = findViewById(R.id.tvComTotalScore)
        tvComTurnTotal = findViewById(R.id.tvComTurnTotal)
        tvYou = findViewById(R.id.tvYou)
        tvComputer = findViewById(R.id.tvComputer)

        ivDiceOne.tag = R.drawable.dice_1
        ivDiceTwo.tag = R.drawable.dice_1

        tvYou.setBackgroundColor(Color.CYAN)
        disableHoldBtn()

        tvPTurnTotal.text = getString(R.string.txtPTurnTotal, turnScore)
        tvPTotalScore.text = getString(R.string.txtPTotalScore, totalScore)
        tvComTurnTotal.text = getString(R.string.txtComTurnTotal, comTurn)
        tvComTotalScore.text = getString(R.string.txtComTotalScore, comTotal)
        tvDiceTotalNum.text = getString(R.string.txtDiceTotalNum, diceTotal)
        tvPGamesWon.text = getString(R.string.txtPGamesWon, gamesWon)
        tvComGamesWon.text = getString(R.string.txtComGamesWon, comGamesWon)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        totalScore = savedInstanceState.getInt("P_TOTAL")
        gamesWon = savedInstanceState.getInt("P_GAMES_WON")
        turnScore = savedInstanceState.getInt("P_TURN_TOTAL")
        comGamesWon = savedInstanceState.getInt("COM_GAMES_WON")
        comTotal = savedInstanceState.getInt("COM_TOTAL")
        comTurn = savedInstanceState.getInt("COM_TURN_TOTAL")
        diceTotal = savedInstanceState.getInt("DICE_TOTAL")

        tvPGamesWon.text = "$gamesWon"
        tvPTotalScore.text = "$totalScore"
        tvPTurnTotal.text = "$turnScore"
        tvComGamesWon.text = "$comGamesWon"
        tvComTotalScore.text = "$comTotal"
        tvComTurnTotal.text = "$comTurn"
        tvDiceTotalNum.text = "$diceTotal"

        val curDiceOneImg = savedInstanceState.getInt("DICE_ONE_TAG")
        ivDiceOne.setImageResource(curDiceOneImg)

        val curDiceTwoImg = savedInstanceState.getInt("DICE_TWO_TAG")
        ivDiceTwo.setImageResource(curDiceTwoImg)

        checkForComWin()
        checkForPlayerWin()

        Log.d("RESTORE STATE", "In onRestore")
    }

    override fun onSaveInstanceState(savedState: Bundle) {
        super.onSaveInstanceState(savedState)

        savedState.putInt("P_GAMES_WON", gamesWon)
        savedState.putInt("P_TOTAL", totalScore)
        savedState.putInt("P_TURN_TOTAL", turnScore)
        savedState.putInt("COM_GAMES_WON", comGamesWon)
        savedState.putInt("COM_TOTAL", comTotal)
        savedState.putInt("COM_TURN_TOTAL", comTurn)
        savedState.putInt("DICE_TOTAL", diceTotal)

        //save dice images
        savedState.putInt("DICE_ONE_TAG", getImageDrawableTag(ivDiceOne))
        savedState.putInt("DICE_TWO_TAG", getImageDrawableTag(ivDiceTwo))

        Log.d("SAVE STATE", "In onSave")

    }

    fun showLeaderBoard(v: View) {
        val intent = Intent(this, LeaderBoard::class.java)
        intent.putParcelableArrayListExtra("winnerList", winnerList)
        startActivity(intent)
    }

    fun onBtnRollClick(v: View) {
        enableHoldBtn()

        val diceRoll1 = rollDice()
        val diceRoll2 = rollDice()

        //update the dice images per roll
        changeImgOnRoll(diceRoll1, diceRoll2)

        Log.d("PLAYER ROLL VALUES", "Player rolled: $diceRoll1, $diceRoll2")

        if ((diceRoll1 == 1 && diceRoll2 != 1) || (diceRoll1 != 1 && diceRoll2 == 1)) {
            disableRollBtn()

            turnScore = 0
            diceTotal = 0

            btnHold.performClick()
        } else if (diceRoll1 == 1 && diceRoll2 == 1) {
            disableRollBtn()

            totalScore = 0
            diceTotal = 0
            turnScore = 0

            btnHold.performClick()
        } else {
            diceTotal = diceRoll1 + diceRoll2
            turnScore += diceTotal

            updatePlayerInfo()
        }
    }

    fun onBtnHoldClick(v: View) {

        val toastTxt = getText(R.string.computerToast)

        Toast.makeText(this, toastTxt, Toast.LENGTH_SHORT).show()

        tvComputer.setBackgroundColor(Color.CYAN)
        tvYou.setBackgroundColor(Color.WHITE)

        totalScore += turnScore
        turnScore = 0

        updatePlayerInfo()

        object : CountDownTimer(3000, 1000) {
            override fun onTick(p0: Long) {

                checkForPlayerWin()
                if (winner == getString(R.string.txtYou)) {
                    this.cancel()
                }

                disableRollBtn()
                disableHoldBtn()

                val diceRoll1 = rollDice()
                val diceRoll2 = rollDice()

                changeImgOnRoll(diceRoll1, diceRoll2)

                Log.d("COM ROLL  VALUES", "The computer rolled:$diceRoll1, $diceRoll2")

                if ((diceRoll1 == 1 && diceRoll2 != 1) || (diceRoll1 != 1 && diceRoll2 == 1)) {
                    comTurn = 0
                    diceTotal = 0

                    updateComInfo()

                    tvComputer.setBackgroundColor(Color.WHITE)
                    tvYou.setBackgroundColor(Color.CYAN)

                    this.cancel()

                    enableRollBtn()
                    disableHoldBtn()
                } else if (diceRoll1 == 1 && diceRoll2 == 1) {
                    comTurn = 0
                    comTotal = 0
                    diceTotal = 0

                    updateComInfo()

                    tvComputer.setBackgroundColor(Color.WHITE)
                    tvYou.setBackgroundColor(Color.CYAN)

                    this.cancel()

                    enableRollBtn()
                    disableHoldBtn()
                } else {
                    diceTotal = diceRoll1 + diceRoll2
                    comTurn += diceTotal

                    updateComInfo()
                }
            }

            override fun onFinish() {
                comTotal += comTurn

                enableRollBtn()
                disableHoldBtn()

                comTurn = 0
                diceTotal = 0

                updateComInfo()

                tvDiceTotalNum.text = getString(R.string.txtDiceTotalNum, diceTotal)

                tvComputer.setBackgroundColor(Color.WHITE)
                tvYou.setBackgroundColor(Color.CYAN)

                checkForComWin()
            }
        }.start()
    }

    private fun changeImgOnRoll(d1: Int, d2: Int) {
        when (d1) {
            1 -> {
                ivDiceOne.setImageResource(R.drawable.dice_1)
                ivDiceOne.tag = R.drawable.dice_1
            }
            2 -> {
                ivDiceOne.setImageResource(R.drawable.dice_2)
                ivDiceOne.tag = R.drawable.dice_2
            }
            3 -> {
                ivDiceOne.setImageResource(R.drawable.dice_3)
                ivDiceOne.tag = R.drawable.dice_3
            }
            4 -> {
                ivDiceOne.setImageResource(R.drawable.dice_4)
                ivDiceOne.tag = R.drawable.dice_4
            }
            5 -> {
                ivDiceOne.setImageResource(R.drawable.dice_5)
                ivDiceOne.tag = R.drawable.dice_5
            }
            6 -> {
                ivDiceOne.setImageResource(R.drawable.dice_6)
                ivDiceOne.tag = R.drawable.dice_6
            }
        }

        when (d2) {
            1 -> {
                ivDiceTwo.setImageResource(R.drawable.dice_1)
                ivDiceTwo.tag = R.drawable.dice_1
            }
            2 -> {
                ivDiceTwo.setImageResource(R.drawable.dice_2)
                ivDiceTwo.tag = R.drawable.dice_2
            }
            3 -> {
                ivDiceTwo.setImageResource(R.drawable.dice_3)
                ivDiceTwo.tag = R.drawable.dice_3
            }
            4 -> {
                ivDiceTwo.setImageResource(R.drawable.dice_4)
                ivDiceTwo.tag = R.drawable.dice_4
            }
            5 -> {
                ivDiceTwo.setImageResource(R.drawable.dice_5)
                ivDiceTwo.tag = R.drawable.dice_5
            }
            6 -> {
                ivDiceTwo.setImageResource(R.drawable.dice_6)
                ivDiceTwo.tag = R.drawable.dice_6
            }
        }
    }

    private fun rollDice(): Int {
        return (1..6).random()
    }

    private fun enableRollBtn() {
        btnRoll.isEnabled = true
        btnRoll.isClickable = true
    }

    private fun disableRollBtn() {
        btnRoll.isClickable = false
        btnRoll.isEnabled = false
    }

    private fun enableHoldBtn() {
        btnHold.isEnabled = true
        btnHold.isClickable = true
    }

    private fun disableHoldBtn() {
        btnHold.isClickable = false
        btnHold.isEnabled = false
    }

    private fun updateComInfo() {
        tvComTurnTotal.text = getString(R.string.txtComTurnTotal, comTurn)
        tvComTotalScore.text = getString(R.string.txtComTotalScore, comTotal)
        tvDiceTotalNum.text = getString(R.string.txtDiceTotalNum, diceTotal)
    }

    private fun updatePlayerInfo() {
        tvDiceTotalNum.text = getString(R.string.txtDiceTotalNum, diceTotal)
        tvPTurnTotal.text = getString(R.string.txtPTurnTotal, turnScore)
        tvPTotalScore.text = getString(R.string.txtPTotalScore, totalScore)
    }

    private fun saveHighScore() {
        val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
        val formattedDate = LocalDateTime.now().format(formatter)

        if (winner == getString(R.string.txtYou)) {
            val newWinner = LeaderBoardItem(winner, "$totalScore", formattedDate)
            winnerList += newWinner
        } else if (winner == getString(R.string.txtComp)) {
            val newWinner = LeaderBoardItem(winner, "$comTotal", formattedDate)
            winnerList += newWinner
        }
    }

    private fun checkForPlayerWin() {
        if (totalScore >= 100) {
            winner = getString(R.string.txtYou)
            ivWinOrLose.setImageResource(R.drawable.youwinimg)
            ivWinOrLose.tag = R.drawable.youwinimg
            ivWinOrLose.bringToFront()
            ivWinOrLose.visibility = View.VISIBLE
            saveHighScore()
        }
    }

    fun checkForComWin() {
        if (comTotal >= 100) {
            winner = getString(R.string.txtComp)
            ivWinOrLose.setImageResource(R.drawable.youloseimg)
            ivWinOrLose.bringToFront()
            ivWinOrLose.tag = R.drawable.youloseimg
            ivWinOrLose.visibility = View.VISIBLE
            saveHighScore()
        }
    }

    fun ivOnClick(v: View) {
        Log.d("END GAME", "Resetting the game")

        //toggle placement
        if (ivWinOrLose.tag == R.drawable.youloseimg) {
            comGamesWon += 1
            tvComGamesWon.text = getString(R.string.txtComGamesWon, comGamesWon)
        } else if (ivWinOrLose.tag == R.drawable.youwinimg) {
            gamesWon += 1
            tvPGamesWon.text = getString(R.string.txtPGamesWon, gamesWon)
        }

        ivWinOrLose.setImageResource(R.drawable.youloseimg)
        ivWinOrLose.visibility = View.INVISIBLE
        ivDiceOne.setImageResource(R.drawable.dice_1)
        ivDiceTwo.setImageResource(R.drawable.dice_1)

        tvYou.setBackgroundColor(Color.CYAN)
        tvComputer.setBackgroundColor(Color.WHITE)

        comTotal = 0
        comTurn = 0
        totalScore = 0
        turnScore = 0
        diceTotal = 0
        winner = ""

        tvPTurnTotal.text = getString(R.string.txtPTurnTotal, turnScore)
        tvPTotalScore.text = getString(R.string.txtPTotalScore, totalScore)
        tvComTurnTotal.text = getString(R.string.txtComTurnTotal, comTurn)
        tvComTotalScore.text = getString(R.string.txtComTotalScore, comTotal)
        tvDiceTotalNum.text = getString(R.string.txtDiceTotalNum, diceTotal)

        enableRollBtn()
        disableHoldBtn()
    }

    private fun getImageDrawableTag(iv: ImageView): Int {
        return iv.tag as Int
    }

}