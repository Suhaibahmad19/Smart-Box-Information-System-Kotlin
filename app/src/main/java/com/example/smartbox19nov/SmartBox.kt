package com.example.smartbox19nov
import android.os.Parcel
import android.os.Parcelable

class PackageItem(val packageID:String) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(packageID)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<PackageItem> {
        override fun createFromParcel(parcel: Parcel): PackageItem {
            return PackageItem(parcel)
        }

        override fun newArray(size: Int): Array<PackageItem?> {
            return arrayOfNulls(size)
        }
    }
}
class SmartBox(
    val id: String,
    val type:Int,
    private var packages: ArrayList<PackageItem>,
    private var isOpen: Boolean
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.createTypedArrayList(PackageItem.CREATOR) ?: ArrayList(),
        parcel.readByte() != 0.toByte()
    )
    private var capacity: () -> Int = {
        if (type == 1)
            8
        else
            11
    }
    private fun hasPackage(packageItem:PackageItem):Boolean{
        for(p in packages){
            if(p == packageItem)
                return true
        }
        return false
    }
    fun openBox(packageItem:PackageItem):Boolean {
        if (hasPackage(packageItem)) {
            isOpen = true
        }
        return isOpen
    }
    fun addParcel(packageItem: PackageItem):Boolean{
        if(packages.contains(packageItem)){
            print("Already has a parcel")
            return false
        }
        else {
            if(packages.count() < capacity())
            packages.add(packageItem)
            return true
        }
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
