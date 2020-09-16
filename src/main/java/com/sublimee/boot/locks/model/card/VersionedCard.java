package com.sublimee.boot.locks.model.card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;

@Entity
@Table(name = "versioned_card")
@Data
@EqualsAndHashCode(exclude = {"id"}, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class VersionedCard extends Card implements Serializable {

    @Version
    @Column(name = "OPTLOCK")
    protected Long version;

}
