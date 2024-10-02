package com.amateuraces.book;


public class Book {
    private Long id;
    private String title;

    public Book(){
        
    }

    public Book(String title){
        this.title = title;
    }
    
    public Book(Long id, String title){
        this.id = id;
        this.title = title;
    }

    public Long getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setId(Long id){
        this.id = id;
    }
    
}