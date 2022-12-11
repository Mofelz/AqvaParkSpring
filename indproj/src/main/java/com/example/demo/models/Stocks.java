package com.example.demo.models;

import org.hibernate.validator.constraints.Range;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Stocks {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;



    @NotNull(message = "Не может быть пустым")
    @Range(min = 1, max = 5, message = "Диапазон от 1 до 5")
    private Integer price;

    public Stocks(Long id, Integer price) {
        this.price = price;
        this.id = id;
    }

    public Stocks() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }


}
