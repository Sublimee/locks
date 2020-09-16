package com.sublimee.boot.locks.model.card;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "card")
@Data
@EqualsAndHashCode(exclude = {"id"})
public class UnversionedCard extends Card implements Serializable {
}
