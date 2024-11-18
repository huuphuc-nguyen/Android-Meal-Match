package edu.utsa.cs3443.mealmatch.groq;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import com.google.gson.JsonObject;

/**
 * Interface for Groq API Client.
 * Provides methods for asynchronous chat completion and streaming responses.
 */
public interface IGroqApiClient {

    /**
     * Sends a chat completion request asynchronously and returns a Single with the result.
     *
     * @param request JsonObject containing the chat request parameters.
     * @return Single<JsonObject> containing the API response.
     */
    Single<JsonObject> createChatCompletionAsync(JsonObject request);

    /**
     * Sends a chat completion request and streams the response asynchronously.
     *
     * @param request JsonObject containing the chat request parameters.
     * @return Observable<JsonObject> streaming each partial result as a JsonObject.
     */
    Observable<JsonObject> createChatCompletionStreamAsync(JsonObject request);
}
