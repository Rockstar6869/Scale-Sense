package com.example.masterapp

import androidx.media3.common.util.Log
import co.yml.charts.common.extensions.isNotNull
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID

class UserDetailRepository(private val firestore: FirebaseFirestore) {

    fun addSubUser(mailId:String,subuser: subuser):Result<String> =
        try{
            val docRef = firestore.collection("users").document(mailId).
            collection("subuser").document("subusers")

            val uuid = UUID.randomUUID().toString()
            val userUUID = subuser.firstName+uuid

            val userMap = mapOf(
                "firstname" to subuser.firstName,
                "lastname" to subuser.lastName,
                "uuid" to userUUID
            )

            docRef.update("users",FieldValue.arrayUnion(userMap))
                .addOnSuccessListener {
                    Result.Success(userUUID)
                }
                .addOnFailureListener{ e->
                    if(e.message?.contains("No document to update")==true){
                        docRef.set(mapOf("users" to listOf(userMap)))
                            .addOnSuccessListener {
                                Result.Success(Unit)
                            }
                            .addOnFailureListener { setError ->

                            }
                    }
                    else{

                    }
                }
            Result.Success(userUUID)
        }
        catch (e:Exception){
            Result.Error(e)
        }

    suspend fun getSubUserList(mailId: String):Result<List<subuser>> =
        try {
            val subUserListSnapshot = firestore.collection("users").
            document(mailId).collection("subuser").document("subusers").get().await()
            if(subUserListSnapshot.exists()){
                val subUsersList = subUserListSnapshot.get("users") as? List<Map<String, Any>>
                if(subUsersList !=null){
                    val userList = subUsersList.map { map ->
                        subuser(
                            firstName = map["firstname"] as String,
                            lastName = map["lastname"] as String,
                            uuid = map["uuid"] as String
                        )
                    }
                    Result.Success(userList)
                }
                else{
                    Result.Error(Exception("user list is empty"))
                }
            } else{
                Result.Error(Exception("Doc does not exist"))
            }
        }
        catch (e:Exception){
            Result.Error(e)
        }


    suspend fun uploadDetails(mailId:String,userData: UserData,subuseruuid: String): Result<Unit> =
        try
        {
            firestore.collection("users").document(mailId) //will be used for sending height, gender age in the upload detail screen
                .collection("subuserdetails").document(subuseruuid).
            collection("userdetails").document("userdata").set(userData).await()
            Result.Success(Unit)
        }
        catch (E:Exception){
            Result.Error(E)
        }
    suspend fun getUserData(mailId:String,subuseruuid: String): Result<UserData> =
        try{
            val ud=firestore.collection("users").
            document(mailId).collection("subuserdetails").document(subuseruuid).
            collection("userdetails").document("userdata").get().await()
            val userDetails = ud.toObject(UserData::class.java)
            if(userDetails!=null){
                Result.Success(userDetails)
            }else {
                Result.Error(Exception("User data not found"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    suspend fun updatehistlist(mailId:String,newHist:hist,subuseruuid: String):Result<Unit> =
        try{
            val userDocRef = firestore.collection("users").document(mailId).
                collection("subuserdetails").document(subuseruuid).collection("history").
            document("userhist")
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
    suspend fun gethistlist(mailId:String,subuseruuid: String):Result<List<hist>> =
    try{
            val historyListSnapshot = firestore.collection("users")
                .document(mailId).
                collection("subuserdetails").
                document(subuseruuid)
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

    suspend fun uploadUnit(mailId:String,units: Units): Result<Unit> =
        try
        {
            firestore.collection("users").document(mailId).    //will be used for setting weight and height units
            collection("units").document("unitsforheightandweight").set(units).await()
            Result.Success(Unit)
        }
        catch (E:Exception){
            Result.Error(E)
        }

    suspend fun getUnits(mailId:String): Result<Units> =
        try{
            val ud=firestore.collection("users").
            document(mailId).collection("units").document("unitsforheightandweight").get().await()
            val units = ud.toObject(Units::class.java)
            if(units!=null){
                Result.Success(units)
            }else {
                Result.Error(Exception("User Units not found"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
}