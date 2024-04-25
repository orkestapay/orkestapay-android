package com.orkestapay.orkestapay.core.devicesession

interface DeviceSessionListener {
    fun onSuccess(deviceSession: String)
    fun onError(error: String)
}
