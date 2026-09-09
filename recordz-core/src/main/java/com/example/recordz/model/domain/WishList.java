package com.example.recordz.model.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class WishList {

    private Long idWishList;
    private Long refUser;
    private LocalDate dateCreation;

    public WishList() {}

    public Long getIdWishList() { return idWishList; }
    public void setIdWishList(Long idWishList) { this.idWishList = idWishList; }

    public Long getRefUser() { return refUser; }
    public void setRefUser(Long refUser) { this.refUser = refUser; }

    public LocalDate getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDate dateCreation) { this.dateCreation = dateCreation; }
}
