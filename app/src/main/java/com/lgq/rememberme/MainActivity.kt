package com.lgq.rememberme

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvTitle.text = "生成的密码是由特殊算法生成的"
        btnExecute.setOnClickListener {
            if (checkLegal()){
                tvResult.text = factoryPassword()
                Log.i("lgq","length:" + tvResult.text.length)
            }else{
                Toast.makeText(this,"用户名和网页url都必须输入",Toast.LENGTH_SHORT).show()
            }
        }

    }

    /**
     * 检查用户名和url是否有效
     */
    private fun checkLegal():Boolean{
        return !(TextUtils.isEmpty(etUsername.text.toString()) || TextUtils.isEmpty(etUrl.text.toString()))
    }

    /**
     * 截取固定的长度设置为密码
     * 如果前后出现相同字符则舍弃
     */
    private fun factoryPassword():String{
        val password =  createPassword(etUsername.text.toString(),etUrl.text.toString())
        val stringBuffer = StringBuffer()
        for (i in 1..password.length){
            if (password[i-1] != password[i]){
                stringBuffer.append(password[i-1])
                if (stringBuffer.length >= 10){
                    return stringBuffer.toString()
                }
            }
        }
        return stringBuffer.toString()
    }



    //sha-256 加密
    private fun createPassword(username:String, url:String):String{
        val newPassword = username + url
        var encodeStr = ""
        try {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            messageDigest.update(newPassword.toByteArray(charset("UTF-8")))
            encodeStr = byte2Hex(messageDigest.digest())
        }catch (e:NoSuchAlgorithmException){
            e.printStackTrace()
        }catch (e:UnsupportedEncodingException){
            e.printStackTrace()
        }
        return encodeStr
    }

    private fun byte2Hex(bytes:ByteArray):String{
        val stringBuffer = StringBuffer()
        var temp = ""
        for (i in bytes.indices) {
            temp = Integer.toHexString((bytes[i] and 0xFF.toByte()).toInt())
            if (temp.length == 1) {
                //1得到一位的进行补0操作
                stringBuffer.append("0")
            }
            stringBuffer.append(temp)
        }
        return stringBuffer.toString()
    }

}
