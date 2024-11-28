package edu.utsa.cs3443.mealmatch.groq;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GroqApiClientImpl implements IGroqApiClient {

    private final String apiKey;
    private final OkHttpClient client;

    public GroqApiClientImpl(String apiKey) {
        this.apiKey = apiKey;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public Single<JsonObject> createChatCompletionAsync(JsonObject request) {
        RequestBody body = RequestBody.create(request.toString(), MediaType.get("application/json; charset=utf-8"));

        Request httpRequest = new Request.Builder()
                .url("https://api.groq.com/openai/v1/chat/completions")
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        return Single.<Response>create(emitter -> {
            client.newCall(httpRequest).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    emitter.onError(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    emitter.onSuccess(response);
                }
            });
        }).map(response -> {
            String responseBody = response.body().string();
            return JsonParser.parseString(responseBody).getAsJsonObject();
        });
    }

    @Override
    public Observable<JsonObject> createChatCompletionStreamAsync(JsonObject request) {
        RequestBody body = RequestBody.create(request.toString(), MediaType.get("application/json; charset=utf-8"));

        Request httpRequest = new Request.Builder()
                .url("https://api.groq.com/openai/v1/chat/completions")
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        return Observable.<String>create(emitter -> {
                    client.newCall(httpRequest).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            emitter.onError(e);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String[] lines = response.body().string().split("\n");
                            for (String line : lines) {
                                if (emitter.isDisposed()) break;
                                emitter.onNext(line);
                            }
                            emitter.onComplete();
                        }
                    });
                }).filter(line -> line.startsWith("data: "))
                .map(line -> line.substring(6))
                .filter(jsonData -> !jsonData.equals("[DONE]"))
                .map(jsonData -> JsonParser.parseString(jsonData).getAsJsonObject());
    }
}

