package lol.svn.reward.impl;

import okhttp3.*;
import lol.svn.reward.HypixelReward;
import lol.svn.reward.exception.RewardException;
import lol.svn.reward.options.BasicRewardOption;
import lol.svn.reward.options.RewardResponse;
import lol.svn.reward.parser.RewardParser;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HypixelRewardImpl implements HypixelReward {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; rv:91.0) Gecko/20100101 Firefox/91.0";
    private static final String BASE_URL = "https://rewards.hypixel.net/claim-reward/";
    private static final Pattern ID_PATTERN = Pattern.compile(
            "[A-Za-z\\d]{8,}"
    );
    private static final Pattern URL_PATTERN = Pattern.compile(
            "https?://(?:www\\.)?(?:rewards\\.)?hypixel.net/claim-reward/([A-Za-z\\d]{8,})"
    );

    private static final String CLAIM_URL_PLACEHOLDER
            = BASE_URL + "claim?option={index}&id={id}&activeAd=1&_csrf={csrf_token}&watchedFallback=false&skipped=0";


    private final String url, id;
    private final OkHttpClient client;
    private final ScheduledExecutorService executor;

    protected HypixelRewardImpl(String url, String id) {
        this.url = url;
        this.id = id;
        this.client = new OkHttpClient.Builder()
                .addNetworkInterceptor(chain -> chain.proceed(
                        chain.request().newBuilder()
                                .addHeader("User-Agent", USER_AGENT)
                                .build()
                ))
                .build();
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    public static HypixelRewardImpl fromUrl(String url) {
        Matcher urlMatcher = URL_PATTERN.matcher(url);
        if (!urlMatcher.find()) {
            throw new IllegalArgumentException("Inputted URL is not valid");
        }
        return new HypixelRewardImpl(url, urlMatcher.group(1));
    }

    public static HypixelRewardImpl fromId(String id) {
        if (!ID_PATTERN.matcher(id).find())
            throw new IllegalArgumentException("Invalid ID format");
        return fromUrl(BASE_URL + id);
    }

    public CompletableFuture<RewardResponse> retrieveRewardOptions() {

        Request request = new Request.Builder()
                .url(this.url)
                .build();

        return doRequest(request).thenApply(pair -> {

            var cookies = Cookie.parseAll(request.url(), pair.response.headers());
            var csrfCookie = cookies.stream()
                    .filter(cookie -> cookie.name().equals("_csrf"))
                    .findFirst()
                    .orElseThrow();

            var parseResponse = RewardParser.parse(pair.body);
            return new RewardResponse(this, csrfCookie.value(), parseResponse);

        });

    }

    public CompletableFuture<Void> claim(RewardResponse rewardResponse, BasicRewardOption option) {

        Request request = new Request.Builder()
                .url(CLAIM_URL_PLACEHOLDER
                        .replaceAll("\\{index}", Integer.toString(option.index()))
                        .replaceAll("\\{id}", this.id)
                        .replaceAll("\\{csrf_token}", rewardResponse.getSecurityToken())
                )
                .addHeader("Cookie", "_csrf=" + rewardResponse.getCsrfCookie())
                .post(RequestBody.create(new byte[0]))
                .build();

        return doRequest(request).thenApply(pair -> {

            if ("reward claimed".equals(pair.body)) {
                return null;
            }

            throw new RewardException("Invalid response from claim: " + pair.body);

        });

    }


    private static class ResponsePair {
        public final Response response;
        public final String body;

        public ResponsePair(Response response, String body) {
            this.response = response;
            this.body = body;
        }
    }

    private CompletableFuture<ResponsePair> doRequest(Request request) {
        return CompletableFuture.supplyAsync(() -> {

            try (Response response = client.newCall(request).execute()) {

                var body = response.body();
                String bodyString = body != null ? body.string() : null;

                if (!response.isSuccessful()) {
                    throw new RewardException("Request was unsuccessful: " + bodyString);
                }

                if (bodyString == null) return null;

                return new ResponsePair(response, bodyString);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }, executor);
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public void close() {
        executor.shutdown();
    }

}
