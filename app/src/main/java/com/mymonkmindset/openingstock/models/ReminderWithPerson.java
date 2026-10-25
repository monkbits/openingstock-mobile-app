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
public class ReminderWithPerson implements Listable {
    private Reminder reminder;
    private String personName;

    @Override
    public String getTitle() {
        return reminder != null ? reminder.getTitle() : "";
    }
}
