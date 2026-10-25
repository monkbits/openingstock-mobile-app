package com.mymonkmindset.openingstock.models;

import com.mymonkmindset.openingstock.interfaces.Listable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class Topic implements Listable {
    private Long topicId;
    private String title;
    private String content;
    private String authorName;
    private int commentCount;
    private int likeCount;

}
