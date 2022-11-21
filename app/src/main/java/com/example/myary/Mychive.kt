package com.example.myary

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_NO_LOCALIZED_COLLATORS
import java.io.FileInputStream
import java.io.FileOutputStream

import android.view.View
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import android.view.ViewGroup
import android.view.LayoutInflater


class Mychive : Fragment() {

    private lateinit var fname: String
    private lateinit var str: String
    private lateinit var calendarView: CalendarView
    private lateinit var updateBtn: Button
    private lateinit var deleteBtn:Button
    private lateinit var saveBtn:Button
    private lateinit var diaryTextView: TextView //선택한 날짜 보여주는 TextVew
    private lateinit var diaryContent:TextView  //일정, 다이어리 내용 보여주는 TextView
    private lateinit var title:TextView
    private lateinit var contextEditText: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_mychive, container, false)

    }


    @SuppressLint("MissingInflatedId")
    override fun onStart() {
        super.onStart()

        // UI값 생성
        calendarView= requireView().findViewById(R.id.calendarView)
        diaryTextView = requireView().findViewById(R.id.diaryTextView)
        saveBtn=requireView().findViewById(R.id.saveBtn)
        deleteBtn=requireView().findViewById(R.id.deleteBtn)
        updateBtn=requireView().findViewById(R.id.updateBtn)
        diaryContent=requireView().findViewById(R.id.diaryContent)
        title=requireView().findViewById(R.id.title)
        contextEditText=requireView().findViewById(R.id.contextEditText)

        title.text = "Mychive"

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth -> //달력 날짜 선택
            diaryTextView.visibility = View.VISIBLE    //날짜 뜸
            saveBtn.visibility = View.VISIBLE   //저장 버튼 뜸
            contextEditText.visibility = View.VISIBLE   //일정 입력하는 EditText 뜸
            diaryContent.visibility = View.INVISIBLE    //저장된 일정 내용 textView 안 보이게
            updateBtn.visibility = View.INVISIBLE       //수정 버튼 안 보임
            deleteBtn.visibility = View.INVISIBLE       //삭제 버튼 안 보임
            diaryTextView.text = String.format("%d / %d / %d", year, month + 1, dayOfMonth)     //날짜 뜨는 textView에 날짜 뜸
            contextEditText.setText("")     //EditText 공백
            checkDay(year, month, dayOfMonth)       //checkDay 호출 (달력 내용 조회, 수정)
        }

        //저장 버튼 누름
        saveBtn.setOnClickListener {
            saveDiary(fname) //saveDiary 호출
            contextEditText.visibility = View.INVISIBLE
            saveBtn.visibility = View.INVISIBLE
            updateBtn.visibility = View.VISIBLE
            deleteBtn.visibility = View.VISIBLE
            str = contextEditText.text.toString() //str 변수에 EditText 내용을 string으로 저장
            diaryContent.text = str
            diaryContent.visibility = View.VISIBLE  //일정 내용 보임
        }
    }

    // 달력 내용 조회, 수정
    fun checkDay(cYear: Int, cMonth: Int, cDay: Int) {
        //저장할 파일 이름설정
        fname = "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt" //저장할 파일 이름

        var fileInputStream: FileInputStream //파일 내용 읽어오기 fileInputStream
        try {
            fileInputStream = requireActivity().openFileInput(fname) //파일 내용 불러오기
            val fileData = ByteArray(fileInputStream.available()) // 읽은 파일 내용을 바이트 배열로
            fileInputStream.read(fileData)  //fileData 읽어오기 fileInputStream
            fileInputStream.close() //close 후 파일에 접근 가능
            str = String(fileData)  //str 변수에 fileData를 string으로 저장
            contextEditText.visibility = View.INVISIBLE     //이때 수정하는 EditText 칸은 안 보임
            diaryContent.visibility = View.VISIBLE      //입력한 일정 내용 칸 보임
            diaryContent.text = "${str}"    //EditText로 입력한 일정 내용(str) 출력
            saveBtn.visibility = View.INVISIBLE
            updateBtn.visibility = View.VISIBLE
            deleteBtn.visibility = View.VISIBLE         //저장 버튼은 안 보이고, 수정과 삭제 버튼이 보임
            
            //수정버튼 누름
            updateBtn.setOnClickListener {
                contextEditText.visibility = View.VISIBLE       //EditText 칸이 보임
                diaryContent.visibility = View.INVISIBLE        //내용 보이던 TextView는 안 보임, 수정할 거니까
                contextEditText.setText(str)        //editText에 TextView에 저장됐던 내용 출력, 수정 위해
                saveBtn.visibility = View.VISIBLE   
                updateBtn.visibility = View.INVISIBLE
                deleteBtn.visibility = View.INVISIBLE       //저장 버튼 보이게, 나머지 버튼은 안 보이게
                diaryContent.text = contextEditText.text    //내용 보이는 TextView에 EditText로 작성한 내용 저장
            }
            
            //삭제버튼 누름
            deleteBtn.setOnClickListener {
                diaryContent.visibility = View.INVISIBLE
                updateBtn.visibility = View.INVISIBLE
                deleteBtn.visibility = View.INVISIBLE
                contextEditText.setText("")     //EditText 부분이 공백으로 바뀜
                contextEditText.visibility = View.VISIBLE       //EditText는 보이게, 내용 저장되어 보여주던 TextView(diaryContent)는 안 보이게
                saveBtn.visibility = View.VISIBLE       //처음 화면처럼 내용 없으니까 저장 버튼 보이게
                removeDiary(fname)      //removeDiary 호출
            }
            
            //diaryContent(TextView)에 아무것도 없다면
            if (diaryContent.text == null) {
                diaryContent.visibility = View.INVISIBLE
                updateBtn.visibility = View.INVISIBLE
                deleteBtn.visibility = View.INVISIBLE
                diaryTextView.visibility = View.VISIBLE     //날짜는 보여주고
                saveBtn.visibility = View.VISIBLE
                contextEditText.visibility = View.VISIBLE       //내용 입력할 수 있게 EditText와 저장버튼 뜨게 함
            }
        } catch (e: Exception) {
            e.printStackTrace()     //에러 발생하면 발생 이유와 위치 로그에 뜨게 함
        }
    }


    // 달력 내용 제거 메소드
    @SuppressLint("WrongConstant")  //Ensures that when parameter in a method only allows a specific set of
    //constants, calls obey those rules.
    fun removeDiary(readDay: String?) {
        var fileOutputStream: FileOutputStream
        try {
            fileOutputStream = requireActivity().openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS)
            val content = ""    //내용 공백
            fileOutputStream.write(content.toByteArray())
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    // 달력 내용 추가 메소드
    @SuppressLint("WrongConstant")
    fun saveDiary(readDay: String?) {
        var fileOutputStream: FileOutputStream //파일 생성
        try {
            fileOutputStream = requireActivity().openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS)
            val content = contextEditText.text.toString() //EditText의 내용을 string으로, content에 저장
            fileOutputStream.write(content.toByteArray())   //파일 작성
            fileOutputStream.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}
