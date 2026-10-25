package com.mymonkmindset.openingstock.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContactItem {
    private  String name;
    private  String number;
    private  String accountType; // Distinguishes SIM vs Device/Google

}
