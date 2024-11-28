package com.coze.api

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import cozeapi.composeapp.generated.resources.Res
import cozeapi.composeapp.generated.resources.compose_multiplatform
import kotlinx.serialization.Serializable
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import kotlinx.serialization.descriptors.StructureKind
import io.ktor.client.call.*
import io.ktor.client.statement.bodyAsText

// 定义数据模型
@Serializable
data class ApiResponse(
    val code: Int,
    val message: String,
    val data: StructureKind.OBJECT // 任意类型
)

// 定义 EnterMessage 数据类
@Serializable
data class EnterMessage(
    val role: String,
    val content: String,
    var content_type: String = "text"
)

// 更新 ChatRequest 数据类
@Serializable
data class ChatRequest(
    val bot_id: String,
    val user_id: String,
    val additional_messages: List<EnterMessage> = emptyList()
)

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        var responseBody by remember { mutableStateOf("No response") }

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { showContent = !showContent }) {
                Text("Click me!")
            }
            AnimatedVisibility(showContent) {
                LaunchedEffect(Unit) {
                    // 创建请求体
                    val body = ChatRequest(
                        bot_id = "7373880376026103809",
                        user_id = "007",
                        additional_messages = listOf(
                            EnterMessage(role = "user", content = "hi there")
                            // 可以添加更多消息
                        )
                    )

                    // 发送请求
                    try {
                        val apiResponse: HttpResponse = client.request("https://api.coze.com/v3/chat") {
                            headers {
                                append(HttpHeaders.ContentType, "application/json")
                                append(HttpHeaders.Authorization, "Bearer pat_XOoyvvnVw9reXta17Idssegj3DNXZVZhCEi1BPzXqYeIRrVzNH1qux9r6HBe26Oc")
                                append(HttpHeaders.UserAgent, "ktor client")
                            }
                            method = HttpMethod.Post
                            setBody(body)
                        }
                        responseBody = apiResponse.bodyAsText()
                    } catch (e: Exception) {
                        println("请求失败: ${e.message}")
                    } finally {
                        client.close()
                    }
                }
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    // 使用状态变量显示响应内容
                    Text("Compose: $responseBody")
                }
            }
        }
    }
}