package io.github.malbano.quarkussocial.domain.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "seguidores")
@Data
public class Seguidor {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seguidor_id", nullable = false)
	private User seguidor;

}
