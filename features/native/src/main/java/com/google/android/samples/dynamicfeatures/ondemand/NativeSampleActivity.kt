/*
 * Copyright 2018 Google LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.samples.dynamicfeatures.ondemand

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.android.play.core.splitinstall.SplitInstallHelper
import com.google.android.samples.dynamicfeatures.BaseSplitActivity
import com.google.android.samples.dynamicfeatures.ondemand.ccode.R
import live.hms.video.error.HMSException
import live.hms.video.media.tracks.HMSTrack
import live.hms.video.sdk.HMSSDK
import live.hms.video.sdk.HMSUpdateListener
import live.hms.video.sdk.models.HMSConfig
import live.hms.video.sdk.models.HMSMessage
import live.hms.video.sdk.models.HMSPeer
import live.hms.video.sdk.models.HMSRoleChangeRequest
import live.hms.video.sdk.models.HMSRoom
import live.hms.video.sdk.models.enums.HMSPeerUpdate
import live.hms.video.sdk.models.enums.HMSRoomUpdate
import live.hms.video.sdk.models.enums.HMSTrackUpdate
import live.hms.video.sdk.models.trackchangerequest.HMSChangeTrackStateRequest
import live.hms.video.signal.init.HMSTokenListener
import live.hms.video.signal.init.TokenRequest

/** A simple activity displaying some text coming through via JNI. */
class NativeSampleActivity : BaseSplitActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //SplitInstallHelper.loadLibrary(this, "hello-jni")

        val hmsInstance = HMSSDK.Builder(application).build()

        hmsInstance.getAuthTokenByRoomCode(TokenRequest("krc-nwj-yeh", "gulzar"),
            null,
            object : HMSTokenListener {
                override fun onError(error: HMSException) {
                    runOnUiThread { Toast.makeText(this@NativeSampleActivity, error.message, Toast.LENGTH_SHORT).show() }
                }

                override fun onTokenSuccess(token: String) {
                    //here's the token !
                    val hmsConfig = HMSConfig(
                        userName = "<gul>", authtoken = token
                    )

                    hmsInstance.join(hmsConfig, object : HMSUpdateListener {
                        override fun onChangeTrackStateRequest(details: HMSChangeTrackStateRequest) {
                        }

                        override fun onError(error: HMSException) {
                            runOnUiThread { Toast.makeText(this@NativeSampleActivity, error.message, Toast.LENGTH_SHORT).show() }
                        }

                        override fun onJoin(room: HMSRoom) {
                            runOnUiThread {
                                Toast.makeText(
                                    this@NativeSampleActivity,
                                    "Joined ${room.roomId}",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }

                        override fun onMessageReceived(message: HMSMessage) {
                        }

                        override fun onPeerUpdate(type: HMSPeerUpdate, peer: HMSPeer) {
                        }

                        override fun onRoleChangeRequest(request: HMSRoleChangeRequest) {
                        }

                        override fun onRoomUpdate(type: HMSRoomUpdate, hmsRoom: HMSRoom) {
                        }

                        override fun onTrackUpdate(
                            type: HMSTrackUpdate, track: HMSTrack, peer: HMSPeer
                        ) {
                        }

                    })

                }
            })

        setContentView(R.layout.activity_hello_jni)
//        findViewById<TextView>(R.id.hello_textview).text = stringFromJNI()
    }

    /** Read a string from packaged native code. */
    external fun stringFromJNI(): String
}
