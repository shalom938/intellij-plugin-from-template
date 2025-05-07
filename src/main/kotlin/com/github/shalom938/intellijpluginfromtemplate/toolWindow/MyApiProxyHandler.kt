package com.github.shalom938.intellijpluginfromtemplate.toolWindow

import com.intellij.openapi.diagnostic.thisLogger
import okhttp3.*
import org.cef.callback.CefCallback
import org.cef.handler.CefResourceHandler
import org.cef.misc.IntRef
import org.cef.misc.StringRef
import org.cef.network.CefPostData
import org.cef.network.CefPostDataElement
import org.cef.network.CefRequest
import org.cef.network.CefResponse
import java.util.*


class MyApiProxyHandler : CefResourceHandler {

    private val okHttpClient = OkHttpClient()

    private var okHttpResponse: Response? = null

    override fun processRequest(
        request: CefRequest,
        callback: CefCallback
    ): Boolean {
        try {
            val postData = request.postData?.let {
                postDataToByteArray(it)
            }
            val requestUrl = request.url
            val requestMethod = request.method
            val headers = mutableMapOf<String, String>()
            request.getHeaderMap(headers)

            val okHttpRequest = toOkHttp3Request(requestUrl, requestMethod, headers, postData)
            okHttpResponse = okHttpClient.newCall(okHttpRequest).execute()
        }catch (e:Exception){
            //mock okhttp error response
            val request = Request.Builder()
                .url(request.url)
                .build()

            val errorJson = """{"error": "${e.message ?: e.toString()}"}"""

            val responseBody = ResponseBody.create(
                MediaType.parse("application/json"),
                errorJson
            )

            okHttpResponse = Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(500)
                .message("Internal jcef Error: ${e.message}")
                .body(responseBody)
                .header("Content-Type", "application/json")
                .header("Cache-Control", "no-cache")
                .build()
        }

        callback.Continue()
        return true
    }

    override fun getResponseHeaders(
        response: CefResponse,
        responseLength: IntRef,
        redirectUrl: StringRef
    ) {
        okHttpResponse?.let { res ->
            response.status = res.code()
            val headers = res.headers().toMultimap().mapValues { it.value.firstOrNull().orEmpty() }
            response.setHeaderMap(headers)

            val contentTypeHeader = headers.entries
                .firstOrNull { it.key.equals("Content-Type", ignoreCase = true) }
                ?.value

            val body = res.body()
            if (body != null) {
                response.mimeType = body.contentType()?.toString() ?: contentTypeHeader
                responseLength.set(body.contentLength().toInt())
            } else {
                responseLength.set(-1)
                contentTypeHeader?.let { response.mimeType = it }
            }
        }
    }

    override fun readResponse(
        dataOut: ByteArray,
        bytesToRead: Int,
        bytesRead: IntRef,
        callback: CefCallback
    ): Boolean {

        return try {
            val inputStream = okHttpResponse?.body()?.byteStream()
            val read = inputStream?.read(dataOut, 0, bytesToRead)
            if (read == null || read <= 0) {
                bytesRead.set(0)
                inputStream?.close()
                return false
            }
            bytesRead.set(read)
            true
        } catch (e: Exception) {
            thisLogger().error(e)
            bytesRead.set(0)
            false
        }
    }

    override fun cancel() {
        okHttpResponse?.close()
    }


    fun postDataToByteArray(postData: CefPostData): ByteArray? {
        val elements = Vector<CefPostDataElement>()
        postData.getElements(elements)

        var allBytes = ByteArray(0)

        elements.forEach { e ->
            val bytesCnt: Int = e.bytesCount
            if (bytesCnt > 0) {
                val bytes = ByteArray(bytesCnt)
                e.getBytes(bytes.size, bytes)
                allBytes = allBytes.plus(bytes)
            }
        }
        return allBytes
    }


    private fun toOkHttp3Request(
        requestUrl: String,
        requestMethod: String,
        headers: MutableMap<String, String>,
        postData: ByteArray?
    ): Request {
        val okHttp3RequestBuilder = Request.Builder().url(requestUrl)
        val okHttp3Body: RequestBody? = postData?.let {
            val contentType = headers["Content-Type"]
            if (contentType == null) {
                RequestBody.create(null, it)
            } else {
                RequestBody.create(MediaType.parse(contentType), it)
            }
        }

        okHttp3RequestBuilder.method(requestMethod, okHttp3Body)
        headers.forEach(okHttp3RequestBuilder::header)
        return okHttp3RequestBuilder.build()
    }


}
