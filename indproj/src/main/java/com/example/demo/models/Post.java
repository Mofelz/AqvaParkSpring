package com.example.demo.models;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull(message = "Дата не может быть пустой")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @FutureOrPresent(message = "Дата посещения не может быть прошлой")
    private Date datapos;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 1, max = 8, message = "От 1 до 8 символов")
    private String uslaga;

    @NotNull(message = "Не может быть пустым")
    @Range(min = 3000,max = 20000, message = "Один пользователь может купить только 5 билетов")
    private Integer price;

    @Range(max = 10, message = "Осталось мест ")
    private Integer maxkolvovisitors = 10;

    @NotNull(message = "Не может быть пустым")
    @Range(min = 1, max = 5, message = "Диапазон от 1 до 5")
    private Integer visitors;

    @ManyToOne
    @JoinColumn(name = "uslugaid", referencedColumnName = "id")
    private Usluga usluga;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 1, max = 8, message = "От 1 до 8 символов")
    private String ogranichenieVremeni;

    private int views;

    public Post(Long id, Date datapos, String uslaga, Integer price, Integer visitors, Integer maxkolvovisitors, String ogranichenieVremeni) {
        this.visitors = visitors;
        this.price = price;
        this.id = id;
        this.datapos = datapos;
        this.uslaga = uslaga;
        this.maxkolvovisitors = maxkolvovisitors;
        this.ogranichenieVremeni = ogranichenieVremeni;
    }

    public Post() {

    }
    public String getOgranichenieVremeni() {return ogranichenieVremeni;}

    public void setOgranichenieVremeni(String ogranichenieVremeni) {this.ogranichenieVremeni = ogranichenieVremeni;}

    public Usluga getUsluga() {
        return usluga;
    }

    public void setUsluga(Usluga usluga) {
        this.usluga = usluga;
    }

    public Integer getVisitors() {
        return visitors;
    }

    public void setVisitors(Integer visitors) {
        this.visitors = visitors;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDatapos() {
        return datapos;
    }

    public void setDatapos(Date datapos) {
        this.datapos = datapos;
    }

    public String getUslaga() {
        return uslaga;
    }

    public void setUslaga(String uslaga) {
        this.uslaga = uslaga;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getMaxkolvovisitors() {
        return maxkolvovisitors;
    }

    public void setMaxkolvovisitors(Integer maxkolvovisitors) {
        this.maxkolvovisitors = maxkolvovisitors;
    }
}