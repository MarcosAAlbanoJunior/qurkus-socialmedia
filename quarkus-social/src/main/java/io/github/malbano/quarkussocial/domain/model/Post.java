package io.github.malbano.quarkussocial.domain.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "post")
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "post_text")
	private String postText;
	
	@Column(name = "data_hora")
	private LocalDateTime dataHora;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User user;
	
	@PrePersist
	public void prePersist() {
		setDataHora(LocalDateTime.now());
	}
}
