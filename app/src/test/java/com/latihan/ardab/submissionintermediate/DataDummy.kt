package com.latihan.ardab.submissionintermediate

import com.latihan.ardab.submissionintermediate.data.response.ListStoryItem
import com.latihan.ardab.submissionintermediate.data.response.LoginResponse
import com.latihan.ardab.submissionintermediate.data.response.LoginResult
import com.latihan.ardab.submissionintermediate.data.response.NewStoryResponse
import com.latihan.ardab.submissionintermediate.data.response.SignUpResponse
import com.latihan.ardab.submissionintermediate.data.response.StoryResponse


object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val quote = ListStoryItem(
                i.toString(),
                "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                "Ardab $i",
                "testing $i",
                "2023-03-08T06:34:18.598Z",
                -13.666,
                -13.666
            )
            items.add(quote)
        }
        return items
    }
}