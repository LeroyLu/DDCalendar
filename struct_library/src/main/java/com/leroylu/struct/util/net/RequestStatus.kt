package com.leroylu.struct.util.net

import androidx.lifecycle.MutableLiveData
import com.leroylu.struct.AppException
import kotlinx.coroutines.Job

/**
 * @author jiaj.lu
 * @date 2020/8/21
 * @description
 */
class RequestStatus(status: Status = Status.DOING) {

    enum class Status {
        DOING, DONE, ERROR;

        var error: AppException? = null

    }

    var job: Job? = null
    var current: MutableLiveData<Status> = MutableLiveData(status)

}