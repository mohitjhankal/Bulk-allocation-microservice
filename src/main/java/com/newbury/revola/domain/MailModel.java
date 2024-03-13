package com.newbury.revola.domain;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MailModel {
    private String from;
    private String to;
    private String cc;
    private String name;
    private String subject;
    private Map<String, String> model;

    public MailModel() {
    }

    public MailModel(String from, String to, String cc, String name, String subject, Map<String, String> model) {
        this.from = from;
        this.to = to;
        this.cc = cc;
        this.name = name;
        this.subject = subject;
        this.model = model;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public Map<String, String> getModel() {
        return model;
    }

    public void setModel(Map<String, String> model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "MailModel{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", cc='" + cc + '\'' +
                ", name='" + name + '\'' +
                ", subject='" + subject + '\'' +
                ", model=" + model +
                '}';
    }
}
