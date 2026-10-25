package com.mymonkmindset.openingstock.models;

import com.mymonkmindset.openingstock.interfaces.Listable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PersonWithPhone implements Listable {
    private Person person;
    private String phoneNumber;
    private String email;
    private String link;
    private String groupName;

    @Override
    public String getTitle() {
        return person != null ? person.getTitle() : "";
    }
}
