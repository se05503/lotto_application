package com.example.mylottery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    //변수 선언 부분
    private val addButton by lazy { findViewById<Button>(R.id.btn_add) }
    private val runButton by lazy { findViewById<Button>(R.id.btn_run)}
    private val clearButton by lazy {findViewById<Button>(R.id.btn_clear)}
    private val numPick by lazy {findViewById<NumberPicker>(R.id.np_num)}

    //TextView 공 6개가 들어가는 리스트 생성
    private val numTextViewList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById(R.id.tv_num1),
            findViewById(R.id.tv_num2),
            findViewById(R.id.tv_num3),
            findViewById(R.id.tv_num4),
            findViewById(R.id.tv_num5),
            findViewById(R.id.tv_num6)
        )
    }

    private var didRun = false //didRun 은 자동 생성 시작 버튼이 눌리지 않았을 때 false, 눌렸을 때 true 이다.
    private val pickNumberSet = hashSetOf<Int>() // 사용자가 고른 숫자를 넣는 공간

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // numberPicker 에서 사용할 수 있는 숫자 제한
        numPick.minValue=1
        numPick.maxValue=45

        initAddButton()
        initRunButton()
        initClearButton()
    }

    private fun initAddButton() {
        addButton.setOnClickListener {
            when {
                // 사용자가 고른 번호를 추가할 때 발생시키는 예외
                didRun -> showToast("번호를 더이상 추가할 수 없습니다! 초기화 해주세요!")
                pickNumberSet.size >= 5 -> showToast("더이상 번호를 추가할 수 없습니다. 생성 시작 버튼을 눌러주세요!")
                pickNumberSet.contains(numPick.value) -> showToast("이미 선택한 숫자입니다! 중복 선택을 불가합니다!")

                // 예외가 발생하지 않는 경우 사용자가 선택한 번호를 보이게 해야함
                else -> {
                    val textView = numTextViewList[pickNumberSet.size]
                    textView.isVisible= true
                    textView.text=numPick.value.toString()
                    setNumBack(numPick.value, textView)
                    pickNumberSet.add(numPick.value)
                }
            }
        }
    }

    private fun initClearButton() {
        clearButton.setOnClickListener {
            numPick.value=1
            pickNumberSet.clear() // 초기화
            numTextViewList.forEach { it.isVisible=false }
            didRun = false
        }
    }

    private fun initRunButton() {
        runButton.setOnClickListener {
            val list = getRandom() // {1,2,3,4,5,6}
            didRun = true
            list.forEachIndexed { index, number ->
                val textView = numTextViewList[index]
                textView.text=number.toString()
                textView.isVisible=true
                setNumBack(number,textView)
            }
        }
    }



    // 매번 긴 한 줄을 입력하기는 힘드니 함수로 지정함
    private fun showToast(message : String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // 공의 색깔을 숫자의 범위에 맞게 설정하는 함수
    private fun setNumBack(num : Int, textview : TextView) {
        val background = when (num) {
            in 1..10 -> R.drawable.circle_yellow
            in 11..20 -> R.drawable.circle_blue
            in 21..30 -> R.drawable.circle_green
            in 31..40 -> R.drawable.circle_gray
            else -> R.drawable.circle_red
        }
        textview.background=ContextCompat.getDrawable(this, background)
    }

    private fun getRandom() : List<Int> {
        val numbers = (1..45).filter { it !in pickNumberSet }
        return (pickNumberSet + numbers.shuffled().take(6-pickNumberSet.size)).sorted()
    }
}