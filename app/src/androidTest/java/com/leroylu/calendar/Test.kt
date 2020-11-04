package com.leroylu.calendar

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.leroylu.calendar.bean.FallowingBean
import com.leroylu.db.DatabaseUtil
import com.leroylu.db.bean.calendar.Vtuber
import org.junit.Test
import org.junit.runner.RunWith
import java.io.BufferedReader
import java.io.InputStreamReader

@RunWith(AndroidJUnit4::class)
class Test {

    @Test
    fun load() {
        val dao = DatabaseUtil.db.vtuber()
        val content = readFile()
        val obj = Gson().fromJson(content, FallowingBean::class.java)

        val transform = obj.data.list.filter {
            it.tag?.contains(257898) ?: false
        }.map {
            Vtuber(
                uid = it.mid.toString(),
                icon = it.face,
                name = it.uname,
                description = it.sign,
                streamRoomId = ""
            )
        }

        val addList = ArrayList<Vtuber>()
        val updateList = ArrayList<Vtuber>()
        transform.forEach {
            if (null == dao.getById(it.vid)) {
                addList.add(it)
            } else {
                updateList.add(it)
            }
        }

        dao.insertAll(addList)
        dao.updateAll(updateList)
    }

    private fun readFile(): String {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val stringBuilder = StringBuilder()

        try {

            val bf = BufferedReader(InputStreamReader(appContext.assets.open("a.json")))

            var line = bf.readLine();
            while (line != null) {
                stringBuilder.append(line)
                line = bf.readLine();
            }

            return stringBuilder.toString()

        } catch (e: Exception) {
            return ""
        }
    }
}