package com.example.kakaomobilitytest.Utils

fun getErrorMessage(apiName: String, errorCode: Int?, errorMessage: String?): String {
    return when {
        errorMessage?.contains("Unable to resolve host") == true -> {
            "경로 설정 API 에러: 네트워크 연결을 확인해주세요."
        }
        errorMessage.isNullOrEmpty() -> {
            when (errorCode) {
                null -> "$apiName API에서 에러가 발생했습니다."
                else -> "$apiName API 에러 코드: $errorCode)"
            }
        }
        else -> "not_found"
    }
}