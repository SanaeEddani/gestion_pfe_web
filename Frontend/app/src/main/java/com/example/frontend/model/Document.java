package com.example.frontend.model;

import java.util.List;

public class Document {
    public int id;
    public String nomDocument;
    public String chemin;
    public String format;
    public String dateUpload;
    public List<Remarque> remarques;

    public static class Remarque {
        public String contenu;
        public String createdAt;
    }
}
