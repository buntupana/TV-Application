package com.buntupana.tv_application.data.providers

import com.buntupana.tv_application.BuildConfig
import com.buntupana.tv_application.domain.providers.UrlProvider

class UrlProviderImpl : UrlProvider {
    override fun getBaseUrl(): String = BuildConfig.API_BASE_URL

    override fun getImageSourceBaseUrl(): String = BuildConfig.API_IMAGE_RESOURCE_BASE_URL
}