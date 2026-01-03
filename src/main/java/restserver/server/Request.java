package restserver.server;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an HTTP request and parses its URL.
 * Stores the full URL, the path, query parameters, and path parts.
 */
public class Request {
    private String urlContent;
    private String pathname;
    private List<String> pathParts;
    private String params;

    public Request(URI url) {
        this.setUrlContent(url.toString());

        this.setPathname(url.getPath());
        this.setParams(url.getQuery());
    }

    public String getUrlContent(){
        return this.urlContent;
    }

    private void setUrlContent(String urlContent) {
        this.urlContent = urlContent;
    }

    public String getPathname() {
        return pathname;
    }

    private void setPathname(String pathname) {
        this.pathname = pathname;
        String[] stringParts = pathname.split("/");
        this.pathParts = new ArrayList<>();
        for (String part :stringParts)
        {
            if (part != null &&
                    part.length() > 0)
            {
                this.pathParts.add(part);
            }
        }

    }
    public String getParams() {
        return params;
    }

    private void setParams(String params) {
        this.params = params;
    }

    public List<String> getPathParts() {
        return pathParts;
    }

    private void setPathParts(List<String> pathParts) {
        this.pathParts = pathParts;
    }

    /**
     * The method getQueryParams returns the params for postman search & filter
     * @return
     */
    public Map<String, String> getQueryParams(URI uri) {
        Map<String, String> params = new HashMap<>();

        String query = uri.getRawQuery();
        if (query == null || query.isBlank()) {
            return params;
        }

        for (String pair : query.split("&")) {
            String[] keyValue = pair.split("=");
            String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
            String value = keyValue.length > 1
                    ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8)
                    : "";

            params.put(key, value);
        }

        return params;
    }
}