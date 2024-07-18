package com.example.masterapp

import androidx.media3.common.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserDetailRepository(private val firestore: FirebaseFirestore) {
    suspend fun uploadDetails(mailId:String,userData: UserData): Result<Unit> =
        try
        {
            firestore.collection("users").document(mailId).    //will be user for sending height, gender age in the upload detail screen
            collection("userdetails").document("userdata").set(userData).await()
            Result.Success(Unit)
        }
        catch (E:Exception){
            Result.Error(E)
        }
    suspend fun getUserData(mailId:String): Result<UserData> =
        try{
            val ud=firestore.collection("users").
            document(mailId).collection("userdetails").document("userdata").get().await()
            val userDetails = ud.toObject(UserData::class.java)
            if(userDetails!=null){
                Result.Success(userDetails)
            }else {
                Result.Error(Exception("User data not found"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    suspend fun updatehistlist(mailId:String,newHist:hist):Result<Unit> =
        try{
            val userDocRef = firestore.collection("users").document(mailId).collection("history").document("userhist")
            userDocRef.get().addOnSuccessListener { document ->
                    if (document.contains("history")) {
                        val currentHistory = document.get("history") as List<Map<String, Any>>
                        val updatedHistory = currentHistory.toMutableList()

                        if (updatedHistory.isNotEmpty()) {
                            val lastHist = updatedHistory.last()
                            val lastHistDate = lastHist["date"] as String

                            if (lastHistDate == newHist.date) {
                                // Remove the last element if dates are equal
                                updatedHistory.removeAt(updatedHistory.size - 1)
                            }
                        }
                        updatedHistory.add(mapOf(
                            "weight" to newHist.weight,
                            "impedance" to newHist.impedance,
                            "date" to newHist.date
                        ))


                        userDocRef.update("history", updatedHistory)
                            .addOnSuccessListener {
                            }
                            .addOnFailureListener { e ->

                            }

                        Result.Success(Unit)
                    }
                    else {

                        val userHist = userHist(listOf(newHist))
                        userDocRef.set(userHist)
                            .addOnSuccessListener {
                                Result.Success(Unit)

                            }
                            .addOnFailureListener { e ->

                            }
                        Result.Success(Unit)
                    }


            }.addOnFailureListener { e ->
               Result.Error(e)
            }

            Result.Success(Unit)
        }
        catch (e: Exception){

            Result.Error(e)
        }

//    suspend fun gethistlist(mailId:String):Result<List<hist>> =
//        try{
//            val historyListSnapshot = firestore.collection("users")
//                .document(mailId)
//                .collection("history")
//                .document("userhist").get().await()
//            if(historyListSnapshot.exists()){
//                val histlist = historyListSnapshot.get("history") as List<hist>
//                Result.Success(histlist)
//            }
//            else{
//             Result.Error(Exception("Weight list is null"))
//            }
//        }catch (e: Exception) {
//            Result.Error(e)
//        }
    suspend fun gethistlist(mailId:String):Result<List<hist>> =
    try{
            val historyListSnapshot = firestore.collection("users")
                .document(mailId)
                .collection("history")
                .document("userhist").get().await()
            if(historyListSnapshot.exists()){
                val histlist = historyListSnapshot.get("history") as? List<Map<String, Any>>
                if (histlist != null) {
                    val userhist = histlist.map { map ->
                        hist(
                            weight = map["weight"] as Double,
                            impedance = (map["impedance"] as Long).toInt(),
                            date = map["date"] as String
                        )
                    }
                    Result.Success(userhist)
                }else {
                    Result.Error(Exception("hist list is null or empty"))
                }
            }
            else{
             Result.Error(Exception("Weight list is null"))
            }
        }catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun bindDevice(mailId: String,device:themistoscale):Result<Unit> =
        try {
            val docRef = firestore.collection("users").document(mailId)
                .collection("devices").document("themistoscale")

            val scaleMap = mapOf(
                "name" to device.name,
                "address" to device.address
            )

            docRef.update("scales", FieldValue.arrayUnion(scaleMap))
                .addOnSuccessListener {
                    Result.Success(Unit)
                }
                .addOnFailureListener { e ->
                    if (e.message?.contains("No document to update") == true) {
                        // If the document does not exist, set it up with the array
                        docRef.set(mapOf("scales" to listOf(scaleMap)))
                            .addOnSuccessListener {
                               Result.Success(Unit)
                            }
                            .addOnFailureListener { setError ->

                            }
                    } else {
                    }
                }
            Result.Success(Unit)
        }
        catch (e:Exception){
            Result.Error(e)
        }

//    suspend fun getDevices(mailId:String):Result<List<themistoscale>> =
//        try{
//            val deviceListSnapshot = firestore.collection("users")
//                .document(mailId)
//                .collection("devices")
//                .document("themistoscale").get().await()
//            if(deviceListSnapshot.exists()){
//                val devicelist = deviceListSnapshot.get("scales") as List<themistoscale>
//                Result.Success(devicelist)
//            }
//            else{
//                Result.Error(Exception("Device list is null"))
//            }
//        }catch (e: Exception) {
//            Result.Error(e)
//        }

    suspend fun getDevices(mailId: String): Result<List<themistoscale>> =
        try {
            val deviceListSnapshot = firestore.collection("users")
                .document(mailId)
                .collection("devices")
                .document("themistoscale").get().await()
            if (deviceListSnapshot.exists()) {
                val scalesList = deviceListSnapshot.get("scales") as? List<Map<String, Any>>
                if (scalesList != null) {
                    val deviceList = scalesList.map { map ->
                        themistoscale(
                            name = map["name"] as String,
                            address = map["address"] as String
                        )
                    }
                    Result.Success(deviceList)
                } else {
                    Result.Error(Exception("Scales list is null or not a list"))
                }
            } else {
                Result.Error(Exception("Document does not exist"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
}