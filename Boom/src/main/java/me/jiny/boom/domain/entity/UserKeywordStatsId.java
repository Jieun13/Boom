package me.jiny.boom.domain.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserKeywordStatsId implements Serializable {

    private String period;
    private Long user;
    private Long keyword;
}

