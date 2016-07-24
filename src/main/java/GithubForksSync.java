import okhttp3.OkHttpClient;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import rx.Observable;

// refelaction

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * Created by Mohamed Kamal on 7/24/2016.
 */
public class GithubForksSync {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter github credentials in the form of \"username:token\"");
        String credentials = scanner.next();

        String username = "mohamedkomalo";

        System.out.println("Retrieving repos with its forks now");

        long startTime = System.nanoTime();
        List<Repo> repos = getReposWithForks(credentials, username);
        long endTime = System.nanoTime();

        System.out.println("Took nano seconds: " + (endTime - startTime));

        for (Repo repo : repos) {
            System.out.println("---");
            System.out.println("Repo: " + repo.getUrl());
            System.out.println("Forks: " + repo.getForksUrls());
            System.out.println("---");
        }

    }

    static class Repo {
        String url;
        List<String> forksUrls;

        public Repo(String url, List<String> forksUrls) {
            this.url = url;
            this.forksUrls = forksUrls;
        }

        public String getUrl() {
            return url;
        }

        public List<String> getForksUrls() {
            return forksUrls;
        }
    }

    private static List<Repo> getReposWithForks(String credentials, String username) throws Exception {
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        String repoJson = githubRequest(client, "https://api.github.com/users/" + username + "/repos", credentials);

        List<Repo> repos = new ArrayList<>();
        JSONArray jsonObject = new JSONArray(repoJson);
        for (Object repo : jsonObject) {
            JSONObject repository = (JSONObject) repo;
            String url = repository.getString("html_url");
            String forksUrl = repository.getString("forks_url");

            String forks = githubRequest(client, forksUrl, credentials);
            JSONArray forksArr = new JSONArray(forks);
            List<String> forksUrls = new ArrayList<>();
            for (Object forkRepo : forksArr) {
                forksUrls.add(((JSONObject) forkRepo).getString("html_url"));
            }
            repos.add(new Repo(url, forksUrls));
        }

        client.close();
        return repos;
    }

    private static String githubRequest(AsyncHttpClient asyncHttpClient, String url, String credentials) throws Exception {
        String authEncoded = Base64.encode((credentials).getBytes());
        return asyncHttpClient
                .prepareGet(url)
                .addHeader("Authorization", "Basic " + authEncoded)
                .execute()
                .get()
                .getResponseBody();
    }
}

