package com.mymonkmindset.openingstock.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CallLogItem {
    private String name;
    private String number;
    private String callType; // Incoming, Outgoing, Missed
    private String timestamp;
    private String duration;
}
