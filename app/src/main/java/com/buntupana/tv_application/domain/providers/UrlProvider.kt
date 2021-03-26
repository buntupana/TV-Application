package com.buntupana.tv_application.domain.providers

interface UrlProvider {
    fun getBaseUrl(): String
    fun getImageSourceBaseUrl(): String
}