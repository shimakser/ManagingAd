package by.shimakser.redis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "translations")
public class EntityTranslation implements Serializable {

    @Id
    @JsonIgnore
    private Long id;

    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "translation_language")
    private String language;

    @Column(name = "name_translation")
    private String translation;
}
