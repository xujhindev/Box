package com.xujhin.box.ext

import android.app.Activity
import android.content.Intent
import android.widget.Toast

fun Activity.showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

inline fun <reified T : Activity> Activity.navigateTo() {
    this.startActivity(Intent(this, T::class.java))
}