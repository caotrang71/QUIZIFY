package com.example.project.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class quiz_banks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int quiz_banks_id;
    private String bank_name;
    private LocalDateTime create_at;
    private String description;
    private LocalDateTime modified_at;
    private String image;
    private int editable;
    private int create_by;
    private int subcategory_id;
    @PrePersist
    protected void OnCreate(){
        this.create_at = LocalDateTime.now();
    }
    @PreUpdate
    protected  void OnUpdate(){
        this.modified_at = LocalDateTime.now();
    }

    public quiz_banks() {
    }

    public quiz_banks(int quiz_banks_id, String bank_name, LocalDateTime create_at, String description, LocalDateTime modified_at, String image, int editable, int create_by, int subcategory_id) {
        this.quiz_banks_id = quiz_banks_id;
        this.bank_name = bank_name;
        this.create_at = create_at;
        this.description = description;
        this.modified_at = modified_at;
        this.image = image;
        this.editable = editable;
        this.create_by = create_by;
        this.subcategory_id = subcategory_id;
    }

    public int getQuiz_banks_id() {
        return quiz_banks_id;
    }

    public void setQuiz_banks_id(int quiz_banks_id) {
        this.quiz_banks_id = quiz_banks_id;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public LocalDateTime getCreate_at() {
        return create_at;
    }

    public void setCreate_at(LocalDateTime create_at) {
        this.create_at = create_at;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getModified_at() {
        return modified_at;
    }

    public void setModified_at(LocalDateTime modified_at) {
        this.modified_at = modified_at;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getEditable() {
        return editable;
    }

    public void setEditable(int editable) {
        this.editable = editable;
    }

    public int getCreate_by() {
        return create_by;
    }

    public void setCreate_by(int create_by) {
        this.create_by = create_by;
    }

    public int getSubcategory_id() {
        return subcategory_id;
    }

    public void setSubcategory_id(int subcategory_id) {
        this.subcategory_id = subcategory_id;
    }

    @Override
    public String toString() {
        return "quiz_banks{" +
                "quiz_banks_id=" + quiz_banks_id +
                ", bank_name='" + bank_name + '\'' +
                ", create_at=" + create_at +
                ", description='" + description + '\'' +
                ", modified_at=" + modified_at +
                ", image='" + image + '\'' +
                ", editable=" + editable +
                ", create_by=" + create_by +
                ", subcategory_id=" + subcategory_id +
                '}';
    }
}
