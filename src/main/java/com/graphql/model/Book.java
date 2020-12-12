package com.graphql.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Table
@Entity
public class Book {

    @Id
    private String isn;
    private String title;
    private String publisher;
    private String[] authors;
    private String publisherDate;

    public Book() {
    }

    public Book(String isn, String title, String publisher, String[] authors, String publisherDate) {
        this.isn = isn;
        this.title = title;
        this.publisher = publisher;
        this.authors = authors;
        this.publisherDate = publisherDate;
    }
}
