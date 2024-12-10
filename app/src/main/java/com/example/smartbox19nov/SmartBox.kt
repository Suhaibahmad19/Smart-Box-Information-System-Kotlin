package com.example.smartbox19nov
import android.os.Parcel
import android.os.Parcelable

class ParcelPackage (
    val parcelId:String,
    val size:String
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString()
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(parcelId)
        parcel.writeString(size)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<ParcelPackage> {
        override fun createFromParcel(parcel: Parcel): ParcelPackage {
            return ParcelPackage(parcel)
        }

        override fun newArray(size: Int): Array<ParcelPackage?> {
            return arrayOfNulls(size)
        }
    }
}
class SmartBox(
    val id: String,
    val type:Int,
    private var packages: ArrayList<ParcelPackage>,
    private var isOpen: Boolean
): Parcelable {
    // Arrays for compartments
    private lateinit var largeCompartments: ArrayList<String>
    private lateinit var mediumCompartments: ArrayList<String>
    private lateinit var smallCompartments: ArrayList<String>

    // Secondary constructor for initialization based on type
    constructor(
        id: String,
        type: Int,
        packages: ArrayList<ParcelPackage>,
        isOpen: Boolean,
        largeCapacity: Int,
        mediumCapacity: Int,
        smallCapacity: Int
    ) : this(id, type, packages, isOpen) {
        when(type){
            2->{
                largeCompartments = arrayListOf("","","")
                mediumCompartments = arrayListOf("","","")
                smallCompartments = arrayListOf("","","","","")
            }
        }
    }
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.createTypedArrayList(ParcelPackage.CREATOR) ?: ArrayList(),
        parcel.readByte() != 0.toByte()
    )
    private var capacity: () -> Int = {
        if (type == 1)
            8
        else
            11
    }
    private fun withdrawPackageExtender(
        parcelPackage: ParcelPackage,
        arrayOfCompartments:ArrayList<String>,
        compartmentSizeMsg:String
    ):String {
        for (i in 0..<arrayOfCompartments.count()){
            if(arrayOfCompartments[i] == parcelPackage.parcelId) {
                packages.remove(parcelPackage)
                arrayOfCompartments[i] = ""
                return compartmentSizeMsg+i.toString()
            }
        }
        return ""
    }
    fun withdrawPackage(parcelPackage: ParcelPackage):String{
        return when(parcelPackage.size){
            "Small"-> withdrawPackageExtender(
                parcelPackage, smallCompartments,"SmallCompartment"
            )

            "Medium"-> withdrawPackageExtender(
                parcelPackage, mediumCompartments,"MediumCompartment"
            )

            "Large"-> withdrawPackageExtender(
                parcelPackage, largeCompartments,"LargeCompartment"
            )

            else -> ""
        }
    }
    private fun hasPackage(packageItem:ParcelPackage):Boolean{
        for(p in packages){
            if(p == packageItem)
                return true
        }
        return false
    }
    fun openBox(packageItem:ParcelPackage):Boolean {
        if (hasPackage(packageItem)) {
            isOpen = true
        }
        return isOpen
    }
    fun addPackageCompartmentWise(
        packageItem: ParcelPackage,
        compartmentArray: ArrayList<String>,
        compartmentSizeMsg: String
    ):String{
        for (i in 0..<compartmentArray.count()){
            if(compartmentArray[i] == ""){
                compartmentArray[i] = packageItem.parcelId
                packages.add(packageItem)
                return compartmentSizeMsg+i.toString()
            }
        }
        return ""
    }
    fun addParcel(parcelPackage: ParcelPackage):String{
        if(packages.contains(parcelPackage)){
            print("Already has a parcel")
        }
        else {
            if(packages.count() < capacity()){
                return when(parcelPackage.size){
                    "Small"-> addPackageCompartmentWise(
                        parcelPackage, smallCompartments,"SmallCompartment"
                    )

                    "Medium"-> addPackageCompartmentWise(
                        parcelPackage, mediumCompartments,"MediumCompartment"
                    )

                    "Large"-> addPackageCompartmentWise(
                        parcelPackage, largeCompartments,"LargeCompartment"
                    )

                    else -> {
                        ""
                    }
                }
            }
        }
        return ""
    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeInt(type)
        parcel.writeTypedList(packages)
        parcel.writeByte(if (isOpen) 1 else 0)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<SmartBox> {
        override fun createFromParcel(parcel: Parcel): SmartBox {
            return SmartBox(parcel)
        }

        override fun newArray(size: Int): Array<SmartBox?> {
            return arrayOfNulls(size)
        }
    }
}

