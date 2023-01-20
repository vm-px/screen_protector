package com.prongbang.screen_protector

import android.app.Activity
import android.view.WindowManager
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

/** ScreenProtectorPlugin */
class ScreenProtectorPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    private var activity: Activity? = null

    private lateinit var channel: MethodChannel

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "screen_protector")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) =
        when (call.method) {
            "protectDataLeakageOn", "preventScreenshotOn" -> {
                try {
                    activity?.window?.setFlags(
                        WindowManager.LayoutParams.FLAG_SECURE,
                        WindowManager.LayoutParams.FLAG_SECURE,
                    )
                    result.success(true)
                } catch (_: Exception) {
                    result.success(false)
                }
            }
            "protectDataLeakageOff", "preventScreenshotOff" -> {
                try {
                    activity?.window?.clearFlags(
                        WindowManager.LayoutParams.FLAG_SECURE,
                    )
                    result.success(true)
                } catch (_: Exception) {
                    result.success(false)
                }
            }
            "isRecording" -> {
                // Not supported.
                result.success(false)
            }
            else -> result.success(false)
        }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivity() {}

}