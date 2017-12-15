package ca.bc.gov.secureimage.data.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * Created by Aidan Laing on 2017-12-14.
 *
 */
open class CameraImage : RealmObject() {

    @PrimaryKey
    var key: String = UUID.randomUUID().toString()

    var createdTime: Long = System.currentTimeMillis()
    var updatedTime: Long = System.currentTimeMillis()
    var byteArray: ByteArray = ByteArray(64)

    fun compareTo(cameraImage: CameraImage): Int  = when {
        createdTime > cameraImage.createdTime -> 1
        createdTime < cameraImage.createdTime -> -1
        else -> 0
    }
}