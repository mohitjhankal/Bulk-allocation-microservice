package com.newbury.revola.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "dmms")
public class DmmConfigurations {

    private List<Dmm> dmm;

    public List<Dmm> getDmm() {
        return dmm;
    }

    public void setDmm(List<Dmm> dmm) {
        this.dmm = dmm;
    }

    public static class Dmm {
        private String name;
        private FetchConfig fetch;
        private UriConfig uri;
        private ApiConfig api;
        private RestApiConfig rest;
        private ErrorConfig error;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public FetchConfig getFetch() {
            return fetch;
        }

        public void setFetch(FetchConfig fetch) {
            this.fetch = fetch;
        }

        public UriConfig getUri() {
            return uri;
        }

        public void setUri(UriConfig uri) {
            this.uri = uri;
        }

        public ApiConfig getApi() {
            return api;
        }

        public void setApi(ApiConfig api) {
            this.api = api;
        }

        public RestApiConfig getRest() {
            return rest;
        }

        public void setRest(RestApiConfig rest) {
            this.rest = rest;
        }

        public ErrorConfig getError() {
            return error;
        }

        public void setError(ErrorConfig error) {
            this.error = error;
        }
    }

    public static class FetchConfig {
        private String assets;
        private String scheduler;

        public String getAssets() {
            return assets;
        }

        public void setAssets(String assets) {
            this.assets = assets;
        }

        public String getScheduler() {
            return scheduler;
        }

        public void setScheduler(String scheduler) {
            this.scheduler = scheduler;
        }
    }

    public static class UriConfig {
        private String external;

        public String getExternal() {
            return external;
        }

        public void setExternal(String external) {
            this.external = external;
        }
    }

    public static class ApiConfig {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class RestApiConfig {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class ErrorConfig {
        private EmailConfig email;

        public EmailConfig getEmail() {
            return email;
        }

        public void setEmail(EmailConfig email) {
            this.email = email;
        }
    }

    public static class EmailConfig {
        private String to;
        private String cc;

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getCc() {
            return cc;
        }

        public void setCc(String cc) {
            this.cc = cc;
        }
    }
}
