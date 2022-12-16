package com.qiren.distributor.request;

import java.util.Date;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderOutRequest {

    @NotNull
    private String itemprice;

    @NotNull
    private String quantity;

    @NotNull
    private Date startdate;

    @NotNull
    private int esttime;

    @NotNull
    private String company;

    @NotNull
    private long customer;

    @NotNull
    private String orderin;

    @NotNull
    private String status;

}
