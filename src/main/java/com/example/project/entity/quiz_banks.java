package com.example.project.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class quiz_banks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int quiz_banks_id;
    @Column(name = "bank_name")
    private String bankName;
    @Column(name = "created_at")
    private LocalDateTime createAt;
    private String description;
    private LocalDateTime modified_at;
    private String image;
    private int editable;
    private int create_by;
    private int subcategory_id;
    @PrePersist
    protected void OnCreate(){
        this.createAt = LocalDateTime.now();
    }
    @PreUpdate
    protected  void OnUpdate(){
        this.modified_at = LocalDateTime.now();
    }

    public quiz_banks() {
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public int getQuiz_banks_id() {
        return quiz_banks_id;
    }

    public void setQuiz_banks_id(int quiz_banks_id) {
        this.quiz_banks_id = quiz_banks_id;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
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
                ", bankName='" + bankName + '\'' +
                ", createAt=" + createAt +
                ", description='" + description + '\'' +
                ", modified_at=" + modified_at +
                ", image='" + image + '\'' +
                ", editable=" + editable +
                ", create_by=" + create_by +
                ", subcategory_id=" + subcategory_id +
                '}';
    }
}
